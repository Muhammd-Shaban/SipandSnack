package com.cust.sipnsnack.Customers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sipnsnack.R;

public class MenuAdapter extends ArrayAdapter <String> {
    private final Activity context;
    private final String[] maintitle;
    private final Integer[] imgid;


    public MenuAdapter(@NonNull Activity context, String[] maintitle, Integer[] imgid) {
        super(context, R.layout.mylist, maintitle);
        this.context = context;
        this.maintitle = maintitle;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        titleText.setText(maintitle[position]);
        imageView.setImageResource(imgid[position]);

        return rowView;

    };
}
