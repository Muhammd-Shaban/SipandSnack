package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MenuItemDetails extends AppCompatActivity {

    TextView nameTV, priceTV, qtyTV, descTV, totalPriceTV;
    ImageView imageIV;
    String nameSTR, priceSTR, qtySTR = "", descSTR, totalPriceSTR = "", itemIdSTR, imageURL, current, uN;
    Button addToCart, minusBtn, plusBtn;
    boolean flag = false;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();
    LoadingDialog loadingDialog;
    ImageView backBtn;

    SharedPreferences spr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_details);

        loadingDialog = new LoadingDialog(MenuItemDetails.this);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        uN = spr.getString("Username", "");

        nameTV = findViewById(R.id.itemNameTV);
        priceTV = findViewById(R.id.itemPriceTV);
        qtyTV = findViewById(R.id.qtyTV);
        descTV = findViewById(R.id.itemDescriptionTV);
        totalPriceTV = findViewById(R.id.itemTotalPriceTV);
        backBtn = findViewById(R.id.backBTN);

        imageIV = findViewById(R.id.itemImgIV);

        addToCart = findViewById(R.id.addToCartBTN);
        minusBtn = findViewById(R.id.minusItemBTN);
        plusBtn = findViewById(R.id.plusItemBTN);

        descSTR = MenuItemsAdapter.description;
        imageURL = MenuItemsAdapter.uri;
        itemIdSTR = MenuItemsAdapter.id;
        nameSTR = MenuItemsAdapter.name;
        priceSTR = MenuItemsAdapter.price;

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FoodMenu.class));
                finish();
            }
        });


        try {
            readQty();
        } catch (Exception e) {
            System.out.println(e);
        }


        if (qtySTR.equals("")) {
            qtySTR = "1";
            totalPriceSTR = MenuItemsAdapter.price;
        }

        nameTV.setText(nameSTR);
        priceTV.setText(priceSTR);
        qtyTV.setText(qtySTR);
        descTV.setText(descSTR);
        totalPriceTV.setText(totalPriceSTR);


        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.logo)
                .into(imageIV);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                addToCart();

                loadingDialog.dismissDialog();
                Toast.makeText(MenuItemDetails.this, "Item Successfully Added to Cart ...", Toast.LENGTH_SHORT).show();

                Intent it = new Intent(getApplicationContext(), FoodMenu.class);
                startActivity(it);
                finish();
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = qtyTV.getText().toString();
                int tempPrice = Integer.parseInt(priceSTR);
                int temp = Integer.parseInt(current);

                if (temp != 1) {
                    temp -= 1;
                    tempPrice *= temp;

                    qtyTV.setText(String.valueOf(temp));
                    totalPriceTV.setText(String.valueOf(tempPrice));
                }
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = qtyTV.getText().toString();
                int tempPrice = Integer.parseInt(priceSTR);
                int temp = Integer.parseInt(current);
                temp += 1;

                tempPrice *= temp;

                qtyTV.setText(String.valueOf(temp));
                totalPriceTV.setText(String.valueOf(tempPrice));
            }
        });
    }


    public void addToCart() {
        qtySTR = qtyTV.getText().toString();
        totalPriceSTR = totalPriceTV.getText().toString();

        FirebaseDatabase.getInstance().getReference().child("temp_order").child(uN)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mDatabaseRef = mDatabase.getReference().child("temp_order").
                                child(uN).child("Items").child(itemIdSTR);
                        mDatabaseRef.child("Id").setValue(itemIdSTR);
                        mDatabaseRef.child("Name").setValue(nameSTR);
                        mDatabaseRef.child("Price").setValue(priceSTR);
                        mDatabaseRef.child("Category").setValue(MenuItemsAdapter.category);
                        mDatabaseRef.child("Description").setValue(descSTR);
                        mDatabaseRef.child("Size").setValue(MenuItemsAdapter.size);
                        mDatabaseRef.child("ImageUrl").setValue(MenuItemsAdapter.uri);
                        mDatabaseRef.child("Quantity").setValue(qtySTR);
                        mDatabaseRef.child("TotalPrice").setValue(totalPriceSTR);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void readQty() {
        FirebaseDatabase.getInstance().getReference().child("temp_order").child(uN).child(itemIdSTR)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        if (snapshot.exists()) {
                            qtySTR = dataSnapshot.child("Quantity").getValue().toString();
                            totalPriceSTR = dataSnapshot.child("TotalPrice").getValue().toString();

                            qtyTV.setText(qtySTR);
                            totalPriceTV.setText(totalPriceSTR);
                        } else {

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), FoodMenu.class));
        finish();
    }
}