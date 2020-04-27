package com.example.lovedays.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.lovedays.common.DateUnitCommon;
import com.example.lovedays.common.exception.LoveDaysCountDayException;
import com.example.lovedays.main.database.DatabaseService;
import com.example.lovedays.main.fragment.FragmentCount;

import java.util.ArrayList;
import java.util.Random;

import static com.example.lovedays.common.DateUtils.getTimeBetween;
import static com.example.lovedays.common.LoveCommon.GOOD_AFTERNOON;
import static com.example.lovedays.common.LoveCommon.GOOD_MORNING;

public class CountDaysService extends Service {
    public ServiceListener mListerner;

    public DateUnitCommon unitCommon;
    public DatabaseService databaseService;
    public String strDay;
    public int count = 0;
    public int countMorning = 0;
    public int sended = 0;
    private IBinder serviceBinder =  new CountDaysServiceBinder();

    public void registerListener(ServiceListener mListerner){
        this.mListerner= mListerner;
    }

    public class CountDaysServiceBinder extends Binder {

        public CountDaysService getService(){
            return CountDaysService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return serviceBinder;
    }

    @Override
    public void onCreate() {
        Log.i("LUCKY_NUMBER", "Start ne ...");
        super.onCreate();

        // Open database
        databaseService = new DatabaseService(getApplicationContext());
        databaseService.open();
        strDay = databaseService.getStartDay();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Thread timer = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run(){

                while (true) {
                    try {
                        Thread.sleep(1000);
                        count ++;
                        countMorning ++;
                        if (count == 3) {
                            strDay = databaseService.getStartDay();
                            count = 0;
                        }
                        try {
                            unitCommon = getTimeBetween(strDay);
                            if(countMorning == 300) {
                                int hour = unitCommon.getHours();
                                if (sended != GOOD_MORNING && hour > 5 && hour < 19) {
                                    // Morning
                                    mListerner.onMorning(GOOD_MORNING);
                                    sended = GOOD_MORNING;
                                    countMorning = 0;

                                } else if (sended != GOOD_AFTERNOON && (hour < 6 || hour > 18 )) {
                                    // Morning
                                    mListerner.onMorning(GOOD_AFTERNOON);
                                    sended = GOOD_AFTERNOON;
                                    countMorning = 0;
                                }
                            }

                            mListerner.onDataReceived(unitCommon);
                        } catch (LoveDaysCountDayException e) {
                            //e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
            }
        };
        timer.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
