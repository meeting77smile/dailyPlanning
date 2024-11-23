package com.example.dailyplanning.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dailyplanning.fragment.BillFragment;

public class BillPagerAdapter extends FragmentPagerAdapter {

    private final int mYear;

    public BillPagerAdapter(@NonNull FragmentManager fm, int year) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mYear=year;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {//此时position+1就是month
        int month = position+1;
        //由于需要显示该格式：如2024-09，故当month小于10的时候，需要在前面加上0
        String zeroMonth = month < 10? "0" + month : String.valueOf(month);
        String yearMonth=mYear+"-"+zeroMonth;
        Log.d("bill",yearMonth);
        return BillFragment.newInstance(yearMonth);
    }

    @Override
    public int getCount() {
        return 12;//一年总共12个月
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (position + 1) + "月份";
    }
}
