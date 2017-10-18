package com.example.sqlitedatabasedemo;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {


    private static final int MENU_DELETE = Menu.FIRST+1;
    private static final int MENU_UPDATE = Menu.FIRST+2;
    private static int BOOK_ID = 0;
    private BooksDB mBooksDB;
    private Cursor mCursor;
    private EditText BookName;
    private EditText BookAuthor;
    private ListView BooksList;
    private final static int MENU_ADD = Menu.FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
    }

    private void setUpViews() {
        mBooksDB = new BooksDB(this);
        mCursor = mBooksDB.select();
        BookName = (EditText) findViewById(R.id.bookname);
        BookAuthor = (EditText) findViewById(R.id.author);
        BooksList = (ListView) findViewById(R.id.bookslist);
        BooksList.setAdapter(new BooksListAdapter(this,mCursor));
        BooksList.setOnItemClickListener(this);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE,MENU_ADD,0,"ADD");
        menu.add(Menu.NONE,MENU_DELETE,0,"DELETE");
        menu.add(Menu.NONE,MENU_UPDATE,0,"UPDATE");
        return true;
    }
    public boolean onOptionsItemsSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case MENU_ADD:
                add();
                break;
            case MENU_DELETE:
                delete();
                break;
            case MENU_UPDATE:
                update();
                break;
        }
        return true;
    }

    private void update() {
        String bookname = BookName.getText().toString();
        String author = BookAuthor.getText().toString();
        if (bookname.equals("")||author.equals("")){
            return;
        }
        mBooksDB.update(BOOK_ID,bookname,author);
        mCursor.requery();
        BooksList.invalidateViews();
        BookName.setText("");
        BookAuthor.setText("");
        Toast.makeText(this,"Update Successd!",Toast.LENGTH_LONG).show();
    }

    private void delete() {
        if (BOOK_ID == 0){
            return;
        }
        mBooksDB.delete(BOOK_ID);
        mCursor.requery();
        BooksList.invalidateViews();
        BookName.setText("");
        BookAuthor.setText("");
        Toast.makeText(this,"Delete Successd!",Toast.LENGTH_LONG).show();
    }

    private void add() {
        String bookname = BookName.getText().toString();
        String author = BookAuthor.getText().toString();
        if (bookname.equals("")||author.equals("")){
            return;
        }
        mBooksDB.insert(bookname,author);
        mCursor.requery();
        BooksList.invalidateViews();
        BookName.setText("");
        BookAuthor.setText("");
        Toast.makeText(this,"Add Success!",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCursor.moveToPosition(position);
        BOOK_ID = mCursor.getInt(0);
        BookName.setText(mCursor.getString(1));
        BookAuthor.setText(mCursor.getString(2));
    }

    private class BooksListAdapter extends BaseAdapter {
        private Context mContext;
        private Cursor mCursor;
        public BooksListAdapter(Context context, Cursor cursor) {
            mContext = context;
            mCursor = cursor;
        }






        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView mTextView = new TextView(mContext);
            mCursor.moveToPosition(position);
            mTextView.setText(mCursor.getString(1)+"_"+mCursor.getString(2));
            return mTextView;
        }
    }
}
