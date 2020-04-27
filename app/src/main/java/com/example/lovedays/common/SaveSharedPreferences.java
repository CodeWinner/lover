package com.example.lovedays.common;

import android.content.Context;
import android.content.SharedPreferences;


import static android.content.Context.MODE_PRIVATE;
import static com.example.lovedays.common.LoveCommon.KEY_POSITION_CAR_X;
import static com.example.lovedays.common.LoveCommon.KEY_UPDATE_START_DAY_COUNT;
import static com.example.lovedays.common.LoveCommon.NAME_SAVE_STORE;

public class SaveSharedPreferences {
    public Context context;

    SharedPreferences mPrefs ;
    public SaveSharedPreferences(Context context) {
        this.context = context;
    }

    public void saveUpdateStartDayCount(int value) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(KEY_UPDATE_START_DAY_COUNT, value);
        editor.commit();
    }

    public int getUpdateStartDayCount() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getInt(KEY_UPDATE_START_DAY_COUNT, 0);
    }

    public void savePositionCar(float pX) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putFloat(KEY_POSITION_CAR_X, pX);
        editor.commit();
    }

    public float getPositionCar() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getFloat(KEY_POSITION_CAR_X, 0);
    }
}
