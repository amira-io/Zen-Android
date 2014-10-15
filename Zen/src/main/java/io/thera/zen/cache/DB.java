package io.thera.zen.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.thera.zen.core.ZenApplication;

/**
 * Created by giovanni on 14/06/14.
 */
public class DB {

    private static volatile SQLiteDatabase _db = null;
    private static volatile DBHelper dbHelper = null;
    private static String[] allColumns = {
            DBHelper.CACHE_ID, DBHelper.CACHE_EID, DBHelper.CACHE_TIMESTAMP,
            DBHelper.CACHE_DATA, DBHelper.CACHE_DURATION
    };

    private static void _init() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(ZenApplication.context());
        }
    }

    private static void _open() throws SQLException {
        _db = dbHelper.getWritableDatabase();
    }

    private static void _close() {
        dbHelper.close();
    }

    public static String find(String key) {
        _init();
        String data, timestamp;
        int duration;
        _open();
        Cursor cursor = _db.query(
                DBHelper.TABLE_CACHE,
                allColumns,
                DBHelper.CACHE_EID + " = '" + key + "'",
                null, null, null, null);
        cursor.moveToFirst();
        try {
            timestamp = cursor.getString(2);
            data = cursor.getString(3);
            duration = cursor.getInt(4);
        }
        catch (Exception e) {
            //e.printStackTrace();
            timestamp = null;
            data = null;
            duration = 0;
        }
        cursor.close();
        _close();
        if (timestamp == null) {
            return null;
        }
        try {
            // expired check
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            Calendar c_date = Calendar.getInstance();
            c_date.add(Calendar.DATE, 0);
            //c_date.set(Calendar.HOUR_OF_DAY, 0);
            c_date.set(Calendar.MINUTE, c_date.get(Calendar.MINUTE)-duration);
            //c_date.set(Calendar.SECOND, 0);
            //c_date.set(Calendar.MILLISECOND, 0);
            Date el_date = dateFormat.parse(timestamp);
            if (el_date.before(c_date.getTime())) {
                ZenApplication.log("Cache expired");
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return data;
    }

    public static long store(String key, String data, int duration) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 0);
        String date = dateFormat.format(now.getTime());
        ContentValues values = new ContentValues();
        values.put(DBHelper.CACHE_EID, key);
        values.put(DBHelper.CACHE_TIMESTAMP, date);
        values.put(DBHelper.CACHE_DATA, data);
        values.put(DBHelper.CACHE_DURATION, duration);
        _init();
        _open();
        try {
            _db.delete(DBHelper.TABLE_CACHE,
                    DBHelper.CACHE_EID + " = '" + key + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long id = _db.insert(DBHelper.TABLE_CACHE, null, values);
        _close();
        return id;
    }

    public static void clean() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        Calendar c_date = Calendar.getInstance();
        c_date.add(Calendar.DATE, 0);
        _init();
        _open();
        try {
            Cursor cursor = _db.query(DBHelper.TABLE_CACHE,
                    allColumns, null,
                    null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String timestamp = cursor.getString(2);
                int duration = cursor.getInt(4);
                c_date.add(Calendar.DATE, 0);
                //c_date.set(Calendar.HOUR_OF_DAY, 0);
                c_date.set(Calendar.MINUTE, c_date.get(Calendar.MINUTE) - duration);
                Date el_date = dateFormat.parse(timestamp);
                if (el_date.before(c_date.getTime())) {
                    ZenApplication.log("Cache: deleting element");
                    _db.delete(DBHelper.TABLE_CACHE, DBHelper.CACHE_ID + " = '" + cursor.getLong(0) + "'", null);
                }
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _close();
    }

    public static void flush() {
        _init();
        _open();
        try {
            _db.delete(DBHelper.TABLE_CACHE, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _close();
    }
}
