package com.newsapp.my_design_new;

import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.Locale;

import static com.newsapp.my_design_new.R.string.title_section;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int COUNT =3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;

        switch (i){
            case 0:
                fragment = new FirstFragment();
                break;
            case 1:
                fragment = new SecondFragment();
                break;
            case 2:
                fragment = new ThirdFragment();
                break;

        }


        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "World News ";
             case 1:
                return "Sport " ;
             case 2:
                return "Ads " ;


        }


        return null;







    }
}
