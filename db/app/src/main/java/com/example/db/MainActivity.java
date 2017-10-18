package com.example.db;

import android.app.Activity;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private ListView listView;
    private DBManager mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        mgr = new DBManager(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
    }
    public void add(View view){
        ArrayList<Person> persons = new ArrayList<Person>();
        Person person1 = new Person("Ella",20,"cute girl");
        Person person2 = new Person("Tom",21,"handom boy");
        Person person3 = new Person("Jack",22,"good boy");
        Person person4 = new Person("Maria",23,"brauty girl");
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);
        mgr.add(persons);
    }
    public void update(View view){
        Person person = new Person();
        person.name = "Jane";
        person.age = 30;
        mgr.updateAge(person);
    }
    public void delete(View view){
        Person person = new Person();
        person.age = 30;
        mgr.deleteOldPerson(person);
    }
    public void query(View view){
        List<Person> persons = mgr.query();
        ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
        for (Person person:persons){
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("name",person.name);
            map.put("info",person.age+"years old,"+person.info);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,android.R.layout.simple_list_item_2,new String[]{"name","info"},new int[]{android.R.id.text1,android.R.id.text2});
        listView.setAdapter(adapter);
    }
    public void queryTheCursor(View view){
        Cursor c = mgr.queryTheCusor();
        startManagingCursor(c);
        CursorWrapper cursorWrapper = new CursorWrapper(c){
            @Override
            public String getString(int columnIndex) {
                if (getColumnName(columnIndex).equals("info")){
                    int age = getInt(getColumnIndex("age"));
                    return age + "years old," + super.getString(columnIndex);
                }
                return super.getString(columnIndex);
            }
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cursorWrapper,new String[]{"name","info"},new int[]{android.R.id.text1,android.R.id.text2});
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
