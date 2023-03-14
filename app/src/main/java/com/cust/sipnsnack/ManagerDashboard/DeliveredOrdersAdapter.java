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


public class DeliveredOrdersAdapter extends RecyclerView.Adapter <DeliveredOrdersAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MyDeliveredOrders> dataSet;
    List<MyDeliveredOrders> listFull;

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
    public static String receiptImage;
    public static String dbKey;
    public static String addressType;

    public DeliveredOrdersAdapter(ArrayList<MyDeliveredOrders> data) {
        this.dataSet = data;
        listFull = new ArrayList<MyDeliveredOrders>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewTotalQty, textViewBikerName, textViewPhone,
                textViewTotalBill, textViewPayment, textViewDate;
        CardView cardView;
        Button showDetails;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);

            this.textViewName = itemView.findViewById(R.id.customerNameTV);
            this.textViewTotalQty = itemView.findViewById(R.id.quantityTV);
            this.textViewTotalBill = itemView.findViewById(R.id.billTV);
            this.textViewBikerName = itemView.findViewById(R.id.bikerNameTV);
            this.textViewPayment = itemView.findViewById(R.id.paymentTypeTV);
            this.textViewPhone = itemView.findViewById(R.id.phoneTV);
            this.textViewDate = itemView.findViewById(R.id.dateTV);

            this.showDetails = itemView.findViewById(R.id.showDetailsBTN);

        }
    }

    @Override
    public DeliveredOrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivered_order_design, parent, false);
        DeliveredOrdersAdapter.MyViewHolder myViewHolder = new DeliveredOrdersAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(DeliveredOrdersAdapter.MyViewHolder holder, final int listPosition) {

        CardView cardView = holder.cardView;

        TextView textViewName = holder.textViewName;
        TextView textViewPaymentType = holder.textViewPayment;
        TextView textViewQuantity = holder.textViewTotalQty;
        TextView textViewBill = holder.textViewTotalBill;
        TextView textViewBiker = holder.textViewBikerName;
        TextView textViewPhone = holder.textViewPhone;
        TextView textViewDate = holder.textViewDate;

        textViewName.setText(dataSet.get(listPosition).getCustomerName());
        textViewPaymentType.setText(dataSet.get(listPosition).getPaymentType());
        textViewQuantity.setText(dataSet.get(listPosition).getTotalQty());
        textViewBill.setText(dataSet.get(listPosition).getTotalBill());
        textViewBiker.setText(dataSet.get(listPosition).getBikerName());
        textViewPhone.setText(dataSet.get(listPosition).getCustomerPhoneNo());
        textViewDate.setText(dataSet.get(listPosition).getOrderDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveredOrdersAdapter.username = dataSet.get(listPosition).getCustomerUsername();
                DeliveredOrdersAdapter.name = dataSet.get(listPosition).getCustomerName();
                DeliveredOrdersAdapter.phoneNo = dataSet.get(listPosition).getCustomerPhoneNo();
                DeliveredOrdersAdapter.address = dataSet.get(listPosition).getCustomerAddress();
                DeliveredOrdersAdapter.paymentType = dataSet.get(listPosition).getPaymentType();
                DeliveredOrdersAdapter.totalQty = dataSet.get(listPosition).getTotalQty();
                DeliveredOrdersAdapter.totalBill = dataSet.get(listPosition).getTotalBill();
                DeliveredOrdersAdapter.acceptedBy = dataSet.get(listPosition).getAcceptedBy();
                DeliveredOrdersAdapter.bikerName = dataSet.get(listPosition).getBikerName();
                DeliveredOrdersAdapter.bikerUsername = dataSet.get(listPosition).getBikerUsername();
                DeliveredOrdersAdapter.bikerPhoneNo = dataSet.get(listPosition).getBikerPhoneNo();
                DeliveredOrdersAdapter.orderDate = dataSet.get(listPosition).getOrderDate();
                DeliveredOrdersAdapter.orderTime = dataSet.get(listPosition).getOrderTime();
                DeliveredOrdersAdapter.receiptImage = dataSet.get(listPosition).getReceiptImage();
                DeliveredOrdersAdapter.dbKey = dataSet.get(listPosition).getDbKey();
                DeliveredOrdersAdapter.addressType = dataSet.get(listPosition).getAddressType();

                Context context = view.getContext();
                Intent it = new Intent(context, DeliveredOrderDetails.class);
                context.startActivity(it);
            }
        });

        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveredOrdersAdapter.username = dataSet.get(listPosition).getCustomerUsername();
                DeliveredOrdersAdapter.name = dataSet.get(listPosition).getCustomerName();
                DeliveredOrdersAdapter.phoneNo = dataSet.get(listPosition).getCustomerPhoneNo();
                DeliveredOrdersAdapter.address = dataSet.get(listPosition).getCustomerAddress();
                DeliveredOrdersAdapter.paymentType = dataSet.get(listPosition).getPaymentType();
                DeliveredOrdersAdapter.totalQty = dataSet.get(listPosition).getTotalQty();
                DeliveredOrdersAdapter.totalBill = dataSet.get(listPosition).getTotalBill();
                DeliveredOrdersAdapter.acceptedBy = dataSet.get(listPosition).getAcceptedBy();
                DeliveredOrdersAdapter.bikerName = dataSet.get(listPosition).getBikerName();
                DeliveredOrdersAdapter.bikerUsername = dataSet.get(listPosition).getBikerUsername();
                DeliveredOrdersAdapter.bikerPhoneNo = dataSet.get(listPosition).getBikerPhoneNo();
                DeliveredOrdersAdapter.orderDate = dataSet.get(listPosition).getOrderDate();
                DeliveredOrdersAdapter.orderTime = dataSet.get(listPosition).getOrderTime();
                DeliveredOrdersAdapter.receiptImage = dataSet.get(listPosition).getReceiptImage();
                DeliveredOrdersAdapter.dbKey = dataSet.get(listPosition).getDbKey();
                DeliveredOrdersAdapter.addressType = dataSet.get(listPosition).getAddressType();

                Context context = view.getContext();
                Intent it = new Intent(context, DeliveredOrderDetails.class);
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
            List<MyDeliveredOrders> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MyDeliveredOrders orders : listFull) {
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
