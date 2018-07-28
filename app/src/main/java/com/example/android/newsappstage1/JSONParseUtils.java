package com.example.android.newsappstage1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChandaB on 7/26/2018.
 */

public class JSONParseUtils {

    public static final String LOG_TAG = JSONParseUtils.class.getName();



    public JSONParseUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link NewsStory} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<NewsStory> extractFromNewsStory(String jsonResponse ) {

        //If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty( jsonResponse )) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<NewsStory> stories = new ArrayList<>();

        // Try to parse the JSON string response. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            //Create a JSON Object from the JSON response string
          JSONObject initialJsonResponse = new JSONObject( jsonResponse );
          JSONObject response = initialJsonResponse.getJSONObject( "response" );

            //Extract the array associated with the features key
            JSONArray storyArray = response.getJSONArray( "results" );
//            JSONArray storyArray = new JSONArray( jsonResponse );

//            Loop through each feature in the array
            for (int i = 0; i < storyArray.length(); i++) {
                Log.d( "STORYARRAY", storyArray.getString( i ));
//            Get earthquake JSONObject at position i
                JSONObject currentStory = storyArray.getJSONObject( i );

//              JSONObject section = currentStory.getJSONObject( "section" );

                String headline = currentStory.getString( "webTitle" );
                String date = currentStory.getString( "webPublicationDate" );
                String category = currentStory.getString( "pillarName" );
                String subcategory = currentStory.getString( "sectionName" );
                String url = currentStory.getString( "webUrl" );
//                String author = currentStory.getString( "author" );

                NewsStory story = new NewsStory( headline, date, category, subcategory, url );

                stories.add( story );

            }


//            Get “properties” JSONObject
//            Extract “mag” for magnitude
//            Extract “place” for location
//            Extract “time” for time
//            Create Earthquake java object from magnitude, location, and time
//            Add earthquake to list of earthquakes
//*/

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("JSONParseUtils", "Problem parsing the NewsStory JSON results", e);
        }

        // Return the list of earthquakes
        return stories;
    }

    public static List<NewsStory> fetchStoryData(String requestURL) {
        //Create the URL object
        URL url = createUrl( requestURL );

        //Perform the request to the URL and receive the response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest( url );
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request." , e);
        }

        List<NewsStory> stories = extractFromNewsStory( jsonResponse );

        return stories;
    }
}
