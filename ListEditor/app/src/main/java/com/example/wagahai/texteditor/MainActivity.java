package com.example.wagahai.texteditor;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.wagahai.texteditor.ID";
    public static final String EXTRA_MIME = "com.example.wagahai.texteditor.MIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int id = intent.getIntExtra(EXTRA_ID, -1);
        String mime_type = intent.getStringExtra(EXTRA_MIME);

        Fragment fragment;
        switch(mime_type) {
            case "image/png":
                fragment = new GraphicFragment();
                ((GraphicFragment)fragment).setId((long)id);
                break;
            case "text/plain":
                fragment = new TextFragment();
                ((TextFragment)fragment).setId((long)id);
                break;
            default:
                Log.e("onCreate", "invalid mime type.");
                return;
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_layout, fragment);
        fragmentTransaction.commit();

    }

}
