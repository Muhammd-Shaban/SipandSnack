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

public class DatesReportAdapter extends RecyclerView.Adapter<DatesReportAdapter.MyViewHolder> implements Filterable {

    private ArrayList<DateWise> dataSet;
    List<DateWise> listFull;

    public static String special;
    public static String pizza;
    public static String burgers;
    public static String fries;
    public static String snacks;
    public static String chilledDrinks;
    public static String seaFoods;
    public static String coffees;
    public static String netSale;
    public static String date;

    public static String special2;
    public static String pizza2;
    public static String burgers2;
    public static String fries2;
    public static String snacks2;
    public static String chilledDrinks2;
    public static String seaFoods2;
    public static String coffees2;


    public DatesReportAdapter(ArrayList<DateWise> data) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_wise_reports_design, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewDate = holder.dateTV;

        textViewDate.setText(dataSet.get(listPosition).getDate());

        holder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatesReportAdapter.special = dataSet.get(listPosition).getSpecial();
                DatesReportAdapter.pizza = dataSet.get(listPosition).getPizza();
                DatesReportAdapter.burgers = dataSet.get(listPosition).getBurgers();
                DatesReportAdapter.fries = dataSet.get(listPosition).getFries();
                DatesReportAdapter.snacks = dataSet.get(listPosition).getSnacks();
                DatesReportAdapter.chilledDrinks = dataSet.get(listPosition).getChilledDrinks();
                DatesReportAdapter.seaFoods = dataSet.get(listPosition).getSeaFoods();
                DatesReportAdapter.coffees = dataSet.get(listPosition).getCoffees();
                DatesReportAdapter.netSale = dataSet.get(listPosition).getNetSale();
                DatesReportAdapter.date = dataSet.get(listPosition).getDate();
                DatesReportAdapter.special2 = dataSet.get(listPosition).getSpecialsAmount();
                DatesReportAdapter.pizza2 = dataSet.get(listPosition).getPizzaAmount();
                DatesReportAdapter.burgers2 = dataSet.get(listPosition).getBurgersAmount();
                DatesReportAdapter.fries2 = dataSet.get(listPosition).getFriesAmount();
                DatesReportAdapter.snacks2 = dataSet.get(listPosition).getSnacksAmount();
                DatesReportAdapter.chilledDrinks2 = dataSet.get(listPosition).getChilledDrinksAmount();
                DatesReportAdapter.seaFoods2 = dataSet.get(listPosition).getSeaFoodsAmount();
                DatesReportAdapter.coffees2 = dataSet.get(listPosition).getCoffeesAmount();

                Context context = view.getContext();
                Intent intent = new Intent(context, DateReportDetails.class);;
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
            List<DateWise> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DateWise date : listFull) {
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
