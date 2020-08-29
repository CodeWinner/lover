package com.bacnk.lovedays.main.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.bacnk.lovedays.common.LoveCommon.APP_BACKGROUND;
import static com.bacnk.lovedays.common.LoveCommon.APP_LOGO_IMAGE;
import static com.bacnk.lovedays.common.LoveCommon.APP_LOVE_TEXT;
import static com.bacnk.lovedays.common.LoveCommon.APP_MUSIC_MESS_NAME;
import static com.bacnk.lovedays.common.LoveCommon.APP_MUSIC_MESS_PATH;
import static com.bacnk.lovedays.common.LoveCommon.APP_MUSIC_NAME;
import static com.bacnk.lovedays.common.LoveCommon.APP_MUSIC_PATH;
import static com.bacnk.lovedays.common.LoveCommon.APP_NOTIFICATION;
import static com.bacnk.lovedays.common.LoveCommon.APP_RATED;
import static com.bacnk.lovedays.common.LoveCommon.APP_START_DAY;
import static com.bacnk.lovedays.common.LoveCommon.APP_THEME;
import static com.bacnk.lovedays.common.LoveCommon.PERSONAL_BIRTH_DAY;
import static com.bacnk.lovedays.common.LoveCommon.PERSONAL_COLOR_BODER_CODE;
import static com.bacnk.lovedays.common.LoveCommon.PERSONAL_COLOR_TEXT;
import static com.bacnk.lovedays.common.LoveCommon.PERSONAL_ID;
import static com.bacnk.lovedays.common.LoveCommon.PERSONAL_IMAGE;
import static com.bacnk.lovedays.common.LoveCommon.PERSONAL_NAME;
import static com.bacnk.lovedays.common.LoveCommon.PERSONAL_SEX;
import static com.bacnk.lovedays.common.LoveCommon.TABLE_INFO_APP;
import static com.bacnk.lovedays.common.LoveCommon.TABLE_INFO_PERSONAL_NAME;
import static com.bacnk.lovedays.common.LoveCommon.TABLE_TIME_LINE;
import static com.bacnk.lovedays.common.LoveCommon.TL_CONTENT;
import static com.bacnk.lovedays.common.LoveCommon.TL_COUNT_DATE;
import static com.bacnk.lovedays.common.LoveCommon.TL_DATE;
import static com.bacnk.lovedays.common.LoveCommon.TL_ID;
import static com.bacnk.lovedays.common.LoveCommon.TL_IMAGE_PATH_1;
import static com.bacnk.lovedays.common.LoveCommon.TL_IMAGE_PATH_2;
import static com.bacnk.lovedays.common.LoveCommon.TL_IMAGE_PATH_3;
import static com.bacnk.lovedays.common.LoveCommon.TL_STATUS;
import static com.bacnk.lovedays.common.LoveCommon.TL_SUBJECT;
import static com.bacnk.lovedays.common.LoveCommon.TL_TYPE;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LoveDayManager";
    private static final int DB_VERSION = 1;

    // Table INFO_PERSONAL

    // Table INFOR_APP
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createINFO_PERSONAL = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                    TABLE_INFO_PERSONAL_NAME, PERSONAL_ID, PERSONAL_NAME,
                    PERSONAL_BIRTH_DAY, PERSONAL_SEX, PERSONAL_IMAGE,
                    PERSONAL_COLOR_BODER_CODE, PERSONAL_COLOR_TEXT);

            String createINFO_APP = String.format("CREATE TABLE %s(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                    TABLE_INFO_APP, APP_START_DAY, APP_THEME, APP_MUSIC_NAME, APP_MUSIC_PATH,
                    APP_BACKGROUND, APP_LOVE_TEXT, APP_MUSIC_MESS_NAME, APP_MUSIC_MESS_PATH,
                    APP_RATED, APP_NOTIFICATION, APP_LOGO_IMAGE);

            String createTIME_LINE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                    TABLE_TIME_LINE, TL_ID, TL_DATE,
                    TL_SUBJECT, TL_CONTENT, TL_STATUS, TL_TYPE,
                    TL_IMAGE_PATH_1, TL_IMAGE_PATH_2, TL_IMAGE_PATH_3, TL_COUNT_DATE);

            db.execSQL(createINFO_PERSONAL);
            db.execSQL(createINFO_APP);
            db.execSQL(createTIME_LINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO_PERSONAL_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO_APP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME_LINE);
        onCreate(db);
    }

}
