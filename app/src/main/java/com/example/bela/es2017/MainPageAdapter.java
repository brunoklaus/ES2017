package com.example.bela.es2017;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.bela.es2017.timer.TimerFragment;

import java.util.List;

/**
 * Created by jaimeossada on 04/12/17.
 */

class MainPageAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments;
    public MainPageAdapter(FragmentManager manager, List<Fragment> fragments){
        super(manager);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
