package com.cust.sipnsnack.Customers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MenuItemsAdapter extends RecyclerView.Adapter <MenuItemsAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MyItems> dataSet;
    List<MyItems> listFull;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();
    boolean dbCheck = false;

    public static String id;
    public static String name;
    public static String price;
    public static String category;
    public static String description;
    public static String size;
    public static String uri;
    public static boolean flag = false;

    public MenuItemsAdapter(ArrayList<MyItems> data) {
        this.dataSet = data;
        listFull = new ArrayList<MyItems>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewSize;
        CardView cardView;
        ImageView itemImageIV;
        Button addToCart;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemNameTV);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.itemPriceTV);
            this.textViewSize = itemView.findViewById(R.id.itemSizeTV);
            this.itemImageIV = itemView.findViewById(R.id.itemImgIV);
            this.addToCart = itemView.findViewById(R.id.addToCartBTN);

        }
    }


    @Override
    public MenuItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_design, parent, false);
        MenuItemsAdapter.MyViewHolder myViewHolder = new MenuItemsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MenuItemsAdapter.MyViewHolder holder, final int listPosition) {

        CardView cardView = holder.cardView;
        TextView textViewName = holder.textViewName;
        TextView textViewPrice = holder.textViewPrice;
        TextView textViewSize = holder.textViewSize;
        ImageView itemImageIV = holder.itemImageIV;

        textViewName.setText(dataSet.get(listPosition).getItemName());
        textViewPrice.setText(dataSet.get(listPosition).getItemPrice());
        textViewSize.setText(dataSet.get(listPosition).getItemSize());

        Picasso.get()
                .load(dataSet.get(listPosition).getItemImgUrl())
                .placeholder(R.drawable.logo)
                .into(itemImageIV);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuItemsAdapter.id = dataSet.get(listPosition).getItemId();
                MenuItemsAdapter.name = dataSet.get(listPosition).getItemName();
                MenuItemsAdapter.price = dataSet.get(listPosition).getItemPrice();
                MenuItemsAdapter.category = dataSet.get(listPosition).getItemCategory();
                MenuItemsAdapter.description = dataSet.get(listPosition).getItemDescription();
                MenuItemsAdapter.size = dataSet.get(listPosition).getItemSize();
                MenuItemsAdapter.uri = dataSet.get(listPosition).getItemImgUrl();

                Context context = view.getContext();
                Intent it = new Intent(context, MenuItemDetails.class);
                context.startActivity(it);
            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuItemsAdapter.id = dataSet.get(listPosition).getItemId();
                MenuItemsAdapter.name = dataSet.get(listPosition).getItemName();
                MenuItemsAdapter.price = dataSet.get(listPosition).getItemPrice();
                MenuItemsAdapter.category = dataSet.get(listPosition).getItemCategory();
                MenuItemsAdapter.description = dataSet.get(listPosition).getItemDescription();
                MenuItemsAdapter.size = dataSet.get(listPosition).getItemSize();
                MenuItemsAdapter.uri = dataSet.get(listPosition).getItemImgUrl();

                Context context = view.getContext();
                Intent it = new Intent(context, MenuItemDetails.class);
                context.startActivity(it);
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
            List<MyItems> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString();
                for (MyItems item : listFull) {
                    if (item.getItemName().contains(filterPattern)) {
                        filteredList.add(item);
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

    public void addToCart() {
        FirebaseDatabase.getInstance().getReference().child("temp_order").child(MenuItems.username)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String tempItemId = id;
                            tempItemId = tempItemId.toLowerCase();
                            String dbItemId = snapshot.child("Id").getValue().toString().toLowerCase();
                            if (dbItemId.equals(tempItemId)) {
                                dbCheck = true;
                                break;
                            } else {

                                dbCheck = false;

                            }
                        }

                        if (!dbCheck) {
                            mDatabaseRef = mDatabase.getReference().child("temp_order").
                                    child(MenuItems.username).child(MenuItemsAdapter.id);
                            mDatabaseRef.child("Id").setValue(MenuItemsAdapter.id);
                            mDatabaseRef.child("Name").setValue(MenuItemsAdapter.name);
                            mDatabaseRef.child("Price").setValue(MenuItemsAdapter.price);
                            mDatabaseRef.child("Category").setValue(MenuItemsAdapter.category);
                            mDatabaseRef.child("Description").setValue(MenuItemsAdapter.description);
                            mDatabaseRef.child("Size").setValue(MenuItemsAdapter.size);
                            mDatabaseRef.child("ImageUrl").setValue(MenuItemsAdapter.uri);
                            mDatabaseRef.child("Quantity").setValue("1");

                            flag = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
