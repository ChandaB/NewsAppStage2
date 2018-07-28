package com.example.android.newsappstage1;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsStory>> {

    private static final int NEWS_STORY_LOADER_ID = 1;

    private NewsStoryListAdapter listAdapter;

    String storyUrl = "https://content.guardianapis.com/search?q=Montreal&api-key=ddc3ebb7-dfc3-4307-9a14-8a73a96647c7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        listAdapter = new NewsStoryListAdapter( this, new ArrayList<NewsStory>(  ));

        // Find a reference to the {@link ListView} in the layout
        ListView NewsHeadlinesListView = findViewById(R.id.list);

        NewsHeadlinesListView.setAdapter( listAdapter );

        NewsHeadlinesListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsStory story = listAdapter.getItem( position );

                Uri NewsStoryUri = Uri.parse( story.getUrl() );

                Intent i = new Intent( Intent.ACTION_VIEW, NewsStoryUri );

                startActivity( i );

            }
        } );

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(NEWS_STORY_LOADER_ID, null, this);
        Log.d( "initLoader", "onCreate: initLoader Executed"  );

    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> stories) {
        listAdapter.clear();

        if (stories != null && !stories.isEmpty()) {
            listAdapter.addAll( stories );
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {
        listAdapter.clear();

    }


    @Override
    public Loader<List<NewsStory>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsStoryLoader(this, storyUrl);
    }
    }
