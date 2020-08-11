package com.example.lovedays.common;

import android.content.Context;
import android.content.SharedPreferences;


import static android.content.Context.MODE_PRIVATE;
import static com.example.lovedays.common.LoveCommon.KEY_APP_THEME;
import static com.example.lovedays.common.LoveCommon.KEY_POSITION_CAR_X;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_1;
import static com.example.lovedays.common.LoveCommon.KEY_UPDATE_MUSIC_BG;
import static com.example.lovedays.common.LoveCommon.KEY_UPDATE_MUSIC_MESS;
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

    /**
     * Save on off music
     * @param isOn
     */
    public void saveMusicBackgroundOnOff(boolean isOn) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(KEY_UPDATE_MUSIC_BG, isOn);
        editor.commit();
    }

    /**
     * Get music bg flag
     */
    public boolean isMusicBackgroundOnOff() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getBoolean(KEY_UPDATE_MUSIC_BG, true);
    }

    /**
     * Get music bg flag
     */
    public boolean isMusicBackgroundMessOnOff() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getBoolean(KEY_UPDATE_MUSIC_MESS, true);
    }

    /**
     * Save on off music
     * @param isOn
     */
    public void saveMusicBackgroundMessOnOff(boolean isOn) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(KEY_UPDATE_MUSIC_MESS, isOn);
        editor.commit();
    }

    /**
     * Save app theme
     * @param appTheme
     */
    public void saveAppTheme(int appTheme) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(KEY_APP_THEME, appTheme);
        editor.commit();
    }

    /**
     * Get app theme
     * @return
     */
    public int getAppTheme() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getInt(KEY_APP_THEME, KEY_THEME_COLOR_1);
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
