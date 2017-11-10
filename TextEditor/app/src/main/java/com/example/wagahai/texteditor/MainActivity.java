package com.example.wagahai.texteditor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (TextView)findViewById(R.id.editText);
        Button clearButton = (Button)findViewById(R.id.button);
        Button displayButton = (Button)findViewById(R.id.button2);

        clearButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditText.setText("");
                    }
                }


        );
        displayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mEditText.getContext(), mEditText.getText().toString(),
                                        Toast.LENGTH_LONG).show();
                    }
                }


        );
    }

}
