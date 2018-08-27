package com.example.administrator.diaokes.recyclerView;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.diaokes.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */

public class viewpageAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;
    private String[] titles;
    public viewpageAdapter(FragmentManager fm,List<Fragment> list,String[] strings){
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
