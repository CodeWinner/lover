package com.bacnk.lovedays.main.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bacnk.lovedays.common.DateUnitCommon;
import com.bacnk.lovedays.common.DateUtils;
import com.bacnk.lovedays.common.LoveCommon;
import com.bacnk.lovedays.common.exception.LoveDaysCountDayException;
import com.bacnk.lovedays.common.service.CountDaysService;
import com.bacnk.lovedays.common.service.ServiceListener;
import com.bacnk.lovedays.R;
import com.bacnk.lovedays.main.database.DatabaseService;

public class FragmentCount extends Fragment implements ServiceListener {
    // Store instance variables

    private CountDaysService countDaysService;
    private boolean bound = false;

    private Intent notifyMeIntent;

    private TextView mYears, mMonths, mWeeks, mDays, mHours, mMinutes, mSeconds, mStartDay;
    private ImageView mImageViewCupid;
    private String startDay;
    private DatabaseService databaseService;

    @SuppressLint("ResourceType")
    private  Animation rotate;
    // newInstance constructor for creating fragment with arguments
    public static FragmentCount newInstance(String startDay) {
        FragmentCount fragmentCount = new FragmentCount();

        return fragmentCount;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_count, container, false);
        initView(view);

        int hour = 0;
        try {
            startDay = databaseService.getStartDay();
            hour = DateUtils.getTimeBetween(startDay).getHours();

            rotate = AnimationUtils.loadAnimation(getContext(), R.animator.slide_in_right);
            if (hour > 5 && hour < 19) {
                mImageViewCupid.setImageResource(R.drawable.cupid1);
            } else {
                mImageViewCupid.setImageResource(R.drawable.cupid_sleep);
            }
            mImageViewCupid.startAnimation(rotate);

            mStartDay.setText("#" + startDay);
        } catch (LoveDaysCountDayException e) {
            e.printStackTrace();
        }
        return view;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Open db
        databaseService = new DatabaseService(getContext());
        databaseService.open();
        startGPSService();

    }

    /**
     * Init view
     * @param view
     */
    private void initView(View view) {
        mYears = view.findViewById(R.id.mYears);
        mMonths = view.findViewById(R.id.mMonths);
        mWeeks = view.findViewById(R.id.mWeeks);
        mDays = view.findViewById(R.id.mDays);
        mHours = view.findViewById(R.id.mHours);
        mMinutes = view.findViewById(R.id.mMinutes);
        mSeconds = view.findViewById(R.id.mSeconds);
        mStartDay = view.findViewById(R.id.mStartDay);
        mImageViewCupid = view.findViewById(R.id.imageViewCupid);
    }

    /**
     * Set dsp
     * @param dateUnitCommon
     */
    private void setDispCount(final DateUnitCommon dateUnitCommon) {
        mYears.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                mYears.setText(Integer.toString(dateUnitCommon.getYears()));
                mMonths.setText(Integer.toString(dateUnitCommon.getMonths()));
                mWeeks.setText(Integer.toString(dateUnitCommon.getWeeks()));
                mDays.setText(Integer.toString(dateUnitCommon.getDays()));
                mHours.setText(String.format("%02d" , dateUnitCommon.getHours()));
                mMinutes.setText(String.format("%02d" , dateUnitCommon.getMinutes()));
                mSeconds.setText(String.format("%02d" , dateUnitCommon.getSeconds()));
                mStartDay.setText("#" + dateUnitCommon.getStrDay());
            }
        });
    }
    /**
     * Connect service
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            CountDaysService.CountDaysServiceBinder binder = (CountDaysService.CountDaysServiceBinder) service;
            countDaysService = binder.getService();
            bound = true;
            countDaysService.registerListener(FragmentCount.this); // register

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    private void bindService() {

        this.getContext().bindService(notifyMeIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(){
        if (bound) {
            countDaysService.registerListener(null); // unregister
            this.getContext().unbindService(serviceConnection);
            bound = false;
        }
    }

    @Override
    public void onDataReceived(DateUnitCommon dateUnitCommon) {
        setDispCount(dateUnitCommon);
    }

    @Override
    public void onMorning(final int isMorning) {
        mImageViewCupid.post(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                rotate = AnimationUtils.loadAnimation(getContext(), R.animator.slide_in_right);
                if (isMorning == LoveCommon.GOOD_MORNING) {
                    mImageViewCupid.setImageResource(R.drawable.cupid1);
                } else {
                    mImageViewCupid.setImageResource(R.drawable.cupid_sleep);
                }
                mImageViewCupid.startAnimation(rotate);
            }
        });
    }

    // Call this method somewhere to start Your GPSService
    private void startGPSService(){
        notifyMeIntent = new Intent(getContext(), CountDaysService.class);
        getContext().startService(notifyMeIntent );
    }

    @Override
    public void onStart() {
        super.onStart();
        bindService();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService();
    }

}

