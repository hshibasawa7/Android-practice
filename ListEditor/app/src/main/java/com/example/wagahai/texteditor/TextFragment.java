package com.example.wagahai.texteditor;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wagahai on 2017/11/04.
 */

public class TextFragment extends Fragment {
    private TextView mEditText;

    private ContentResolver contentResolver;
    private Uri fileUri;
    private long id = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = (TextView)view.findViewById(R.id.editText);
        Button clearButton = (Button)view.findViewById(R.id.button);
        Button loadButton = (Button)view.findViewById(R.id.button2);
        Button saveButton = (Button)view.findViewById(R.id.button3);

        contentResolver = view.getContext().getContentResolver();
        fileUri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, id);
        loadText();

        clearButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditText.setText("");
                    }
                }
        );
        loadButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadText();
                    }
                }
        );
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            OutputStream fstream = contentResolver.openOutputStream(fileUri);
                            fstream.write(mEditText.getText().toString().getBytes());
                        } catch(Exception e) {
                            Toast.makeText(mEditText.getContext(), e.toString(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("TextFragment", e.toString());
                            return;
                        }

                        ContentValues values = new ContentValues();
                        values.put("length", mEditText.getText().length());
                        Log.d("TextFragment", "text length:" + mEditText.getText().length());
                        Uri uri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, id);
                        contentResolver.update(uri, values, null, null);

                        Toast.makeText(mEditText.getContext(), "保存完了",
                                Toast.LENGTH_LONG).show();

                    }
                }
        );

    }

    public void setId(long id) {
        this.id = id;
    }

    public void loadText() {
        byte[] data = null;
        try {
            InputStream in = contentResolver.openInputStream(fileUri);
            int size = in.available();
            data = new byte[size];
            in.read(data);
        } catch(FileNotFoundException e) {
            Toast.makeText(mEditText.getContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
            Log.e("TextFragment", e.toString());
            return;
        } catch(IOException e) {
            Toast.makeText(mEditText.getContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
            Log.e("TextFragment", e.toString());
            return;
        }
        mEditText.setText(new String(data));

    }

}
