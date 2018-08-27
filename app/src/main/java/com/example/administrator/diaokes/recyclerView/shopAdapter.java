package com.example.administrator.diaokes.recyclerView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */

public class shopAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private String[] titles;

    public shopAdapter(FragmentManager fm, List<Fragment> list, String[] strings){
        super(fm);
        this.list = list;
        this.titles = strings;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
