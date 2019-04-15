package com.locus.locusdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.locus.locusdemo.LocusDemo;

public class DatabaseHelper extends SQLiteOpenHelper implements HelperInterface {
    private static final String DB_NAME = "db_locus.db";
    private static final int DB_VERSION_NO = 1;
    private static DatabaseHelper mInstance = null;
    static final Object lock = new Object();

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION_NO);
    }

    public static DatabaseHelper getInstance() {


        if (mInstance == null) {

            mInstance = new DatabaseHelper(LocusDemo.getInstance());
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + DATA_TBLE  +
                " (" + TABLE_PID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DATA_TYPE + " VARCHAR(50), " +
                DATA_ID + " VARCHAR(50), " +
                DATA_OPTIONS+ " VARCHAR(50), " +
                DATA_TITLE + " VARCHAR(50)" +

                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
