package com.cust.sipnsnack.ManagerDashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BikersAdapter extends RecyclerView.Adapter<BikersAdapter.MyViewHolder> {

    private ArrayList<BikerModel> dataSet;
    List<BikerModel> listFull;

    public static String username;
    public static String name;
    public static String phone_no;
    public static String address;
    int loopSize2;
    public String cUsername, cName, cPhone, cAddress, cPayment, acceptedBy, cQty, cBill, date, time,
            receipt, orderId, adrType, lon, lat;
    DatabaseReference orderOnTheWayNodeRef, bikerStatusREF;
    String iD, nAme, pRice, cAtegory, dEscription, sIze, uRl, qTy, tOtal;

    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        String dateString = dateFormat.format(new Date()).toString();
        return dateString;
    }

    public static String getTodayDate() {
        Date date;
        DateFormat setDate;
        date = Calendar.getInstance().getTime();
        setDate = new SimpleDateFormat("dd/MM/yyyy");
        String current_date = setDate.format(date);
        return current_date;
    }

    public BikersAdapter(ArrayList<BikerModel> data) {
        this.dataSet = data;
        listFull = new ArrayList<>(data);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhoneNo, textViewAvailability;
        Button assignBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.bikerNameTV);
            this.textViewPhoneNo = (TextView) itemView.findViewById(R.id.bikerPhoneNoTV);
            this.textViewAvailability = (TextView) itemView.findViewById(R.id.bikerAvailabilityTV);

            this.assignBtn = itemView.findViewById(R.id.assignBikerBTN);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_biker_design, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewPhoneNo = holder.textViewPhoneNo;
        TextView textViewAvailability = holder.textViewAvailability;

        textViewName.setText(dataSet.get(listPosition).getBikerName());
        textViewPhoneNo.setText(dataSet.get(listPosition).getBikerPhoneNo());
        textViewAvailability.setText(dataSet.get(listPosition).getBikerAvailability());

        holder.assignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View view2 = layoutInflater.inflate(R.layout.ask_assign_order_dialog, null);
                Button yesBTN = view2.findViewById(R.id.yesBTN);
                Button noBTN = view2.findViewById(R.id.noBTN);
                final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).setView(view2).create();
                alertDialog.show();
                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BikersAdapter.username = dataSet.get(listPosition).getBikerUsername();
                        BikersAdapter.name = dataSet.get(listPosition).getBikerName();
                        BikersAdapter.phone_no = dataSet.get(listPosition).getBikerPhoneNo();
                        BikersAdapter.address = dataSet.get(listPosition).getBikerAddress();

                        cUsername = AssignOrder.username;
                        cName = AssignOrder.name;
                        cPhone = AssignOrder.phoneNo;
                        cAddress = AssignOrder.address;
                        cPayment = AssignOrder.paymentType;
                        cQty = AssignOrder.quantity;
                        cBill = AssignOrder.totalBill;
                        acceptedBy = AssignOrder.acceptedBy;
                        receipt = AssignOrder.receipt;
                        orderId = AssignOrder.ordId;
                        adrType = AssignOrder.adrType;
                        lon = AssignOrder.lon;
                        lat = AssignOrder.lat;

                        assignOrder();
                        setStatus(username);

                        Context context = view.getContext();
                        Toast.makeText(context, "Order Assigned Successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, DashBoard.class);
                        context.startActivity(intent);

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
            List<BikerModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (BikerModel bikers : listFull) {
                    if (bikers.getBikerPhoneNo().toLowerCase().contains(filterPattern) ||
                            bikers.getBikerName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(bikers);
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

    public void assignOrder() {
        final DatabaseReference acceptOrderNode = FirebaseDatabase.getInstance().getReference().
                child("Orders").child("On the Way").child(orderId);

        acceptOrderNode.child("CustomerUsername").setValue(cUsername);
        acceptOrderNode.child("CustomerPhoneNo").setValue(cPhone);
        acceptOrderNode.child("AddressType").setValue(adrType);

        if (adrType.equals("Manual")) {
            acceptOrderNode.child("CustomerAddress").setValue(cAddress);
        } else {
            acceptOrderNode.child("Longitude").setValue(lon);
            acceptOrderNode.child("Latitude").setValue(lat);
        }
        acceptOrderNode.child("CustomerName").setValue(cName);
        acceptOrderNode.child("CustomerPaymentType").setValue(cPayment);
        acceptOrderNode.child("CustomerTotalQuantity").setValue(cQty);
        acceptOrderNode.child("CustomerTotalBill").setValue(cBill);
        acceptOrderNode.child("AcceptedBy").setValue(acceptedBy);
        acceptOrderNode.child("BikerUsername").setValue(username);
        acceptOrderNode.child("BikerPhoneNo").setValue(phone_no);
        acceptOrderNode.child("BikerName").setValue(name);
        acceptOrderNode.child("OrderDate").setValue(getTodayDate());
        acceptOrderNode.child("OrderTime").setValue(getTime());
        acceptOrderNode.child("Status").setValue("On the Way");
        acceptOrderNode.child("ReceiptImage").setValue(receipt);


        final DatabaseReference liverOrderNode = FirebaseDatabase.getInstance().getReference().
                child("LiverOrders").child(orderId);

        liverOrderNode.child("latitude").setValue("33.65046885");
        liverOrderNode.child("longitude").setValue("73.16691711");

        copyData(orderId);
        
    }

    public void copyData(String orderID) {
        loopSize2 = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted").
                child(orderID).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.exists()) {
                                loopSize2++;
                            } else {
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted").
                child(orderID).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                String id = snapshot.child("Id").getValue().toString();

                                orderOnTheWayNodeRef = FirebaseDatabase.getInstance().getReference().
                                        child("Orders").child("On the Way").child(orderID).child("Items")
                                        .child(id);

                                iD = snapshot.child("Id").getValue().toString();
                                nAme = snapshot.child("Name").getValue().toString();
                                pRice = snapshot.child("Price").getValue().toString();
                                cAtegory = snapshot.child("Category").getValue().toString();
                                dEscription = snapshot.child("Description").getValue().toString();
                                sIze = snapshot.child("Size").getValue().toString();
                                uRl = snapshot.child("ImageUrl").getValue().toString();
                                qTy = snapshot.child("Quantity").getValue().toString();
                                tOtal = snapshot.child("TotalPrice").getValue().toString();

                                orderOnTheWayNodeRef.child("Id").setValue(iD);
                                orderOnTheWayNodeRef.child("Name").setValue(nAme);
                                orderOnTheWayNodeRef.child("Price").setValue(pRice);
                                orderOnTheWayNodeRef.child("Category").setValue(cAtegory);
                                orderOnTheWayNodeRef.child("Description").setValue(dEscription);
                                orderOnTheWayNodeRef.child("Size").setValue(sIze);
                                orderOnTheWayNodeRef.child("ImageUrl").setValue(uRl);
                                orderOnTheWayNodeRef.child("Quantity").setValue(qTy);
                                orderOnTheWayNodeRef.child("TotalPrice").setValue(tOtal);
                            }

                            deleteAcceptedOrder(orderID);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void deleteAcceptedOrder(String ordId) {
        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted").
                child(ordId).removeValue();
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setStatus(String USN) {
        bikerStatusREF = FirebaseDatabase.getInstance().getReference().
                child("Users").child("Bikers").child(USN);

        bikerStatusREF.child("AvailabilityStatus").setValue("Unavailable");
    }
}
