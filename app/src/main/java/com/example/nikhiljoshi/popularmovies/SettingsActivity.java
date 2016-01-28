package com.example.nikhiljoshi.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * This is a new Activity. When a user clicks on the settings button, we are going to wire up the code,
 * so that we start a new intent with this class passed in. In this class, we are going to show
 * the users the preferences from which they can choose. We need to make sure that the
 * preferences are stored in SharedPreferences -- SharedPreferences is a class that provides
 * framework to store stuff in key value pairs. These key value pairs will persist across
 * user sessions (even when a user session is killed)
 *
 *
 * Each Preference you add has a corresponding key-value pair that the system uses to save the
 * setting in a default SharedPreferences file for your app's settings. When the user changes a
 * setting, the system updates the corresponding value in the SharedPreferences file for you. The
 * only time you should directly interact with the associated SharedPreferences file is when you
 * need to read the value in order to determine your app's behavior based on the user's setting.
 *
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.image_sorting_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        
        // First get the SharedPreferences object
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        final String currPreferenceValue = defaultSharedPreferences.getString(preference.getKey(), "");
        onPreferenceChange(preference, currPreferenceValue);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            // When it comes to list preference, the thing that happens is that the value
            // stored in shared preferences is the Values StringArray. As we had previously read,
            // we want to showcase the other StringArray that looks nice.
            ListPreference listPref = (ListPreference) preference;
            // grab the index of the value
            final int indexOfPreferenceValue = listPref.findIndexOfValue(stringValue);
            if (indexOfPreferenceValue >= 0) {
                // go into the 'other' StringArray and display the results!
                preference.setSummary(listPref.getEntries()[indexOfPreferenceValue]);
            }
        } else {
            preference.setSummary(stringValue);
        }
        return true;
    }

}
