package com.cust.sipnsnack.Customers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CartItemsAdapter extends RecyclerView.Adapter <CartItemsAdapter.MyViewHolder> {

    private ArrayList<CartItems> dataSet;

    public static String id;
    public static String name;
    public static String price;
    public static String category;
    public static String description;
    public static String size;
    public static String uri;
    public static String quantity;
    public static String totalPrice;
    String current, Username, Id;
    public int totalBill = 0;
    public boolean flag = false;
    String tempId, tempQty, tempTPrice;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    public CartItemsAdapter(ArrayList<CartItems> data) {
        this.dataSet = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewSize, textViewTotalPrice, textViewQty;
        CardView cardView;
        ImageView itemImageIV, deleteItemIV;
        Button minus, plus;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemNameTV);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.itemPriceTV);
            this.textViewSize = (TextView) itemView.findViewById(R.id.itemSizeTV);
            this.textViewQty = (TextView) itemView.findViewById(R.id.qtyTV);
            this.textViewTotalPrice = (TextView) itemView.findViewById(R.id.itemTotalPriceTV);
            this.itemImageIV = (ImageView) itemView.findViewById(R.id.itemImgIV);
            this.deleteItemIV = (ImageView) itemView.findViewById(R.id.deleteItemIV);
            this.minus = (Button) itemView.findViewById(R.id.minusItemBTN);
            this.plus = (Button) itemView.findViewById(R.id.plusItemBTN);
        }
    }

    @Override
    public CartItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_design, parent, false);
        CartItemsAdapter.MyViewHolder myViewHolder = new CartItemsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(CartItemsAdapter.MyViewHolder holder, final int listPosition) {

        CardView cardView = holder.cardView;
        TextView textViewName = holder.textViewName;
        TextView textViewPrice = holder.textViewPrice;
        TextView textViewSize = holder.textViewSize;
        TextView textViewTotalPrice = holder.textViewTotalPrice;
        TextView textViewQty = holder.textViewQty;
        Button minus = holder.minus;
        Button plus = holder.plus;
        ImageView itemImageIV = holder.itemImageIV;
        ImageView itemDeleteIV = holder.deleteItemIV;

        textViewName.setText(dataSet.get(listPosition).getItemName());
        textViewPrice.setText(dataSet.get(listPosition).getItemPrice());
        textViewSize.setText(dataSet.get(listPosition).getItemSize());
        textViewTotalPrice.setText(dataSet.get(listPosition).getItemTotalPrice());
        textViewQty.setText(dataSet.get(listPosition).getItemQty());

        Picasso.get()
                .load(dataSet.get(listPosition).getItemImgUrl())
                .placeholder(R.drawable.logo)
                .into(itemImageIV);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Name : "+dataSet.get(listPosition).getItemName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CartItemsAdapter.id = dataSet.get(listPosition).getItemId();
                current = textViewQty.getText().toString();
                totalPrice = dataSet.get(listPosition).getItemPrice();
                int tempPrice = Integer.parseInt(totalPrice);
                int temp = Integer.parseInt(current);
                temp += 1;

                totalBill += tempPrice;

                tempPrice *= temp;

                textViewQty.setText(String.valueOf(temp));
                textViewTotalPrice.setText(String.valueOf(tempPrice));

                Username = FoodCart.username;
                tempId = dataSet.get(listPosition).getItemId();
                tempQty = textViewQty.getText().toString();
                tempTPrice = textViewTotalPrice.getText().toString();

                UpdateCart(tempId, tempQty, tempTPrice, Username);


            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = textViewQty.getText().toString();
                totalPrice = dataSet.get(listPosition).getItemPrice();
                int tempPrice = Integer.parseInt(totalPrice);
                int temp = Integer.parseInt(current);

                if (temp != 1) {
                    temp -= 1;
                    totalBill -= tempPrice;

                    tempPrice *= temp;

                    textViewQty.setText(String.valueOf(temp));
                    textViewTotalPrice.setText(String.valueOf(tempPrice));

                    Username = FoodCart.username;
                    tempId = dataSet.get(listPosition).getItemId();
                    tempQty = textViewQty.getText().toString();
                    tempTPrice = textViewTotalPrice.getText().toString();

                    UpdateCart(tempId, tempQty, tempTPrice, Username);

                }

            }
        });
        
        holder.deleteItemIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Id = dataSet.get(listPosition).getItemId();
                Username = FoodCart.username;

                deleteItem(Id, Username, view.getContext());

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void deleteItem(String id, String username, Context ctx) {

        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.ask_delete_product_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(ctx).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("temp_order").child(username).child("Items")
                        .child(id).removeValue();
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Context context = v.getContext();
                Intent intent = new Intent(context, FoodCart.class);
                context.startActivity(intent);
                Toast.makeText(context, "Item Deleted Successfully from Cart", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });

        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                flag = false;
            }
        });
    }

    public void UpdateCart(String iD, String qTy, String pRice, String uN) {

        FirebaseDatabase.getInstance().getReference().child("temp_order").child(uN).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        this.dataSnapshot = snapshot;

                        mRef = FirebaseDatabase.getInstance().getReference().child("temp_order").
                                child(uN).child("Items").child(iD);
                        mRef.child("Quantity").setValue(qTy);
                        mRef.child("TotalPrice").setValue(pRice);

                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
