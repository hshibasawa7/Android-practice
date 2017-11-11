package com.example.wagahai.texteditor;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wagahai on 2017/11/04.
 */

public class GraphicFragment extends Fragment {
    private ContentResolver contentResolver;
    private CanvasView canvasView;
    private Uri fileUri;
    private long id = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graphic_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.layout);
        canvasView = new CanvasView(this.getActivity());
        layout.addView(canvasView);

        Button clearButton = (Button)view.findViewById(R.id.button);
        Button loadButton = (Button)view.findViewById(R.id.button2);
        Button saveButton = (Button)view.findViewById(R.id.button3);

        contentResolver = view.getContext().getContentResolver();
        fileUri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, id);
        loadBitmap();


        clearButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        canvasView.clearBmp();
                    }
                }
        );
        loadButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadBitmap();
                    }
                }
        );
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            OutputStream fstream = contentResolver.openOutputStream(fileUri);
                            canvasView.getmBitmap().compress(Bitmap.CompressFormat.PNG, 100, fstream);
                        } catch(Exception e) {
                            Toast.makeText(canvasView.getContext(), e.toString(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        ContentValues values = new ContentValues();
                        values.put("length", canvasView.getmBitmapSize());
                        Log.d("TextFragment", "text length:" + canvasView.getmBitmapSize());
                        Uri uri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, id);
                        contentResolver.update(uri, values, null, null);

                        Toast.makeText(canvasView.getContext(), "保存完了",
                                Toast.LENGTH_LONG).show();

                    }
                }
        );

    }

    public void setId(long id) {
        this.id = id;
    }

    public void loadBitmap() {
        try {
            InputStream fstream = contentResolver.openInputStream(fileUri);
            canvasView.setmBitmap(BitmapFactory.decodeStream(fstream));
        } catch (FileNotFoundException e) {
            Toast.makeText(canvasView.getContext(), "saved file not found",
                    Toast.LENGTH_LONG).show();
            Log.w("loadBitmap", e.toString());
        }
    }

}
