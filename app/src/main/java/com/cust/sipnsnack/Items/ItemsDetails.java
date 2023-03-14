package com.cust.sipnsnack.Items;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ItemsDetails extends AppCompatActivity {

    TextView itemID, itemName, itemPrice, itemCategory, itemSize, itemDescription, itemDT;
    Button okBtn;
    ImageView edit, delete, itemImage,backBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        loadingDialog = new LoadingDialog(ItemsDetails.this);
        loadingDialog.startLoadingDialog();

        itemID = findViewById(R.id.idRight);
        itemName = findViewById(R.id.nameRight);
        itemPrice = findViewById(R.id.priceRight);
        itemCategory = findViewById(R.id.categoryRight);
        itemSize = findViewById(R.id.sizeRight);
        itemDescription = findViewById(R.id.descriptionRight);
        itemDT = findViewById(R.id.itemsDetailTV);
        okBtn = findViewById(R.id.okItemsButton);
        edit = findViewById(R.id.itemsDetailEditIV);
        delete = findViewById(R.id.itemsDetailDeleteIV);
        itemImage = findViewById(R.id.itemImg);
        backBtn = findViewById(R.id.backBTN);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewItems.class));
                finish();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewItems.class);
                startActivity(intent);
                finish();
            }
        });

        itemDT.setText(MyItemsAdapter.id);
        itemID.setText(MyItemsAdapter.id);
        itemName.setText(MyItemsAdapter.name);
        itemPrice.setText(MyItemsAdapter.price);
        itemSize.setText(MyItemsAdapter.size);
        itemCategory.setText(MyItemsAdapter.category);
        itemDescription.setText(MyItemsAdapter.description);

        loadPicture(MyItemsAdapter.uri);


        // EDIT Image View
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateItems.class);
                startActivity(intent);
                finish();
            }
        });


        // DELETE Image View
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemDialog();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ViewItems.class);
        startActivity(intent);
        finish();
    }


    public void deleteItemDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_delete_product_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.getReference().child("Items").child(MyItemsAdapter.id).removeValue();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Context context = v.getContext();
                Intent intent = new Intent(context, ViewItems.class);
                context.startActivity(intent);
                Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
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

    public void loadPicture(String URL) {
        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.logo)
                .into(itemImage);
        loadingDialog.dismissDialog();
    }
}