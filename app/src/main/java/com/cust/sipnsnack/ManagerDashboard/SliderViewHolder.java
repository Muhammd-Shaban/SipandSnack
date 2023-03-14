package com.cust.sipnsnack.ManagerDashboard;

import android.view.View;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderViewHolder extends SliderViewAdapter.ViewHolder {

    private ImageView imageView;

    public SliderViewHolder(View itemView, ImageView imageView) {
        super(itemView);
        this.imageView = imageView;
    }
}
