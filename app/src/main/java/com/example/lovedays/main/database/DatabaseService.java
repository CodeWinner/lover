package com.example.lovedays.main.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.lovedays.R;
import com.example.lovedays.main.units.InfoApp;
import com.example.lovedays.main.units.InfoPersonal;
import com.example.lovedays.main.units.TimeLine;

import java.util.ArrayList;
import java.util.List;

import static com.example.lovedays.common.LoveCommon.APP_BACKGROUND;
import static com.example.lovedays.common.LoveCommon.APP_LOGO_IMAGE;
import static com.example.lovedays.common.LoveCommon.APP_LOVE_TEXT;
import static com.example.lovedays.common.LoveCommon.APP_MUSIC_MESS_NAME;
import static com.example.lovedays.common.LoveCommon.APP_MUSIC_MESS_PATH;
import static com.example.lovedays.common.LoveCommon.APP_MUSIC_NAME;
import static com.example.lovedays.common.LoveCommon.APP_MUSIC_PATH;
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
import static com.example.lovedays.common.LoveCommon.TL_COUNT_DATE;
import static com.example.lovedays.common.LoveCommon.TL_DATE;
import static com.example.lovedays.common.LoveCommon.TL_ID;
import static com.example.lovedays.common.LoveCommon.TL_IMAGE_PATH_1;
import static com.example.lovedays.common.LoveCommon.TL_IMAGE_PATH_2;
import static com.example.lovedays.common.LoveCommon.TL_IMAGE_PATH_3;
import static com.example.lovedays.common.LoveCommon.TL_STATUS;
import static com.example.lovedays.common.LoveCommon.TL_SUBJECT;
import static com.example.lovedays.common.LoveCommon.TL_TYPE;

public class DatabaseService {
    private DatabaseLoverOnListener mListener;
    private DatabaseInfoAppListener mInforAppListener;
    private DatabaseTimelineOnListener mTimlineListener;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    public DatabaseService (Context context, DatabaseLoverOnListener mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public DatabaseService (Context context, DatabaseInfoAppListener mInforAppListener) {
        this.context = context;
        this.mInforAppListener = mInforAppListener;
    }

    public DatabaseService (Context context, DatabaseTimelineOnListener mTimlineListener) {
        this.context = context;
        this.mTimlineListener = mTimlineListener;
    }

    public DatabaseService (Context context){
        this.context = context;
    }
    /**
     * Open SQL
     * @return
     * @throws SQLException
     */
    public DatabaseService open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Add info person
     * @param infoPersonal
     * @throws Exception
     */
    public void addPerson(InfoPersonal infoPersonal) {
        try {
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(PERSONAL_ID, infoPersonal.getId());
            values.put(PERSONAL_NAME, infoPersonal.getName());
            values.put(PERSONAL_BIRTH_DAY, infoPersonal.getBirthDay());
            values.put(PERSONAL_SEX, infoPersonal.getSex());
            values.put(PERSONAL_IMAGE, infoPersonal.getImage());
            values.put(PERSONAL_COLOR_BODER_CODE, infoPersonal.getColorBoderCode());
            values.put(PERSONAL_COLOR_TEXT, infoPersonal.getColorText());

            database.insert(TABLE_INFO_PERSONAL_NAME, null, values);
            mListener.updPersonSuccess(infoPersonal.getId());
        } catch (SQLException e) {
            mListener.updPersonError(context.getApplicationContext().getResources().getString(R.string.update_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    /**
     * Update person
     * @param infoPersonal
     */
    public void updatePerson(InfoPersonal infoPersonal) {
            try {
                database = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(PERSONAL_NAME, infoPersonal.getName());
                values.put(PERSONAL_BIRTH_DAY, infoPersonal.getBirthDay());
                values.put(PERSONAL_SEX, infoPersonal.getSex());
                values.put(PERSONAL_IMAGE, infoPersonal.getImage());
                values.put(PERSONAL_COLOR_BODER_CODE, infoPersonal.getColorBoderCode());
                values.put(PERSONAL_COLOR_TEXT, infoPersonal.getColorText());

                database.update(TABLE_INFO_PERSONAL_NAME, values, PERSONAL_ID + " = ?", new String[]{infoPersonal.getId()} );
                mListener.updPersonSuccess(infoPersonal.getId());
            } catch (SQLException e) {
                mListener.updPersonError(context.getApplicationContext().getResources().getString(R.string.update_error));
            } finally {
                if (database != null) {
                    database.close();
                }
            }
    }

    /**
     * GetInfor person
     * @param id
     */
    public InfoPersonal getPerson(String id) {
        try {
            database = dbHelper.getReadableDatabase();

            Cursor cursor = database.query(TABLE_INFO_PERSONAL_NAME, null, PERSONAL_ID + " = ?", new String[] { String.valueOf(id) },null, null, null);
            if(cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() != 0) {
                    InfoPersonal personal = new InfoPersonal();
                    personal.setId(cursor.getString(0));
                    personal.setName(cursor.getString(1));
                    personal.setBirthDay(cursor.getString(2));
                    personal.setSex(cursor.getString(3));
                    personal.setImage(cursor.getString(4));
                    personal.setColorBoderCode(cursor.getString(5));
                    personal.setColorText(cursor.getString(6));

                    return personal;
                }
            }

        } catch (SQLException e) {
            mListener.getPersonError(context.getApplicationContext().getResources().getString(R.string.get_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }

        return null;
    }

    /**
     * Get all person
     * @return
     */
    public List<InfoPersonal> getAllPersons() {
        try {
            List<InfoPersonal>  personalList = new ArrayList<>();
            String query = "SELECT * FROM " + TABLE_INFO_PERSONAL_NAME;

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            while(cursor.isAfterLast() == false) {

                InfoPersonal personal = new InfoPersonal();
                personal.setId(cursor.getString(0));
                personal.setName(cursor.getString(1));
                personal.setBirthDay(cursor.getString(2));
                personal.setSex(cursor.getString(3));
                personal.setImage(cursor.getString(4));
                personal.setColorBoderCode(cursor.getString(5));
                personal.setColorText(cursor.getString(6));

                personalList.add(personal);
                cursor.moveToNext();
            }
            return personalList;
        } catch (SQLException e) {
            mListener.getPersonError(context.getApplicationContext().getResources().getString(R.string.get_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return null;
    }

    /**
     * Check ton tai person
     * @param id
     */
    public boolean checkExistPerson(String id) {
        try {
            database = dbHelper.getReadableDatabase();

            Cursor cursor = database.query(TABLE_INFO_PERSONAL_NAME, null, PERSONAL_ID + " = ?", new String[] { String.valueOf(id) },null, null, null);
            if(cursor != null && cursor.getCount() != 0) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            mListener.getPersonError(context.getApplicationContext().getResources().getString(R.string.get_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }

        return false;
    }

    /******************************************************************************************/
    // INFO_APP
    /******************************************************************************************/

    /**
     * Add info person
     * @throws Exception
     */
    public void addDefaultInfo() {
        try {
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(APP_START_DAY, "11/08/2020");
            values.put(APP_THEME, "");
            values.put(APP_MUSIC_NAME, "");
            values.put(APP_MUSIC_PATH, "");
            values.put(APP_BACKGROUND, "");
            values.put(APP_LOVE_TEXT, "");
            values.put(APP_MUSIC_MESS_NAME, "");
            values.put(APP_MUSIC_MESS_PATH, "");
            values.put(APP_RATED, "");
            values.put(APP_NOTIFICATION, "");
            values.put(APP_LOGO_IMAGE, "");

            database.insert(TABLE_INFO_APP, null, values);
        } catch (SQLException e) {
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    /**
     * Update start day
     * @param startDay
     */
    public void updateStartDay(String startDay) {
        try {
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(APP_START_DAY, startDay);


            database.update(TABLE_INFO_APP, values, null, null );
            mInforAppListener.updPersonSuccess(startDay);
        } catch (SQLException e) {
            mInforAppListener.updPersionError(context.getApplicationContext().getResources().getString(R.string.update_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    /**
     * Get start day
     * @return
     */
    public String getStartDay() {
        try {
            database = dbHelper.getReadableDatabase();

            Cursor cursor = database.query(TABLE_INFO_APP, new String[]{APP_START_DAY}, null, null,null, null, null);
            if(cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() != 0) {
                    return cursor.getString(0);
                }
            }

        } catch (SQLException e) {

        } finally {
            if (database != null) {
                database.close();
            }
        }

        return null;
    }

    // Background
    /**
     * Update start day
     * @param pathImage
     */
    public void updateImageBackground(String pathImage) {
        try {
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(APP_BACKGROUND, pathImage);


            database.update(TABLE_INFO_APP, values, null, null );
        } catch (SQLException e) {
            mInforAppListener.updateInforError(context.getApplicationContext().getResources().getString(R.string.update_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    /**
     * Get start day
     * @return
     */
    public String getImageBackground() {
        try {
            database = dbHelper.getReadableDatabase();

            Cursor cursor = database.query(TABLE_INFO_APP, new String[]{APP_BACKGROUND}, null, null,null, null, null);
            if(cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() != 0) {
                    return cursor.getString(0);
                }
            }

        } catch (SQLException e) {

        } finally {
            if (database != null) {
                database.close();
            }
        }

        return null;
    }

    // Music background
    /**
     * Update start day
     * @param pathMusic
     */
    public void updateMusicBackground(String musicName, String pathMusic) {
        try {
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(APP_MUSIC_NAME, musicName);
            values.put(APP_MUSIC_PATH, pathMusic);


            database.update(TABLE_INFO_APP, values, null, null );
        } catch (SQLException e) {
            mInforAppListener.updateInforError(context.getApplicationContext().getResources().getString(R.string.update_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    /**
     * Get start day
     * @return
     */
    public String getMusicBackground() {
        try {
            database = dbHelper.getReadableDatabase();

            Cursor cursor = database.query(TABLE_INFO_APP, new String[]{APP_BACKGROUND}, null, null,null, null, null);
            if(cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() != 0) {
                    return cursor.getString(0);
                }
            }

        } catch (SQLException e) {

        } finally {
            if (database != null) {
                database.close();
            }
        }

        return null;
    }


    // Mess
    /**
     * Update Mess infor
     * @param pathMusic
     */
    public void updateInforMess(String mess, String musicName,String pathMusic) {
        try {
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(APP_MUSIC_MESS_NAME, musicName);
            values.put(APP_MUSIC_MESS_PATH, pathMusic);
            values.put(APP_LOVE_TEXT, mess);


            database.update(TABLE_INFO_APP, values, null, null );
            mInforAppListener.updPersonSuccess(pathMusic);
        } catch (SQLException e) {
            mInforAppListener.updateInforError(context.getApplicationContext().getResources().getString(R.string.update_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }


    /**
     * Get infor setting app
     * @return
     */
    public InfoApp getInforApp() {
        try {
            List<InfoApp>  infoApps = new ArrayList<>();
            String query = "SELECT * FROM " + TABLE_INFO_APP;

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {

                InfoApp infoApp = new InfoApp();
                infoApp.setStartDay(cursor.getString(0));
                infoApp.setAppTheme(cursor.getString(1));
                infoApp.setMusicNameBackGround(cursor.getString(2));
                infoApp.setMusicPathBackGround(cursor.getString(3));
                infoApp.setBackground(cursor.getString(4));
                infoApp.setLoveText(cursor.getString(5));
                infoApp.setMusicNameMess(cursor.getString(6));
                infoApp.setMusicPathMess(cursor.getString(7));
                infoApp.setRated(cursor.getString(8));
                infoApp.setNotifaction(cursor.getString(9));
                infoApp.setLogoImage(cursor.getString(10));

                infoApps.add(infoApp);
                cursor.moveToNext();
            }
            return infoApps.get(0);
        } catch (SQLException e) {
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return null;
    }
    //===========================================================================================
    /**
     * History
     */
    //===========================================================================================
    /**
     * Add timeline
     * @param timeLine
     */
    public void addTimeline(TimeLine timeLine) {
        try {
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TL_ID, timeLine.getId());
            values.put(TL_DATE, timeLine.getDate());
            values.put(TL_CONTENT, timeLine.getContent());
            values.put(TL_SUBJECT, timeLine.getSubject());
            values.put(TL_IMAGE_PATH_1, timeLine.getImagePath1());
            values.put(TL_IMAGE_PATH_2, timeLine.getImagePath2());
            values.put(TL_IMAGE_PATH_3, timeLine.getImagePath3());
            values.put(TL_STATUS, timeLine.getStatus());
            values.put(TL_TYPE, timeLine.getType());
            values.put(TL_COUNT_DATE, timeLine.getCountDate());

            database.insert(TABLE_TIME_LINE, null, values);
            mTimlineListener.addTimelineSuccess(timeLine.getId());
        } catch (SQLException e) {
            mTimlineListener.addTimeleError(context.getApplicationContext().getResources().getString(R.string.update_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    /**
     * Update start day
     * @param timeLine
     */
    public void updateTimeLine(TimeLine timeLine) {
        try {
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TL_ID, timeLine.getId());
            values.put(TL_DATE, timeLine.getDate());
            values.put(TL_CONTENT, timeLine.getContent());
            values.put(TL_SUBJECT, timeLine.getSubject());
            values.put(TL_IMAGE_PATH_1, timeLine.getImagePath1());
            values.put(TL_IMAGE_PATH_2, timeLine.getImagePath2());
            values.put(TL_IMAGE_PATH_3, timeLine.getImagePath3());
            values.put(TL_STATUS, timeLine.getStatus());
            values.put(TL_TYPE, timeLine.getType());
            values.put(TL_COUNT_DATE, timeLine.getCountDate());


            database.update(TABLE_TIME_LINE, values, TL_ID + " = ?", new String[]{timeLine.getId()});

            mTimlineListener.updateTimelineSuccess(timeLine.getId());
        } catch (SQLException e) {
            mTimlineListener.updateTimelineError(context.getApplicationContext().getResources().getString(R.string.update_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    /**
     * Get a time line
     * @return
     */
    public TimeLine getTimeline(String idTimeline) {
        try {
            database = dbHelper.getReadableDatabase();

            Cursor cursor = database.query(TABLE_TIME_LINE, null, TL_ID + " = ?", new String[] { String.valueOf(idTimeline) },null, null, null);
            if(cursor != null) {
                cursor.moveToFirst();

                if (cursor.getCount() != 0) {
                    TimeLine timeLine = new TimeLine();
                    timeLine.setId(cursor.getString(0));
                    timeLine.setDate(cursor.getString(1));
                    timeLine.setSubject(cursor.getString(2));
                    timeLine.setContent(cursor.getString(3));
                    timeLine.setStatus(cursor.getString(4));
                    timeLine.setType(cursor.getString(5));
                    timeLine.setImagePath1(cursor.getString(6));
                    timeLine.setImagePath2(cursor.getString(7));
                    timeLine.setImagePath3(cursor.getString(8));
                    timeLine.setCountDate(cursor.getString(9));
                    return timeLine;
                }
            }

        } catch (SQLException e) {
            mTimlineListener.getTimelineError(context.getApplicationContext().getResources().getString(R.string.get_error));
        } finally {
            if (database != null) {
                database.close();
            }
        }

        return null;
    }

    /**
     * Get all person
     * @return
     */
    public List<TimeLine> getAllTimeline() {
        try {
            List<TimeLine>  timeLineList = new ArrayList<>();
            String query = "SELECT ID, TL_DATE, TL_SUBJECT," +
                    " TL_CONTENT, TL_STATUS, TL_TYPE, TL_IMAGE_PATH_1, TL_IMAGE_PATH_2, TL_IMAGE_PATH_3," +
                    " TL_COUNT_DATE FROM "
                    + TABLE_TIME_LINE;

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            while(cursor.isAfterLast() == false) {

                TimeLine timeLine = new TimeLine();
                timeLine.setId(cursor.getString(0));
                timeLine.setDate(cursor.getString(1));
                timeLine.setSubject(cursor.getString(2));
                timeLine.setContent(cursor.getString(3));
                timeLine.setStatus(cursor.getString(4));
                timeLine.setType(cursor.getString(5));
                timeLine.setImagePath1(cursor.getString(6));
                timeLine.setImagePath2(cursor.getString(7));
                timeLine.setImagePath3(cursor.getString(8));
                timeLine.setCountDate(cursor.getString(9));

                timeLineList.add(timeLine);
                cursor.moveToNext();
            }
            return timeLineList;
        } catch (SQLException e) {

        } finally {
            if (database != null) {
                database.close();
            }
        }
        return null;
    }

    /**
     * Delete time line
     * @param idTime
     * @param position
     */
    public void deleteTimeline(String idTime, int position) {
        try {
            dbHelper.getWritableDatabase().delete(TABLE_TIME_LINE, TL_ID +"=?", new String[]{idTime});
            mTimlineListener.delTimelineSuccess(position);
        } catch (Exception e) {
            mTimlineListener.delTimelineError(context.getApplicationContext().getResources().getString(R.string.get_error));
        }
    }
}
