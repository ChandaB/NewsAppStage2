package com.example.android.newsappstage2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ChandaB on 7/26/2018.
 */

public class NewsStoryListAdapter extends ArrayAdapter<NewsStory> {

    String formattedDate;
    String formattedTime;

    /**
     * Constructor for news adapter
     */
    public NewsStoryListAdapter(Context context, List<NewsStory> stories) {
        super( context, 0, stories );
    }

    /**
     * Set the layout to the list_item layout and populate the data in the layout
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemview = convertView;
        if (listitemview == null) {
            listitemview = LayoutInflater.from( getContext() ).inflate( R.layout.list_item, parent, false );
        }

        // Create a new NewsStory object and get it's position
        NewsStory currentStory = getItem( position );

        // Format the date to be populated in the date/time textviews
        String dateBeforeSplit = currentStory.getDate();
        dateBeforeSplit = dateBeforeSplit.replace( "Z", "" );

        SimpleDateFormat inputFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
        SimpleDateFormat outputDate = new SimpleDateFormat( "MM-dd-yyyy" );
        SimpleDateFormat outputTime = new SimpleDateFormat( "hh:mm a" );

        Date date = null;
        try {
            date = inputFormat.parse( dateBeforeSplit );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        formattedDate = outputDate.format( date );
        formattedTime = outputTime.format( date );

        //Code for images
        //Set imageview to the layout's thumbnail_image
        ImageView thumbnailImage = listitemview.findViewById( R.id.thumbnail_image );

        //If the storyimageURL is not blank, get the imageURL and set the imageview to visible
        if (currentStory.getStoryImageURL() != "") {
            new DownloadImageTask( thumbnailImage ).execute( currentStory.getStoryImageURL() );
            thumbnailImage.setVisibility( View.VISIBLE );
            //If the storyimageURL is blank, then set the imageview to gone and don't display any image
        } else {
            thumbnailImage.setVisibility( View.GONE );
        }

        // Populate the formatted date into the date textfield
        TextView dateTextView = listitemview.findViewById( R.id.date );
        dateTextView.setText( formattedDate );

        // Populate the formatted time into the time textfield
        TextView timeTextView = listitemview.findViewById( R.id.time );
        timeTextView.setText( formattedTime );

        // Populate the headline in the textview
        TextView headlineTextView = listitemview.findViewById( R.id.header );
        headlineTextView.setText( currentStory.getHeadline() );

        // Populate the category in the textview
        TextView categoryTextView = listitemview.findViewById( R.id.category );
        categoryTextView.setText( currentStory.getCategory() );

        // Populate the author in the textview
        TextView authorTextView = listitemview.findViewById( R.id.author );
        authorTextView.setText( currentStory.getAuthor() );

        return listitemview;
    }

    //Class to handle downloading an image from a URL
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmapImage;

        public DownloadImageTask(ImageView bitmapImage) {
            this.bitmapImage = bitmapImage;
        }

        //Grab the image URL, return a bitmap image
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL( urlDisplay ).openStream();
                bmp = BitmapFactory.decodeStream( in );
            } catch (Exception e) {
                Log.e( "Error", e.getMessage() );
                e.printStackTrace();
            }
            return bmp;
        }

        //set the image after download
        protected void onPostExecute(Bitmap result) {
            bitmapImage.setImageBitmap( result );
        }
    }
}
