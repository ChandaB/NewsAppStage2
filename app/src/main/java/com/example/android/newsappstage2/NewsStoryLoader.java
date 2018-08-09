package com.example.android.newsappstage2;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by ChandaB on 7/27/2018.
 */

public class NewsStoryLoader extends AsyncTaskLoader<List<NewsStory>> {

    private String Url;

    // Set the url to the current context's url
    public NewsStoryLoader(Context context, String url) {
        super( context );
        Url = url;
        Log.d( "URL AFTER URI PARSE", url );
    }

    // forceload once the loader starts loading
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // Get the story data on another thread
    @Override
    public List<NewsStory> loadInBackground() {
        if (Url == null) {
            return null;
        }

        List<NewsStory> stories = JSONParseUtils.fetchStoryData( Url );

        return stories;
    }
}
