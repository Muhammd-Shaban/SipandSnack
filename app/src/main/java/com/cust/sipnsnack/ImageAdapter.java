package com.cust.sipnsnack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter <ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<ImgUpload> uploads;

    public ImageAdapter(Context context, List<ImgUpload> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ImageViewHolder holder, int position) {
        ImgUpload uploadCurrent = uploads.get(position);
        holder.imgName.setText(uploadCurrent.getImgName());
        System.out.println("URL : "+uploadCurrent.getImgUrl());
        Picasso.get()
                .load(uploadCurrent.getImgUrl())
                .fit()
                .centerCrop()
                .into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView imgName;
        public ImageView imgView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imgName = itemView.findViewById(R.id.text_view_name);
            imgView = itemView.findViewById(R.id.image_view_upload);
        }
    }
}
