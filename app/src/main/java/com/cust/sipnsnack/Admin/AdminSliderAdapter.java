package com.cust.sipnsnack.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sipnsnack.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class AdminSliderAdapter extends SliderViewAdapter<AdminSliderAdapter.AdminSliderViewHolder> {

    public AdminSliderAdapter(int[] images) {
        this.images = images;
    }
    private int[] images;
    private String[] text;

    @Override
    public AdminSliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_slider, null);
        return new AdminSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminSliderAdapter.AdminSliderViewHolder viewHolder, int position) {
        viewHolder.imageView.setImageResource(images[position]);
    }


    @Override
    public int getCount() {
        return images.length;
    }

    public static class AdminSliderViewHolder extends SliderViewAdapter.ViewHolder {

        private ImageView imageView;

        public AdminSliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
