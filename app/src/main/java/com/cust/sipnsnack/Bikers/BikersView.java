package com.cust.sipnsnack.Bikers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import com.cust.sipnsnack.ManagerDashboard.AcceptedOrderItems;
import com.cust.sipnsnack.ManagerDashboard.AcceptedOrderItemsAdapter;
import com.cust.sipnsnack.ManagerDashboard.MyAcceptedOrderData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cust.sipnsnack.LoginActivity;

import com.example.sipnsnack.R;
import com.google.android.material.navigation.NavigationView;
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

public class BikersView extends AppCompatActivity {

    NavigationView navView;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences spr;
    ImageView infoIcon, profileIV, locationIV;
    RelativeLayout mainRL;
    TextView noOrderTV;
    ImageView phoneCallIV, noOrderIV;
    int loopSize2;
    LoadingDialog loadingDialog;
    TextView customerNameTV, customerPhoneNoTV, customerAddressTV, paymentTypeTV, totalQtyTV,
            totalPriceTV, dateTV, timeTV;

    String addressType, lat, lon;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();

    private FirebaseDatabase mDatabase2 = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef2 = mDatabase.getReference();

    Boolean flag = false;
    String username, name, phone, address, payment, qty, price, bikerUsername,
            dbBikerUSN, acceptedBy, bikerName, bikerPhone, receipt;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    int loopSize;
    String iD, nAme, pRice, cAtegory, dEscription, sIze, uRl, qTy, tOtal, billTotal, orderId;
    ArrayList<AcceptedOrderItems> myItem;
    AcceptedOrderItemsAdapter acceptedOrderItemsAdapter;
    Button confirmDelivery;
    DatabaseReference orderDeliverNodeRef;
    int specials, pizza, burgers, fries, snacks, chilled_drinks, sea_foods, coffees, net_sale;
    int specialsAmt, pizzaAmt, burgersAmt, friesAmt, snacksAmt, chilled_drinksAmt, sea_foodsAmt, coffeesAmt;
    String sp, pi, bu, fr, sn, ch, se, co, ns;
    String spAmt, piAmt, buAmt, frAmt, snAmt, chAmt, seAmt, coAmt, nsAmt;
    String itemCategory, itemQuantity, total, itemTotal;

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

    public static String getFormatDate() {
        Date date;
        DateFormat setDate;
        date = Calendar.getInstance().getTime();
        setDate = new SimpleDateFormat("dd_MM_yyyy");
        String current_date = setDate.format(date);
        return current_date;
    }

    public static String getMonth() {
        String Month;
        Date date = new Date();
        int month  = date.getMonth();
        month += 1;

        Month = "January";
        if (month == 1) {
            Month = "January";
        } else if (month == 2) {
            Month = "February";
        } else if (month == 3) {
            Month = "March";
        } else if (month == 4) {
            Month = "April";
        } else if (month == 5) {
            Month = "May";
        } else if (month == 6) {
            Month = "June";
        } else if (month == 7) {
            Month = "July";
        } else if (month == 8) {
            Month = "August";
        } else if (month == 9) {
            Month = "September";
        } else if (month == 10) {
            Month = "October";
        } else if (month == 11) {
            Month = "November";
        } else if (month == 12) {
            Month = "December";
        }

        return Month;
    }

    public static String getYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return String.valueOf(year);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikers_view);

        loadingDialog = new LoadingDialog(BikersView.this);

        setUpToolbar();

        navView = findViewById(R.id.navView);
        infoIcon = findViewById(R.id.infoIconIV);
        mainRL = findViewById(R.id.allInfoRL);
        noOrderTV = findViewById(R.id.noOrdersTV);

        customerNameTV = findViewById(R.id.customerNameTV);
        customerPhoneNoTV = findViewById(R.id.customerPhoneNoTV);
        customerAddressTV = findViewById(R.id.customerAddressTV);
        paymentTypeTV = findViewById(R.id.paymentTypeTV);
        totalQtyTV = findViewById(R.id.totalQtyTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        dateTV = findViewById(R.id.dateTV);
        timeTV = findViewById(R.id.timeTV);
        confirmDelivery = findViewById(R.id.confirmDeliveryBTN);
        profileIV = findViewById(R.id.profileIconIV);
        phoneCallIV = findViewById(R.id.phoneCallIV);
        noOrderIV = findViewById(R.id.noOrdersIV);
        locationIV = findViewById(R.id.locationIV);

        timeTV.setText(getTime());
        dateTV.setText(getTodayDate());

        recyclerView = (RecyclerView) findViewById(R.id.assigned_items_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myItem = new ArrayList<AcceptedOrderItems>();

        ReadFromDB();

        confirmDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliverOrder(username);
            }
        });

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), BikerProfile.class);
                startActivity(it);
                finish();
            }
        });

        locationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it;
                if (addressType.equals("Manual")) {
                    it = new Intent(getApplicationContext(), LocationOfAddress.class);
                    it.putExtra("Address", address);
                } else {
                    it = new Intent(getApplicationContext(), ShareLiveLocation.class);
                    it.putExtra("Longitude", lon);
                    it.putExtra("Latitude", lat);
                }
                it.putExtra("PhoneNo", phone);
                it.putExtra("OrderId", orderId);
                startActivity(it);
                finish();
            }
        });

        specials = pizza = burgers = fries = snacks = chilled_drinks = sea_foods = coffees = net_sale = 0;
        specialsAmt = pizzaAmt = burgersAmt = friesAmt = snacksAmt = chilled_drinksAmt = sea_foodsAmt = coffeesAmt = 0;

        drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(navView)) {
            drawer.closeDrawer(navView);
        }

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        bikerUsername = spr.getString("Username", "");


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.nav_profile) {
                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), BikerProfile.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.nav_logout) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        logoutDialog();
                    }
                }


                if(item.getItemId() == R.id.nav_changePWD) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), BikerPasswordChange.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.nav_report) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), BikerReport.class);
                        startActivity(it);
                        finish();
                    }
                }

                if(item.getItemId() == R.id.nav_stars) {
                    if(drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        Intent it = new Intent(getApplicationContext(), BikersStars.class);
                        startActivity(it);
                        finish();
                    }
                }

                return false;
            }
        });

        phoneCallIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

    }

    public void deliverOrder(String uN) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_deliver_order_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.startLoadingDialog();

                saveToReports();

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


    public void setUpToolbar() {
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void logoutDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_logout_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = spr.edit();
                editor.putString("User", "");
                editor.putString("Status", "");
                editor.putString("Username", "");
                editor.apply();


                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Logout Successful ...", Toast.LENGTH_SHORT).show();
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


    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                dbBikerUSN = snapshot.child("BikerUsername").getValue().toString();
                                if (dbBikerUSN.equals(bikerUsername)) {
                                    orderId = snapshot.getKey().toString();
                                    flag = true;
                                    break;
                                }
                            }

                            if (flag) {

                                FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                                        .child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {

                                    DataSnapshot dataSnapshot;

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.exists()) {

                                            mainRL.setVisibility(View.VISIBLE);
                                            noOrderTV.setVisibility(View.GONE);
                                            noOrderIV.setVisibility(View.GONE);

                                            this.dataSnapshot = snapshot;

                                            name = dataSnapshot.child("CustomerName").getValue().toString();
                                            phone = dataSnapshot.child("CustomerPhoneNo").getValue().toString();
                                            payment = dataSnapshot.child("CustomerPaymentType").getValue().toString();
                                            qty = dataSnapshot.child("CustomerTotalQuantity").getValue().toString();
                                            price = dataSnapshot.child("CustomerTotalBill").getValue().toString();
                                            acceptedBy = dataSnapshot.child("AcceptedBy").getValue().toString();
                                            bikerName = dataSnapshot.child("BikerName").getValue().toString();
                                            bikerUsername = dataSnapshot.child("BikerUsername").getValue().toString();
                                            bikerPhone = dataSnapshot.child("BikerPhoneNo").getValue().toString();
                                            receipt = dataSnapshot.child("ReceiptImage").getValue().toString();
                                            username = dataSnapshot.child("CustomerUsername").getValue().toString();
                                            addressType = dataSnapshot.child("AddressType").getValue().toString();

                                            if (addressType.equals("Manual")) {
                                                address = dataSnapshot.child("CustomerAddress").getValue().toString();
                                                customerAddressTV.setText(address);
                                            } else {
                                                lat = dataSnapshot.child("Latitude").getValue().toString();
                                                lon = dataSnapshot.child("Longitude").getValue().toString();

                                                customerAddressTV.setText("(On Maps)");
                                            }

                                            customerNameTV.setText(name);
                                            customerPhoneNoTV.setText(phone);
                                            paymentTypeTV.setText(payment);
                                            totalQtyTV.setText(qty);
                                            totalPriceTV.setText(price);

                                            ReadItems(orderId);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                loadingDialog.dismissDialog();
                                noOrderTV.setVisibility(View.VISIBLE);
                                noOrderIV.setVisibility(View.VISIBLE);
                                mainRL.setVisibility(View.GONE);
                            }
                    } else {
                            loadingDialog.dismissDialog();
                            noOrderTV.setVisibility(View.VISIBLE);
                            mainRL.setVisibility(View.GONE);
                            noOrderIV.setVisibility(View.VISIBLE);
                        }

                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void ReadItems(String ordId) {
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way").
                child(ordId).child("Items")
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .child(ordId).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MyAcceptedOrderData.itemId = new String[loopSize];
                        MyAcceptedOrderData.itemName = new String[loopSize];
                        MyAcceptedOrderData.itemPrice = new String[loopSize];
                        MyAcceptedOrderData.itemCategory = new String[loopSize];
                        MyAcceptedOrderData.itemDescription = new String[loopSize];
                        MyAcceptedOrderData.itemSize = new String[loopSize];
                        MyAcceptedOrderData.itemURL = new String[loopSize];
                        MyAcceptedOrderData.itemTotalPrice = new String[loopSize];
                        MyAcceptedOrderData.itemQuantity = new String[loopSize];

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MyAcceptedOrderData.itemId[j] = snapshot.child("Id").getValue().toString();
                            MyAcceptedOrderData.itemName[j] = snapshot.child("Name").getValue().toString();
                            MyAcceptedOrderData.itemPrice[j] = snapshot.child("Price").getValue().toString();
                            MyAcceptedOrderData.itemCategory[j] = snapshot.child("Category").getValue().toString();
                            MyAcceptedOrderData.itemDescription[j] = snapshot.child("Description").getValue().toString();
                            MyAcceptedOrderData.itemSize[j] = snapshot.child("Size").getValue().toString();
                            MyAcceptedOrderData.itemURL[j] = snapshot.child("ImageUrl").getValue().toString();
                            MyAcceptedOrderData.itemQuantity[j] = snapshot.child("Quantity").getValue().toString();
                            MyAcceptedOrderData.itemTotalPrice[j] = snapshot.child("TotalPrice").getValue().toString();

                            j++;
                        }

                        for (int i = 0; i < MyAcceptedOrderData.itemId.length; i++) {
                            myItem.add(new AcceptedOrderItems(
                                    MyAcceptedOrderData.itemId[i],
                                    MyAcceptedOrderData.itemName[i],
                                    MyAcceptedOrderData.itemCategory[i],
                                    MyAcceptedOrderData.itemPrice[i],
                                    MyAcceptedOrderData.itemDescription[i],
                                    MyAcceptedOrderData.itemSize[i],
                                    MyAcceptedOrderData.itemURL[i],
                                    MyAcceptedOrderData.itemQuantity[i],
                                    MyAcceptedOrderData.itemTotalPrice[i]
                            ));
                        }

                        if (loopSize == 0) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(BikersView.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();

                        } else {
                            loadingDialog.dismissDialog();
                            acceptedOrderItemsAdapter = new AcceptedOrderItemsAdapter(myItem);
                            recyclerView.setAdapter(acceptedOrderItemsAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void setData(String un) {
        final DatabaseReference deliverOrderNode = FirebaseDatabase.getInstance().getReference().
                child("Orders").child("Delivered").child(orderId);

        deliverOrderNode.child("CustomerUsername").setValue(username);
        deliverOrderNode.child("CustomerPhoneNo").setValue(phone);
        deliverOrderNode.child("AddressType").setValue(addressType);

        if (addressType.equals("Manual")) {
            deliverOrderNode.child("CustomerAddress").setValue(address);
        } else {
            deliverOrderNode.child("Latitude").setValue(lat);
            deliverOrderNode.child("Longitude").setValue(lon);
        }

        deliverOrderNode.child("CustomerName").setValue(name);
        deliverOrderNode.child("CustomerPaymentType").setValue(payment);
        deliverOrderNode.child("CustomerTotalBill").setValue(price);
        deliverOrderNode.child("CustomerTotalQuantity").setValue(qty);
        deliverOrderNode.child("AcceptedBy").setValue(acceptedBy);
        deliverOrderNode.child("BikerUsername").setValue(bikerUsername);
        deliverOrderNode.child("BikerName").setValue(bikerName);
        deliverOrderNode.child("BikerPhoneNo").setValue(bikerPhone);
        deliverOrderNode.child("OrderDate").setValue(getTodayDate());
        deliverOrderNode.child("OrderTime").setValue(getTime());
        deliverOrderNode.child("ReceiptImage").setValue(receipt);
        deliverOrderNode.child("Status").setValue("Delivered");

        copyData(orderId);
    }

    public void copyData(String keyy) {
        loopSize2 = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way").
                child(keyy).child("Items")
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


        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way").
                child(keyy).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                String id = snapshot.child("Id").getValue().toString();

                                orderDeliverNodeRef = FirebaseDatabase.getInstance().getReference().
                                        child("Orders").child("Delivered").child(keyy).
                                        child("Items").child(id);

                                iD = snapshot.child("Id").getValue().toString();
                                nAme = snapshot.child("Name").getValue().toString();
                                pRice = snapshot.child("Price").getValue().toString();
                                cAtegory = snapshot.child("Category").getValue().toString();
                                dEscription = snapshot.child("Description").getValue().toString();
                                sIze = snapshot.child("Size").getValue().toString();
                                uRl = snapshot.child("ImageUrl").getValue().toString();
                                qTy = snapshot.child("Quantity").getValue().toString();
                                tOtal = snapshot.child("TotalPrice").getValue().toString();

                                orderDeliverNodeRef.child("Id").setValue(iD);
                                orderDeliverNodeRef.child("Name").setValue(nAme);
                                orderDeliverNodeRef.child("Price").setValue(pRice);
                                orderDeliverNodeRef.child("Category").setValue(cAtegory);
                                orderDeliverNodeRef.child("Description").setValue(dEscription);
                                orderDeliverNodeRef.child("Size").setValue(sIze);
                                orderDeliverNodeRef.child("ImageUrl").setValue(uRl);
                                orderDeliverNodeRef.child("Quantity").setValue(qTy);
                                orderDeliverNodeRef.child("TotalPrice").setValue(tOtal);
                            }

                            deleteOnTheWayOrder(keyy);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteOnTheWayOrder(String ordId) {
        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way").
                child(ordId).removeValue();


        FirebaseDatabase.getInstance().getReference().child("LiveOrders").
                child(ordId).removeValue();

        sendNotification(bikerName, payment, price);
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            setStatus(bikerUsername);
        }
    }

    public void setStatus(String userName) {
        final DatabaseReference bikerStatusNode = FirebaseDatabase.getInstance().getReference().
                child("Users").child("Bikers").child(userName);

        bikerStatusNode.child("AvailabilityStatus").setValue("Available");

        startActivity(getIntent());
        finish();
    }

    public void saveToReports() {
        String todayDate = getFormatDate();
        getCategoriesQty(todayDate);

    }

    public void getCategoriesQty(String dt) {
        FirebaseDatabase.getInstance().getReference().child("Reports").child("date_wise").child(dt)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        if (dataSnapshot.exists()) {
                            sp = dataSnapshot.child("Specials").getValue().toString();
                            pi = dataSnapshot.child("Pizza").getValue().toString();
                            bu = dataSnapshot.child("Burgers").getValue().toString();
                            fr = dataSnapshot.child("Fries").getValue().toString();
                            sn = dataSnapshot.child("Snacks").getValue().toString();
                            ch = dataSnapshot.child("Chilled Drinks").getValue().toString();
                            se = dataSnapshot.child("Sea Foods").getValue().toString();
                            co = dataSnapshot.child("Coffees").getValue().toString();
                            total = dataSnapshot.child("Net Sale").getValue().toString();

                            spAmt = dataSnapshot.child("SpecialsAmount").getValue().toString();
                            piAmt = dataSnapshot.child("PizzaAmount").getValue().toString();
                            buAmt = dataSnapshot.child("BurgersAmount").getValue().toString();
                            frAmt = dataSnapshot.child("FriesAmount").getValue().toString();
                            snAmt = dataSnapshot.child("SnacksAmount").getValue().toString();
                            chAmt = dataSnapshot.child("Chilled DrinksAmount").getValue().toString();
                            seAmt = dataSnapshot.child("Sea FoodsAmount").getValue().toString();
                            coAmt = dataSnapshot.child("CoffeesAmount").getValue().toString();

                        } else {
                            mDatabaseRef = mDatabase.getReference().child("Reports").
                                    child("date_wise").child(dt);

                            sp = pi = bu = fr = sn = ch = se = co = total = "0";
                            spAmt = piAmt = buAmt = frAmt = snAmt = chAmt = seAmt = coAmt = "0";

                            mDatabaseRef.child("Specials").setValue("0");
                            mDatabaseRef.child("Pizza").setValue("0");
                            mDatabaseRef.child("Burgers").setValue("0");
                            mDatabaseRef.child("Fries").setValue("0");
                            mDatabaseRef.child("Snacks").setValue("0");
                            mDatabaseRef.child("Chilled Drinks").setValue("0");
                            mDatabaseRef.child("Sea Foods").setValue("0");
                            mDatabaseRef.child("Coffees").setValue("0");
                            mDatabaseRef.child("Net Sale").setValue("0");
                            mDatabaseRef.child("Date").setValue(getFormatDate());
                            mDatabaseRef.child("SpecialsAmount").setValue("0");
                            mDatabaseRef.child("PizzaAmount").setValue("0");
                            mDatabaseRef.child("BurgersAmount").setValue("0");
                            mDatabaseRef.child("FriesAmount").setValue("0");
                            mDatabaseRef.child("SnacksAmount").setValue("0");
                            mDatabaseRef.child("Chilled DrinksAmount").setValue("0");
                            mDatabaseRef.child("Sea FoodsAmount").setValue("0");
                            mDatabaseRef.child("CoffeesAmount").setValue("0");

                        }

                        saveToDB(sp, pi, bu, fr, sn, ch, se, co, total, spAmt, piAmt, buAmt, frAmt,
                                snAmt, chAmt, seAmt, coAmt);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void saveToDB(String ss, String pa, String bs, String fs, String sn, String cs,
                         String se, String ds, String to, String ss2, String pa2, String bs2,
                         String fs2, String sn2, String cs2, String se2, String ds2) {

        specials = Integer.parseInt(ss);
        pizza = Integer.parseInt(pa);
        burgers = Integer.parseInt(bs);
        fries = Integer.parseInt(fs);
        snacks = Integer.parseInt(sn);
        chilled_drinks = Integer.parseInt(cs);
        sea_foods = Integer.parseInt(se);
        coffees = Integer.parseInt(ds);
        net_sale = Integer.parseInt(to);
        specialsAmt = Integer.parseInt(ss2);
        pizzaAmt = Integer.parseInt(pa2);
        burgersAmt = Integer.parseInt(bs2);
        friesAmt = Integer.parseInt(fs2);
        snacksAmt = Integer.parseInt(sn2);
        chilled_drinksAmt = Integer.parseInt(cs2);
        sea_foodsAmt = Integer.parseInt(se2);
        coffeesAmt = Integer.parseInt(ds2);


        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                dbBikerUSN = snapshot.child("BikerUsername").getValue().toString();

                                if (dbBikerUSN.equals(bikerUsername)) {
                                    orderId = snapshot.getKey().toString();
                                    billTotal = snapshot.child("CustomerTotalBill").getValue().toString();
                                    net_sale = net_sale + Integer.parseInt(billTotal);
                                    flag = true;
                                    break;
                                }
                            }
                            getItems(orderId);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    // Notification
    @RequiresApi(api = Build.VERSION_CODES.O)
    void sendNotification(String nam, String pnt, String prc) {

        loadingDialog.dismissDialog();

        Toast.makeText(getApplicationContext(), "Order Delivered Successfully ...", Toast.LENGTH_SHORT).show();

        String textTitle = "ORDER DELIVERED !";
        String textContent = "Dear "+ nam + ", Order has been Delivered Successfully !";

        if (pnt.equals("Online")) {
            textContent.concat("\nBill has been Paid Online.");
        } else {
            textContent.concat("\nCollect "+ prc + " Rs. from Customer.");
        }

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

    public void getItems(String id) {
        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .child(id).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                itemCategory = snapshot.child("Category").getValue().toString();
                                itemQuantity = snapshot.child("Quantity").getValue().toString();
                                itemTotal = snapshot.child("TotalPrice").getValue().toString();

                                if (itemCategory.equals("Specials")) {
                                    specials = specials + Integer.parseInt(itemQuantity);
                                    specialsAmt = specialsAmt + Integer.parseInt(itemTotal);
                                } else if (itemCategory.equals("Pizza")) {
                                    pizza = pizza + Integer.parseInt(itemQuantity);
                                    pizzaAmt = pizzaAmt + Integer.parseInt(itemTotal);
                                } else if (itemCategory.equals("Burgers")) {
                                    burgers = burgers + Integer.parseInt(itemQuantity);
                                    burgersAmt = burgersAmt + Integer.parseInt(itemTotal);
                                } else if (itemCategory.equals("Fries")) {
                                    fries = fries + Integer.parseInt(itemQuantity);
                                    friesAmt = friesAmt + Integer.parseInt(itemTotal);
                                } else if (itemCategory.equals("Snacks")) {
                                    snacks = snacks + Integer.parseInt(itemQuantity);
                                    snacksAmt = snacksAmt + Integer.parseInt(itemTotal);
                                } else if (itemCategory.equals("Chilled Drinks")) {
                                    chilled_drinks = chilled_drinks + Integer.parseInt(itemQuantity);
                                    chilled_drinksAmt = chilled_drinksAmt + Integer.parseInt(itemTotal);
                                } else if (itemCategory.equals("Sea Foods")) {
                                    sea_foods = sea_foods + Integer.parseInt(itemQuantity);
                                    sea_foodsAmt = sea_foodsAmt + Integer.parseInt(itemTotal);
                                } else if (itemCategory.equals("Coffees")) {
                                    coffees = coffees + Integer.parseInt(itemQuantity);
                                    coffeesAmt = coffeesAmt + Integer.parseInt(itemTotal);
                                }
                            }
                        saveReport(specials, pizza, burgers, fries, snacks, chilled_drinks,
                                sea_foods, coffees, net_sale, specialsAmt, pizzaAmt, burgersAmt,
                                friesAmt, snacksAmt, chilled_drinksAmt, sea_foodsAmt, coffeesAmt);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public void saveReport(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j,
                           int k, int l, int m, int n, int o, int p, int q) {
        sp = String.valueOf(a);
        pi = String.valueOf(b);
        bu = String.valueOf(c);
        fr = String.valueOf(d);
        sn = String.valueOf(e);
        ch = String.valueOf(f);
        se = String.valueOf(g);
        co = String.valueOf(h);
        total = String.valueOf(i);
        spAmt = String.valueOf(j);
        piAmt = String.valueOf(k);
        buAmt = String.valueOf(l);
        frAmt = String.valueOf(m);
        snAmt = String.valueOf(n);
        chAmt = String.valueOf(o);
        seAmt = String.valueOf(p);
        coAmt = String.valueOf(q);

        mDatabaseRef = mDatabase.getReference().child("Reports").
                child("date_wise").child(getFormatDate());

        mDatabaseRef.child("Specials").setValue(sp);
        mDatabaseRef.child("Pizza").setValue(pi);
        mDatabaseRef.child("Burgers").setValue(bu);
        mDatabaseRef.child("Fries").setValue(fr);
        mDatabaseRef.child("Snacks").setValue(sn);
        mDatabaseRef.child("Chilled Drinks").setValue(ch);
        mDatabaseRef.child("Sea Foods").setValue(se);
        mDatabaseRef.child("Coffees").setValue(co);
        mDatabaseRef.child("Net Sale").setValue(total);
        mDatabaseRef.child("Date").setValue(getFormatDate());
        mDatabaseRef.child("SpecialsAmount").setValue(spAmt);
        mDatabaseRef.child("PizzaAmount").setValue(piAmt);
        mDatabaseRef.child("BurgersAmount").setValue(buAmt);
        mDatabaseRef.child("FriesAmount").setValue(frAmt);
        mDatabaseRef.child("SnacksAmount").setValue(snAmt);
        mDatabaseRef.child("Chilled DrinksAmount").setValue(chAmt);
        mDatabaseRef.child("Sea FoodsAmount").setValue(seAmt);
        mDatabaseRef.child("CoffeesAmount").setValue(coAmt);

        setMonthlySales();

    }

    public void setMonthlySales() {
        String todayDate = getMonth();
        getMonthRecord(todayDate);
    }

    public void getMonthRecord(String month) {
        FirebaseDatabase.getInstance().getReference().child("Reports").child("month_wise").
                child(month).addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        if (dataSnapshot.exists()) {
                            sp = dataSnapshot.child("Specials").getValue().toString();
                            pi = dataSnapshot.child("Pizza").getValue().toString();
                            bu = dataSnapshot.child("Burgers").getValue().toString();
                            fr = dataSnapshot.child("Fries").getValue().toString();
                            sn = dataSnapshot.child("Snacks").getValue().toString();
                            ch = dataSnapshot.child("Chilled Drinks").getValue().toString();
                            se = dataSnapshot.child("Sea Foods").getValue().toString();
                            co = dataSnapshot.child("Coffees").getValue().toString();
                            ns = dataSnapshot.child("Net Sale").getValue().toString();

                            spAmt = dataSnapshot.child("SpecialsAmount").getValue().toString();
                            piAmt = dataSnapshot.child("PizzaAmount").getValue().toString();
                            buAmt = dataSnapshot.child("BurgersAmount").getValue().toString();
                            frAmt = dataSnapshot.child("FriesAmount").getValue().toString();
                            snAmt = dataSnapshot.child("SnacksAmount").getValue().toString();
                            chAmt = dataSnapshot.child("Chilled DrinksAmount").getValue().toString();
                            seAmt = dataSnapshot.child("Sea FoodsAmount").getValue().toString();
                            coAmt = dataSnapshot.child("CoffeesAmount").getValue().toString();

                        } else {
                            mDatabaseRef = mDatabase.getReference().child("Reports").
                                    child("month_wise").child(month);

                            sp = pi = bu = fr = sn = ch = se = co = ns = "0";
                            spAmt = piAmt = buAmt = frAmt = snAmt = chAmt = seAmt = coAmt = "0";

                            mDatabaseRef.child("Specials").setValue("0");
                            mDatabaseRef.child("Pizza").setValue("0");
                            mDatabaseRef.child("Burgers").setValue("0");
                            mDatabaseRef.child("Fries").setValue("0");
                            mDatabaseRef.child("Snacks").setValue("0");
                            mDatabaseRef.child("Chilled Drinks").setValue("0");
                            mDatabaseRef.child("Sea Foods").setValue("0");
                            mDatabaseRef.child("Coffees").setValue("0");
                            mDatabaseRef.child("Net Sale").setValue("0");
                            mDatabaseRef.child("Month").setValue(month);
                            mDatabaseRef.child("SpecialsAmount").setValue("0");
                            mDatabaseRef.child("PizzaAmount").setValue("0");
                            mDatabaseRef.child("BurgersAmount").setValue("0");
                            mDatabaseRef.child("FriesAmount").setValue("0");
                            mDatabaseRef.child("SnacksAmount").setValue("0");
                            mDatabaseRef.child("Chilled DrinksAmount").setValue("0");
                            mDatabaseRef.child("Sea FoodsAmount").setValue("0");
                            mDatabaseRef.child("CoffeesAmount").setValue("0");

                        }
                        saveToDBMonthly(sp, pi, bu, fr, sn, ch, se, co, ns, spAmt, piAmt, buAmt,
                                frAmt, snAmt, chAmt, seAmt, coAmt);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void saveToDBMonthly(String ss, String pa, String bs, String fs, String sn, String cs,
                         String se, String ds, String ne, String ss2, String pa2, String bs2,
                                String fs2, String sn2, String cs2, String se2, String ds2) {

        specials = Integer.parseInt(ss);
        pizza = Integer.parseInt(pa);
        burgers = Integer.parseInt(bs);
        fries = Integer.parseInt(fs);
        snacks = Integer.parseInt(sn);
        chilled_drinks = Integer.parseInt(cs);
        sea_foods = Integer.parseInt(se);
        coffees = Integer.parseInt(ds);
        net_sale = Integer.parseInt(ne);

        specialsAmt = Integer.parseInt(ss2);
        pizzaAmt = Integer.parseInt(pa2);
        burgersAmt = Integer.parseInt(bs2);
        friesAmt = Integer.parseInt(fs2);
        snacksAmt = Integer.parseInt(sn2);
        chilled_drinksAmt = Integer.parseInt(cs2);
        sea_foodsAmt = Integer.parseInt(se2);
        coffeesAmt = Integer.parseInt(ds2);


        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                dbBikerUSN = snapshot.child("BikerUsername").getValue().toString();

                                if (dbBikerUSN.equals(bikerUsername)) {
                                    orderId = snapshot.getKey().toString();
                                    billTotal = snapshot.child("CustomerTotalBill").getValue().toString();
                                    net_sale = net_sale + Integer.parseInt(billTotal);
                                    flag = true;
                                    break;
                                }
                            }
                            getItemsMonthly(orderId);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getItemsMonthly(String id) {
        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .child(id).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            itemCategory = snapshot.child("Category").getValue().toString();
                            itemQuantity = snapshot.child("Quantity").getValue().toString();
                            itemTotal = snapshot.child("TotalPrice").getValue().toString();

                            if (itemCategory.equals("Specials")) {
                                specials = specials + Integer.parseInt(itemQuantity);
                                specialsAmt = specialsAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Pizza")) {
                                pizza = pizza + Integer.parseInt(itemQuantity);
                                pizzaAmt = pizzaAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Burgers")) {
                                burgers = burgers + Integer.parseInt(itemQuantity);
                                burgersAmt = burgersAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Fries")) {
                                fries = fries + Integer.parseInt(itemQuantity);
                                friesAmt = friesAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Snacks")) {
                                snacks = snacks + Integer.parseInt(itemQuantity);
                                snacksAmt = snacksAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Chilled Drinks")) {
                                chilled_drinks = chilled_drinks + Integer.parseInt(itemQuantity);
                                chilled_drinksAmt = chilled_drinksAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Sea Foods")) {
                                sea_foods = sea_foods + Integer.parseInt(itemQuantity);
                                sea_foodsAmt = sea_foodsAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Coffees")) {
                                coffees = coffees + Integer.parseInt(itemQuantity);
                                coffeesAmt = coffeesAmt + Integer.parseInt(itemTotal);
                            }
                        }

                        saveReportMonthly(specials, pizza, burgers, fries, snacks, chilled_drinks,
                                sea_foods, coffees, net_sale, specialsAmt, pizzaAmt, burgersAmt,
                                friesAmt, snacksAmt, chilled_drinksAmt, sea_foodsAmt, coffeesAmt);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void saveReportMonthly(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j,
                                  int k, int l, int m ,int n, int o, int p, int q) {
        sp = String.valueOf(a);
        pi = String.valueOf(b);
        bu = String.valueOf(c);
        fr = String.valueOf(d);
        sn = String.valueOf(e);
        ch = String.valueOf(f);
        se = String.valueOf(g);
        co = String.valueOf(h);
        ns = String.valueOf(i);

        spAmt = String.valueOf(j);
        piAmt = String.valueOf(k);
        buAmt = String.valueOf(l);
        frAmt = String.valueOf(m);
        snAmt = String.valueOf(n);
        chAmt = String.valueOf(o);
        seAmt = String.valueOf(p);
        coAmt = String.valueOf(q);

        mDatabaseRef2 = mDatabase2.getReference().child("Reports").
                child("month_wise").child(getMonth());

        mDatabaseRef2.child("Specials").setValue(sp);
        mDatabaseRef2.child("Pizza").setValue(pi);
        mDatabaseRef2.child("Burgers").setValue(bu);
        mDatabaseRef2.child("Fries").setValue(fr);
        mDatabaseRef2.child("Snacks").setValue(sn);
        mDatabaseRef2.child("Chilled Drinks").setValue(ch);
        mDatabaseRef2.child("Sea Foods").setValue(se);
        mDatabaseRef2.child("Coffees").setValue(co);
        mDatabaseRef2.child("Net Sale").setValue(ns);
        mDatabaseRef2.child("Month").setValue(getMonth());

        mDatabaseRef2.child("SpecialsAmount").setValue(spAmt);
        mDatabaseRef2.child("PizzaAmount").setValue(piAmt);
        mDatabaseRef2.child("BurgersAmount").setValue(buAmt);
        mDatabaseRef2.child("FriesAmount").setValue(frAmt);
        mDatabaseRef2.child("SnacksAmount").setValue(snAmt);
        mDatabaseRef2.child("Chilled DrinksAmount").setValue(chAmt);
        mDatabaseRef2.child("Sea FoodsAmount").setValue(seAmt);
        mDatabaseRef2.child("CoffeesAmount").setValue(coAmt);

        setYearlySales();

    }

    public void setYearlySales() {
        String currentYear = getYear();
        getYearRecord(currentYear);
    }


    public void getYearRecord(String year) {
        FirebaseDatabase.getInstance().getReference().child("Reports").child("year_wise").
                child(year).addListenerForSingleValueEvent(new ValueEventListener() {

            DataSnapshot dataSnapshot;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                this.dataSnapshot = snapshot;

                if (dataSnapshot.exists()) {
                    sp = dataSnapshot.child("Specials").getValue().toString();
                    pi = dataSnapshot.child("Pizza").getValue().toString();
                    bu = dataSnapshot.child("Burgers").getValue().toString();
                    fr = dataSnapshot.child("Fries").getValue().toString();
                    sn = dataSnapshot.child("Snacks").getValue().toString();
                    ch = dataSnapshot.child("Chilled Drinks").getValue().toString();
                    se = dataSnapshot.child("Sea Foods").getValue().toString();
                    co = dataSnapshot.child("Coffees").getValue().toString();
                    ns = dataSnapshot.child("Net Sale").getValue().toString();

                    spAmt = dataSnapshot.child("SpecialsAmount").getValue().toString();
                    piAmt = dataSnapshot.child("PizzaAmount").getValue().toString();
                    buAmt = dataSnapshot.child("BurgersAmount").getValue().toString();
                    frAmt = dataSnapshot.child("FriesAmount").getValue().toString();
                    snAmt = dataSnapshot.child("SnacksAmount").getValue().toString();
                    chAmt = dataSnapshot.child("Chilled DrinksAmount").getValue().toString();
                    seAmt = dataSnapshot.child("Sea FoodsAmount").getValue().toString();
                    coAmt = dataSnapshot.child("CoffeesAmount").getValue().toString();

                } else {
                    mDatabaseRef = mDatabase.getReference().child("Reports").
                            child("year_wise").child(year);

                    sp = pi = bu = fr = sn = ch = se = co = ns = "0";
                    spAmt = piAmt = buAmt = frAmt = snAmt = chAmt = seAmt = coAmt = "0";

                    mDatabaseRef.child("Specials").setValue("0");
                    mDatabaseRef.child("Pizza").setValue("0");
                    mDatabaseRef.child("Burgers").setValue("0");
                    mDatabaseRef.child("Fries").setValue("0");
                    mDatabaseRef.child("Snacks").setValue("0");
                    mDatabaseRef.child("Chilled Drinks").setValue("0");
                    mDatabaseRef.child("Sea Foods").setValue("0");
                    mDatabaseRef.child("Coffees").setValue("0");
                    mDatabaseRef.child("Net Sale").setValue("0");
                    mDatabaseRef.child("Year").setValue(year);

                    mDatabaseRef.child("SpecialsAmount").setValue("0");
                    mDatabaseRef.child("PizzaAmount").setValue("0");
                    mDatabaseRef.child("BurgersAmount").setValue("0");
                    mDatabaseRef.child("FriesAmount").setValue("0");
                    mDatabaseRef.child("SnacksAmount").setValue("0");
                    mDatabaseRef.child("Chilled DrinksAmount").setValue("0");
                    mDatabaseRef.child("Sea FoodsAmount").setValue("0");
                    mDatabaseRef.child("CoffeesAmount").setValue("0");

                }
                saveToDBYearly(sp, pi, bu, fr, sn, ch, se, co, ns, spAmt, piAmt, buAmt,
                        frAmt, snAmt, chAmt, seAmt, coAmt);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void saveToDBYearly(String ss, String pa, String bs, String fs, String sn, String cs,
                                String se, String ds, String ne, String ss2, String pa2, String bs2,
                               String fs2, String sn2, String cs2, String se2, String ds2) {

        specials = Integer.parseInt(ss);
        pizza = Integer.parseInt(pa);
        burgers = Integer.parseInt(bs);
        fries = Integer.parseInt(fs);
        snacks = Integer.parseInt(sn);
        chilled_drinks = Integer.parseInt(cs);
        sea_foods = Integer.parseInt(se);
        coffees = Integer.parseInt(ds);
        net_sale = Integer.parseInt(ne);

        specialsAmt = Integer.parseInt(ss2);
        pizzaAmt = Integer.parseInt(pa2);
        burgersAmt = Integer.parseInt(bs2);
        friesAmt = Integer.parseInt(fs2);
        snacksAmt = Integer.parseInt(sn2);
        chilled_drinksAmt = Integer.parseInt(cs2);
        sea_foodsAmt = Integer.parseInt(se2);
        coffeesAmt = Integer.parseInt(ds2);


        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                dbBikerUSN = snapshot.child("BikerUsername").getValue().toString();

                                if (dbBikerUSN.equals(bikerUsername)) {
                                    orderId = snapshot.getKey().toString();
                                    billTotal = snapshot.child("CustomerTotalBill").getValue().toString();
                                    net_sale = net_sale + Integer.parseInt(billTotal);
                                    flag = true;
                                    break;
                                }
                            }
                            getItemsYearly(orderId);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getItemsYearly(String id) {
        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .child(id).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            itemCategory = snapshot.child("Category").getValue().toString();
                            itemQuantity = snapshot.child("Quantity").getValue().toString();
                            itemTotal = snapshot.child("TotalPrice").getValue().toString();

                            if (itemCategory.equals("Specials")) {
                                specials = specials + Integer.parseInt(itemQuantity);
                                specialsAmt = specialsAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Pizza")) {
                                pizza = pizza + Integer.parseInt(itemQuantity);
                                pizzaAmt = pizzaAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Burgers")) {
                                burgers = burgers + Integer.parseInt(itemQuantity);
                                burgersAmt = burgersAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Fries")) {
                                fries = fries + Integer.parseInt(itemQuantity);
                                friesAmt = friesAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Snacks")) {
                                snacks = snacks + Integer.parseInt(itemQuantity);
                                snacksAmt = snacksAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Chilled Drinks")) {
                                chilled_drinks = chilled_drinks + Integer.parseInt(itemQuantity);
                                chilled_drinksAmt = chilled_drinksAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Sea Foods")) {
                                sea_foods = sea_foods + Integer.parseInt(itemQuantity);
                                sea_foodsAmt = sea_foodsAmt + Integer.parseInt(itemTotal);
                            } else if (itemCategory.equals("Coffees")) {
                                coffees = coffees + Integer.parseInt(itemQuantity);
                                coffeesAmt = coffeesAmt + Integer.parseInt(itemTotal);
                            }
                        }

                        saveReportYearly(specials, pizza, burgers, fries, snacks, chilled_drinks,
                                sea_foods, coffees, net_sale, specialsAmt, pizzaAmt, burgersAmt,
                                friesAmt, snacksAmt, chilled_drinksAmt, sea_foodsAmt, coffeesAmt);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void saveReportYearly(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j,
                                 int k, int l, int m ,int n, int o, int p, int q) {
        sp = String.valueOf(a);
        pi = String.valueOf(b);
        bu = String.valueOf(c);
        fr = String.valueOf(d);
        sn = String.valueOf(e);
        ch = String.valueOf(f);
        se = String.valueOf(g);
        co = String.valueOf(h);
        ns = String.valueOf(i);

        spAmt = String.valueOf(j);
        piAmt = String.valueOf(k);
        buAmt = String.valueOf(l);
        frAmt = String.valueOf(m);
        snAmt = String.valueOf(n);
        chAmt = String.valueOf(o);
        seAmt = String.valueOf(p);
        coAmt = String.valueOf(q);

        mDatabaseRef2 = mDatabase2.getReference().child("Reports").
                child("year_wise").child(getYear());

        mDatabaseRef2.child("Specials").setValue(sp);
        mDatabaseRef2.child("Pizza").setValue(pi);
        mDatabaseRef2.child("Burgers").setValue(bu);
        mDatabaseRef2.child("Fries").setValue(fr);
        mDatabaseRef2.child("Snacks").setValue(sn);
        mDatabaseRef2.child("Chilled Drinks").setValue(ch);
        mDatabaseRef2.child("Sea Foods").setValue(se);
        mDatabaseRef2.child("Coffees").setValue(co);
        mDatabaseRef2.child("Net Sale").setValue(ns);
        mDatabaseRef2.child("Year").setValue(getYear());

        mDatabaseRef2.child("SpecialsAmount").setValue(spAmt);
        mDatabaseRef2.child("PizzaAmount").setValue(piAmt);
        mDatabaseRef2.child("BurgersAmount").setValue(buAmt);
        mDatabaseRef2.child("FriesAmount").setValue(frAmt);
        mDatabaseRef2.child("SnacksAmount").setValue(snAmt);
        mDatabaseRef2.child("Chilled DrinksAmount").setValue(chAmt);
        mDatabaseRef2.child("Sea FoodsAmount").setValue(seAmt);
        mDatabaseRef2.child("CoffeesAmount").setValue(coAmt);

        setData(username);

    }
}