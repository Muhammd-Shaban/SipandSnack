package com.cust.sipnsnack.ManagerDashboard;

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


public class FoodFeedbackAdapter extends RecyclerView.Adapter <FoodFeedbackAdapter.MyViewHolder> implements Filterable {

    private ArrayList<FoodModel> dataSet;
    List<FoodModel> listFull;

    public static String name, phone, feedback, id;

    public FoodFeedbackAdapter(ArrayList<FoodModel> data) {
        this.dataSet = data;
        listFull = new ArrayList<FoodModel>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhoneNo, textViewFeedback;
        CardView cardView;
        Button details;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.textViewName = (TextView) itemView.findViewById(R.id.customerNameTV);
            this.textViewPhoneNo = (TextView) itemView.findViewById(R.id.customerPhoneNoTV);
            this.textViewFeedback = itemView.findViewById(R.id.feedbackTV);
            this.details = itemView.findViewById(R.id.detailsBTN);

        }
    }

    @Override
    public FoodFeedbackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_feedback_design, parent, false);
        FoodFeedbackAdapter.MyViewHolder myViewHolder = new FoodFeedbackAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(FoodFeedbackAdapter.MyViewHolder holder, final int listPosition) {

        CardView cardView = holder.cardView;
        TextView textViewName = holder.textViewName;
        TextView textViewPhoneNo = holder.textViewPhoneNo;
        TextView textViewFeedback = holder.textViewFeedback;
        Button showDetails = holder.details;


        textViewName.setText(dataSet.get(listPosition).getCustomerName());
        textViewPhoneNo.setText(dataSet.get(listPosition).getCustomerPhoneNo());
        textViewFeedback.setText(dataSet.get(listPosition).getFeedback());

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodFeedbackAdapter.name = dataSet.get(listPosition).getCustomerName();
                FoodFeedbackAdapter.phone = dataSet.get(listPosition).getCustomerPhoneNo();
                FoodFeedbackAdapter.feedback = dataSet.get(listPosition).getFeedback();
                FoodFeedbackAdapter.id = dataSet.get(listPosition).getOrderId();

                view.getContext().startActivity(new Intent(view.getContext(), FoodFeedbackDetails.class));
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodFeedbackAdapter.name = dataSet.get(listPosition).getCustomerName();
                FoodFeedbackAdapter.phone = dataSet.get(listPosition).getCustomerPhoneNo();
                FoodFeedbackAdapter.feedback = dataSet.get(listPosition).getFeedback();
                FoodFeedbackAdapter.id = dataSet.get(listPosition).getOrderId();

                view.getContext().startActivity(new Intent(view.getContext(), FoodFeedbackDetails.class));
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
            List<FoodModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (FoodModel orders : listFull) {
                    if (orders.getCustomerName().toLowerCase().contains(filterPattern) ||
                            orders.getCustomerName().toLowerCase().contains(filterPattern)) {
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
