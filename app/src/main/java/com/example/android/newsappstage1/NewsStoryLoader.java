package com.example.android.newsappstage1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by ChandaB on 7/27/2018.
 */

public class NewsStoryLoader extends AsyncTaskLoader<List<NewsStory>> {

    private String Url;

    public NewsStoryLoader(Context context, String url) {
        super( context );
        Url = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsStory> loadInBackground() {
        if (Url == null) {
            return null;
        }

        List<NewsStory> stories = JSONParseUtils.fetchStoryData( Url );

        return stories;
    }
}
