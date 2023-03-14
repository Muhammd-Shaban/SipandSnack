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

public class MonthsReportAdapter extends RecyclerView.Adapter<MonthsReportAdapter.MyViewHolder> implements Filterable {

    private ArrayList<ReportsModel> dataSet;
    List<ReportsModel> listFull;

    public static String special;
    public static String pizza;
    public static String burgers;
    public static String fries;
    public static String snacks;
    public static String chilledDrinks;
    public static String seaFoods;
    public static String coffees;
    public static String netSale;
    public static String month;

    public static String special2;
    public static String pizza2;
    public static String burgers2;
    public static String fries2;
    public static String snacks2;
    public static String chilledDrinks2;
    public static String seaFoods2;
    public static String coffees2;


    public MonthsReportAdapter(ArrayList<ReportsModel> data) {
        this.dataSet = data;
        listFull = new ArrayList<>(data);
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView monthTV;
        ImageView addIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.monthTV = (TextView) itemView.findViewById(R.id.monthRight);
            this.addIcon = itemView.findViewById(R.id.addIconIV);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_wise_reports_design, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewMonth = holder.monthTV;

        textViewMonth.setText(dataSet.get(listPosition).getMonth());

        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MonthsReportAdapter.special = dataSet.get(listPosition).getSpecial();
                MonthsReportAdapter.pizza = dataSet.get(listPosition).getPizza();
                MonthsReportAdapter.burgers = dataSet.get(listPosition).getBurgers();
                MonthsReportAdapter.fries = dataSet.get(listPosition).getFries();
                MonthsReportAdapter.snacks = dataSet.get(listPosition).getSnacks();
                MonthsReportAdapter.chilledDrinks = dataSet.get(listPosition).getChilledDrinks();
                MonthsReportAdapter.seaFoods = dataSet.get(listPosition).getSeaFoods();
                MonthsReportAdapter.coffees = dataSet.get(listPosition).getCoffees();
                MonthsReportAdapter.netSale = dataSet.get(listPosition).getNetSale();
                MonthsReportAdapter.month = dataSet.get(listPosition).getMonth();

                MonthsReportAdapter.special2 = dataSet.get(listPosition).getSpecialsAmount();
                MonthsReportAdapter.pizza2 = dataSet.get(listPosition).getPizzaAmount();
                MonthsReportAdapter.burgers2 = dataSet.get(listPosition).getBurgersAmount();
                MonthsReportAdapter.fries2 = dataSet.get(listPosition).getFriesAmount();
                MonthsReportAdapter.snacks2 = dataSet.get(listPosition).getSnacksAmount();
                MonthsReportAdapter.chilledDrinks2 = dataSet.get(listPosition).getChilledDrinksAmount();
                MonthsReportAdapter.seaFoods2 = dataSet.get(listPosition).getSeaFoodsAmount();
                MonthsReportAdapter.coffees2 = dataSet.get(listPosition).getCoffeesAmount();

                Context context = view.getContext();
                Intent intent = new Intent(context, MonthReportDetail.class);;
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
            List<ReportsModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ReportsModel date : listFull) {
                    if (date.getMonth().toLowerCase().contains(filterPattern)) {
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
