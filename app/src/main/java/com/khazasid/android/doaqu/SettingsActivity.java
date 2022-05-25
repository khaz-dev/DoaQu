package com.khazasid.android.doaqu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.khazasid.android.doaqu.Tools.Tools;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Tools.setDarkOrLightTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        setSettingsToolbar();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    private void setSettingsToolbar(){
        Toolbar settingsToolbar = findViewById(R.id.settings_toolbar);
        settingsToolbar.setTitleTextAppearance(this, R.style.MaturascTextAppearance);

        setSupportActionBar(settingsToolbar);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            SwitchPreferenceCompat darkMode = findPreference(getString(R.string.dark_mode_pref));

            if(getContext()!= null && darkMode != null){
                darkMode.setChecked(Tools.getDoaQuPrefs(getContext()).getBoolean(getString(R.string.dark_mode_pref), false));
                darkMode.setOnPreferenceChangeListener((Preference preference, Object newValue) -> {

                    if(getActivity() != null){

                        Tools.changeDarkOrLightTheme(getActivity());

                        Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);

                        // Create the TaskStackBuilder and add the intent, which inflates the back stack
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
                        stackBuilder.addNextIntentWithParentStack(settingsIntent);

                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getActivity(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                        getActivity().finish();
                        stackBuilder.startActivities(bundle);

                    }

                    return true;
                });
            }

            Preference othersApp = findPreference(getString(R.string.other_apps_pref));
            if(othersApp != null){
                othersApp.setOnPreferenceClickListener((Preference preference) -> {
                    String devID = "7045347224404603528";
                    String market = "market://dev?id=";
                    String url = "http://play.google.com/store/apps/dev?id=";

                    if(getActivity() != null){
                        Tools.searchInPlaystore(getActivity(), url, market, devID);
                    }

                    return true;
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Settings Activity closed
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }
}