package com.cust.sipnsnack.Items;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyItemsAdapter extends RecyclerView.Adapter<MyItemsAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Items> dataSet;
    List<Items> listFull;

    public static String id;
    public static String name;
    public static String price;
    public static String category;
    public static String description;
    public static String size;
    public static String uri;


    public MyItemsAdapter(ArrayList<Items> data) {
        this.dataSet = data;
        listFull = new ArrayList<>(data);
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId, textViewName, textViewPrice, textViewCategory, textViewDescription, textViewSize;
        ImageView addIcon;
        CardView cardView;
        CircleImageView itemImageIV;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewId = (TextView) itemView.findViewById(R.id.itemIdTV);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemNameTV);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.itemPriceTV);
            this.textViewCategory = (TextView) itemView.findViewById(R.id.itemCategoryTV);
            this.textViewDescription = (TextView) itemView.findViewById(R.id.itemDescriptionTV);
            this.textViewSize = itemView.findViewById(R.id.itemSizeTV);
            this.addIcon = itemView.findViewById(R.id.addiconItems);
            this.itemImageIV = itemView.findViewById(R.id.itemIMG);
            this.cardView = itemView.findViewById(R.id.card_view);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_products, parent, false);
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
        CircleImageView itemImageIV = holder.itemImageIV;
        CardView cardView = holder.cardView;

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

        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyItemsAdapter.id = dataSet.get(listPosition).getItemId();
                MyItemsAdapter.name = dataSet.get(listPosition).getItemName();
                MyItemsAdapter.price = dataSet.get(listPosition).getItemPrice();
                MyItemsAdapter.category = dataSet.get(listPosition).getItemCategory();
                MyItemsAdapter.description = dataSet.get(listPosition).getItemDescription();
                MyItemsAdapter.size = dataSet.get(listPosition).getItemSize();
                MyItemsAdapter.uri = dataSet.get(listPosition).getItemImgUrl();

                Context context = view.getContext();
                Intent intent = new Intent(context, ItemsDetails.class);
                context.startActivity(intent);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyItemsAdapter.id = dataSet.get(listPosition).getItemId();
                MyItemsAdapter.name = dataSet.get(listPosition).getItemName();
                MyItemsAdapter.price = dataSet.get(listPosition).getItemPrice();
                MyItemsAdapter.category = dataSet.get(listPosition).getItemCategory();
                MyItemsAdapter.description = dataSet.get(listPosition).getItemDescription();
                MyItemsAdapter.size = dataSet.get(listPosition).getItemSize();
                MyItemsAdapter.uri = dataSet.get(listPosition).getItemImgUrl();

                Context context = view.getContext();
                Intent intent = new Intent(context, ItemsDetails.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public Filter getProductFilter() {
        return productFilter;
    }

    private Filter productFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Items> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Items items : listFull) {
                    if (items.getItemName().toLowerCase().contains(filterPattern) || items.getItemCategory().toLowerCase().contains(filterPattern)) {
                        filteredList.add(items);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataSet.clear();
            dataSet.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
