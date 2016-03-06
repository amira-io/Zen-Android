/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.amira.zen.core.ZenResManager;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_CACHE = "cache";
    public static final String CACHE_ID = "_id";
    public static final String CACHE_EID = "eid";
    public static final String CACHE_TIMESTAMP = "timestamp";
    public static final String CACHE_DATA = "data";
    public static final String CACHE_DURATION = "duration";

    private static final String DATABASE_NAME = "zencache.db";
    //private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String CACHE_CREATE = "create table "
            + TABLE_CACHE + "(" + CACHE_ID
            + " integer primary key autoincrement, " + CACHE_EID
            + " text not null, " + CACHE_TIMESTAMP + " text not null, "
            + CACHE_DATA + " text not null, " + CACHE_DURATION
            + " integer not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, getDbVersion());
    }

    public static int getDbVersion() {
        String sver = ZenResManager.getString("app_version");
        //ZenLog.l(sver);
        String[] vl = sver.split("\\.");
        int ver = 0;
        if (vl.length > 1) {
            ver = 10*Integer.parseInt(vl[0])+Integer.parseInt(vl[1]);
        } else {
            ver = Integer.parseInt(vl[0]);
        }
        return ver;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CACHE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHE);
        onCreate(db);
    }

}
