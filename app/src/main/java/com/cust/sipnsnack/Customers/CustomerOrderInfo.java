package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.material.textfield.TextInputEditText;
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

public class CustomerOrderInfo extends AppCompatActivity {

    SharedPreferences spr;
    TextInputEditText nameET, phoneNoET;
    Button confirmBtn;
    LoadingDialog loadingDialog;
    String name, phoneNo, username;
    int loopSize = 0;
    ArrayList<CartItems> myItem;
    DatabaseReference orderPlaceNodeRef;
    String iD, nAme, pRice, cAtegory, dEscription, sIze, uRl, qTy, tOtal;
    ImageView BackBtn;
    String orderId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_info);

        loadingDialog = new LoadingDialog(CustomerOrderInfo.this);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");

        nameET = findViewById(R.id.nameET);
        phoneNoET = findViewById(R.id.phoneNoET);
        confirmBtn = findViewById(R.id.confirmBtn);
        BackBtn  = findViewById(R.id.backBTN);

        myItem = new ArrayList<CartItems>();

        readFromDB(username);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationDialog(username);
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), PaymentProcess.class);
                startActivity(it);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), PaymentProcess.class);
        startActivity(it);
        finish();
    }

    public void readFromDB(String userName) {
        loadingDialog.startLoadingDialog();

        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                .child(userName).addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                name = dataSnapshot.child("Name").getValue().toString();
                phoneNo = dataSnapshot.child("PhoneNo").getValue().toString();

                nameET.setText(name);
                phoneNoET.setText(phoneNo);

                loadingDialog.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void confirmationDialog(String uN) {
        loadingDialog.startLoadingDialog();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_place_order_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placeOrder(uN);

                Intent intent = new Intent(getApplicationContext(), CustomerView.class);
                startActivity(intent);
                finish();

                Toast.makeText(getApplicationContext(), "Order Placed Successfully ...", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();
                alertDialog.dismiss();
            }
        });

        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.dismissDialog();
                alertDialog.dismiss();
            }
        });
    }

    public void placeOrder(String un) {

        name = nameET.getText().toString();
        phoneNo = phoneNoET.getText().toString();

        DatabaseReference tempOrderNode = FirebaseDatabase.getInstance().getReference().child("temp_order").child(un);
        final DatabaseReference orderPlaceNode = FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").push();

        orderId = orderPlaceNode.getKey().toString();

        tempOrderNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String paymentType = dataSnapshot.child("PaymentType").getValue().toString();
                String receiptImage = dataSnapshot.child("ReceiptImage").getValue().toString();
                String quantity = getIntent().getStringExtra("Qty");
                String bill = getIntent().getStringExtra("Bill");
                String addressType = getIntent().getStringExtra("Type");

                System.out.println("AddressType = " + addressType);

                if (addressType.equals("Manual")) {
                    String customerAddress = getIntent().getStringExtra("Address");
                    orderPlaceNode.child("Address").setValue(customerAddress);
                    orderPlaceNode.child("AddressType").setValue("Manual");
                } else {

                    String lon, lat;


                    lon = getIntent().getStringExtra("Longitude");
                    lat = getIntent().getStringExtra("Latitude");

                    if (lon == null || lat == null) {
                        orderPlaceNode.child("AddressType").setValue("CurrentLocation");
                        orderPlaceNode.child("Longitude").setValue("33.6438629");
                        orderPlaceNode.child("Latitude").setValue("73.163703");
                    } else {
                        orderPlaceNode.child("AddressType").setValue("CurrentLocation");
                        orderPlaceNode.child("Longitude").setValue(lon);
                        orderPlaceNode.child("Latitude").setValue(lat);
                    }
                }

                orderPlaceNode.child("PaymentType").setValue(paymentType);
                orderPlaceNode.child("ReceiptImage").setValue(receiptImage);
                orderPlaceNode.child("Username").setValue(un);
                orderPlaceNode.child("PhoneNo").setValue(phoneNo);
                orderPlaceNode.child("Name").setValue(name);
                orderPlaceNode.child("Status").setValue("Pending");
                orderPlaceNode.child("OrderDate").setValue(getTodayDate());
                orderPlaceNode.child("OrderTime").setValue(getTime());
                orderPlaceNode.child("TotalBill").setValue(bill);
                orderPlaceNode.child("TotalQty").setValue(quantity);

                copyData(un, orderId);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void copyData(String usn, String ordID) {
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("temp_order").child(usn).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.exists()) {
                                loopSize++;
                            } else {
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("temp_order").child(usn).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                String id = snapshot.child("Id").getValue().toString();

                                orderPlaceNodeRef = FirebaseDatabase.getInstance().getReference().
                                        child("Orders").child("Pending").child(ordID).child("Items")
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

                                orderPlaceNodeRef.child("Id").setValue(iD);
                                orderPlaceNodeRef.child("Name").setValue(nAme);
                                orderPlaceNodeRef.child("Price").setValue(pRice);
                                orderPlaceNodeRef.child("Category").setValue(cAtegory);
                                orderPlaceNodeRef.child("Description").setValue(dEscription);
                                orderPlaceNodeRef.child("Size").setValue(sIze);
                                orderPlaceNodeRef.child("ImageUrl").setValue(uRl);
                                orderPlaceNodeRef.child("Quantity").setValue(qTy);
                                orderPlaceNodeRef.child("TotalPrice").setValue(tOtal);
                            }

                            deleteTemp(usn);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteTemp(String userName) {
        FirebaseDatabase.getInstance().getReference().child("temp_order").
                child(userName).removeValue();

        sendNotification(name);
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void sendNotification(String nam) {

        String textTitle = "ORDER PLACED !";
        String textContent = "Dear "+ nam + " Please wait for the confirmation call !";

        // Creating a notification channel
        NotificationChannel channel = new NotificationChannel("channel1", "hello", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        // Creating the notification object
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),"channel1");

        // Notification.setAutoCancel(true);
        notification.setContentTitle(textTitle);
        notification.setContentText(textContent);
        notification.setSmallIcon(R.drawable.cafe_main_logo);

        // Make the notification manager to issue a notification on the notification's channel
        manager.notify(121,notification.build());

    }
}