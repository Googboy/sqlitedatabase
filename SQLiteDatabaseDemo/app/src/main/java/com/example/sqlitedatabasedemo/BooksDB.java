package com.example.sqlitedatabasedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 潘硕 on 2017/10/18.
 */

class BooksDB extends SQLiteOpenHelper{
    private final static String DATABASE_NAME = "BOOKS.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "books_table";
    private static final String BOOK_ID = "book_id";
    private static final String BOOK_NAME = "book_name";
    private static final String BOOK_AUTHOR = "book_author";
    public BooksDB(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,null,null,null,null,null,null);
        return cursor;
    }

    public long insert(String bookname, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BOOK_NAME,bookname);
        cv.put(BOOK_AUTHOR,author);
        long row = db.insert(TABLE_NAME,null,cv);
        return row;
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = BOOK_ID+"=?";
        String[] whereValue = {Integer.toString(id)};
        db.delete(TABLE_NAME,where,whereValue);
    }

    public void update(int id, String bookname, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = BOOK_ID+"=?";
        String[] whereValue = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(BOOK_NAME,bookname);
        cv.put(BOOK_AUTHOR,author);
        db.update(TABLE_NAME,cv,where,whereValue);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql  = "CREATE TABLE"+TABLE_NAME+"("+BOOK_ID+"INTEGER primary key autoincrement,"+BOOK_NAME+"text,"+BOOK_AUTHOR+"text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS"+TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}
