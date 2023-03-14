package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FeedbackAdapter extends FragmentPagerAdapter {

    public FeedbackAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new FoodFeedbacks();
            case 1:
                return new BikerFeedbacks();
        }
        return new FoodFeedbacks();
    }

    @Override
    public int getCount() {
        return 2 ;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if(position==0)
        {
            title = "Food";
        } else if (position == 1 )
        {
            title = "Bikers";
        }

        return title;
    }
}