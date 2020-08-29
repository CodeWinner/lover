package com.bacnk.lovedays.main.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bacnk.lovedays.main.fragment.FragmentCount;
import com.bacnk.lovedays.main.fragment.FragmentMain;
import com.bacnk.lovedays.main.fragment.FragmentStory;

public class PagerAdapterMain extends FragmentPagerAdapter {

    public PagerAdapterMain(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return FragmentMain.newInstance(0, "100");

            case 1:
                return FragmentCount.newInstance("");
            case 2:
                return FragmentStory.newInstance(2, "PAGE #2");
             default:
                    return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page BACC " + position;
    }
}
