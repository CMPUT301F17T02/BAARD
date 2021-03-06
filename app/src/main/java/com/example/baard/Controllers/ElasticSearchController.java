/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.baard.Entities.User;
import com.example.baard.Entities.UserList;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;


/**
 * Implements communication tasks with the elastic search server
 * http://cmput301.softwareprocess.es:8080/cmput301f17t02/
 *
 * @see FileController
 * @author minsoung
 * @since 1.0
 * @version 3.0
 */
public class ElasticSearchController {

    private static JestDroidClient client;

    /**
     * Class to control Async background task that will
     * create a user in the database and return the User class.
     */
    public static class AddUserTask extends AsyncTask<String, Void, User> {

        /**
         * Checks for username in database, if it does not find one, it returns a new user.
         * Adds user into database.
         *
         * @param parameters String of the user's username
         * @return the User instance created
         */
        @Override
        protected  User doInBackground(String... parameters) {
            verifySettings();

            User user = null;
            // Query to check if username exists
            String query = "{\n" +
                    "    \"query\" : {\n" +
                    "       \"constant_score\" : {\n" +
                    "           \"filter\" : {\n" +
                    "               \"term\" : {\"username\": \"" + parameters[1] + "\"}\n" +
                    "             }\n" +
                    "         }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex("cmput301f17t02")
                    .addType("User")
                    .build();

            // Create body for POST API of ElasticSearch
            String source = "{\"name\": \"" + parameters[0] + "\"," +
                    "\"username\": \"" + parameters[1] + "\"," +
                    "\"habits\": {\"habits\": []}," +
                    "\"friends\": [],"  +
                    "\"receivedRequests\": []}";
            Index index = new Index.Builder(source).index("cmput301f17t02").type("User").build();

            try {
                SearchResult result = client.execute(search);
                JsonObject hits = result.getJsonObject().getAsJsonObject("hits");

                if (result.isSucceeded()) {
                    if (hits.get("total").getAsInt() == 0) {
                        DocumentResult execute = client.execute(index);
                        if (execute.isSucceeded()) {
                            user = new User(parameters[0], parameters[1], execute.getId());
                            Log.i("ESC.AddUserTask", "User has been created.");
                        } else {
                            Log.i("ESC.AddUserTask", "Application failed to create new user.");
                        }
                    } else {
                        Log.i("ESC.AddUserTask", "The user already exists.");
                    }
                } else {
                    Log.e("ESC.AddUserTask", "The search query failed.");
                }
            }
            catch (Exception e) {
                Log.e("ESC.AddUserTask", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return user;
        }
    }

    /**
     * Class to control Async background task that will update a user in the database
     */
    public static class UpdateUserTask extends AsyncTask<User, Void, Void> {

        /**
         * Finds the user in the database, and then stores the updated version of itself.
         *
         * @param users The user to be updated (only one)
         */
        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                Gson gson = new Gson();

                // Create body for PUT API of ElasticSearch
                // Need to extract fields separately since some of the fields are transient
                String source = "{\"name\": \"" + user.getName() + "\"," +
                                "\"username\": \"" + user.getUsername() + "\"," +
                                "\"habits\": " + gson.toJson(user.getHabits()) + "," +
                                "\"friends\": " + gson.toJson(user.getFriends()) + "," +
                                "\"receivedRequests\": " + gson.toJson(user.getReceivedRequests()) + "}";

                String doc = "{" + "\"doc\": " + source + "}";
                Log.d("ESC.UpdateUserTask", doc);

                Update update = new Update.Builder(doc).index("cmput301f17t02").type("User").id(user.getId()).build();
                Log.d("ESC.UpdateUserTask", user.getId());

                try {
                    DocumentResult execute = client.execute(update);
                    if (execute.isSucceeded()) {
                      Log.i("ESC.UpdateUserTask", "User has been updated.");
                    }
                }
                catch (Exception e) {
                    Log.e("ESC.UpdateUserTask", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }

            }
            return null;
        }
    }

    /**
     * Class to control Async background task that will grab a user from the database
     */
    public static class GetUserTask extends AsyncTask<String, Void, User> {
        /**
         * Finds the user in the database based on the username string, and then
         * returns the user.
         *
         * @param parameters String of the user's username requested
         * @return User instance or null if not found
         * @throws RuntimeExecutionException upon failed communication
         */
        @Override
        protected User doInBackground(String... parameters) {
            verifySettings();

            User user = null;
            String query = "{\n" +
                           "    \"query\" : {\n" +
                           "       \"constant_score\" : {\n" +
                           "           \"filter\" : {\n" +
                           "               \"term\" : {\"username\": \"" + parameters[0] + "\"}\n" +
                           "             }\n" +
                           "         }\n" +
                           "    }\n" +
                           "}";

            Log.d("ESC.GetUserTask", query);
            Search search = new Search.Builder(query)
                    .addIndex("cmput301f17t02")
                    .addType("User")
                    .build();

            try {
                SearchResult result = client.execute(search);
                JsonObject hits = result.getJsonObject().getAsJsonObject("hits");

                if (result.isSucceeded()) {
                    if (hits.get("total").getAsInt() == 1) {
                        JsonObject userInfo = hits.getAsJsonArray("hits").get(0).getAsJsonObject();
                        JsonObject userInfoSource = userInfo.get("_source").getAsJsonObject();

                        // Need to extract Id separately because Jest
                        String id = userInfo.get("_id").getAsString();

                        user = new Gson().fromJson(userInfoSource, User.class);

                        user.setId(id);

                        Log.i("ESC.GetUserTask", "Unique user was found.");
                    } else {
                        Log.i("ESC.GetUserTask", "User does not exist or is not unique.");
                    }
                } else {
                    Log.e("ESC.GetUserTask", "The search query failed.");
                }
            }
            catch (Exception e) {
                Log.e("ESC.GetUserTask", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return user;
        }
    }

    /**
     * Class to control Async background task that will grab all users from the database
     */
    public static class GetAllUsersTask extends AsyncTask<String, Void, UserList> {
        /**
         * Finds all users in the database , and then
         * returns the user list.
         *
         * @param parameters not used
         * @return UserList of all users
         * @throws RuntimeExecutionException upon search failure
         */
        @Override
        protected UserList doInBackground(String... parameters) {
            verifySettings();

            UserList users = new UserList();
            String query = "{\n" +
                    "    \"size\": 1000,\n" +
                    "    \"query\": {\n" +
                    "       \"match_all\" : {}\n" +
                    "    }\n" +
                    "}";

            Log.d("ESC.GetAllUsersTask", query);
            Search search = new Search.Builder(query)
                    .addIndex("cmput301f17t02")
                    .addType("User")
                    .build();

            try {
                SearchResult result = client.execute(search);
                JsonObject hits = result.getJsonObject().getAsJsonObject("hits");

                if (result.isSucceeded()) {
                    Log.d("ESC.GetAllUsersTask", "Number of hits: "+hits.get("total").getAsString());
                    for (int i = 0; i<hits.get("total").getAsInt(); i++) {
                        JsonObject userInfo = hits.getAsJsonArray("hits").get(i).getAsJsonObject();
                        JsonObject userInfoSource = userInfo.get("_source").getAsJsonObject();

                        // Need to extract Id separately because Jest
                        String id = userInfo.get("_id").getAsString();

                        User user = new Gson().fromJson(userInfoSource, User.class);

                        user.setId(id);

                        users.add(user);
                    }
                } else {
                    Log.e("ESC.GetUserTask", "The search query failed.");
                }
            }
            catch (Exception e) {
                Log.e("ESC.GetUserTask", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return users;
        }
    }


    /**
     * Async Task to delete a user from the database
     */
    public static class DeleteUserTask extends AsyncTask<User, Void, Void> {
        /**
         * @param users the User instance to be deleted from database
         */
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                try {
                    DocumentResult result = client.execute(new Delete.Builder(user.getId())
                            .index("cmput301f17t02")
                            .type("User")
                            .build());
                   if (result.isSucceeded()) {
                       Log.i("ESC.DeleteUserTask", "The user was delete successfully.");
                   } else {
                       Log.e("ESC.DeleteUserTask", "Failed to delete user.");
                   }
                } catch (Exception e) {
                    Log.e("ESC.UpdateUserTask", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }

            }

            return null;
        }
    }

    /**
     * Checks if the client is connected to the server with the right configuration.
     * Generated from the CMPUT 301 labs
     */
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
