package com.khazasid.android.doaqu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.khazasid.android.doaqu.Tools.Tools;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AdView bannerAdView;
    private final String TAG = getClass().getSimpleName();
    private ConsentForm form;

    private RelativeLayout secondaryToolbar;
    private LinearLayout bannerView;
    private TabLayout tabLayout;
    private MenuItem menuItemSearch;
    private SearchView searchView;
    private ViewPager2 viewPager;
    private Snackbar mSnackbar;
    private Toolbar mainToolbar;
    private AppBarLayout appBarLayout;
    private DrawerLayout drawerLayout;
    private SearchFragment searchFragment;
    private MenuItem activeNavigationDrawerItem;
    private TextView hijriDateText;
    private FloatingActionButton findFAB;
    private IntentFilter filterDateChange, filterConnectivityChange;
    private BroadcastReceiver listenerDateChange, listenerConnectivityChange;
    private FrameLayout searchContainer;

    private MenuItem menuItemDarkLightMode, menuItemShareApp, menuItemRateUs;
    private InterstitialAd mInterstitialAd;

    private Context mContext;
    private boolean adLoaded;

    private final ActivityResultLauncher<Intent> settingsActivityResultLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if(result.getResultCode() == Activity.RESULT_OK){
            unCheckActiveNavigationDrawerItem();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure to setTheme before calling super.onCreate
        Tools.setDarkOrLightTheme(this);
        super.onCreate(savedInstanceState);

        // Handle the splash screen transition.
        //LoadData.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        mContext = this;

        MobileAds.initialize(this, initializationStatus -> {
        });

        addBannerAd();
        addInterstitialAd();

        // Cancel Notification
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(getResources().getInteger(R.integer.DAILY_NOTIF_ID));
        nMgr.cancel(getResources().getInteger(R.integer.EVENT_NOTIF_ID));

        // Listener for connectivity change
        filterConnectivityChange = new IntentFilter();
        filterConnectivityChange.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        listenerConnectivityChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(!adLoaded){
                    bannerAdView.loadAd(Tools.getAdRequest(MainActivity.this));
                }

                if(mInterstitialAd==null){
                    addInterstitialAd();
                }
                //Tools.showOrHideBannerAd(MainActivity.this, bannerAdView);
            }
        };

        // Listener for date change to change hijri date
        filterDateChange = new IntentFilter();
        filterDateChange.addAction(Intent.ACTION_DATE_CHANGED);
        listenerDateChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                updateHijriDate();
            }
        };

        setNavigationDrawer();

        setMainLayout();

        searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.search_container, searchFragment).commit();
        searchContainer = findViewById(R.id.search_container);

        // hide searchContainer
        searchContainer.post(() -> {
            searchContainer.setTranslationY(-searchContainer.getHeight());
            searchContainer.setVisibility(View.GONE);

        });

        handleIntent(getIntent());

        checkForConsent();
    }

    private void addInterstitialAd() {

        InterstitialAd.load(this, getString(R.string.INTERSTITIAL_AD_UNIT_ID),
                Tools.getAdRequest(MainActivity.this), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                mInterstitialAd = null;
                                onClickMainFAB();
                            }

                            @Override
                            public void onAdImpression() {
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdClicked() {
                                // Show toast thank to user for ad clicked
                                mInterstitialAd = null;
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

    // Adding and setup ad Banner
    private void addBannerAd(){
        bannerView = findViewById(R.id.ads_shown);

        bannerAdView = new AdView(this);
        bannerAdView.setAdSize(AdSize.BANNER);
        bannerAdView.setAdUnitId(getString(R.string.BANNER_AD_UNIT_ID));
        bannerAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adLoaded = true;
                bannerView.addView(bannerAdView);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                adLoaded = false;
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Toast.makeText(mContext, R.string.ad_clicked, Toast.LENGTH_LONG).show();
            }
        });

        bannerAdView.loadAd(Tools.getAdRequest(MainActivity.this));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction()) && searchView != null) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, false);
            searchView.clearFocus();
        }
    }

    private void slideFromRightToLeft(View view) {
        TranslateAnimation animate = new TranslateAnimation(view.getWidth(),0f, 0f, 0f);
        animate.setDuration(200);
        view.startAnimation(animate);
    }

    private void setNavigationDrawer(){
        mainToolbar = findViewById(R.id.main_toolbar);
        mainToolbar.setTitleTextAppearance(mContext, R.style.MaturascTextAppearance);
        appBarLayout = findViewById(R.id.app_barr);
        secondaryToolbar = findViewById(R.id.secondary_toolbar);

        appBarLayout.setElevation(6*getResources().getDisplayMetrics().density);

        Typeface type = ResourcesCompat.getFont(mContext, R.font.maturasc);

        setSupportActionBar(mainToolbar);

        drawerLayout = findViewById(R.id.main_activity);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        NavigationView nv = findViewById(R.id.nv);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TextView splashTitle = nv.getHeaderView(0).findViewById(R.id.splash_app_title);
        splashTitle.setTypeface(type);

        nv.setNavigationItemSelectedListener((MenuItem item) -> {
            activeNavigationDrawerItem = item;
            item.setCheckable(true);
            item.setChecked(true);

            int id = item.getItemId();
            if(id == R.id.navbar_adab_sebab){
                String[] adabs = getResources().getStringArray(R.array.adab_content);
                String[] adabRefs = getResources().getStringArray(R.array.ref_adab);

                setDialog(getString(R.string.adab_title), adabs, adabRefs);
                return true;
            } else if( id == R.id.navbar_waktu_keadaan_tempat) {
                String[] waktus = getResources().getStringArray(R.array.waktu);
                String[] waktuRefs = getResources().getStringArray(R.array.waktu_ref);

                setDialog(getString(R.string.waktu_title), waktus, waktuRefs);
                return true;
            } else if( id == R.id.navbar_penghalang) {
                String[] halangs = getResources().getStringArray(R.array.halang);
                String[] halangRefs = getResources().getStringArray(R.array.halang_ref);

                setDialog(getString(R.string.halang_title), halangs, halangRefs);
                return true;
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);

                Intent settings = new Intent(mContext, SettingsActivity.class);

                settingsActivityResultLauncher.launch(settings);
                return true;
            }
        });

        /*@Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            // Settings Activity closed
            if(requestCode == 0){
                unCheckActiveNavigationDrawerItem();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }*/

        final CoordinatorLayout layout = findViewById(R.id.main_view);
        mSnackbar = Snackbar.make(layout, R.string.press_back_again, Snackbar.LENGTH_SHORT);

        TextView navbarFooter = findViewById(R.id.navbar_footer);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String versionName = pInfo.versionName;
            navbarFooter.setText(getString(R.string.version_name_format, versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //Set Hijri Date typepace and date
        hijriDateText = findViewById(R.id.hijri_date);
        hijriDateText.setTypeface(type);

        updateHijriDate();
    }

    private void updateHijriDate(){
        UmmalquraCalendar hijriDate = new UmmalquraCalendar();

        String[] days = getResources().getStringArray(R.array.dayOfWeek);
        String dayOfWeek = days[hijriDate.get(Calendar.DAY_OF_WEEK)-1];

        String dayOfMonth = String.valueOf(hijriDate.get(Calendar.DAY_OF_MONTH));

        String[] months = getResources().getStringArray(R.array.month);
        String month = months[hijriDate.get(Calendar.MONTH)];

        String year = String.valueOf(hijriDate.get(Calendar.YEAR));

        hijriDateText.setText(getString(R.string.hijriDateFormat , dayOfWeek, dayOfMonth, month, year));
    }

    private void setDialog(String title, String[] syaratContent, String[] refContent){
        View view=View.inflate(mContext, R.layout.syarat_dialog, null);

        TextView adabContent = view.findViewById(R.id.syarat_content);

        StringBuilder adabContentString = new StringBuilder();
        for(int i=0; i < syaratContent.length; i++){
            String content = (i+1)+". "+syaratContent[i]+".\n\n";
            adabContentString.append(content);
        }
        adabContent.setText(adabContentString.toString());

        TextView adabRef = view.findViewById(R.id.ref_content);

        StringBuilder adabRefString = new StringBuilder();
        for(String ref: refContent){
            String singleRef = ref+".\n";
            adabRefString.append(singleRef);
        }
        adabRef.setText(adabRefString.toString());

        showDialog(view, title);
    }

    private void showDialog(View view, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setView(view)
                .setTitle(title)
                .setPositiveButton(R.string.OK, (DialogInterface dialog, int which) ->
                    unCheckActiveNavigationDrawerItem()
                )
                .setOnCancelListener((DialogInterface dialog) ->
                unCheckActiveNavigationDrawerItem()
        );

        AlertDialog dialog = builder.create();
        if(dialog.getWindow() != null){
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        dialog.show();
    }

    public void unCheckActiveNavigationDrawerItem(){
        activeNavigationDrawerItem.setChecked(false);
        activeNavigationDrawerItem.setCheckable(false);
    }

    private void setMainLayout(){

        MainAdapter mAdapter = new MainAdapter(mContext, this);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(mAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                appBarLayout.setExpanded(true);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if(menuItemSearch != null){
                    menuItemSearch.collapseActionView();
                }
            }
        });

        // Give the TabLayout the ViewPager
        tabLayout = findViewById(R.id.tabs);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                true, (tab, position) -> tab.setText(mAdapter.getTabTitle(position)));
        tabLayoutMediator.attach();

        findFAB = findViewById(R.id.search_doa);
        findFAB.setOnClickListener((View v) -> {

            if(mInterstitialAd == null){
                addInterstitialAd();
            }

            // Show the ad if it's ready. Otherwise toast and restart the game.
            if (mInterstitialAd != null && Tools.is30SecondsPasses(mContext)) {
                mInterstitialAd.show(this);
                mInterstitialAd = null;
                addInterstitialAd();
            } else{
                onClickMainFAB();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuItemDarkLightMode = menu.findItem(R.id.action_dark_mode);

        menuItemSearch = menu.findItem(R.id.action_search);
        menuItemShareApp = menu.findItem(R.id.action_share_app);
        menuItemRateUs = menu.findItem(R.id.action_rate_us);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menuItemSearch.getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
        searchView.setImeOptions(EditorInfo.IME_ACTION_GO);

        menuItemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            final AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mainToolbar.getLayoutParams();

            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {

                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                slideFromRightToLeft(mainToolbar);

                showMenuItem(false);
                showToolbarUtil(View.GONE);

                params.setScrollFlags(0);

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {

                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                showMenuItem(true);
                showToolbarUtil(View.VISIBLE);

                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);

                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.isEmpty()){
                    if(mainToolbar.getElevation() == 0){
                        appBarLayout.setElevation(6 * getResources().getDisplayMetrics().density);
                    }

                    searchContainer.animate().translationY(-searchContainer.getHeight()).setListener(
                            new Animator.AnimatorListener() {

                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if(searchContainer.getVisibility() == View.VISIBLE){
                                        searchContainer.setVisibility(View.GONE);
                                    }

                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }
                    );

                    return true;
                } else{

                    searchContainer.animate().translationY(0).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            if(searchContainer.getVisibility() == View.GONE){
                                searchContainer.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    callSearch(newText);
                }
                return true;
            }

        });

        searchView.setOnQueryTextFocusChangeListener((View v, boolean hasFocus) ->{

                if (hasFocus) {
                    ObjectAnimator.ofFloat(findFAB, "rotation", 0f, 180f).setDuration(300).start();
                    findFAB.setImageResource(R.drawable.ic_close_white_56dp);
                } else {

                    ObjectAnimator.ofFloat(findFAB, "rotation", 180f, 0f).setDuration(300).start();
                    findFAB.setImageResource(R.drawable.ic_search_white_56dp);
                }
        });

        return true;
    }

    private void onClickMainFAB(){
        appBarLayout.getLayoutTransition().setDuration(300);

        // Cancel snackbar from showing
        if(mSnackbar.isShown()){
            mSnackbar.dismiss();
        }

        // Get the SearchView and set the searchable configuration
        if(menuItemSearch.isActionViewExpanded()){

            if(searchView.hasFocus()){
                menuItemSearch.collapseActionView();
            } else{
                searchView.requestFocus();
                showKeyboard();
            }

        } else{

            menuItemSearch.expandActionView();

        }
    }

    private void callSearch(String query){
        searchFragment.searchDoa(query);
    }

    private void showKeyboard(){
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void showMenuItem(boolean show){
        menuItemDarkLightMode.setVisible(show);
        menuItemShareApp.setVisible(show);
        menuItemRateUs.setVisible(show);
    }

    private void showToolbarUtil(int visible){

        secondaryToolbar.setVisibility(visible);
        tabLayout.setVisibility(visible);
        if(visible == View.VISIBLE){
            appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }else{
            appBarLayout.setBackgroundColor(getColorFromAttrs(R.attr.backgroundCardColor));
        }

    }

    private int getColorFromAttrs(int attrColor){

        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(attrColor, typedValue, true))
            return typedValue.data;
        else
            return Color.TRANSPARENT;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else if(item.getItemId() == R.id.action_dark_mode) {
            setActionDarkMode();
            return true;
        } else if(item.getItemId() == R.id.action_share_app) {
            shareDoaQu();
            return true;
        } else{
            rateUs();
            return true;
        }
    }

    public void setActionDarkMode(){

        Tools.changeDarkOrLightTheme(mContext);

        finish();

        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(mContext,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

        startActivity(getIntent(), bundle);
    }

    private void shareDoaQu(){
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        sharedIntent.setType("text/plain");
        sharedIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_doaqu_text));
        sharedIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared_doaqu_subject));
        startActivity(Intent.createChooser(sharedIntent, getString(R.string.shared_doaqu_title)));
    }

    private void rateUs(){
        // you can also use BuildConfig.APPLICATION_ID
        String appId = getPackageName();
        String market = "market://details?id=";
        String url = "https://play.google.com/store/apps/details?id=";

        Tools.searchInPlaystore(mContext, url, market, appId);
    }

    private void checkForConsent() {
        ConsentInformation consentInformation = ConsentInformation.getInstance(mContext);
        String[] publisherIds = {getString(R.string.ADMOB_PUBLISHER_ID)};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                switch (consentStatus) {
                    case PERSONALIZED:
                        Log.d(TAG, "Showing Personalized ads");
                        ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.PERSONALIZED);
                        break;
                    case NON_PERSONALIZED:
                        Log.d(TAG, "Showing Non-Personalized ads");
                        ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                        break;
                    case UNKNOWN:
                        Log.d(TAG, "Requesting Consent");
                        if (ConsentInformation.getInstance(getBaseContext())
                                .isRequestLocationInEeaOrUnknown()) {
                            requestConsent();
                        } else {
                            ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.PERSONALIZED);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String reason) {
                Log.d(TAG, "Failed Update Consent Info");
            }
        });
    }

    private void requestConsent() {
        URL privacyUrl = null;
        try {
            // your app's privacy policy URL.
            privacyUrl = new URL(getString(R.string.privacy_policy_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(mContext, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        Log.d(TAG, "Requesting Consent: onConsentFormLoaded");
                        showForm();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                        Log.d(TAG, "Requesting Consent: onConsentFormOpened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        Log.d(TAG, "Requesting Consent: onConsentFormClosed");
                        Log.d(TAG, "Requesting Consent: Requesting consent again");
                        switch (consentStatus) {
                            case PERSONALIZED:
                                ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.PERSONALIZED);
                                break;
                            case NON_PERSONALIZED:
                            case UNKNOWN:
                                ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                                break;
                        }
                        // Consent form was closed.
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        Log.d(TAG, "Requesting Consent: onConsentFormError. Error - " + errorDescription);
                        // Consent form error.
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build();
        form.load();
    }

    private void showForm() {
        if (form == null) {
            Log.d(TAG, "Consent form is null");
        }
        if (form != null) {
            Log.d(TAG, "Showing consent form");
            form.show();
        } else {
            Log.d(TAG, "Not Showing consent form");
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(viewPager.getCurrentItem() != 0){
            viewPager.setCurrentItem(0);
        } else{
            if (mSnackbar.isShown()) {
                super.onBackPressed();
            } else {
                mSnackbar.show();
            }
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(listenerDateChange);
        unregisterReceiver(listenerConnectivityChange);
        bannerAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(listenerDateChange, filterDateChange);
        registerReceiver(listenerConnectivityChange, filterConnectivityChange);
        bannerAdView.resume();
    }

    @Override
    protected void onDestroy() {
        bannerAdView.destroy();
        super.onDestroy();
    }
}