package com.example.wagahai.texteditor;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;

/**
 * Created by wagahai on 2017/11/04.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private static final int PAGE_NUM = 2;
    public MyPagerAdapter(FragmentManager fm) { super(fm); }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TextFragment();
                break;
            case 1:
                fragment = new GraphicFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() { return PAGE_NUM; }
}
