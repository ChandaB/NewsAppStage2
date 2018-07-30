package com.example.android.newsappstage1;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsStory>> {

    private static final int NEWS_STORY_LOADER_ID = 1;
    String storyUrl = "https://content.guardianapis.com/search?order-by=newest&show-tags=contributor&q=Montreal&api-key=ddc3ebb7-dfc3-4307-9a14-8a73a96647c7";
    private NewsStoryListAdapter listAdapter;
    private TextView emptyResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Bind ListView Object to the list layout
        ListView NewsHeadlinesListView = findViewById( R.id.list );

        // Bind the emptyResultsTextView to the empty_view textview.
        emptyResultsTextView = findViewById( R.id.empty_view );

        // Call the Network Checking method
        checkNetworkConnection();

        // Create a new list adapter
        listAdapter = new NewsStoryListAdapter( this, new ArrayList<NewsStory>() );

        // Set the listview contents to the listAdapter contents
        NewsHeadlinesListView.setAdapter( listAdapter );

        // Create an OnItemClickListener to open the news story in a browser
        NewsHeadlinesListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsStory story = listAdapter.getItem( position );

                Uri NewsStoryUri = Uri.parse( story.getUrl() );

                Intent i = new Intent( Intent.ACTION_VIEW, NewsStoryUri );

                startActivity( i );

            }
        } );


    }

    /**
     * This methoed is responsible for checking network connectivity.  If there is a problem with
     * the connection a message will be displayed.
     */
    private void checkNetworkConnection() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader( NEWS_STORY_LOADER_ID, null, this );
        } else {
            // Otherwise, display error
            emptyResultsTextView.setText( R.string.no_network );

            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    /**
     * Once the data has loaded refresh the list adapter and add the stories from the list
     */
    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> stories) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state textview to display when no results found
        emptyResultsTextView.setText( R.string.sorry_no_news_stories_available );

        // Clear the adapter
        listAdapter.clear();

        // If there are results, set the emptyresultsTextView to gone and add the stories to the adapter
        if (stories != null && !stories.isEmpty()) {
            emptyResultsTextView.setVisibility( View.GONE );
            listAdapter.addAll( stories );
        }

    }

    /**
     *
     * @param loader to clear the adapter
     */
    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {
        listAdapter.clear();

    }


    /**
     * Loader for news story
     */
    @Override
    public Loader<List<NewsStory>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsStoryLoader( this, storyUrl );
    }
}
