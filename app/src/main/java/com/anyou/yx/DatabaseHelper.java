package com.anyou.yx;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "database.db";
    public static final String TABLE_NAME = "store";
    public static final String F_ID = "id";
    public static final String F_NAME = "name";
    public static final String F_ADDRESS = "address";
    public static final String F_IMAGE = "image";
    public static final String F_MARK = "mark";
    public static final String F_LNG = "lng";
    public static final String F_LAT = "lat";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME
                + "("
                + F_ID + " varchar(64) not null,"
                + F_NAME + " varchar(64) not null,"
                + F_ADDRESS + " varchar(64) not null,"
                + F_IMAGE + " varchar(128) not null,"
                + F_MARK + " varchar(64) not null,"
                + F_LNG + " varchar(64) ,"
                + F_LAT + " varchar(64) "
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

