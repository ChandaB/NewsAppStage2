package com.example.android.newsappstage2;

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
    public static String storyImageURL;

    /**
     * Default constructor
     */
    public JSONParseUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL( stringUrl );
        } catch (MalformedURLException e) {
            Log.e( LOG_TAG, "Problem building the URL ", e );
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the JSON response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        //Initialize http connection
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout( 10000 /* milliseconds */ );
            urlConnection.setConnectTimeout( 15000 /* milliseconds */ );
            urlConnection.setRequestMethod( "GET" );
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response,
            // else throw an error.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream( inputStream );
            } else {
                Log.e( LOG_TAG, "Error response code: " + urlConnection.getResponseCode() );
            }
            //Log error if there is a problem getting JSON results
        } catch (IOException e) {
            Log.e( LOG_TAG, "Problem retrieving the JSON results.", e );
            //cleanup connection
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
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream, Charset.forName( "UTF-8" ) );
            BufferedReader reader = new BufferedReader( inputStreamReader );
            String line = reader.readLine();
            while (line != null) {
                output.append( line );
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link NewsStory} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<NewsStory> extractFromNewsStory(String jsonResponse) {

        String author = "";

        //If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty( jsonResponse )) {
            return null;
        }

        // Create an empty ArrayList that we can start adding stories to
        List<NewsStory> stories = new ArrayList<>();

        // Try to parse the JSON string response. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSON Object from the JSON response string
            JSONObject initialJsonResponse = new JSONObject( jsonResponse );
            JSONObject response = initialJsonResponse.getJSONObject( "response" );

            // Extract the array associated with the results key
            JSONArray storyArray = response.getJSONArray( "results" );

            // Loop through each result in the array
            for (int i = 0; i < storyArray.length(); i++) {
                int arraylength = storyArray.length();
                Log.d( "ArrayLength ", Integer.toString( arraylength ) );
                Log.d( "STORYARRAY", storyArray.getString( i ) );

                // Get current story JSONObject at position i
                JSONObject currentStory = storyArray.getJSONObject( i );

                // Get values from JSON keys
                String headline = currentStory.getString( "webTitle" );
                String date = currentStory.getString( "webPublicationDate" );
                String category = currentStory.getString( "sectionName" );
                String url = currentStory.getString( "webUrl" );

                //Check to see if the JSON Object has a fields key, if it does, then parse the
                //thumbnail image, if not, then set the storyImageURL to blank.
                if (currentStory.has( "fields" )) {
                    JSONObject currentFields = currentStory.getJSONObject( "fields" );
                    storyImageURL = currentFields.getString( "thumbnail" );

                } else {
                    storyImageURL = "";
                }

                // Extract the array associated with the tags key
                JSONArray tagsArray = currentStory.getJSONArray( "tags" );

                // Loop through each key in the tags array
                if (tagsArray != null && tagsArray.length() > 0) {
                    for (int n = 0; n < tagsArray.length(); n++) {
                        Log.d( "TAGSCONTENT", tagsArray.getString( n ) );
                        JSONObject authorExistsCurrentStory = tagsArray.getJSONObject( n );
                        int tagsArraylength = tagsArray.length();
                        Log.d( "TagsArrayLength", Integer.toString( tagsArraylength ) );
                        {
                            // Get author value from webTitle JSON key
                            author = authorExistsCurrentStory.getString( "webTitle" );
                        }
                    }
                    // If no author (webTitle) is found, message will display
                } else {
                    author = "No author for this story";
                }

                // Create new NewsStory object with JSON results
                NewsStory story = new NewsStory( headline, date, category, url, author, storyImageURL );

                // Add currentStory to the stories array
                stories.add( story );


            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e( "JSONParseUtils", "Problem parsing the NewsStory JSON results", e );
        }


        // Return the list of stories
        return stories;
    }

    /**
     * Create NewsStory list
     */
    public static List<NewsStory> fetchStoryData(String requestURL) {
        //Create the URL object
        URL url = createUrl( requestURL );

        //Perform the request to the URL and receive the response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest( url );
        } catch (IOException e) {
            Log.e( LOG_TAG, "Problem making the HTTP request.", e );
        }

        List<NewsStory> stories = extractFromNewsStory( jsonResponse );

        return stories;
    }
}
