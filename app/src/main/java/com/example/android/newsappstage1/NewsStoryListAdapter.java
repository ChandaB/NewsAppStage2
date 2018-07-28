package com.example.android.newsappstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ChandaB on 7/26/2018.
 */

public class NewsStoryListAdapter extends ArrayAdapter<NewsStory> {


    public NewsStoryListAdapter(@NonNull Context context, @NonNull List<NewsStory> stories) {
        super( context, 0, stories );
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemview = convertView;
        if(listitemview == null){
            listitemview = LayoutInflater.from( getContext()).inflate( R.layout.list_item, parent, false );
        }

        NewsStory currentStory = getItem( position );

        Date dateObject = new Date( currentStory.getDate() );

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);

        SimpleDateFormat timeFormatter = new SimpleDateFormat( "h:mm a" );
        String timeToDisplay = timeFormatter.format( dateObject );

        TextView dateTextView = listitemview.findViewById( R.id.date );
        dateTextView.setText( String.valueOf(dateToDisplay) );

        TextView timeTextView = listitemview.findViewById(R.id.time );
        timeTextView.setText( String.valueOf(timeToDisplay) );

        TextView headlineTextView = listitemview.findViewById( R.id.header );
        headlineTextView.setText( currentStory.getHeadline() );

        TextView categoryTextView = listitemview.findViewById( R.id.category );
        categoryTextView.setText( currentStory.getCategory() );

        TextView subCategoryTextView = listitemview.findViewById( R.id.subcategory );
        subCategoryTextView.setText( currentStory.getSubcategory() );

        TextView authorTextView = listitemview.findViewById( R.id.author );
        authorTextView.setText( currentStory.getAuthor() );

        return listitemview;
    }
}
