package com.example.wagahai.texteditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by wagahai on 2017/11/04.
 */

public class GraphicFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graphic_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.layout);
        final CanvasView canvasView = new CanvasView(this.getActivity());
        layout.addView(canvasView);
        //Log.d("ViewSize", "H:" + canvasView.getHeight() + ", W:" + canvasView.getWidth());
        //Log.d("LayoutSize", "H:" + layout.getHeight() + ", W:" + layout.getWidth());

        /*
        canvasView.setOnTouchListener(
                new View.OnTouchListener() {

                }
        );
*/
        Button clearButton = (Button)view.findViewById(R.id.button);
        Button loadButton = (Button)view.findViewById(R.id.button2);
        Button saveButton = (Button)view.findViewById(R.id.button3);

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
                        try {
                            FileInputStream fstream = v.getContext().openFileInput("image.bmp");
                            canvasView.setmBitmap(BitmapFactory.decodeStream(fstream));
                        } catch (Exception e) {
                            Toast.makeText(canvasView.getContext(), e.toString(),
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
                        try {
                            FileOutputStream fstream = v.getContext().openFileOutput("image.bmp", Context.MODE_PRIVATE);
                            canvasView.getmBitmap().compress(Bitmap.CompressFormat.PNG, 100, fstream);
                        } catch(Exception e) {
                            Toast.makeText(canvasView.getContext(), e.toString(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(canvasView.getContext(), "保存完了",
                                Toast.LENGTH_LONG).show();

                    }
                }
        );

    }



}
