package com.cust.sipnsnack.Customers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;

import java.util.ArrayList;
import java.util.List;


public class CustomerOrdersAdapter extends RecyclerView.Adapter <CustomerOrdersAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MyOrdersModel> dataSet;
    List<MyOrdersModel> listFull;

    public CustomerOrdersAdapter(ArrayList<MyOrdersModel> data) {
        this.dataSet = data;
        listFull = new ArrayList<MyOrdersModel>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPayment, textViewQuantity, textViewBill, textViewDate,
                textViewTime;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.textViewName = (TextView) itemView.findViewById(R.id.nameTV);
            this.textViewPayment = (TextView) itemView.findViewById(R.id.paymentTV);
            this.textViewQuantity = (TextView) itemView.findViewById(R.id.quantityTV);
            this.textViewBill = (TextView) itemView.findViewById(R.id.billTV);
            this.textViewDate = (TextView) itemView.findViewById(R.id.dateTV);
            this.textViewTime = (TextView) itemView.findViewById(R.id.timeTV);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_order_design, parent, false);
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

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewPayment.setText(dataSet.get(listPosition).getPaymentType());
        textViewQuantity.setText(dataSet.get(listPosition).getQuantity());
        textViewTotal.setText(dataSet.get(listPosition).getTotalBill());
        textViewDate.setText(dataSet.get(listPosition).getOrderDate());
        textViewTime.setText(dataSet.get(listPosition).getOrderTime());
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
