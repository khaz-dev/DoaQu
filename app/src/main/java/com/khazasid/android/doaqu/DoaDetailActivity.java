package com.khazasid.android.doaqu;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.khazasid.android.doaqu.Tools.Tools;

public class DoaDetailActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        Tools.setDarkOrLightTheme(this);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            // inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            // set an enter transition
            getWindow().setEnterTransition(new Explode());
            // set an exit transition
            getWindow().setExitTransition(new Fade());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_doa);

        addInterstitialAd();

        RelativeLayout numberContainer = findViewById(R.id.number_container);
        numberContainer.setOnClickListener((View v) ->
                onBackPressed()
        );
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if(savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            DetailFragment detailFragment = DetailFragment.newInstance(getIntent().getShortExtra(DetailFragment.ROW_ID_KEY, (short)0));
            getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, detailFragment).commit();
        }

        // Cancel Notification
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(getResources().getInteger(R.integer.DAILY_NOTIF_ID));
        nMgr.cancel(getResources().getInteger(R.integer.EVENT_NOTIF_ID));
    }

    private void addInterstitialAd(){

        InterstitialAd.load(this, getString(R.string.DETAIL_INTERSTITIAL_AD_UNIT_ID),
                Tools.getAdRequest(DoaDetailActivity.this), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                onBackPressed();
                            }

                            @Override
                            public void onAdClicked() {
                                // Show toast thank to user for ad clicked
                                Toast.makeText(mContext, R.string.ad_clicked, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Show the ad if it's ready. Otherwise just back to Main Activity.
        if (mInterstitialAd != null && Tools.is30SecondsPasses(this)) {
            mInterstitialAd.show(this);
        }
        supportFinishAfterTransition();
        super.onBackPressed();
    }
}
