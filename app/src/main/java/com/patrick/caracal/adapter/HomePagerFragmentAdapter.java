package com.patrick.caracal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.patrick.caracal.ui.fragment.ActiveFragment;
import com.patrick.caracal.ui.fragment.FileFragment;

/**
 * Created by 15920 on 2016/8/8.
 */
public class HomePagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] mTab = new String[]{"活跃", "归档"};

    public HomePagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ActiveFragment.newInstance();
        } else {
            return FileFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTab.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTab[position];
    }
}
