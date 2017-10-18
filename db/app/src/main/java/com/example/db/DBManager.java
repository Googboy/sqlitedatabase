package com.example.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 潘硕 on 2017/10/18.
 */

class DBManager {
    private final DBHelper helper;
    private final SQLiteDatabase db;

    public DBManager(MainActivity mainActivity) {
        helper = new DBHelper(mainActivity);
        db = helper.getWritableDatabae();
    }

    public void closeDB() {
        db.close();
    }

    public void add(ArrayList<Person> persons) {
        db.beginTransaction();
        try {
            for (Person person:persons){
                db.execSQL("Insert into person values(null,?,?,?)",new Object[]{person.name,person.age,person.info});
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void updateAge(Person person) {
        ContentValues cv = new ContentValues();
        cv.put("age",person.age);
        db.update("person",cv,"name=?",new String[]{person.name});
    }
    public void deleteOldPerson(Person person) {
        db.delete("person","age >= ?",new String[]{String.valueOf(person.age)});
    }

    public List<Person> query() {
        ArrayList<Person> persons = new ArrayList<Person>();
        Cursor c = queryTheCusor();
        while (c.moveToNext()){
            Person person = new Person();
            person._id = c.getInt(c.getColumnIndex("_id"));
            person.name = c.getString(c.getColumnIndex("name"));
            person.age = c.getInt(c.getColumnIndex("age"));
            person.info = c.getString(c.getColumnIndex("info"));
            persons.add(person);
        }
        c.close();
        return persons;
    }

    public Cursor queryTheCusor() {
        Cursor c = db.rawQuery("Select * from person",null);
        return c;
    }


}
