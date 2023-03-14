package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class fragment_adapter extends FragmentPagerAdapter {


    public fragment_adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new Pending_fragment();
            case 1:
                return new Accepted_fragment();
            case 2:
                return new OntheWay_fragment();
            case 3:
                return new Delivered_fragment();
        }
        return new Pending_fragment();
    }

    @Override
    public int getCount() {
        return 4 ;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if(position==0)
        {
            title="Pending";
        } else if (position == 1 )
        {
            title = "Accepted";
        }
         else if (position == 2 )
         {
             title = "On Way";
         }
         else if (position == 3 )
        {
            title = "Deliverd";
        }
        return title;
    }
}
