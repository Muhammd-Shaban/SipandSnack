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


public class AcceptedOrdersAdapter extends RecyclerView.Adapter <AcceptedOrdersAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MyOrders> dataSet;
    List<MyOrders> listFull;

    public static String username;
    public static String name;
    public static String address;
    public static String phoneNo;
    public static String paymentType;
    public static String receiptImage;
    public static String totalQty;
    public static String totalBill;
    public static String orderDate;
    public static String orderTime;
    public static String acceptedBy;
    public static String orderId;
    public static String addressType, latitude, longitude;

    public AcceptedOrdersAdapter(ArrayList<MyOrders> data) {
        this.dataSet = data;
        listFull = new ArrayList<MyOrders>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhoneNo, textViewPaymentType, textViewTotalQty,
                textViewTotalBill, textViewAcceptedBy;
        CardView cardView;
        Button showDetails;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.textViewName = (TextView) itemView.findViewById(R.id.customerNameTV);
            this.textViewPhoneNo = (TextView) itemView.findViewById(R.id.customerPhoneNoTV);
            this.textViewPaymentType = itemView.findViewById(R.id.paymentTypeTV);
            this.textViewTotalQty = itemView.findViewById(R.id.quantityTV);
            this.textViewTotalBill = itemView.findViewById(R.id.billTV);
            this.textViewAcceptedBy = itemView.findViewById(R.id.acceptedByTV);
            this.showDetails = itemView.findViewById(R.id.showDetailsBTN);

        }
    }

    @Override
    public AcceptedOrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_order_design, parent, false);
        AcceptedOrdersAdapter.MyViewHolder myViewHolder = new AcceptedOrdersAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AcceptedOrdersAdapter.MyViewHolder holder, final int listPosition) {

        CardView cardView = holder.cardView;
        TextView textViewName = holder.textViewName;
        TextView textViewPhoneNo = holder.textViewPhoneNo;
        TextView textViewPaymentType = holder.textViewPaymentType;
        TextView textViewQuantity = holder.textViewTotalQty;
        TextView textViewBill = holder.textViewTotalBill;
        TextView textViewAcceptedBy = holder.textViewAcceptedBy;

        textViewName.setText(dataSet.get(listPosition).getCustomerName());
        textViewPhoneNo.setText(dataSet.get(listPosition).getCustomerPhoneNo());
        textViewPaymentType.setText(dataSet.get(listPosition).getPaymentType());
        textViewQuantity.setText(dataSet.get(listPosition).getTotalQty());
        textViewBill.setText(dataSet.get(listPosition).getTotalBill());
        textViewAcceptedBy.setText(dataSet.get(listPosition).getAcceptedBy());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptedOrdersAdapter.username = dataSet.get(listPosition).getCustomerUsername();
                AcceptedOrdersAdapter.name = dataSet.get(listPosition).getCustomerName();
                AcceptedOrdersAdapter.phoneNo = dataSet.get(listPosition).getCustomerPhoneNo();
                AcceptedOrdersAdapter.address = dataSet.get(listPosition).getCustomerAddress();
                AcceptedOrdersAdapter.paymentType = dataSet.get(listPosition).getPaymentType();
                AcceptedOrdersAdapter.receiptImage = dataSet.get(listPosition).getReceiptImage();
                AcceptedOrdersAdapter.totalQty = dataSet.get(listPosition).getTotalQty();
                AcceptedOrdersAdapter.totalBill = dataSet.get(listPosition).getTotalBill();
                AcceptedOrdersAdapter.acceptedBy = dataSet.get(listPosition).getAcceptedBy();
                AcceptedOrdersAdapter.orderDate = dataSet.get(listPosition).getOrderDate();
                AcceptedOrdersAdapter.orderTime = dataSet.get(listPosition).getOrderTime();
                AcceptedOrdersAdapter.orderId = dataSet.get(listPosition).getOrderId();
                AcceptedOrdersAdapter.addressType = dataSet.get(listPosition).getAddressType();
                AcceptedOrdersAdapter.latitude = dataSet.get(listPosition).getLatitude();
                AcceptedOrdersAdapter.longitude = dataSet.get(listPosition).getLongitude();

                Context context = view.getContext();
                Intent it = new Intent(context, AcceptedOrderDetails.class);
                context.startActivity(it);
            }
        });

        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptedOrdersAdapter.username = dataSet.get(listPosition).getCustomerUsername();
                AcceptedOrdersAdapter.name = dataSet.get(listPosition).getCustomerName();
                AcceptedOrdersAdapter.phoneNo = dataSet.get(listPosition).getCustomerPhoneNo();
                AcceptedOrdersAdapter.address = dataSet.get(listPosition).getCustomerAddress();
                AcceptedOrdersAdapter.paymentType = dataSet.get(listPosition).getPaymentType();
                AcceptedOrdersAdapter.receiptImage = dataSet.get(listPosition).getReceiptImage();
                AcceptedOrdersAdapter.totalQty = dataSet.get(listPosition).getTotalQty();
                AcceptedOrdersAdapter.totalBill = dataSet.get(listPosition).getTotalBill();
                AcceptedOrdersAdapter.acceptedBy = dataSet.get(listPosition).getAcceptedBy();
                AcceptedOrdersAdapter.orderDate = dataSet.get(listPosition).getOrderDate();
                AcceptedOrdersAdapter.orderTime = dataSet.get(listPosition).getOrderTime();
                AcceptedOrdersAdapter.orderId = dataSet.get(listPosition).getOrderId();
                AcceptedOrdersAdapter.addressType = dataSet.get(listPosition).getAddressType();
                AcceptedOrdersAdapter.latitude = dataSet.get(listPosition).getLatitude();
                AcceptedOrdersAdapter.longitude = dataSet.get(listPosition).getLongitude();

                Context context = view.getContext();
                Intent it = new Intent(context, AcceptedOrderDetails.class);
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
