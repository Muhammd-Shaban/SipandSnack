package com.cust.sipnsnack.ManagerDashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sipnsnack.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int expense[];
    String[] expenseName;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] flags, String[] countryNames) {
        this.context = applicationContext;
        this.expense = flags;
        this.expenseName = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return expense.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.expense_spinner, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(expense[i]);
        names.setText(expenseName[i]);
        return view;
    }
}
