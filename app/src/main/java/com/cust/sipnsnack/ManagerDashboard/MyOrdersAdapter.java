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
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersAdapter extends RecyclerView.Adapter <MyOrdersAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MyOrders> dataSet;
    List<MyOrders> listFull;

    public static String username;
    public static String name;
    public static String address;
    public static String phoneNo;
    public static String paymentType;
    public static String receiptImage;
    public static String date;
    public static String time;
    public static String orderId;
    public static String addressType;
    public static String lat;
    public static String lon;

    public MyOrdersAdapter(ArrayList<MyOrders> data) {
        this.dataSet = data;
        listFull = new ArrayList<MyOrders>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhoneNo, textViewAddress, textViewPaymentType,
        textViewTime;
        CardView cardView;
        Button showDetails;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.textViewName = (TextView) itemView.findViewById(R.id.customerNameTV);
            this.textViewPhoneNo = (TextView) itemView.findViewById(R.id.customerPhoneNoTV);
            this.textViewAddress = itemView.findViewById(R.id.customerAddressTV);
            this.textViewPaymentType = itemView.findViewById(R.id.paymentTypeTV);
            this.textViewTime = itemView.findViewById(R.id.timeTV);
            this.showDetails = itemView.findViewById(R.id.showDetailsBTN);

        }
    }

    @Override
    public MyOrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_design, parent, false);
        MyOrdersAdapter.MyViewHolder myViewHolder = new MyOrdersAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyOrdersAdapter.MyViewHolder holder, final int listPosition) {

        CardView cardView = holder.cardView;
        TextView textViewName = holder.textViewName;
        TextView textViewPhoneNo = holder.textViewPhoneNo;
        TextView textViewAddress = holder.textViewAddress;
        TextView textViewPaymentType = holder.textViewPaymentType;
        TextView textViewTime = holder.textViewTime;

        System.out.println("Address Type : " + dataSet.get(listPosition).getAddressType());

        textViewName.setText(dataSet.get(listPosition).getCustomerName());
        textViewPhoneNo.setText(dataSet.get(listPosition).getCustomerPhoneNo());

        if (dataSet.get(listPosition).getAddressType().equals("Manual")) {
            textViewAddress.setText(dataSet.get(listPosition).getCustomerAddress());
        } else {
            textViewAddress.setText("(On Maps)");
        }

        textViewPaymentType.setText(dataSet.get(listPosition).getPaymentType());
        textViewTime.setText(dataSet.get(listPosition).getOrderTime());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOrdersAdapter.username = dataSet.get(listPosition).getCustomerUsername();
                MyOrdersAdapter.name = dataSet.get(listPosition).getCustomerName();
                MyOrdersAdapter.phoneNo = dataSet.get(listPosition).getCustomerPhoneNo();
                MyOrdersAdapter.address = dataSet.get(listPosition).getCustomerAddress();
                MyOrdersAdapter.paymentType = dataSet.get(listPosition).getPaymentType();
                MyOrdersAdapter.receiptImage = dataSet.get(listPosition).getReceiptImage();
                MyOrdersAdapter.date = dataSet.get(listPosition).getOrderDate();
                MyOrdersAdapter.time = dataSet.get(listPosition).getOrderTime();
                MyOrdersAdapter.orderId = dataSet.get(listPosition).getOrderId();
                MyOrdersAdapter.addressType = dataSet.get(listPosition).getAddressType();
                MyOrdersAdapter.lon = dataSet.get(listPosition).getLongitude();
                MyOrdersAdapter.lat = dataSet.get(listPosition).getLatitude();

                Context context = view.getContext();
                Intent it = new Intent(context, PendingOrderDetails.class);
                context.startActivity(it);
            }
        });

        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOrdersAdapter.username = dataSet.get(listPosition).getCustomerUsername();
                MyOrdersAdapter.name = dataSet.get(listPosition).getCustomerName();
                MyOrdersAdapter.phoneNo = dataSet.get(listPosition).getCustomerPhoneNo();
                MyOrdersAdapter.address = dataSet.get(listPosition).getCustomerAddress();
                MyOrdersAdapter.paymentType = dataSet.get(listPosition).getPaymentType();
                MyOrdersAdapter.receiptImage = dataSet.get(listPosition).getReceiptImage();
                MyOrdersAdapter.date = dataSet.get(listPosition).getOrderDate();
                MyOrdersAdapter.time = dataSet.get(listPosition).getOrderTime();
                MyOrdersAdapter.orderId = dataSet.get(listPosition).getOrderId();
                MyOrdersAdapter.addressType = dataSet.get(listPosition).getAddressType();
                MyOrdersAdapter.lon = dataSet.get(listPosition).getLongitude();
                MyOrdersAdapter.lat = dataSet.get(listPosition).getLatitude();

                Context context = view.getContext();
                Intent it = new Intent(context, PendingOrderDetails.class);
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
            List<MyOrders> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MyOrders orders : listFull) {
                    if (orders.getCustomerName().toLowerCase().contains(filterPattern) ||
                            orders.getCustomerAddress().toLowerCase().contains(filterPattern)) {
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
