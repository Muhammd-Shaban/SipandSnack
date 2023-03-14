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

public class YearsReportAdapter extends RecyclerView.Adapter<YearsReportAdapter.MyViewHolder> implements Filterable {

    private ArrayList<ReportsModel2> dataSet;
    List<ReportsModel2> listFull;

    public static String special;
    public static String pizza;
    public static String burgers;
    public static String fries;
    public static String snacks;
    public static String chilledDrinks;
    public static String seaFoods;
    public static String coffees;
    public static String netSale;
    public static String year;

    public static String special2;
    public static String pizza2;
    public static String burgers2;
    public static String fries2;
    public static String snacks2;
    public static String chilledDrinks2;
    public static String seaFoods2;
    public static String coffees2;


    public YearsReportAdapter(ArrayList<ReportsModel2> data) {
        this.dataSet = data;
        listFull = new ArrayList<>(data);
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView yearTV;
        ImageView addIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.yearTV = (TextView) itemView.findViewById(R.id.yearRight);
            this.addIcon = itemView.findViewById(R.id.addIconIV);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_wise_reports_design, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewYear = holder.yearTV;

        textViewYear.setText(dataSet.get(listPosition).getYear());

        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YearsReportAdapter.special = dataSet.get(listPosition).getSpecial();
                YearsReportAdapter.pizza = dataSet.get(listPosition).getPizza();
                YearsReportAdapter.burgers = dataSet.get(listPosition).getBurgers();
                YearsReportAdapter.fries = dataSet.get(listPosition).getFries();
                YearsReportAdapter.snacks = dataSet.get(listPosition).getSnacks();
                YearsReportAdapter.chilledDrinks = dataSet.get(listPosition).getChilledDrinks();
                YearsReportAdapter.seaFoods = dataSet.get(listPosition).getSeaFoods();
                YearsReportAdapter.coffees = dataSet.get(listPosition).getCoffees();
                YearsReportAdapter.netSale = dataSet.get(listPosition).getNetSale();
                YearsReportAdapter.year = dataSet.get(listPosition).getYear();

                YearsReportAdapter.special2 = dataSet.get(listPosition).getSpecialsAmount();
                YearsReportAdapter.pizza2 = dataSet.get(listPosition).getPizzaAmount();
                YearsReportAdapter.burgers2 = dataSet.get(listPosition).getBurgersAmount();
                YearsReportAdapter.fries2 = dataSet.get(listPosition).getFriesAmount();
                YearsReportAdapter.snacks2 = dataSet.get(listPosition).getSnacksAmount();
                YearsReportAdapter.chilledDrinks2 = dataSet.get(listPosition).getChilledDrinksAmount();
                YearsReportAdapter.seaFoods2 = dataSet.get(listPosition).getSeaFoodsAmount();
                YearsReportAdapter.coffees2 = dataSet.get(listPosition).getCoffeesAmount();

                Context context = view.getContext();
                Intent intent = new Intent(context, YearReportDetail.class);;
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
            List<ReportsModel2> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ReportsModel2 date : listFull) {
                    if (date.getYear().toLowerCase().contains(filterPattern)) {
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
