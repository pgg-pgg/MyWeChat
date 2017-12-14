package com.pgg.mywechatem.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pgg.mywechatem.Fragment.BaseFragment;
import com.pgg.mywechatem.Fragment.FragmentFactory;


/**
 * Created by PDD on 2017/11/16.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment= FragmentFactory.createFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
