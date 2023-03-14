package com.cust.sipnsnack.Customers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;

import java.util.ArrayList;
import java.util.List;


public class CustomerOTWAdapter extends RecyclerView.Adapter <CustomerOTWAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MyOrdersModel> dataSet;
    List<MyOrdersModel> listFull;
    public static String orderID;
    public static String addressType;
    public static String longitude, latitude, address;
    public static String phoneNo;

    public CustomerOTWAdapter(ArrayList<MyOrdersModel> data) {
        this.dataSet = data;
        listFull = new ArrayList<MyOrdersModel>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPayment, textViewQuantity, textViewBill, textViewDate,
                textViewBikerName, textViewBikerPhone, textViewTime;
        ImageView phoneCall, locationIV;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.textViewName = (TextView) itemView.findViewById(R.id.nameTV);
            this.textViewPayment = (TextView) itemView.findViewById(R.id.paymentTV);
            this.textViewQuantity = (TextView) itemView.findViewById(R.id.quantityTV);
            this.textViewBill = (TextView) itemView.findViewById(R.id.billTV);
            this.textViewDate = (TextView) itemView.findViewById(R.id.dateTV);
            this.textViewTime = (TextView) itemView.findViewById(R.id.timeTV);
            this.textViewBikerName = (TextView) itemView.findViewById(R.id.bikerNameTV);
            this.textViewBikerPhone = (TextView) itemView.findViewById(R.id.bikerPhoneNoTV);
            this.phoneCall = itemView.findViewById(R.id.phoneCallIV);
            this.locationIV = itemView.findViewById(R.id.locationIV);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_otw_design, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewPayment = holder.textViewPayment;
        TextView textViewQuantity = holder.textViewQuantity;
        TextView textViewTotal = holder.textViewBill;
        TextView textViewTime = holder.textViewTime;
        TextView textViewDate = holder.textViewDate;
        TextView textViewBiker = holder.textViewBikerName;
        TextView textViewBikerPhone = holder.textViewBikerPhone;
        ImageView phoneIV = holder.phoneCall;
        ImageView locationIV = holder.locationIV;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewPayment.setText(dataSet.get(listPosition).getPaymentType());
        textViewQuantity.setText(dataSet.get(listPosition).getQuantity());
        textViewTotal.setText(dataSet.get(listPosition).getTotalBill());
        textViewDate.setText(dataSet.get(listPosition).getOrderDate());
        textViewTime.setText(dataSet.get(listPosition).getOrderTime());
        textViewBiker.setText(dataSet.get(listPosition).getBikerName());
        textViewBikerPhone.setText(dataSet.get(listPosition).getBikerPhone());

        phoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + dataSet.get(listPosition).getBikerPhone()));
                view.getContext().startActivity(intent);
            }
        });

        locationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerOTWAdapter.orderID = dataSet.get(listPosition).getOrderId();
                CustomerOTWAdapter.addressType = dataSet.get(listPosition).getAddressType();
                CustomerOTWAdapter.longitude = dataSet.get(listPosition).getLongitude();
                CustomerOTWAdapter.latitude = dataSet.get(listPosition).getLatitude();
                CustomerOTWAdapter.address = dataSet.get(listPosition).getAddress();
                CustomerOTWAdapter.phoneNo = dataSet.get(listPosition).getBikerPhone();

                Context context = view.getContext();

                Intent intent;
                if (CustomerOTWAdapter.addressType.equals("Manual")) {
                    intent = new Intent(view.getContext(), TrackOrder.class);
                } else {
                    intent = new Intent(view.getContext(), TrackOrderLocation.class);
                }
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
            List<MyOrdersModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MyOrdersModel orders : listFull) {
                    if (orders.getOrderTime().toLowerCase().contains(filterPattern) ||
                            orders.getPaymentType().toLowerCase().contains(filterPattern)) {
                        filteredList.add(orders);
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
