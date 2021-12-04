package com.example.myhealthapplication.Newsfeed;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    int tabCount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new HomeFragment();


            case 1:
                return new SportsFragment();


            case 2:
                return new ScienceFragment();


            case 3:
                return new TechFragment();

            case 4:

            return new EntertainmentFragment();


            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
