package com.khazasid.android.doaqu.Tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.khazasid.android.doaqu.R;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class Tools {

    public static SharedPreferences getDoaQuPrefs(Context context){
        return context.getSharedPreferences(context.getString(R.string.doaqu_pref),
                MODE_PRIVATE);
    }

    public static void setDarkOrLightTheme(Context context){

        boolean isDarkMode = getDoaQuPrefs(context).getBoolean(context.getString(R.string.dark_mode_pref), false);

        if(isDarkMode){
            context.setTheme(R.style.MainActivityThemeDark);
        } else{
            context.setTheme(R.style.MainActivityThemeLight);
        }
    }

    public static void changeDarkOrLightTheme(Context context){

        boolean isDarkMode = getDoaQuPrefs(context).getBoolean(context.getString(R.string.dark_mode_pref), false);
        SharedPreferences.Editor doaquPrefsEditor = getDoaQuPrefs(context).edit();

        doaquPrefsEditor.putBoolean(context.getString(R.string.dark_mode_pref), !isDarkMode);

        doaquPrefsEditor.apply();

    }


    public static AdRequest getAdRequest(Context context){
        AdRequest.Builder adRequest = new AdRequest.Builder();

        ConsentStatus consentStatus = ConsentInformation.getInstance(context).getConsentStatus();
        if(consentStatus.toString().equals("PERSONALIZED")){
            return adRequest.build();
        } else{
            return adRequest.addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build();
        }
    }

    public static boolean is30SecondsPasses(Context context){
        long prevTime = getDoaQuPrefs(context).getLong(context.getString(R.string.acces_time_preferences), 0L);

        long now = new Date().getTime();

        long diffInMin = TimeUnit.MILLISECONDS.toSeconds(now - prevTime);

        if(diffInMin > 30){
            getDoaQuPrefs(context).edit().putLong(context.getString(R.string.acces_time_preferences), now).apply();
            return true;
        }else{
            return false;
        }
    }

    private static Bundle getNonPersonalizedAdsBundle() {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");

        return extras;
    }

    public static void searchInPlaystore(Context context, String url, String market, String content){
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(market + content));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;
            }
        }
        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url + content));
            context.startActivity(webIntent);
        }
    }

}
