package com.example.wagahai.texteditor;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;

/**
 * Created by wagahai on 2017/11/09.
 */

public class MyContentProvider extends ContentProvider {
//    public static final String AUTHORITY = "com.example.wagahai.texteditor.mycontentprovider";
    public static final String AUTHORITY = "com.example.wagahai.texteditor.mycontentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String DB_NAME = "mydb";
    public static final String TABLE_NAME = "notes";
    private MyDatabaseHelper mOpenHelper;

    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri contentUri, @NonNull String sMode)
            throws FileNotFoundException {
        int mode = ParcelFileDescriptor.parseMode(sMode);
        long id = ContentUris.parseId(contentUri);
        File file = new File(this.getContext().getFilesDir().getPath() + "/" + id);

        if (!file.exists()) {
            try {
                Log.d("MyContentProvider", "create new file");
                file.createNewFile();
            } catch (IOException e) {
                Log.e("MyContentProvider", e.toString());
                throw new FileNotFoundException("MyContentProvider: failed to create new file");
            }
        }

        return ParcelFileDescriptor.open(file, mode);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MyDatabaseHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        long id = ContentUris.parseId(uri);
        if(0 <= id) {
            if (selection == null) { selection = "_id=" + id; }
            else { selection += "_id=" + id + selection; }
        }
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (values == null) {
            values = new ContentValues();
        }
        if (values.get("title") == null || ((String)values.get("title")).length() == 0) {
            values.put("title", "noTitle");
        }
        values.put("created", new Date().getTime());
        values.put("modified", new Date().getTime());

        long id = db.insert(TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long id = ContentUris.parseId(uri);
        if(0 <= id) {
            if (selection == null) { selection = "_id=" + id; }
            else { selection = "_id=" + id + selection; }
        }
        return db.delete(TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Is "Nullable ContentValues" bug?
        if (values == null) {
            return 0;
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long id = ContentUris.parseId(uri);
        if(0 <= id) {
            if (selection == null) { selection = "_id=" + id; }
            else { selection = "_id=" + id + selection; }
        }
        if (values.get("title") != null && ((String)values.get("title")).length() == 0) {
            values.put("title", "noTitle");
        }
        values.put("modified", new Date().getTime());
        return db.update(TABLE_NAME, values, selection, selectionArgs);
    }

}

