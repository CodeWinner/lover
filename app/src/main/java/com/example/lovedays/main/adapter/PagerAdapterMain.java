package com.example.lovedays.main.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lovedays.main.fragment.FragmentCount;
import com.example.lovedays.main.fragment.FragmentMain;
import com.example.lovedays.main.fragment.FragmentStory;

public class PagerAdapterMain extends FragmentPagerAdapter {

    public PagerAdapterMain(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.i("VIEW_PAGER","VIEW_PAGER" + position);
        switch (position) {
            case 0:
                Log.i("VIEW_PAGER","PAGE #0");
                return FragmentMain.newInstance(0, "100");
            case 1:
                Log.i("VIEW_PAGER","PAGE #1");
                return FragmentCount.newInstance(1, "Chúng ta đã bên nhau...");
            case 2:
                Log.i("VIEW_PAGER","PAGE #2");
                return FragmentStory.newInstance(2, "PAGE #2");
             default:
                    return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Log.i("VIEW_PAGER",object.toString());
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
