package com.cust.sipnsnack.ManagerDashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;

import java.util.ArrayList;


public class AcceptedOrderItemsAdapter extends RecyclerView.Adapter <AcceptedOrderItemsAdapter.MyViewHolder> {

    public ArrayList<AcceptedOrderItems> dataSet;

    public static String id;
    public static String name;
    public static String price;
    public static String category;
    public static String description;
    public static String size;
    public static String uri;
    public static String totalQty;
    public static String totalBill;
    int qty = 0, Tprice = 0;

    public AcceptedOrderItemsAdapter(ArrayList<AcceptedOrderItems> data) {
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
    public AcceptedOrderItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_design, parent, false);
        AcceptedOrderItemsAdapter.MyViewHolder myViewHolder = new AcceptedOrderItemsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AcceptedOrderItemsAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewTotalPrice = holder.textViewTotalPrice;
        TextView textViewQty = holder.textViewQty;

        qty += Integer.parseInt(dataSet.get(listPosition).getItemQty());
        Tprice += Integer.parseInt(dataSet.get(listPosition).getItemPrice());

        totalBill = String.valueOf(Tprice);
        totalQty = String.valueOf(qty);

        textViewName.setText(dataSet.get(listPosition).getItemName());
        textViewTotalPrice.setText(dataSet.get(listPosition).getItemTotalPrice());
        textViewQty.setText(dataSet.get(listPosition).getItemQty());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
