package com.anyou.yx;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import androidx.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;


public class App extends Application {
    private static App myApplication = null;

    public static App getApplication() {
        return myApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static String readEnv(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        StringBuilder buffer = new StringBuilder();
        try (BufferedSource source = Okio.buffer(Okio.source(file))) {
            for (String line; (line = source.readUtf8Line()) != null; ) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static void writeEnv(String fileName, StoreLocation storeLocation) {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        String readLines = readEnv(fileName);
        Type type = new TypeToken<ArrayList<StoreLocation>>() {
        }.getType();
        Gson gson = new Gson();
        List<StoreLocation> list = gson.fromJson(readLines, type);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(storeLocation);

        String json = gson.toJson(list);
        if (json == null) {
            System.out.println("保存文件失败");
            return;
        }
        try (Sink fileSink = Okio.sink(file);
             BufferedSink bufferedSink = Okio.buffer(fileSink)) {
            bufferedSink.writeUtf8(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;

    public void initDB() {
        if (mDatabaseHelper == null || mSqLiteDatabase == null) {
            mDatabaseHelper = new DatabaseHelper(this);
            mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        }
    }

    public void insertDB(StoreLocation storeLocation) {
        initDB();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.F_ID, storeLocation.id);
        values.put(DatabaseHelper.F_NAME, storeLocation.name);
        values.put(DatabaseHelper.F_ADDRESS, storeLocation.address);
        values.put(DatabaseHelper.F_IMAGE, storeLocation.image);
        values.put(DatabaseHelper.F_MARK, storeLocation.mark);
        values.put(DatabaseHelper.F_LNG, storeLocation.lng);
        values.put(DatabaseHelper.F_LAT, storeLocation.lat);
        long index = mSqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, values);
        if (index != -1) {
            System.out.println("插入成功");
        }
    }

    public void deleteDB(String id) {
        initDB();
        String[] args = {id};
        int numbers = mSqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.F_ID + "=?", args);
        if (numbers == 0) {
            System.out.println("没有找到符合条件的");
        } else {
            System.out.println("找到的个数是： " + numbers);
        }
    }

    public void updateDB(String id, StoreLocation storeLocation) {
        initDB();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.F_ID, storeLocation.id);
        values.put(DatabaseHelper.F_NAME, storeLocation.name);
        values.put(DatabaseHelper.F_ADDRESS, storeLocation.address);
        values.put(DatabaseHelper.F_IMAGE, storeLocation.image);
        values.put(DatabaseHelper.F_MARK, storeLocation.mark);
        values.put(DatabaseHelper.F_LNG, storeLocation.lng);
        values.put(DatabaseHelper.F_LAT, storeLocation.lat);
        String[] args = {id};
        int affectNum = mSqLiteDatabase.update(DatabaseHelper.TABLE_NAME,
                values, DatabaseHelper.F_ID + "=?", args);
        if (affectNum == 0) {
            System.out.println("没有找到符合条件的，没有修改");
        } else {
            System.out.println("找到的个数是： " + affectNum);
        }
    }

    public void queryDB() {
        initDB();
        Cursor cursor = mSqLiteDatabase.query(DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.F_ID,
                        DatabaseHelper.F_NAME,
                        DatabaseHelper.F_ADDRESS,
                        DatabaseHelper.F_IMAGE,
                        DatabaseHelper.F_MARK,
                        DatabaseHelper.F_LNG,
                        DatabaseHelper.F_LAT},
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getCount();
            for (int i = 0; i < count; i++) {
                String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.F_ID));
                System.out.println(id);
            }
        }
    }
}