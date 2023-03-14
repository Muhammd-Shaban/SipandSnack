package com.cust.sipnsnack.ManagerDashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;

import java.util.ArrayList;
import java.util.List;


public class OnTheWayOrdersAdapter extends RecyclerView.Adapter <OnTheWayOrdersAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MyOnTheWayOrders> dataSet;
    List<MyOnTheWayOrders> listFull;

    public static String username;
    public static String name;
    public static String address;
    public static String phoneNo;
    public static String paymentType;
    public static String totalQty;
    public static String totalBill;
    public static String acceptedBy;
    public static String bikerUsername;
    public static String bikerName;
    public static String bikerPhoneNo;
    public static String orderDate;
    public static String orderTime;
    public static String orderId;
    public static String addressType, lon, lat;

    public OnTheWayOrdersAdapter(ArrayList<MyOnTheWayOrders> data) {
        this.dataSet = data;
        listFull = new ArrayList<MyOnTheWayOrders>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewTotalQty, textViewBikerName, textViewAddress,
                textViewTotalBill, textViewAcceptedBy, textViewPayment;
        CardView cardView;
        Button showDetails;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);

            this.textViewName = itemView.findViewById(R.id.customerNameTV);
            this.textViewTotalQty = itemView.findViewById(R.id.quantityTV);
            this.textViewTotalBill = itemView.findViewById(R.id.billTV);
            this.textViewAcceptedBy = itemView.findViewById(R.id.acceptedByTV);
            this.textViewBikerName = itemView.findViewById(R.id.bikerNameTV);
            this.textViewPayment = itemView.findViewById(R.id.paymentTypeTV);
            this.textViewAddress = itemView.findViewById(R.id.addressTV);

            this.showDetails = itemView.findViewById(R.id.showDetailsBTN);

        }
    }

    @Override
    public OnTheWayOrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ontheway_order_design, parent, false);
        OnTheWayOrdersAdapter.MyViewHolder myViewHolder = new OnTheWayOrdersAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(OnTheWayOrdersAdapter.MyViewHolder holder, final int listPosition) {

        CardView cardView = holder.cardView;

        TextView textViewName = holder.textViewName;
        TextView textViewPaymentType = holder.textViewPayment;
        TextView textViewQuantity = holder.textViewTotalQty;
        TextView textViewBill = holder.textViewTotalBill;
        TextView textViewAcceptedBy = holder.textViewAcceptedBy;
        TextView textViewBiker = holder.textViewBikerName;
        TextView textViewAddress = holder.textViewAddress;

        textViewName.setText(dataSet.get(listPosition).getCustomerName());
        textViewPaymentType.setText(dataSet.get(listPosition).getPaymentType());
        textViewQuantity.setText(dataSet.get(listPosition).getTotalQty());
        textViewBill.setText(dataSet.get(listPosition).getTotalBill());
        textViewAcceptedBy.setText(dataSet.get(listPosition).getAcceptedBy());
        textViewBiker.setText(dataSet.get(listPosition).getBikerName());

        if (dataSet.get(listPosition).getAddressType().equals("Manual")) {
            textViewAddress.setText(dataSet.get(listPosition).getCustomerAddress());
        } else {
            textViewAddress.setText("(On Maps)");
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnTheWayOrdersAdapter.username = dataSet.get(listPosition).getCustomerUsername();
                OnTheWayOrdersAdapter.name = dataSet.get(listPosition).getCustomerName();
                OnTheWayOrdersAdapter.phoneNo = dataSet.get(listPosition).getCustomerPhoneNo();
                OnTheWayOrdersAdapter.address = dataSet.get(listPosition).getCustomerAddress();
                OnTheWayOrdersAdapter.paymentType = dataSet.get(listPosition).getPaymentType();
                OnTheWayOrdersAdapter.totalQty = dataSet.get(listPosition).getTotalQty();
                OnTheWayOrdersAdapter.totalBill = dataSet.get(listPosition).getTotalBill();
                OnTheWayOrdersAdapter.acceptedBy = dataSet.get(listPosition).getAcceptedBy();
                OnTheWayOrdersAdapter.bikerName = dataSet.get(listPosition).getBikerName();
                OnTheWayOrdersAdapter.bikerUsername = dataSet.get(listPosition).getBikerUsername();
                OnTheWayOrdersAdapter.bikerPhoneNo = dataSet.get(listPosition).getBikerPhoneNo();
                OnTheWayOrdersAdapter.orderDate = dataSet.get(listPosition).getOrderDate();
                OnTheWayOrdersAdapter.orderTime = dataSet.get(listPosition).getOrderTime();
                OnTheWayOrdersAdapter.orderId = dataSet.get(listPosition).getOrderId();
                OnTheWayOrdersAdapter.addressType = dataSet.get(listPosition).getAddressType();
                OnTheWayOrdersAdapter.lon = dataSet.get(listPosition).getLongitude();
                OnTheWayOrdersAdapter.lat = dataSet.get(listPosition).getLatitude();

                Context context = view.getContext();
                Intent it = new Intent(context, OnTheWayOrderDetails.class);
                context.startActivity(it);
            }
        });

        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnTheWayOrdersAdapter.username = dataSet.get(listPosition).getCustomerUsername();
                OnTheWayOrdersAdapter.name = dataSet.get(listPosition).getCustomerName();
                OnTheWayOrdersAdapter.phoneNo = dataSet.get(listPosition).getCustomerPhoneNo();
                OnTheWayOrdersAdapter.address = dataSet.get(listPosition).getCustomerAddress();
                OnTheWayOrdersAdapter.paymentType = dataSet.get(listPosition).getPaymentType();
                OnTheWayOrdersAdapter.totalQty = dataSet.get(listPosition).getTotalQty();
                OnTheWayOrdersAdapter.totalBill = dataSet.get(listPosition).getTotalBill();
                OnTheWayOrdersAdapter.acceptedBy = dataSet.get(listPosition).getAcceptedBy();
                OnTheWayOrdersAdapter.bikerName = dataSet.get(listPosition).getBikerName();
                OnTheWayOrdersAdapter.bikerUsername = dataSet.get(listPosition).getBikerUsername();
                OnTheWayOrdersAdapter.bikerPhoneNo = dataSet.get(listPosition).getBikerPhoneNo();
                OnTheWayOrdersAdapter.orderDate = dataSet.get(listPosition).getOrderDate();
                OnTheWayOrdersAdapter.orderTime = dataSet.get(listPosition).getOrderTime();
                OnTheWayOrdersAdapter.orderId = dataSet.get(listPosition).getOrderId();
                OnTheWayOrdersAdapter.addressType = dataSet.get(listPosition).getAddressType();
                OnTheWayOrdersAdapter.lon = dataSet.get(listPosition).getLongitude();
                OnTheWayOrdersAdapter.lat = dataSet.get(listPosition).getLatitude();

                Context context = view.getContext();
                Intent it = new Intent(context, OnTheWayOrderDetails.class);
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
            List<MyOnTheWayOrders> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MyOnTheWayOrders orders : listFull) {
                    if (orders.getCustomerName().toLowerCase().contains(filterPattern) ||
                            orders.getBikerName().toLowerCase().contains(filterPattern)) {
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
