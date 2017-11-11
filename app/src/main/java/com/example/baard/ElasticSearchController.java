/*
* Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
*/

package com.example.baard;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by biancaangotti on 2017-11-05.
 */

public class ElasticSearchController {

    private static JestDroidClient client;

    public static class AddUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected  User doInBackground(String... parameters) {
            verifySettings();

            User user = null;
            // Check if username exists
            String query = "{\n" +
                    "    \"query\" : {\n" +
                    "       \"term\" : {\"username\": \"" + parameters[1] + "\"}\n" +
                    "    }\n" +
                    "}";
            Search search = new Search.Builder(query)
                    .addIndex("cmput301f17t02")
                    .addType("User")
                    .build();

            // Create body for POST API of ElasticSearch
            String source = "{\"name\": \"" + parameters[0] + "\"," +
                    "\"username\": \"" + parameters[1] + "\"," +
                    "\"habits\": []," +
                    "\"friends\": [],"  +
                    "\"receivedRequests\": []}";
            Index index = new Index.Builder(source).index("cmput301f17t02").type("User").build();

            try {
                SearchResult result = client.execute(search);
                JsonObject hits = result.getJsonObject().getAsJsonObject("hits");
                if (result.isSucceeded() && hits.get("total").getAsInt() == 0) {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()) {
                        Log.d("elasticSearch", execute.getId());
                        user = new User(parameters[0], parameters[1]);
                        user.setId(execute.getId());
                        Log.d("elasticSearch", "User has been created.");
                    } else {
                        Log.d("elasticSearch", "Application failed to create new user.");
                    }
                } else {
                    Log.d("elasticSearch", "User already exists!");
                }
            }
            catch (Exception e) {
                Log.i("Error", "The application failed to build and send the habits");
            }

            return user;
        }
    }

    public static class UpdateUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                JsonObject o;
                JsonParser parser = new JsonParser();

                // Get Habits attribute as string
                o = parser.parse(new Gson().toJson(user.getHabits())).getAsJsonObject();
                String habitsJSON = o.getAsJsonArray("habits").toString();
                Log.d("elasticSearch", habitsJSON);

                // Get Friends attribute as string
                o = parser.parse(new Gson().toJson(user.getFriends())).getAsJsonObject();
                String friendsJSON = o.getAsJsonArray("users").toString();
                Log.d("elasticSearch", friendsJSON);

                // Get ReceivedRequests attribute as string
                o = parser.parse(new Gson().toJson(user.getReceivedRequests())).getAsJsonObject();
                String receivedRequestsJSON = o.getAsJsonArray("users").toString();
                Log.d("elasticSearch", receivedRequestsJSON);

                // Create body for POST API of ElasticSearch
                String source = "{\"name\": \"" + user.getName() + "\"," +
                                "\"username\": \"" + user.getUsername() + "\"," +
                                "\"habits\": " + habitsJSON + "," +
                                "\"friends\": " + friendsJSON + "," +
                                "\"receivedRequests\": " + receivedRequestsJSON + "}";

                Index index = new Index.Builder(source).index("cmput301f17t02").type("User").build();

                try {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()) {
                        Log.d("elasticSearch", "User has been created.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the habits");
                }

            }
            return null;
        }
    }

    public static class GetUserTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... parameters) throws RuntimeExecutionException {
            verifySettings();

            User user = null;
            String query = "{\n" +
                           "    \"query\" : {\n" +
                           "       \"term\" : {\"username\": \"" + parameters[0] + "\"}\n" +
                           "    }\n" +
                           "}";

            Log.d("elasticSearch", query);
            Search search = new Search.Builder(query)
                    .addIndex("cmput301f17t02")
                    .addType("User")
                    .build();

            try {
                SearchResult result = client.execute(search);
                JsonObject hits = result.getJsonObject().getAsJsonObject("hits");
                Log.d("elasticSearch", hits.toString());

                if (result.isSucceeded() && hits.get("total").getAsInt() == 1) {
                    JsonObject userInfo = hits.getAsJsonArray("hits").get(0).getAsJsonObject();
                    JsonObject userInfoSource = userInfo.get("_source").getAsJsonObject();

                    // Create HabitList habits from JSON string
                    String habitsJSON = "{\"habits\":" + userInfoSource.get("friends").toString() + "}";
                    HabitList habitsList = new Gson().fromJson(habitsJSON, HabitList.class);

                    // Create UserList friendsList from JSON string
                    String friendsJSON = "{\"users\":" + userInfoSource.get("friends").toString() + "}";
                    UserList friendsList = new Gson().fromJson(friendsJSON, UserList.class);

                    // Create UserList receivedRequest from JSON string
                    String receivedRequestsJSON = "{\"users\":" + userInfoSource.get("friends").toString() + "}";
                    UserList receivedRequestsList = new Gson().fromJson(receivedRequestsJSON, UserList.class);

                    user = result.getSourceAsObject(User.class);
                    user.setHabits(habitsList);
                    user.setFriends(friendsList);
                    user.setReceivedRequests(receivedRequestsList);

                    Log.d("elasticSearch", "User was found.");
                } else {
                    Log.i("elasticSearch","The search query failed to find any username that matched.");
                }
            }
            catch (Exception e) {
                Log.e("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return user;
        }
    }

    public static void verifySettings() {
        if (client == null) {
            // classes that build other classes for you
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
