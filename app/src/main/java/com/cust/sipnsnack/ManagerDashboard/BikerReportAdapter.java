package com.cust.sipnsnack.ManagerDashboard;

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

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class BikerReportAdapter extends RecyclerView.Adapter <BikerReportAdapter.MyViewHolder> implements Filterable {

    private ArrayList<ReportModel> dataSet;
    List<ReportModel> listFull;

    public BikerReportAdapter(ArrayList<ReportModel> data) {
        this.dataSet = data;
        listFull = new ArrayList<ReportModel>();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhoneNo, textViewReport, textViewDate, textViewTime;
        CardView cardView;
        Button delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.textViewName = (TextView) itemView.findViewById(R.id.nameTV);
            this.textViewPhoneNo = (TextView) itemView.findViewById(R.id.phoneNoTV);
            this.textViewReport = itemView.findViewById(R.id.reportTV);
            this.textViewDate = itemView.findViewById(R.id.dateTV);
            this.textViewTime = itemView.findViewById(R.id.timeTV);
            this.delete = itemView.findViewById(R.id.deleteBTN);

        }
    }

    @Override
    public BikerReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_report_design, parent, false);
        BikerReportAdapter.MyViewHolder myViewHolder = new BikerReportAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(BikerReportAdapter.MyViewHolder holder, final int listPosition) {

        CardView cardView = holder.cardView;
        TextView textViewName = holder.textViewName;
        TextView textViewPhoneNo = holder.textViewPhoneNo;
        TextView textViewReport = holder.textViewReport;
        TextView textViewDate = holder.textViewDate;
        TextView textViewTime = holder.textViewTime;
        Button delete = holder.delete;


        textViewName.setText(dataSet.get(listPosition).getName());
        textViewPhoneNo.setText(dataSet.get(listPosition).getPhoneNo());
        textViewReport.setText(dataSet.get(listPosition).getReport());
        textViewDate.setText(dataSet.get(listPosition).getDate());
        textViewTime.setText(dataSet.get(listPosition).getTime());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                LayoutInflater layoutInflater = LayoutInflater.from(view1.getContext());
                View view = layoutInflater.inflate(R.layout.ask_delete_report_dialog, null);
                Button yesBTN = view.findViewById(R.id.yesBTN);
                Button noBTN = view.findViewById(R.id.noBTN);
                final AlertDialog alertDialog = new AlertDialog.Builder(view1.getContext()).setView(view).create();
                alertDialog.show();
                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("ReportsForSystem").
                                child("Bikers").child(dataSet.get(listPosition).getId()).removeValue();
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DashBoard.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "Report Deleted Successfully", Toast.LENGTH_SHORT).show();
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
            List<ReportModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ReportModel orders : listFull) {
                    if (orders.getName().toLowerCase().contains(filterPattern) ||
                            orders.getDate().toLowerCase().contains(filterPattern)) {
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
