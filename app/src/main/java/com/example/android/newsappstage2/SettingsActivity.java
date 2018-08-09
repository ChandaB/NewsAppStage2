package com.example.android.newsappstage2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ChandaB on 8/1/2018.
 * Setting activity that handles displaying the settings menu
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        //Set view to the settings_activity layout file
        setContentView( R.layout.settings_activity );
    }

    //Fragment inside the settings activity
    public static class NewsAppPreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate( savedInstanceState );
            //set the layout to the settings_main xml
            super.addPreferencesFromResource( R.xml.settings_main );

            //create preference to order results by newest, oldest, relevance
            Preference orderBy = findPreference( getString( R.string.settings_order_by_key ) );
            bindPreferenceSummaryToValue( orderBy );

            //create preference to allow user to enter a keyword search
            Preference search_Term = findPreference( getString( R.string.settings_search_term_key ) );
            bindPreferenceSummaryToValue( search_Term );

            //create preference to allow user to select a specific category for their results
            Preference filterByCategory = findPreference( getString( R.string.settings_filter_by_category_key ) );
            bindPreferenceSummaryToValue( filterByCategory );
        }

        //Bind the values for the preferences by the key
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener( this );

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( preference.getContext() );
            String preferenceString = preferences.getString( preference.getKey(), "" );
            onPreferenceChange( preference, preferenceString );
        }

        //update the settings screen when a preference is changed
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            //Update the preference selected on the settings screen
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue( stringValue );
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary( labels[prefIndex] );
                }
            } else {
                preference.setSummary( stringValue );
            }

            return true;
        }
    }
}