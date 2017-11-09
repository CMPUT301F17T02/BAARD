/*
* Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
*/

package com.example.baard;

import android.os.AsyncTask;
import android.util.Log;

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
    public static class AddHabitsTask extends AsyncTask<Habit, Void, Void> {

        @Override
        protected Void doInBackground(Habit... habits) {
            //verifySettings();

            for (Habit habit : habits) {
                Index index = new Index.Builder(habit).index("CMPUT301F17T02").type("habit").build();

                try {
                    // where is the client?
                    DocumentResult execute = client.execute(index);
                    if(execute.isSucceeded()) {
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the habits");
                }

            }
            return null;
        }
    }

    // TODO we need a function which gets habits from elastic search
    public static class GetHabitsTask extends AsyncTask<String, Void, ArrayList<Habit>> {
        @Override
        protected ArrayList<Habit> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Habit> habits = new ArrayList<Habit>();

            // TODO Build the query
            String query = "{\n" +
                    "    \"query\" : {\n" +
                    "        \"term\" : { \"message\" : \"" + search_parameters[0] + "\" }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex("testing")
                    .addType("habit")
                    .build();

            try {
                // TODO get the results of the query
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Habit> foundHabits = result.getSourceAsObjectList(Habit.class);
                    habits.addAll(foundHabits);
                }
                else {
                    Log.e("Error","The search query failed to find any habits that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return habits;
        }
    }


    public static void verifySettings() {
        if (client == null) {
            // classes that build other classes for you
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080/cmput301f17t02");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
