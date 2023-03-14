package com.cust.sipnsnack.Customers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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


public class CustomerPendingAdapter extends RecyclerView.Adapter <CustomerPendingAdapter.MyViewHolder> implements Filterable {

    private ArrayList<MyOrdersModel> dataSet;
    List<MyOrdersModel> listFull;
    String userName;
    String cancelOrd;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();

    public CustomerPendingAdapter(ArrayList<MyOrdersModel> data, String usn) {
        this.dataSet = data;
        listFull = new ArrayList<MyOrdersModel>();
        this.userName = usn;
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPayment, textViewQuantity, textViewBill, textViewDate,
                textViewTime;
        Button cancelBtn;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.textViewName = (TextView) itemView.findViewById(R.id.nameTV);
            this.textViewPayment = (TextView) itemView.findViewById(R.id.paymentTV);
            this.textViewQuantity = (TextView) itemView.findViewById(R.id.quantityTV);
            this.textViewBill = (TextView) itemView.findViewById(R.id.billTV);
            this.textViewDate = (TextView) itemView.findViewById(R.id.dateTV);
            this.textViewTime = (TextView) itemView.findViewById(R.id.timeTV);
            this.cancelBtn = itemView.findViewById(R.id.cancelBTN);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_pending_design, parent, false);
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
        Button cancel = holder.cancelBtn;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewPayment.setText(dataSet.get(listPosition).getPaymentType());
        textViewQuantity.setText(dataSet.get(listPosition).getQuantity());
        textViewTotal.setText(dataSet.get(listPosition).getTotalBill());
        textViewDate.setText(dataSet.get(listPosition).getOrderDate());
        textViewTime.setText(dataSet.get(listPosition).getOrderTime());


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {



                LayoutInflater layoutInflater = LayoutInflater.from(view1.getContext());
                View view = layoutInflater.inflate(R.layout.ask_delete_biker_dialog, null);
                Button yesBTN = view.findViewById(R.id.yesBTN);
                Button noBTN = view.findViewById(R.id.noBTN);
                final AlertDialog alertDialog = new AlertDialog.Builder(view1.getContext()).setView(view).create();
                alertDialog.show();
                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending")
                                .child(dataSet.get(listPosition).getOrderId()).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userName)
                                .addListenerForSingleValueEvent(new ValueEventListener() {

                                    DataSnapshot dataSnapshot;

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        this.dataSnapshot = snapshot;

                                        cancelOrd = dataSnapshot.child("CancelledOrders").getValue().toString();

                                        int ord = Integer.parseInt(cancelOrd);
                                        ord++;

                                        mDatabaseRef = mDatabase.getReference().child("Users").child("Customers").child(userName);
                                        mDatabaseRef.child("CancelledOrders").setValue(String.valueOf(ord));

                                        Toast.makeText(v.getContext(), "Order was Cancelled ...", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                        Context context = v.getContext();
                        Intent intent = new Intent(context, CustomerView.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "Order Cancelled Successfully", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                noBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
