package com.cust.sipnsnack.ManagerDashboard;

import android.content.Context;
import android.content.Intent;
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

public class MyCustomersAdapter extends RecyclerView.Adapter<MyCustomersAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Customers> dataSet;
    List<Customers> listFull;

    public static String username;
    public static String name;
    public static String cancelOrder;
    public static String status;


    public MyCustomersAdapter(ArrayList<Customers> data) {
        this.dataSet = data;
        listFull = new ArrayList<>(data);
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewName, textViewOrder, textViewStatus;
        ImageView addIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewUsername = (TextView) itemView.findViewById(R.id.customerUsernameTV);
            this.textViewName = (TextView) itemView.findViewById(R.id.customerNameTV);
            this.textViewOrder = (TextView) itemView.findViewById(R.id.cancelOrdersTV);
            this.textViewStatus = (TextView) itemView.findViewById(R.id.statusTV);
            this.addIcon = itemView.findViewById(R.id.addiconCustomers);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewUsername = holder.textViewUsername;
        TextView textViewOrder = holder.textViewOrder;
        TextView textViewStatus = holder.textViewStatus;

        textViewUsername.setText(dataSet.get(listPosition).getUserName());
        textViewName.setText(dataSet.get(listPosition).getName());
        textViewOrder.setText(dataSet.get(listPosition).getCancelOrders());
        textViewStatus.setText(dataSet.get(listPosition).getStatus());

        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomersAdapter.username = dataSet.get(listPosition).getUserName();
                MyCustomersAdapter.name = dataSet.get(listPosition).getName();
                MyCustomersAdapter.cancelOrder = dataSet.get(listPosition).getCancelOrders();
                MyCustomersAdapter.status = dataSet.get(listPosition).getStatus();

                Context context = view.getContext();
                Intent intent;
                if (MyCustomersAdapter.status.equals("Blocked")) {
                    intent = new Intent(context, CustomerDetailsUnblock.class);
                } else {
                    intent = new Intent(context, CustomerDetails.class);
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
            List<Customers> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Customers customers : listFull) {
                    if (customers.getUserName().toLowerCase().contains(filterPattern)|| customers.getCancelOrders().toLowerCase().contains(filterPattern)) {
                        filteredList.add(customers);
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
