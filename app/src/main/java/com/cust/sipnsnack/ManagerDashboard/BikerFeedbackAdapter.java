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


public class BikerFeedbackAdapter extends RecyclerView.Adapter <BikerFeedbackAdapter.MyViewHolder> implements Filterable {

    private ArrayList<BikerFeedbackModel> dataSet;
    List<BikerFeedbackModel> listFull;

    public static String id, name1, name2, phone1, phone2, feedback;

    public BikerFeedbackAdapter(ArrayList<BikerFeedbackModel> data) {
        this.dataSet = data;
        listFull = new ArrayList<BikerFeedbackModel>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhoneNo, textViewFeedback, textViewName2, textViewPhoneNo2;
        Button details;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.textViewName = (TextView) itemView.findViewById(R.id.customerNameTV);
            this.textViewPhoneNo = (TextView) itemView.findViewById(R.id.customerPhoneNoTV);
            this.textViewFeedback = itemView.findViewById(R.id.feedbackTV);
            this.textViewName2 = itemView.findViewById(R.id.bikerNameTV);
            this.textViewPhoneNo2 = (TextView) itemView.findViewById(R.id.bikerPhoneNoTV);
            this.details = itemView.findViewById(R.id.detailsBTN);

        }
    }

    @Override
    public BikerFeedbackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.biker_feedback_design, parent, false);
        BikerFeedbackAdapter.MyViewHolder myViewHolder = new BikerFeedbackAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(BikerFeedbackAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewPhoneNo = holder.textViewPhoneNo;
        TextView textViewFeedback = holder.textViewFeedback;
        TextView textViewName2 = holder.textViewName2;
        TextView textViewPhoneNo2 = holder.textViewPhoneNo2;

        Button detailsBtn = holder.details;
        CardView cardView = holder.cardView;

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BikerFeedbackAdapter.id = dataSet.get(listPosition).getOrderId();
                BikerFeedbackAdapter.name1 = dataSet.get(listPosition).getCustomerName();
                BikerFeedbackAdapter.name2 = dataSet.get(listPosition).getBikerName();
                BikerFeedbackAdapter.phone1 = dataSet.get(listPosition).getCustomerPhoneNo();
                BikerFeedbackAdapter.phone2 = dataSet.get(listPosition).getBikerPhoneNo();
                BikerFeedbackAdapter.feedback = dataSet.get(listPosition).getFeedback();

                view.getContext().startActivity(new Intent(view.getContext(), BikerFeedbackDetails.class));
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BikerFeedbackAdapter.id = dataSet.get(listPosition).getOrderId();
                BikerFeedbackAdapter.name1 = dataSet.get(listPosition).getCustomerName();
                BikerFeedbackAdapter.name2 = dataSet.get(listPosition).getBikerName();
                BikerFeedbackAdapter.phone1 = dataSet.get(listPosition).getCustomerPhoneNo();
                BikerFeedbackAdapter.phone2 = dataSet.get(listPosition).getBikerPhoneNo();
                BikerFeedbackAdapter.feedback = dataSet.get(listPosition).getFeedback();

                view.getContext().startActivity(new Intent(view.getContext(), BikerFeedbackDetails.class));
            }
        });


        textViewName.setText(dataSet.get(listPosition).getCustomerName());
        textViewPhoneNo.setText(dataSet.get(listPosition).getCustomerPhoneNo());
        textViewName2.setText(dataSet.get(listPosition).getBikerName());
        textViewPhoneNo2.setText(dataSet.get(listPosition).getBikerPhoneNo());
        textViewFeedback.setText(dataSet.get(listPosition).getFeedback());

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
            List<BikerFeedbackModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (BikerFeedbackModel orders : listFull) {
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
