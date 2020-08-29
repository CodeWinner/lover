package com.bacnk.lovedays.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;


import com.bacnk.lovedays.R;
import com.bacnk.lovedays.main.units.TitleDisplay;

import static android.content.Context.MODE_PRIVATE;
import static com.bacnk.lovedays.common.LoveCommon.KEY_APP_THEME;
import static com.bacnk.lovedays.common.LoveCommon.KEY_POSITION_CAR_X;
import static com.bacnk.lovedays.common.LoveCommon.KEY_RATED_APP;
import static com.bacnk.lovedays.common.LoveCommon.KEY_SHOWCASE_LOVE_MESS;
import static com.bacnk.lovedays.common.LoveCommon.KEY_SHOWCASE_START_DATE;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_1;
import static com.bacnk.lovedays.common.LoveCommon.KEY_UPDATE_MUSIC_BG;
import static com.bacnk.lovedays.common.LoveCommon.KEY_UPDATE_MUSIC_MESS;
import static com.bacnk.lovedays.common.LoveCommon.KEY_UPDATE_START_DAY_COUNT;
import static com.bacnk.lovedays.common.LoveCommon.KEY_UPDATE_TITLE_BOTTOM;
import static com.bacnk.lovedays.common.LoveCommon.KEY_UPDATE_TITLE_TOP;
import static com.bacnk.lovedays.common.LoveCommon.KEY_UPDATE_TOPIC;
import static com.bacnk.lovedays.common.LoveCommon.KEY_UPGRATE_APP;
import static com.bacnk.lovedays.common.LoveCommon.KEY_VERSION_APP;
import static com.bacnk.lovedays.common.LoveCommon.NAME_SAVE_STORE;

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
        return mPrefs.getBoolean(KEY_UPDATE_MUSIC_BG, false);
    }

    /**
     * Get music bg flag
     */
    public boolean isMusicBackgroundMessOnOff() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getBoolean(KEY_UPDATE_MUSIC_MESS, false);
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

    public void saveTopicMess(String topic) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY_UPDATE_TOPIC, topic);
        editor.commit();
    }

    public String getTopicMess() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getString(KEY_UPDATE_TOPIC, "");
    }

    public void saveTitleDisplay(String top, String bottom) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY_UPDATE_TITLE_TOP, top);
        editor.putString(KEY_UPDATE_TITLE_BOTTOM, bottom);
        editor.commit();
    }

    public TitleDisplay getTitleDisplay() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        TitleDisplay titleDisplay = new TitleDisplay();
        titleDisplay.setTitleTop(mPrefs.getString(KEY_UPDATE_TITLE_TOP, context.getString(R.string.title_top_display)));
        titleDisplay.setTitleBottom(mPrefs.getString(KEY_UPDATE_TITLE_BOTTOM, context.getString(R.string.title_bottom_display)));
        return titleDisplay;
    }

    public void saveShowCaseStartDay(boolean isShow) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(KEY_SHOWCASE_START_DATE, isShow);
        editor.commit();
    }

    public void saveShowCaseLoveMess(boolean isShow) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(KEY_SHOWCASE_LOVE_MESS, isShow);
        editor.commit();
    }

    public Boolean getShowCaseStartDay() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getBoolean(KEY_SHOWCASE_START_DATE, false);
    }

    public Boolean getShowCaseLoveMess() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getBoolean(KEY_SHOWCASE_LOVE_MESS, false);
    }

    public void saveRated(boolean isRate) {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(KEY_RATED_APP, isRate);
        editor.commit();
    }

    public Boolean getRated() {
        SharedPreferences mPrefs = context.getSharedPreferences(NAME_SAVE_STORE, MODE_PRIVATE);
        return mPrefs.getBoolean(KEY_RATED_APP, false);
    }
}
