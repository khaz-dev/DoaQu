package com.khazasid.android.doaqu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.khazasid.android.doaqu.Database.DoaSupportJoin;
import com.khazasid.android.doaqu.Tools.Tools;

public class DetailFragment extends Fragment implements View.OnClickListener {

    private AdView bannerAdView;
    private TextView mArabText, mLatinText, mTranslateText;
    private TextView numberText, titleText, footnoteText;
    private AppCompatImageButton mArabArrowButton, mLatinArrowButton, mTranslateArrowButton;
    private String sharedDoa, titleDoa;

    private boolean isBookmark;
    private short rowId;

    private FloatingActionButton bookmarkFAB;

    private DoaViewModel doaViewModel;

    private DoaSupportJoin detailDoaSupportJoin;

    private ScrollView detailFragment;

    private Snackbar mSnackBar;
    private Context mContext;

    private IntentFilter filterConnectivityChange;
    private BroadcastReceiver listenerConnectivityChange;

    private LinearLayout bannerView;
    private boolean adLoaded;

    static final String ROW_ID_KEY = "row_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        if(getArguments() != null && getArguments().containsKey(ROW_ID_KEY)){

            rowId = getArguments().getShort(ROW_ID_KEY);

            doaViewModel = new ViewModelProvider(this).get(DoaViewModel.class);

            detailDoaSupportJoin = doaViewModel.getAllDoaDetail(rowId);

        }

        // Listener for connectivity change
        filterConnectivityChange = new IntentFilter();
        filterConnectivityChange.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        listenerConnectivityChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(!adLoaded){
                    bannerAdView.loadAd(Tools.getAdRequest(getActivity()));
                }
            }
        };
    }

    public DetailFragment(){}

    static DetailFragment newInstance(short id){
        Bundle args = new Bundle();
        args.putShort(ROW_ID_KEY, id);
        DetailFragment f = new DetailFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_doa_detail, container, false);

        detailFragment = rootView.findViewById(R.id.detail_fragment);

        bannerView = rootView.findViewById(R.id.ads_shown);

        addBannerAdView();

        if(getActivity() != null){
            numberText = getActivity().findViewById(R.id.tvNumber);

            titleText = getActivity().findViewById(R.id.doa_title);

            footnoteText = getActivity().findViewById(R.id.doa_footnote);
        }

        // gain CardView shown status from preferences
        boolean isArabCVShown = Tools.getDoaQuPrefs(mContext).getBoolean(getString(R.string.arab_cv_pref), true);
        boolean isLatinCVShown = Tools.getDoaQuPrefs(mContext).getBoolean(getString(R.string.latin_cv_pref), true);
        boolean isTranslateCVShown = Tools.getDoaQuPrefs(mContext).getBoolean(getString(R.string.translate_cv_pref), true);

        // get every card view content
        mArabText = rootView.findViewById(R.id.doa_arab);
        mLatinText = rootView.findViewById(R.id.doa_latin);
        mTranslateText = rootView.findViewById(R.id.doa_translate);

        // get all ArrowButton from root
        mArabArrowButton = rootView.findViewById(R.id.arab_arrow_down);
        mLatinArrowButton = rootView.findViewById(R.id.latin_arrow_down);
        mTranslateArrowButton = rootView.findViewById(R.id.translate_arrow_down);

        // show/hide CardView content accordingly to its shown preferences
        shownOrHideCardViewContent(isArabCVShown, mArabText, mArabArrowButton);
        shownOrHideCardViewContent(isLatinCVShown, mLatinText, mLatinArrowButton);
        shownOrHideCardViewContent(isTranslateCVShown, mTranslateText, mTranslateArrowButton);

        // get all CardView view from root
        CardView mArabDoaCV = rootView.findViewById(R.id.arab_doa_cv);
        CardView mLatinDoaCV = rootView.findViewById(R.id.latin_doa_cv);
        CardView mTranslateDoaCV = rootView.findViewById(R.id.translate_doa_cv);

        // Set OnClickListener to CV and ArrowButton
        setOnClickOnCVandArrowButton(mArabDoaCV, mArabArrowButton);
        setOnClickOnCVandArrowButton(mLatinDoaCV, mLatinArrowButton);
        setOnClickOnCVandArrowButton(mTranslateDoaCV, mTranslateArrowButton);

        // get another view and set some listener again
        FloatingActionButton shareFAB = getActivity().findViewById(R.id.share_doa);
        shareFAB.setOnClickListener(this);

        bookmarkFAB = getActivity().findViewById(R.id.bookmark_doa);
        bookmarkFAB.setOnClickListener(this);

        if(detailDoaSupportJoin != null){

            // get all data from detailDoaSupportJoin
            short numberDoa = detailDoaSupportJoin.getRowId();
            titleDoa = detailDoaSupportJoin.getTitle();
            String arabDoa = detailDoaSupportJoin.getArabic();
            String latinDoa = detailDoaSupportJoin.getLatin();
            String translateDoa = detailDoaSupportJoin.getTranslate();
            String footnoteDoa = "(" + detailDoaSupportJoin.getFootnote() + ")";
            isBookmark = (detailDoaSupportJoin.getBookmark() == 1) ;

            // save for shared doa
            sharedDoa = titleDoa + "\n\n" + arabDoa + "\n" + latinDoa + "\n\n\"" + translateDoa + ".\"\n" +
                    footnoteDoa+ "\n\n";

            numberText.setText(String.valueOf(numberDoa));
            titleText.setText(titleDoa);
            footnoteText.setText(footnoteDoa);
            mArabText.setText(arabDoa);
            mLatinText.setText(latinDoa);
            mTranslateText.setText(getString(R.string.translate_doa, translateDoa));

            setBookmarkFAB(isBookmark);
        }

        return rootView;
    }

    private void addBannerAdView(){

        bannerAdView = new AdView(mContext);
        bannerAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        bannerAdView.setAdUnitId(getString(R.string.DETAIL_BANNER_AD_UNIT_ID));
        bannerAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                bannerView.addView(bannerAdView);
                adLoaded = true;
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

        bannerAdView.loadAd(Tools.getAdRequest(getActivity()));
    }

    private void shownOrHideCardViewContent(boolean isShown , TextView cardViewContent, AppCompatImageButton arrow){
        if(isShown){
            arrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_up_accent_24dp));
        }else{
            cardViewContent.post(()-> {
                cardViewContent.setTranslationY(-cardViewContent.getHeight());
                cardViewContent.setVisibility(View.GONE);
            });
            arrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_down_accent_24dp));
        }
    }

    private void setOnClickOnCVandArrowButton(CardView cardView, AppCompatImageButton arrowButton){
        cardView.setOnClickListener(this);
        arrowButton.setOnClickListener(this);
    }

    private void setBookmarkFAB(boolean bookmarkStatus){
        if(bookmarkStatus){
            bookmarkFAB.setImageResource(R.drawable.ic_star_bookmark_accent_40dp);
        }else{
            bookmarkFAB.setImageResource(R.drawable.ic_star_unbookmark_accent_40dp);
        }
    }

    private void colapseCardView(TextView colapseTextView, AppCompatImageButton cv_arrow, String CVprefs){

        SharedPreferences.Editor editor = Tools.getDoaQuPrefs(mContext).edit();

        ObjectAnimator anim;

        if(colapseTextView.getVisibility() == View.VISIBLE){
            // it's expanded - collapse it
            colapseTextView.animate().translationY(-colapseTextView.getHeight()).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    colapseTextView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            cv_arrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_down_accent_24dp));
            anim = ObjectAnimator.ofFloat(cv_arrow, "rotation", 180f, 0f);
            editor.putBoolean(CVprefs, false);
        } else{
            // it's collapsed - expand it
            colapseTextView.animate().translationY(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    colapseTextView.setVisibility(View.VISIBLE);
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
            cv_arrow.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_up_accent_24dp));
            anim = ObjectAnimator.ofFloat(cv_arrow, "rotation", -180f, 0f);
            editor.putBoolean(CVprefs, true);
        }

        editor.apply();
        anim.setDuration(200)
                .start();
    }

    private void sharedDoa(){
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        sharedIntent.setType("text/plain");
        sharedIntent.putExtra(Intent.EXTRA_TEXT, sharedDoa + getString(R.string.shared_doaqu_text));
        sharedIntent.putExtra(Intent.EXTRA_SUBJECT, titleDoa);
        startActivity(Intent.createChooser(sharedIntent, getString(R.string.shared_doa_title)));
    }

    private void bookmarkDoa(){
        if(mSnackBar != null){
            if(mSnackBar.isShown()){
                mSnackBar.dismiss();
            }
        }

        if(isBookmark){
            bookmarkFAB.setImageResource(R.drawable.ic_star_unbookmark_accent_40dp);
            mSnackBar = Snackbar.make(detailFragment, R.string.unbookmarked,
                    Snackbar.LENGTH_SHORT);
            doaViewModel.updateDoaSupportBookmark(rowId, (short)0);
            isBookmark = false;
        }else{
            bookmarkFAB.setImageResource(R.drawable.ic_star_bookmark_accent_40dp);
            mSnackBar = Snackbar.make(detailFragment, R.string.bookmarked,
                    Snackbar.LENGTH_SHORT);
            doaViewModel.updateDoaSupportBookmark(rowId, (short)1);
            isBookmark = true;
        }

        mSnackBar.show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.arab_doa_cv || view.getId() == R.id.arab_arrow_down){
            colapseCardView(mArabText, mArabArrowButton, getString(R.string.arab_cv_pref));
        } else if(view.getId() == R.id.latin_doa_cv || view.getId() == R.id.latin_arrow_down){
            colapseCardView(mLatinText, mLatinArrowButton, getString(R.string.latin_cv_pref));
        } else if(view.getId() == R.id.translate_doa_cv || view.getId() == R.id.translate_arrow_down){
            colapseCardView(mTranslateText, mTranslateArrowButton, getString(R.string.translate_cv_pref));
        } else if(view.getId() == R.id.share_doa){
            sharedDoa();
        } else{
            bookmarkDoa();
        }
    }

    @Override
    public void onPause() {
        if(getActivity() != null){
            getActivity().unregisterReceiver(listenerConnectivityChange);
        }
        bannerAdView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null){
            getActivity().registerReceiver(listenerConnectivityChange, filterConnectivityChange);
        }
        bannerAdView.resume();
    }

    @Override
    public void onDestroy() {
        bannerAdView.destroy();
        super.onDestroy();
    }
}
