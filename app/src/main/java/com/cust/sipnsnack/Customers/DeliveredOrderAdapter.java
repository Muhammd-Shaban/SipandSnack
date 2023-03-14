package com.cust.sipnsnack.Customers;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DeliveredOrderAdapter extends RecyclerView.Adapter <DeliveredOrderAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MyOrdersModel> dataSet;
    List<MyOrdersModel> listFull;

    String bikerUsn;
    float currentRating, dbRating, dbSum, newRating;
    int feedbackCounts;

    String name, phone;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();
    private DatabaseReference mDatabaseRef2 = mDatabase.getReference();

    public DeliveredOrderAdapter(ArrayList<MyOrdersModel> data, String nm, String ph) {
        this.dataSet = data;
        listFull = new ArrayList<MyOrdersModel>();

        this.name = nm;
        this.phone = ph;
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPayment, textViewQuantity, textViewBill, textViewDate,
                textViewTime;
        Button feedback, bikerFeedback;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.textViewName = (TextView) itemView.findViewById(R.id.nameTV);
            this.textViewPayment = (TextView) itemView.findViewById(R.id.paymentTV);
            this.textViewQuantity = (TextView) itemView.findViewById(R.id.quantityTV);
            this.textViewBill = (TextView) itemView.findViewById(R.id.billTV);
            this.textViewDate = (TextView) itemView.findViewById(R.id.dateTV);
            this.textViewTime = (TextView) itemView.findViewById(R.id.timeTV);

            this.feedback = itemView.findViewById(R.id.feedbackBTN);
            this.bikerFeedback = itemView.findViewById(R.id.bikerFeedbackBTN);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivered_design, parent, false);
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

        Button feedbackBtn = holder.feedback;
        Button bikerFeedback = holder.bikerFeedback;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewPayment.setText(dataSet.get(listPosition).getPaymentType());
        textViewQuantity.setText(dataSet.get(listPosition).getQuantity());
        textViewTotal.setText(dataSet.get(listPosition).getTotalBill());
        textViewDate.setText(dataSet.get(listPosition).getOrderDate());
        textViewTime.setText(dataSet.get(listPosition).getOrderTime());


        // FOOD FEEDBACK BUTTON
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View view2 = layoutInflater.inflate(R.layout.feedback_dialog, null);

                Button yesBTN = view2.findViewById(R.id.submitBTN);
                Button closeBTN = view2.findViewById(R.id.closeBTN);
                EditText feedback = view2.findViewById(R.id.feedbackTXT);
                RatingBar ratingBar = view2.findViewById(R.id.ratingBar);

                final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).setView(view2).create();
                alertDialog.show();
                alertDialog.setCancelable(true);

                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (feedback.getText().toString().equals("")) {
                            feedback.setError("You must need to Enter Feedback");
                            Toast.makeText(view.getContext(), "Feedback must be Entered ...", Toast.LENGTH_SHORT).show();
                        } else {

                            mDatabaseRef = mDatabase.getReference().child("Feedbacks").child(dataSet.get(listPosition).getOrderId());
                            mDatabaseRef.child("FeedbackText").setValue(feedback.getText().toString());
                            mDatabaseRef.child("CustomerName").setValue(name);
                            mDatabaseRef.child("CustomerPhoneNo").setValue(phone);

                            mDatabaseRef2 = mDatabase.getReference().child("OrdersStars").child(dataSet.get(listPosition).getOrderId());
                            mDatabaseRef2.child("Stars").setValue(String.valueOf(ratingBar.getRating()));
                            mDatabaseRef2.child("CustomerName").setValue(dataSet.get(listPosition).getName());
                            mDatabaseRef2.child("OrderDate").setValue(dataSet.get(listPosition).getOrderDate());
                            mDatabaseRef2.child("OrderTime").setValue(dataSet.get(listPosition).getOrderTime());

                            Toast.makeText(view.getContext(), "Feedback Submitted Successfully.", Toast.LENGTH_SHORT).show();

                            alertDialog.dismiss();

                        }

                        Intent it = new Intent(view.getContext(), CustomerView.class);
                        view.getContext().startActivity(it);
                    }
                });


                closeBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            }
        });


        // BIKER FEEDBACK BUTTON ...
        bikerFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bikerUsn  = dataSet.get(listPosition).getBikerUsername();

                FirebaseDatabase.getInstance().getReference().child("BikersStars").child(bikerUsn)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        feedbackCounts = Integer.parseInt(dataSnapshot.child("FeedbackCount").getValue().toString());
                        dbSum = Float.parseFloat(dataSnapshot.child("StarsSum").getValue().toString());
                        dbRating = Float.parseFloat(dataSnapshot.child("TotalRating").getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View view2 = layoutInflater.inflate(R.layout.biker_feedback_dialog, null);
                Button yesBTN = view2.findViewById(R.id.submitBTN);
                Button closeBTN = view2.findViewById(R.id.closeBTN);
                RatingBar ratingBar = view2.findViewById(R.id.ratingBar);
                EditText feedback = view2.findViewById(R.id.feedbackTXT);

                final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).setView(view2).create();
                alertDialog.show();
                alertDialog.setCancelable(true);

                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        currentRating = ratingBar.getRating();

                        dbSum = dbSum + currentRating;
                        feedbackCounts++;

                        newRating = dbSum / feedbackCounts;

                        if (feedback.getText().toString().equals("")) {
                            feedback.setError("You must need to Enter Feedback");
                            Toast.makeText(view.getContext(), "Feedback must be Entered ...", Toast.LENGTH_SHORT).show();
                        } else {

                            mDatabaseRef = mDatabase.getReference().child("BikersFeedback").child(dataSet.get(listPosition).getOrderId());
                            mDatabaseRef.child("FeedbackText").setValue(feedback.getText().toString());
                            mDatabaseRef.child("CustomerName").setValue(name);
                            mDatabaseRef.child("CustomerPhoneNo").setValue(phone);
                            mDatabaseRef.child("BikerName").setValue(dataSet.get(listPosition).getBikerName());
                            mDatabaseRef.child("BikerPhoneNo").setValue(dataSet.get(listPosition).getBikerPhone());


                            mDatabaseRef2 = mDatabase.getReference().child("BikersStars").child(dataSet.get(listPosition).getBikerUsername());
                            mDatabaseRef2.child("FeedbackCount").setValue(String.valueOf(feedbackCounts));
                            mDatabaseRef2.child("StarsSum").setValue(String.valueOf(dbSum));
                            mDatabaseRef2.child("TotalRating").setValue(String.valueOf(newRating));


                            Toast.makeText(view.getContext(), "Feedback Submitted Successfully.", Toast.LENGTH_SHORT).show();

                            alertDialog.dismiss();

                        }

                        Intent it = new Intent(view.getContext(), CustomerView.class);
                        view.getContext().startActivity(it);
                    }
                });

                closeBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
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
