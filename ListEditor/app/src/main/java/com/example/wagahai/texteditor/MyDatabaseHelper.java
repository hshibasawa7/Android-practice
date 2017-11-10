package com.example.wagahai.texteditor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.wagahai.texteditor.MyContentProvider.DB_NAME;
import static com.example.wagahai.texteditor.MyContentProvider.TABLE_NAME;

/**
 * Created by wagahai on 2017/11/10.
 */

public final class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_NOTES = "CREATE TABLE " + TABLE_NAME + " (" +
            " _id INTEGER PRIMARY KEY," +
            " title TEXT," +
            " mime_type TEXT," +
            " length INTEGER," +
            " created  INTEGER," +
            " modified INTEGER" +
            ");";

    MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("MyDatabaseHelper", "called onUpgrade()");
    }
}
