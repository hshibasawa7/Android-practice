package com.example.wagahai.texteditor;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * Created by wagahai on 2017/11/04.
 */

public class TextFragment extends Fragment {
    private TextView mEditText;

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
                        Log.d("filepath",  v.getContext().getFilesDir().getPath());
                        try {
                            FileInputStream fstream = v.getContext().openFileInput("text.txt");
                            byte[] data = new byte[fstream.available()];
                            fstream.read(data);
                            mEditText.setText(new String(data));
                        } catch(IOException e) {
                            Toast.makeText(mEditText.getContext(), e.toString(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                }
        );
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("filepath",  v.getContext().getFilesDir().getPath());
                        try {
                            FileOutputStream fstream = v.getContext().openFileOutput("text.txt", Context.MODE_PRIVATE);
                            fstream.write(mEditText.getText().toString().getBytes());
                        } catch(Exception e) {
                            Toast.makeText(mEditText.getContext(), e.toString(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(mEditText.getContext(), "保存完了",
                                Toast.LENGTH_LONG).show();

                    }
                }
        );

    }


}
