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

public class ExpenseMonthAdapter extends RecyclerView.Adapter<ExpenseMonthAdapter.MyViewHolder> implements Filterable {

    private ArrayList<ExpenseMonth> dataSet;
    List<ExpenseMonth> listFull;

    public static String date;
    public static String total;
    public static String crockery;
    public static String kitchen;
    public static String bikers;
    public static String maintenance;
    public static String others;


    public ExpenseMonthAdapter(ArrayList<ExpenseMonth> data) {
        this.dataSet = data;
        listFull = new ArrayList<>(data);
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV;
        ImageView addIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.dateTV = (TextView) itemView.findViewById(R.id.dateRight);
            this.addIcon = itemView.findViewById(R.id.addIconIV);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_month_design, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewMonth = holder.dateTV;

        textViewMonth.setText(dataSet.get(listPosition).getDate());

        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ExpenseMonthAdapter.date = dataSet.get(listPosition).getDate();
                ExpenseMonthAdapter.total = dataSet.get(listPosition).getTotal();
                ExpenseMonthAdapter.crockery = dataSet.get(listPosition).getCrockery();
                ExpenseMonthAdapter.kitchen = dataSet.get(listPosition).getKitchen();
                ExpenseMonthAdapter.bikers = dataSet.get(listPosition).getBiker();
                ExpenseMonthAdapter.maintenance = dataSet.get(listPosition).getMaintenance();
                ExpenseMonthAdapter.others = dataSet.get(listPosition).getOthers();


                Context context = view.getContext();
                Intent intent = new Intent(context, ExpenseDate.class);
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
            List<ExpenseMonth> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ExpenseMonth date : listFull) {
                    if (date.getDate().toLowerCase().contains(filterPattern)) {
                        filteredList.add(date);
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
