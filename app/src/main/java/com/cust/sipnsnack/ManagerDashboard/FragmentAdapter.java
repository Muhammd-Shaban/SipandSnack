package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new PendingOrdersDetails();
            case 1:
                return new AcceptedOrdersDetails();
            case 2:
                return new OntheWayOrdersDetails();
            case 3:
                return new DeliveredOrdersDetails();
        }
        return new PendingOrdersDetails();
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
            title = DashBoard.pd;
        } else if (position == 1 )
        {
            title = DashBoard.ac;
        }
         else if (position == 2 )
         {
             title = DashBoard.ot;
         }
         else if (position == 3 )
        {
            title = DashBoard.dl;
        }
        return title;
    }
}