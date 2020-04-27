package com.example.lovedays.main.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.lovedays.R;
import com.example.lovedays.main.units.InfoPersonal;

import static com.example.lovedays.common.LoveCommon.APP_BACKGROUND;
import static com.example.lovedays.common.LoveCommon.APP_LOGO_IMAGE;
import static com.example.lovedays.common.LoveCommon.APP_LOVE_TEXT;
import static com.example.lovedays.common.LoveCommon.APP_MUSIC;
import static com.example.lovedays.common.LoveCommon.APP_NOTIFICATION;
import static com.example.lovedays.common.LoveCommon.APP_RATED;
import static com.example.lovedays.common.LoveCommon.APP_START_DAY;
import static com.example.lovedays.common.LoveCommon.APP_THEME;
import static com.example.lovedays.common.LoveCommon.PERSONAL_BIRTH_DAY;
import static com.example.lovedays.common.LoveCommon.PERSONAL_COLOR_BODER_CODE;
import static com.example.lovedays.common.LoveCommon.PERSONAL_COLOR_TEXT;
import static com.example.lovedays.common.LoveCommon.PERSONAL_ID;
import static com.example.lovedays.common.LoveCommon.PERSONAL_IMAGE;
import static com.example.lovedays.common.LoveCommon.PERSONAL_NAME;
import static com.example.lovedays.common.LoveCommon.PERSONAL_SEX;
import static com.example.lovedays.common.LoveCommon.TABLE_INFO_APP;
import static com.example.lovedays.common.LoveCommon.TABLE_INFO_PERSONAL_NAME;
import static com.example.lovedays.common.LoveCommon.TABLE_TIME_LINE;
import static com.example.lovedays.common.LoveCommon.TL_CONTENT;
import static com.example.lovedays.common.LoveCommon.TL_DATE;
import static com.example.lovedays.common.LoveCommon.TL_ID;
import static com.example.lovedays.common.LoveCommon.TL_STATUS;
import static com.example.lovedays.common.LoveCommon.TL_SUBJECT;

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
        String createINFO_PERSONAL = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_INFO_PERSONAL_NAME, PERSONAL_ID, PERSONAL_NAME,
                PERSONAL_BIRTH_DAY, PERSONAL_SEX, PERSONAL_IMAGE,
                PERSONAL_COLOR_BODER_CODE, PERSONAL_COLOR_TEXT);

        String createINFO_APP = String.format("CREATE TABLE %s(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_INFO_APP, APP_START_DAY, APP_THEME,
                APP_BACKGROUND, APP_LOVE_TEXT, APP_MUSIC,
                APP_RATED, APP_NOTIFICATION, APP_LOGO_IMAGE);

        String addFirstRecord = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s , %s, %s) VALUES('20/04/2020', %s, %s, %s, %s, %s , %s, %s)",
                TABLE_INFO_APP, APP_START_DAY, APP_THEME,
                APP_BACKGROUND, APP_LOVE_TEXT, APP_MUSIC,
                APP_RATED, APP_NOTIFICATION, APP_LOGO_IMAGE,
                null, null, null, null, null, null, null);

        String createTIME_LINE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_TIME_LINE, TL_ID, TL_DATE,
                TL_SUBJECT, TL_CONTENT, TL_STATUS);

        db.execSQL(createINFO_PERSONAL);
        db.execSQL(createINFO_APP);
        db.execSQL(addFirstRecord);
        db.execSQL(createTIME_LINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
