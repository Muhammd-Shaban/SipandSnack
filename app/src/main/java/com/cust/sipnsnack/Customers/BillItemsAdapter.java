package com.cust.sipnsnack.Customers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;

import java.util.ArrayList;


public class BillItemsAdapter extends RecyclerView.Adapter <BillItemsAdapter.MyViewHolder> {

    public ArrayList<CartItems> dataSet;

    public static String id;
    public static String name;
    public static String price;
    public static String category;
    public static String description;
    public static String size;
    public static String uri;

    public BillItemsAdapter(ArrayList<CartItems> data) {
        this.dataSet = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewTotalPrice, textViewQty;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemNameTV);
            this.textViewQty = (TextView) itemView.findViewById(R.id.itemQtyTV);
            this.textViewTotalPrice = (TextView) itemView.findViewById(R.id.itemTotalPriceTV);
        }
    }

    @Override
    public BillItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_design, parent, false);
        BillItemsAdapter.MyViewHolder myViewHolder = new BillItemsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(BillItemsAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewTotalPrice = holder.textViewTotalPrice;
        TextView textViewQty = holder.textViewQty;

        textViewName.setText(dataSet.get(listPosition).getItemName());
        textViewTotalPrice.setText(dataSet.get(listPosition).getItemTotalPrice());
        textViewQty.setText(dataSet.get(listPosition).getItemQty());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
