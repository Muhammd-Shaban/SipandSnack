package com.cust.sipnsnack.Bikers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;

import java.util.ArrayList;
import java.util.List;

public class MyBikersAdapter extends RecyclerView.Adapter<MyBikersAdapter.MyViewHolder> {

    private ArrayList<BikersModel> dataSet;
    List<BikersModel> listFull;

    public static String username;
    public static String name;
    public static String phone_no;
    public static String address;

    public MyBikersAdapter(ArrayList<BikersModel> data) {
        this.dataSet = data;
        listFull = new ArrayList<>(data);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewName, textViewPhoneNo, textViewAddress, textViewAvailability;
        ImageView addIcon;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewUsername = (TextView) itemView.findViewById(R.id.bikerUsernameTV);
            this.textViewName = (TextView) itemView.findViewById(R.id.bikerNameTV);
            this.textViewPhoneNo = (TextView) itemView.findViewById(R.id.bikerPhoneNoTV);
            this.textViewAddress = (TextView) itemView.findViewById(R.id.bikerAddressTV);
            this.textViewAvailability = (TextView) itemView.findViewById(R.id.bikerAvailabilityTV);

            this.addIcon = itemView.findViewById(R.id.addiconBikerIV);
            this.cardView = itemView.findViewById(R.id.card_view);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.biker_row_products, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewUsername = holder.textViewUsername;
        TextView textViewPhoneNo = holder.textViewPhoneNo;
        TextView textViewAddress = holder.textViewAddress;
        TextView textViewAvailability = holder.textViewAvailability;
        CardView cardView = holder.cardView;


        textViewUsername.setText(dataSet.get(listPosition).getBikerUsername());
        textViewName.setText(dataSet.get(listPosition).getBikerName());
        textViewPhoneNo.setText(dataSet.get(listPosition).getBikerPhoneNo());
        textViewAddress.setText(dataSet.get(listPosition).getBikerAddress());
        textViewAvailability.setText(dataSet.get(listPosition).getBikerAvailability());

        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBikersAdapter.username = dataSet.get(listPosition).getBikerUsername();
                MyBikersAdapter.name = dataSet.get(listPosition).getBikerName();
                MyBikersAdapter.phone_no = dataSet.get(listPosition).getBikerPhoneNo();
                MyBikersAdapter.address = dataSet.get(listPosition).getBikerAddress();

                Context context = view.getContext();
                Intent intent = new Intent(context, BikersDetails.class);
                context.startActivity(intent);

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBikersAdapter.username = dataSet.get(listPosition).getBikerUsername();
                MyBikersAdapter.name = dataSet.get(listPosition).getBikerName();
                MyBikersAdapter.phone_no = dataSet.get(listPosition).getBikerPhoneNo();
                MyBikersAdapter.address = dataSet.get(listPosition).getBikerAddress();

                Context context = view.getContext();
                Intent intent = new Intent(context, BikersDetails.class);
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
            List<BikersModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (BikersModel bikers : listFull) {
                    if (bikers.getBikerUsername().toLowerCase().contains(filterPattern)|| bikers.getBikerName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(bikers);
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
