package com.bacnk.lovedays.main.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bacnk.lovedays.R;
import com.bacnk.lovedays.common.LoveCommon;
import com.bacnk.lovedays.main.units.InfoApp;
import com.bacnk.lovedays.main.units.InfoPersonal;
import com.bacnk.lovedays.main.units.TimeLine;

import java.util.ArrayList;
import java.util.List;

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
            values.put(LoveCommon.PERSONAL_ID, infoPersonal.getId());
            values.put(LoveCommon.PERSONAL_NAME, infoPersonal.getName());
            values.put(LoveCommon.PERSONAL_BIRTH_DAY, infoPersonal.getBirthDay());
            values.put(LoveCommon.PERSONAL_SEX, infoPersonal.getSex());
            values.put(LoveCommon.PERSONAL_IMAGE, infoPersonal.getImage());
            values.put(LoveCommon.PERSONAL_COLOR_BODER_CODE, infoPersonal.getColorBoderCode());
            values.put(LoveCommon.PERSONAL_COLOR_TEXT, infoPersonal.getColorText());

            database.insert(LoveCommon.TABLE_INFO_PERSONAL_NAME, null, values);
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
                values.put(LoveCommon.PERSONAL_NAME, infoPersonal.getName());
                values.put(LoveCommon.PERSONAL_BIRTH_DAY, infoPersonal.getBirthDay());
                values.put(LoveCommon.PERSONAL_SEX, infoPersonal.getSex());
                values.put(LoveCommon.PERSONAL_IMAGE, infoPersonal.getImage());
                values.put(LoveCommon.PERSONAL_COLOR_BODER_CODE, infoPersonal.getColorBoderCode());
                values.put(LoveCommon.PERSONAL_COLOR_TEXT, infoPersonal.getColorText());

                database.update(LoveCommon.TABLE_INFO_PERSONAL_NAME, values, LoveCommon.PERSONAL_ID + " = ?", new String[]{infoPersonal.getId()} );
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

            Cursor cursor = database.query(LoveCommon.TABLE_INFO_PERSONAL_NAME, null, LoveCommon.PERSONAL_ID + " = ?", new String[] { String.valueOf(id) },null, null, null);
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
            String query = "SELECT * FROM " + LoveCommon.TABLE_INFO_PERSONAL_NAME;

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

            Cursor cursor = database.query(LoveCommon.TABLE_INFO_PERSONAL_NAME, null, LoveCommon.PERSONAL_ID + " = ?", new String[] { String.valueOf(id) },null, null, null);
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
            values.put(LoveCommon.APP_START_DAY, "11/08/2020");
            values.put(LoveCommon.APP_THEME, "");
            values.put(LoveCommon.APP_MUSIC_NAME, "");
            values.put(LoveCommon.APP_MUSIC_PATH, "");
            values.put(LoveCommon.APP_BACKGROUND, "");
            values.put(LoveCommon.APP_LOVE_TEXT, context.getString(R.string.love_text_default));
            values.put(LoveCommon.APP_MUSIC_MESS_NAME, "");
            values.put(LoveCommon.APP_MUSIC_MESS_PATH, "");
            values.put(LoveCommon.APP_RATED, "");
            values.put(LoveCommon.APP_NOTIFICATION, "");
            values.put(LoveCommon.APP_LOGO_IMAGE, "");

            database.insert(LoveCommon.TABLE_INFO_APP, null, values);
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
            values.put(LoveCommon.APP_START_DAY, startDay);


            database.update(LoveCommon.TABLE_INFO_APP, values, null, null );
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

            if (database != null){
                Cursor cursor = database.query(LoveCommon.TABLE_INFO_APP, new String[]{LoveCommon.APP_START_DAY}, null, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    if (cursor.getCount() != 0) {
                        return cursor.getString(0);
                    }
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
            values.put(LoveCommon.APP_BACKGROUND, pathImage);


            database.update(LoveCommon.TABLE_INFO_APP, values, null, null );
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

            Cursor cursor = database.query(LoveCommon.TABLE_INFO_APP, new String[]{LoveCommon.APP_BACKGROUND}, null, null,null, null, null);
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
            values.put(LoveCommon.APP_MUSIC_NAME, musicName);
            values.put(LoveCommon.APP_MUSIC_PATH, pathMusic);


            database.update(LoveCommon.TABLE_INFO_APP, values, null, null );
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

            Cursor cursor = database.query(LoveCommon.TABLE_INFO_APP, new String[]{LoveCommon.APP_BACKGROUND}, null, null,null, null, null);
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
            values.put(LoveCommon.APP_MUSIC_MESS_NAME, musicName);
            values.put(LoveCommon.APP_MUSIC_MESS_PATH, pathMusic);
            values.put(LoveCommon.APP_LOVE_TEXT, mess);


            database.update(LoveCommon.TABLE_INFO_APP, values, null, null );
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
     * Get love text
     * @return
     */
    public String getLoveText() {
        try {
            database = dbHelper.getReadableDatabase();

            Cursor cursor = database.query(LoveCommon.TABLE_INFO_APP, new String[]{LoveCommon.APP_LOVE_TEXT}, null, null,null, null, null);
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

    /**
     * Get infor setting app
     * @return
     */
    public InfoApp getInforApp() {
        try {
            List<InfoApp>  infoApps = new ArrayList<>();
            String query = "SELECT * FROM " + LoveCommon.TABLE_INFO_APP;

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
            values.put(LoveCommon.TL_ID, timeLine.getId());
            values.put(LoveCommon.TL_DATE, timeLine.getDate());
            values.put(LoveCommon.TL_CONTENT, timeLine.getContent());
            values.put(LoveCommon.TL_SUBJECT, timeLine.getSubject());
            values.put(LoveCommon.TL_IMAGE_PATH_1, timeLine.getImagePath1());
            values.put(LoveCommon.TL_IMAGE_PATH_2, timeLine.getImagePath2());
            values.put(LoveCommon.TL_IMAGE_PATH_3, timeLine.getImagePath3());
            values.put(LoveCommon.TL_STATUS, timeLine.getStatus());
            values.put(LoveCommon.TL_TYPE, timeLine.getType());
            values.put(LoveCommon.TL_COUNT_DATE, timeLine.getCountDate());

            database.insert(LoveCommon.TABLE_TIME_LINE, null, values);
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
     * Add timeline
     * @param timeLine
     */
    public void addTimelineNoListener(TimeLine timeLine) {
        try {
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(LoveCommon.TL_ID, timeLine.getId());
            values.put(LoveCommon.TL_DATE, timeLine.getDate());
            values.put(LoveCommon.TL_CONTENT, timeLine.getContent());
            values.put(LoveCommon.TL_SUBJECT, timeLine.getSubject());
            values.put(LoveCommon.TL_IMAGE_PATH_1, timeLine.getImagePath1());
            values.put(LoveCommon.TL_IMAGE_PATH_2, timeLine.getImagePath2());
            values.put(LoveCommon.TL_IMAGE_PATH_3, timeLine.getImagePath3());
            values.put(LoveCommon.TL_STATUS, timeLine.getStatus());
            values.put(LoveCommon.TL_TYPE, timeLine.getType());
            values.put(LoveCommon.TL_COUNT_DATE, timeLine.getCountDate());

            database.insert(LoveCommon.TABLE_TIME_LINE, null, values);
        } catch (SQLException e) {
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
            values.put(LoveCommon.TL_ID, timeLine.getId());
            values.put(LoveCommon.TL_DATE, timeLine.getDate());
            values.put(LoveCommon.TL_CONTENT, timeLine.getContent());
            values.put(LoveCommon.TL_SUBJECT, timeLine.getSubject());
            values.put(LoveCommon.TL_IMAGE_PATH_1, timeLine.getImagePath1());
            values.put(LoveCommon.TL_IMAGE_PATH_2, timeLine.getImagePath2());
            values.put(LoveCommon.TL_IMAGE_PATH_3, timeLine.getImagePath3());
            values.put(LoveCommon.TL_STATUS, timeLine.getStatus());
            values.put(LoveCommon.TL_TYPE, timeLine.getType());
            values.put(LoveCommon.TL_COUNT_DATE, timeLine.getCountDate());


            database.update(LoveCommon.TABLE_TIME_LINE, values, LoveCommon.TL_ID + " = ?", new String[]{timeLine.getId()});

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

            Cursor cursor = database.query(LoveCommon.TABLE_TIME_LINE, null, LoveCommon.TL_ID + " = ?", new String[] { String.valueOf(idTimeline) },null, null, null);
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
                    + LoveCommon.TABLE_TIME_LINE;

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
            dbHelper.getWritableDatabase().delete(LoveCommon.TABLE_TIME_LINE, LoveCommon.TL_ID +"=?", new String[]{idTime});
            mTimlineListener.delTimelineSuccess(position);
        } catch (Exception e) {
            mTimlineListener.delTimelineError(context.getApplicationContext().getResources().getString(R.string.get_error));
        }
    }
}
