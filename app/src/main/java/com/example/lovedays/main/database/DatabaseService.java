package com.example.lovedays.main.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lovedays.R;
import com.example.lovedays.main.units.InfoPersonal;

import java.util.ArrayList;
import java.util.List;

import static com.example.lovedays.common.LoveCommon.PERSONAL_BIRTH_DAY;
import static com.example.lovedays.common.LoveCommon.PERSONAL_COLOR_BODER_CODE;
import static com.example.lovedays.common.LoveCommon.PERSONAL_COLOR_TEXT;
import static com.example.lovedays.common.LoveCommon.PERSONAL_ID;
import static com.example.lovedays.common.LoveCommon.PERSONAL_IMAGE;
import static com.example.lovedays.common.LoveCommon.PERSONAL_NAME;
import static com.example.lovedays.common.LoveCommon.PERSONAL_SEX;
import static com.example.lovedays.common.LoveCommon.TABLE_INFO_PERSONAL_NAME;

public class DatabaseService {
    private DatabaseOnListener mListener;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    public DatabaseService (Context context, DatabaseOnListener mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    /**
     * Open SQL
     * @return
     * @throws SQLException
     */
    public DatabaseService open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
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
            mListener.updPersionError(context.getApplicationContext().getResources().getString(R.string.update_error));
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
                mListener.updPersionError(context.getApplicationContext().getResources().getString(R.string.update_error));
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
}
