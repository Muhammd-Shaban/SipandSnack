package com.cust.sipnsnack.Admin;

import android.view.View;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;

public class AdminSliderViewHolder extends SliderViewAdapter.ViewHolder {

    private ImageView imageView;

    public AdminSliderViewHolder(View itemView, ImageView imageView) {
        super(itemView);
        this.imageView = imageView;
    }
}
