package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 潘硕 on 2017/10/18.
 */

class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "test.db";
    private SQLiteDatabase writableDatabae;
    private SQLiteDatabase mDatabase;
    private boolean mIsInitializing;
    private Object mName;
    private Context mContext;
    private int mNewVersion;
    private android.database.sqlite.SQLiteDatabase.CursorFactory mFactory;

    public DBHelper(MainActivity mainActivity) {
        super(mainActivity,DATABASE_NAME,null,DATABASE_VERSION);
    }


    

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table if not exists person"+"(_id integer primary key autoincrement,name VARCHAR,age INTEGER,info TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table person ADD COLUMN other STRING");
    }


    public SQLiteDatabase getWritableDatabae() {
        if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
            return mDatabase;
        }
        if (mIsInitializing) {
            throw new IllegalStateException("getWritableDatabase called recursively");
        }
        boolean success = false;
        SQLiteDatabase db = null;
        if (mDatabase != null)
            //mDatabase.lock();
            try {
                mIsInitializing = true;
                if (mName == null) {
                    db = SQLiteDatabase.create(null);
                } else {
                    db = mContext.openOrCreateDatabase((String) mName, 0, mFactory);
                }
                int version = db.getVersion();
                if (version != mNewVersion) {
                    db.beginTransaction();
                    try {
                        if (version == 0) {
                            onCreate(db);
                        } else {
                            onUpgrade(db, version, mNewVersion);
                        }
                        db.setVersion(mNewVersion);
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                }
                onOpen(db);
                success = true;
                return db;
            } finally {
                mIsInitializing = false;
                if (success) {
                    if (mDatabase != null) {
                        try {
                            mDatabase.close();
                        } catch (Exception e) {

                        }
                        //mDatabase.unlock();
                    }
                    mDatabase = db;
                } else {
                    if (mDatabase != null)
                        //mDatabase.unlock();
                        if (db != null)
                            db.close();
                }
            }
        return db;
    }
}
