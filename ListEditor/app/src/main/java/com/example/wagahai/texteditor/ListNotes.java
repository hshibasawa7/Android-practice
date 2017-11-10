package com.example.wagahai.texteditor;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;


public class ListNotes extends AppCompatActivity {
    private ContentResolver contentResolver;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);
        contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(MyContentProvider.CONTENT_URI, null, null, null, null);
        Log.d("ListNotes", "list count:" + cursor.getCount());
/*
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("***");
        startActivityForResult(Intent.createChooser(intent, "Select memo"), REQUEST_CODE);
*/

        ListView listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (listView.getContext(), android.R.layout.simple_list_item_1);
        int colIdx = cursor.getColumnIndex("title");
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            arrayAdapter.add(cursor.getString(colIdx));
        }
        listView.setAdapter(arrayAdapter);

        registerForContextMenu(listView);

        Button newTextButton = (Button)findViewById(R.id.button4);
        newTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("mime_type", "text/plain");
                values.put("length", 0);
                contentResolver.insert(MyContentProvider.CONTENT_URI, values);
            }
        });
        /*
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Log.i("intent", intent.getDataString());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Edit content");
        menu.add("Edit title");
        menu.add("DELETE");
    }


}
