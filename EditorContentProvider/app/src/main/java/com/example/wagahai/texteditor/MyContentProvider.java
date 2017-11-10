package com.example.wagahai.texteditor;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
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

/**
 * Created by wagahai on 2017/11/09.
 */

public class MyContentProvider extends ContentProvider {
//    public static final String AUTHORITY = "com.example.wagahai.texteditor.mycontentprovider";
    public static final String AUTHORITY = "com.example.wagahai.texteditor.mycontentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri contentUri, @NonNull String sMode)
            throws FileNotFoundException {
        int mode = ParcelFileDescriptor.parseMode(sMode);
        long id = ContentUris.parseId(contentUri);
        File file = new File(this.getContext().getFilesDir().getPath() + id);

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

    //Todo: use database
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}

