package com.example.wagahai.texteditor;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class ListNotes extends AppCompatActivity {
    private ContentResolver contentResolver;
    private static final int REQUEST_CODE = 1;
    private ListView mListView;
    private Cursor mCursor;

    private float x;
    private float y;

    private final int MENU_ID_NEW_TEXT = 1;
    private final int MENU_ID_NEW_DRAW = 2;
    private final int MENU_ID_EXIT = 3;

    private final int MENU_ID_EDIT_CONTENT = 8;
    private final int MENU_ID_EDIT_TITLE = 9;
    private final int MENU_ID_DELETE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);
        contentResolver = getContentResolver();

        mListView = (ListView)findViewById(R.id.listView);
        registerForContextMenu(mListView);


        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getX();
                y = event.getY();
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Build.VERSION.SDK_INT >= 24) {
                    view.showContextMenu(x, y - view.getTop());
                } else {
                    view.showContextMenu();
                }

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        setTitleList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Log.i("intent", intent.getDataString());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_NEW_TEXT, 0, "New Text");
        menu.add(Menu.NONE, MENU_ID_NEW_DRAW, 1, "New Draw");
        menu.add(Menu.NONE, MENU_ID_EXIT, 2, "Exit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_ID_NEW_TEXT:
                createNewData("text");
                return true;
            case MENU_ID_NEW_DRAW:
                createNewData("draw");
                return true;
            case MENU_ID_EXIT:
                System.exit(0);
            default:
                Log.e("onOptionItemSelected", "invalid menu id");
                return false;
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int row = (int)((AdapterView.AdapterContextMenuInfo)menuInfo).id;
        menu.add(row, MENU_ID_EDIT_CONTENT, 0, "Edit content");
        menu.add(row, MENU_ID_EDIT_TITLE, 1, "Edit title");
        menu.add(row, MENU_ID_DELETE, 2, "DELETE");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        mCursor.moveToPosition(item.getGroupId());
        int id = mCursor.getInt(mCursor.getColumnIndex("_id"));

        switch (item.getItemId()) {
            case MENU_ID_EDIT_CONTENT:
                String mime_type = mCursor.getString(mCursor.getColumnIndex("mime_type"));
                openEditor(id, mime_type);
                return true;
            case MENU_ID_EDIT_TITLE:
                String ex_title = mCursor.getString(mCursor.getColumnIndex("title"));
                updateTitle(id, ex_title);
                return true;
            case MENU_ID_DELETE:
                Uri uri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, (long)id);
                contentResolver.delete(uri, null, null);

                setTitleList();
                return true;
            default:
                Log.e("onContextItemSelected", "invalid menu id");
                return false;
        }
    }

    public void setTitleList() {
        String orderBy = "modified DESC";
        mCursor = contentResolver.query(MyContentProvider.CONTENT_URI, null, null, null, orderBy);
        Log.d("ListNotes", "list count:" + mCursor.getCount());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (mListView.getContext(), android.R.layout.simple_list_item_1);
        int colIdx = mCursor.getColumnIndex("title");
        for (int i = 0; i < mCursor.getCount(); i++) {
            mCursor.moveToNext();
            arrayAdapter.add(mCursor.getString(colIdx));
        }
        mListView.setAdapter(arrayAdapter);
    }

    public void createNewData(final String dataType) {
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.title_alert, null);
        new AlertDialog.Builder(this)
                .setTitle("Input title")
                .setView(layout)
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText)layout.findViewById(R.id.alertText1);
                                String title = editText.getText().toString();
                                String mime_type;
                                switch(dataType) {
                                    case "draw":
                                        mime_type = "image/png";
                                        break;
                                    case "text":
                                        mime_type = "text/plain";
                                        break;
                                    default:
                                        Log.e("createNewData", "invalid data type");
                                        return;
                                }
                                ContentValues values = new ContentValues();
                                values.put("mime_type", mime_type);
                                values.put("length", 0);
                                values.put("title", title);
                                Uri uri = contentResolver.insert(MyContentProvider.CONTENT_URI, values);

                                int id = (int)ContentUris.parseId(uri);
                                openEditor(id, mime_type);
                                setTitleList();
                            }
                        })
                .show();
    }

    public void updateTitle(final int id, final String currentTitle) {
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.title_alert, null);
        new AlertDialog.Builder(this)
                .setTitle("Input title")
                .setView(layout)
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText)layout.findViewById(R.id.alertText1);
                                String title = editText.getText().toString();
                                ContentValues values = new ContentValues();
                                values.put("title", title);

                                Uri uri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, id);
                                contentResolver.update(uri, values, null, null);

                                setTitleList();
                            }
                        })
                .show();
    }

    public void openEditor(final int id, final String mime_type) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_ID, id);
        intent.putExtra(MainActivity.EXTRA_MIME, mime_type);
        startActivity(intent);
    }
}
