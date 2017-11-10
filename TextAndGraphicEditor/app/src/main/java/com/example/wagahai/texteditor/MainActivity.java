package com.example.wagahai.texteditor;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);


    }

}
