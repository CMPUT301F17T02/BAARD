/*
* Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
*/

package com.example.baard;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by biancaangotti on 2017-11-05.
 */

public class ElasticSearchController {

    private static JestDroidClient client;

    // TODO we need a function which adds habits to elastic search
    public static class AddUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user : users) {
                String source = "{\"name\": \"" + user.getName() + "\"," +
                                "\"username\": \"" + user.getUsername() + "\"," +
                                "\"habits\": []," +
                                "\"friends\": []," +
                                "\"receivedRequests\": []}";
                Index index = new Index.Builder(source).index("cmput301f17t02").type("User").build();

                try {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()) {}
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the habits");
                }

            }
            return null;
        }
    }

    // TODO we need a function which gets habits from elastic search
    public static class GetUserTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... search_parameters) {
            verifySettings();
            ArrayList<User> users = new ArrayList<User>();

            String query = "{\n" +
                           "    \"query\" : {\n" +
                           "       \"term\" : {\"username\": \"" + search_parameters[0] + "\"}\n" +
                           "    }\n" +
                           "}";

            Log.d("elasticSearch", query);
            Search search = new Search.Builder(query)
                    .addIndex("cmput301f17t02")
                    .addType("User")
                    .build();

            try {
                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    Object object = result.getValue("_source");
                    Log.d("elasticSearch", "source: " + result.getSourceAsString());
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else {
                    Log.d("elasticSearch", "Unable to find any habits.");
                    Log.e("Error","The search query failed to find any habits that matched.");
                }
            }
            catch (Exception e) {
                Log.d("elasticSearch", "Error with elasticsearch Server");
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return new User("hi", "hello");
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
