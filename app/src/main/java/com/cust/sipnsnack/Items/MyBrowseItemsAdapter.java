package com.cust.sipnsnack.Items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyBrowseItemsAdapter extends RecyclerView.Adapter<MyBrowseItemsAdapter.MyViewHolder> {

    private ArrayList<Items> dataSet;
    List<Items> listFull;

    public static String id;
    public static String name;
    public static String price;
    public static String category;
    public static String description;
    public static String size;
    public static String uri;


    public MyBrowseItemsAdapter(ArrayList<Items> data) {
        this.dataSet = data;
        listFull = new ArrayList<>(data);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId, textViewName, textViewPrice, textViewCategory, textViewDescription, textViewSize;
        CircleImageView imgIV;
        ImageView addIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewId = (TextView) itemView.findViewById(R.id.itemIdTV);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemNameTV);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.itemPriceTV);
            this.textViewCategory = (TextView) itemView.findViewById(R.id.itemCategoryTV);
            this.textViewDescription = (TextView) itemView.findViewById(R.id.itemDescriptionTV);
            this.textViewSize = itemView.findViewById(R.id.itemSizeTV);

            this.imgIV = itemView.findViewById(R.id.itemIMG);
            this.addIcon = itemView.findViewById(R.id.addiconItems);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_products2, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewId = holder.textViewId;
        TextView textViewPrice = holder.textViewPrice;
        TextView textViewCategory = holder.textViewCategory;
        TextView textViewDescription = holder.textViewDescription;
        TextView textViewSize = holder.textViewSize;

        CircleImageView itemImageIV = holder.imgIV;

        textViewId.setText(dataSet.get(listPosition).getItemId());
        textViewName.setText(dataSet.get(listPosition).getItemName());
        textViewPrice.setText(dataSet.get(listPosition).getItemPrice());
        textViewCategory.setText(dataSet.get(listPosition).getItemCategory());
        textViewDescription.setText(dataSet.get(listPosition).getItemDescription());
        textViewSize.setText(dataSet.get(listPosition).getItemSize());

        Picasso.get()
                .load(dataSet.get(listPosition).getItemImgUrl())
                .placeholder(R.drawable.logo)
                .into(itemImageIV);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
