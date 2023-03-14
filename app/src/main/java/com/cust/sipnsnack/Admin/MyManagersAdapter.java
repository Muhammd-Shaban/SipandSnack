package com.cust.sipnsnack.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;

import java.util.ArrayList;
import java.util.List;

public class MyManagersAdapter extends RecyclerView.Adapter<MyManagersAdapter.MyViewHolder> {

    private ArrayList<ManagersModel> dataSet;
    List<ManagersModel> listFull;

    public static String username;
    public static String name;
    public static String phone_no;

    public MyManagersAdapter(ArrayList<ManagersModel> data) {
        this.dataSet = data;
        listFull = new ArrayList<>(data);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewName, textViewPhoneNo;
        ImageView addIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewUsername = (TextView) itemView.findViewById(R.id.managerUsernameTV);
            this.textViewName = (TextView) itemView.findViewById(R.id.managerNameTV);
            this.textViewPhoneNo = (TextView) itemView.findViewById(R.id.managerPhoneNoTV);

            this.addIcon = itemView.findViewById(R.id.addiconManagerIV);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_row_products, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewUsername = holder.textViewUsername;
        TextView textViewPhoneNo = holder.textViewPhoneNo;


        textViewUsername.setText(dataSet.get(listPosition).getManagerUsername());
        textViewName.setText(dataSet.get(listPosition).getManagerName());
        textViewPhoneNo.setText(dataSet.get(listPosition).getManagerPhoneNo());

        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyManagersAdapter.username = dataSet.get(listPosition).getManagerUsername();
                MyManagersAdapter.name = dataSet.get(listPosition).getManagerName();
                MyManagersAdapter.phone_no = dataSet.get(listPosition).getManagerPhoneNo();

                Context context = view.getContext();
                Intent intent = new Intent(context, ManagersDetails.class);
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
            List<ManagersModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ManagersModel managers : listFull) {
                    if (managers.getManagerUsername().toLowerCase().contains(filterPattern)|| managers.getManagerName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(managers);
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
