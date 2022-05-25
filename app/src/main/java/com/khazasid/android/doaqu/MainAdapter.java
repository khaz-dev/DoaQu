package com.khazasid.android.doaqu;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainAdapter extends FragmentStateAdapter {

    private final Context mContext;

    MainAdapter(Context context, FragmentActivity fm) {
        super(fm);
        mContext = context;
    }

    public String getTabTitle(int position){
        switch(position){
            case 0:
                return mContext.getString(R.string.tab_doa);
            case 1:
                return mContext.getString(R.string.tab_bookmark);
            default:
                return null;
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0){
            return new DoaFragment();
        } else{
            return new BookmarkFragment();
        }
    }

    @Override
    public int getItemCount() {
        return mContext.getResources().getInteger(R.integer.PAGE_COUNT);
    }
}
