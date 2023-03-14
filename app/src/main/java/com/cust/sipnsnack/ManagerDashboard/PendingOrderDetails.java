package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PendingOrderDetails extends AppCompatActivity {

    LoadingDialog loadingDialog;
    TextView customerNameTV, customerPhoneNoTV, customerAddressTV, paymentTypeTV, totalQtyTV,
            totalPriceTV, dateTV, timeTV;
    ImageView receiptImageIV;
    Button acceptBTN, rejectBTN;
    String username, name, phone, address, payment, qty, price, receipt, mngUsername, mngName, addressType, lon, lat,
    time, date, orderId;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    int loopSize, loopSize2 = 0;
    int dbPrice = 0, totalBill = 0, dbQty = 0, totalQty = 0;
    ArrayList<PendingOrderItems> myItem;
    PendingOrderItemsAdapter pendingOrderItemsAdapter;
    SharedPreferences spr;
    String iD, nAme, pRice, cAtegory, dEscription, sIze, uRl, qTy, tOtal;
    DatabaseReference orderAcceptNodeRef, orderRejectNodeRef;
    ImageView backBtn, location;

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
        setContentView(R.layout.activity_pending_order_details);

        loadingDialog = new LoadingDialog(PendingOrderDetails.this);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        mngUsername = spr.getString("Username", "");

        customerNameTV = findViewById(R.id.customerNameTV);
        customerPhoneNoTV = findViewById(R.id.customerPhoneNoTV);
        customerAddressTV = findViewById(R.id.customerAddressTV);
        paymentTypeTV = findViewById(R.id.paymentTypeTV);
        totalQtyTV = findViewById(R.id.totalQtyTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        dateTV = findViewById(R.id.dateTV);
        timeTV = findViewById(R.id.timeTV);
        receiptImageIV = findViewById(R.id.receiptImageIV);
        acceptBTN = findViewById(R.id.acceptBtn);
        rejectBTN = findViewById(R.id.rejectBtn);
        backBtn = findViewById(R.id.backBTN);
        location = findViewById(R.id.locationIV);

        setTextViews();

        recyclerView = (RecyclerView) findViewById(R.id.pending_items_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        receipt = MyOrdersAdapter.receiptImage;
        Picasso.get()
                .load(receipt)
                .placeholder(R.drawable.logo)
                .into(receiptImageIV);

        myItem = new ArrayList<PendingOrderItems>();

        ReadFromDB();
        readManager(mngUsername);
        CalculateBill();

        acceptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptOrder(orderId);

            }
        });

        rejectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectOrder(orderId);
                startActivity(new Intent(getApplicationContext(), AllOrdersDetails.class));
                finish();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (addressType.equals("Manual")) {
                    intent = new Intent(getApplicationContext(), LocationByAddress.class);
                    intent.putExtra("Address", address);
                } else {
                    intent = new Intent(getApplicationContext(), LocationByDirection.class);
                    intent.putExtra("Latitude", lat);
                    intent.putExtra("Longitude", lon);
                }
                intent.putExtra("PhoneNo", phone);
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllOrdersDetails.class));
                finish();
            }
        });
    }

    public void setTextViews() {
        username = MyOrdersAdapter.username;
        name = MyOrdersAdapter.name;
        phone = MyOrdersAdapter.phoneNo;

        addressType = MyOrdersAdapter.addressType;

        if (addressType.equals("Manual")) {
            address = MyOrdersAdapter.address;
            customerAddressTV.setText(address);

            receiptImageIV.setVisibility(View.GONE);
        } else {
            lon = MyOrdersAdapter.lon;
            lat = MyOrdersAdapter.lat;
            customerAddressTV.setText("(On Maps)");
        }

        payment = MyOrdersAdapter.paymentType;
        time = MyOrdersAdapter.time;
        date = MyOrdersAdapter.date;
        orderId = MyOrdersAdapter.orderId;

        customerNameTV.setText(name);
        customerPhoneNoTV.setText(phone);
        paymentTypeTV.setText(payment);
        dateTV.setText(date);
        timeTV.setText(time);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AllOrdersDetails.class));
        finish();
    }

    public void CalculateBill() {
        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").
                child(orderId).child("Items")

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                dbPrice = Integer.parseInt(snapshot.child("TotalPrice").getValue().toString());
                                dbQty = Integer.parseInt(snapshot.child("Quantity").getValue().toString());

                                totalBill += dbPrice;
                                totalQty += dbQty;
                            }
                            price = String.valueOf(totalBill);
                            qty = String.valueOf(totalQty);

                            totalPriceTV.setText(price);
                            totalQtyTV.setText(qty);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void readManager(String userName) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Managers")
                .child(userName).addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                mngName = dataSnapshot.child("Name").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").
                child(orderId).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            loopSize++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending")
                .child(orderId).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MyPendingOrderData.itemId = new String[loopSize];
                        MyPendingOrderData.itemName = new String[loopSize];
                        MyPendingOrderData.itemPrice = new String[loopSize];
                        MyPendingOrderData.itemCategory = new String[loopSize];
                        MyPendingOrderData.itemDescription = new String[loopSize];
                        MyPendingOrderData.itemSize = new String[loopSize];
                        MyPendingOrderData.itemURL = new String[loopSize];
                        MyPendingOrderData.itemTotalPrice = new String[loopSize];
                        MyPendingOrderData.itemQuantity = new String[loopSize];
                        MyPendingOrderData.orderDate = new String[loopSize];
                        MyPendingOrderData.orderTime = new String[loopSize];

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MyPendingOrderData.itemId[j] = snapshot.child("Id").getValue().toString();
                            MyPendingOrderData.itemName[j] = snapshot.child("Name").getValue().toString();
                            MyPendingOrderData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                            MyPendingOrderData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                            MyPendingOrderData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                            MyPendingOrderData.itemSize[j] = snapshot.child("Size").getValue().toString();
                            MyPendingOrderData.itemURL[j] = snapshot.child("ImageUrl").getValue().toString();
                            MyPendingOrderData.itemQuantity[j] = snapshot.child("Quantity").getValue().toString();
                            MyPendingOrderData.itemTotalPrice[j] = snapshot.child("TotalPrice").getValue().toString();

                            j++;
                        }

                        for (int i = 0; i < MyPendingOrderData.itemId.length; i++) {
                            myItem.add(new PendingOrderItems(
                                    MyPendingOrderData.itemId[i],
                                    MyPendingOrderData.itemName[i],
                                    MyPendingOrderData.itemCategory[i],
                                    MyPendingOrderData.itemPrice[i],
                                    MyPendingOrderData.itemDescription[i],
                                    MyPendingOrderData.itemSize[i],
                                    MyPendingOrderData.itemURL[i],
                                    MyPendingOrderData.itemQuantity[i],
                                    MyPendingOrderData.itemTotalPrice[i]
                            ));
                        }

                        if (loopSize == 0) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(PendingOrderDetails.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();

                        } else {
                            loadingDialog.dismissDialog();

                            pendingOrderItemsAdapter = new PendingOrderItemsAdapter(myItem);
                            recyclerView.setAdapter(pendingOrderItemsAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void acceptOrder(String ord) {
        loadingDialog.startLoadingDialog();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_accept_order_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setData(ord);

                Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                startActivity(intent);
                finish();

                Toast.makeText(getApplicationContext(), "Order Accepted Successfully ...", Toast.LENGTH_SHORT).show();
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

    public void setData(String ordID) {
        String totalAmount = totalPriceTV.getText().toString();
        String totalQTY = totalQtyTV.getText().toString();

        final DatabaseReference acceptOrderNode = FirebaseDatabase.getInstance().getReference().
                child("Orders").child("Accepted").child(ordID);

        acceptOrderNode.child("Username").setValue(username);
        acceptOrderNode.child("PhoneNo").setValue(phone);
        acceptOrderNode.child("AddressType").setValue(addressType);

        if (addressType.equals("Manual")) {
            acceptOrderNode.child("Address").setValue(address);
        } else {
            acceptOrderNode.child("Longitude").setValue(lon);
            acceptOrderNode.child("Latitude").setValue(lat);
        }

        acceptOrderNode.child("Name").setValue(name);
        acceptOrderNode.child("PaymentType").setValue(payment);
        acceptOrderNode.child("ReceiptImage").setValue(receipt);
        acceptOrderNode.child("TotalBill").setValue(totalAmount);
        acceptOrderNode.child("TotalQuantity").setValue(totalQTY);
        acceptOrderNode.child("AcceptedBy").setValue(mngName);
        acceptOrderNode.child("OrderDate").setValue(getTodayDate());
        acceptOrderNode.child("OrderTime").setValue(getTime());
        acceptOrderNode.child("Status").setValue("Accepted");

        copyData(ordID);
    }

    public void copyData(String ordId) {
        loopSize2 = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").
                child(ordId).child("Items")
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


        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").
                child(ordId).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                String id = snapshot.child("Id").getValue().toString();

                                orderAcceptNodeRef = FirebaseDatabase.getInstance().getReference().
                                        child("Orders").child("Accepted").child(ordId).child("Items")
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

                                orderAcceptNodeRef.child("Id").setValue(iD);
                                orderAcceptNodeRef.child("Name").setValue(nAme);
                                orderAcceptNodeRef.child("Price").setValue(pRice);
                                orderAcceptNodeRef.child("Category").setValue(cAtegory);
                                orderAcceptNodeRef.child("Description").setValue(dEscription);
                                orderAcceptNodeRef.child("Size").setValue(sIze);
                                orderAcceptNodeRef.child("ImageUrl").setValue(uRl);
                                orderAcceptNodeRef.child("Quantity").setValue(qTy);
                                orderAcceptNodeRef.child("TotalPrice").setValue(tOtal);
                            }

                            deletePendingOrder(ordId);

                            sendNotification(mngName);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void deletePendingOrder(String odr) {
        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").
                child(odr).removeValue();
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void rejectOrder(String odr) {
        loadingDialog.startLoadingDialog();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_reject_order_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setRejectedData(odr);

                Toast.makeText(getApplicationContext(), "Order Rejected Successfully ...", Toast.LENGTH_LONG).show();
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

    public void setRejectedData(String ordID) {
        String totalAmount = totalPriceTV.getText().toString();
        String totalQTY = totalQtyTV.getText().toString();

        final DatabaseReference acceptOrderNode = FirebaseDatabase.getInstance().getReference().
                child("Orders").child("Rejected").child(ordID);

        acceptOrderNode.child("Username").setValue(username);
        acceptOrderNode.child("PhoneNo").setValue(phone);
        acceptOrderNode.child("Address").setValue(address);
        acceptOrderNode.child("Name").setValue(name);
        acceptOrderNode.child("PaymentType").setValue(payment);
        acceptOrderNode.child("ReceiptImage").setValue(receipt);
        acceptOrderNode.child("TotalBill").setValue(totalAmount);
        acceptOrderNode.child("TotalQuantity").setValue(totalQTY);
        acceptOrderNode.child("AcceptedBy").setValue(mngName);

        copyRejectedData(ordID);
    }

    public void copyRejectedData(String ord) {
        loopSize2 = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").
                child(ord).child("Items")
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


        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").
                child(ord).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                String id = snapshot.child("Id").getValue().toString();

                                orderRejectNodeRef = FirebaseDatabase.getInstance().getReference().
                                        child("Orders").child("Rejected").child(ord).child("Items")
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

                                orderRejectNodeRef.child("Id").setValue(iD);
                                orderRejectNodeRef.child("Name").setValue(nAme);
                                orderRejectNodeRef.child("Price").setValue(pRice);
                                orderRejectNodeRef.child("Category").setValue(cAtegory);
                                orderRejectNodeRef.child("Description").setValue(dEscription);
                                orderRejectNodeRef.child("Size").setValue(sIze);
                                orderRejectNodeRef.child("ImageUrl").setValue(uRl);
                                orderRejectNodeRef.child("Quantity").setValue(qTy);
                                orderRejectNodeRef.child("TotalPrice").setValue(tOtal);
                            }
                            deletePendingOrder(ord);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void sendNotification(String nam) {

        String textTitle = "ORDER ACCEPTED !";
        String textContent = "Dear "+ nam + ", Order has been Accepted Successfully !";

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