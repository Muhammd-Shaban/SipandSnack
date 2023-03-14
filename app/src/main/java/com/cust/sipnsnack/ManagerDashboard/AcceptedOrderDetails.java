package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class AcceptedOrderDetails extends AppCompatActivity {

    LoadingDialog loadingDialog;
    TextView customerNameTV, customerPhoneNoTV, customerAddressTV, paymentTypeTV, totalQtyTV,
            totalPriceTV, acceptedByTV, textViewDate, textViewTime;
    ImageView location;
    ImageView receiptImageIV;
    Button generateBillBTN, assignOrderBTN;
    String username, name, phone, address, payment, qty, price, receipt, acceptedBy, orderId, addressType, lon, lat;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    int loopSize;
    ArrayList<AcceptedOrderItems> myItem;
    AcceptedOrderItemsAdapter acceptedOrderItemsAdapter;
    ImageView backBtn;

    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

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
        setContentView(R.layout.activity_accepted_order_details);

        loadingDialog = new LoadingDialog(AcceptedOrderDetails.this);

        customerNameTV = findViewById(R.id.customerNameTV);
        customerPhoneNoTV = findViewById(R.id.customerPhoneNoTV);
        customerAddressTV = findViewById(R.id.customerAddressTV);
        paymentTypeTV = findViewById(R.id.paymentTypeTV);
        totalQtyTV = findViewById(R.id.totalQtyTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        acceptedByTV = findViewById(R.id.acceptedByTV);
        textViewDate = findViewById(R.id.dateTV);
        textViewTime = findViewById(R.id.timeTV);
        receiptImageIV = findViewById(R.id.receiptImageIV);
        generateBillBTN = findViewById(R.id.generateBillBtn);
        assignOrderBTN = findViewById(R.id.assignOrderBtn);
        backBtn = findViewById(R.id.backBTN);
        location = findViewById(R.id.locationIV);

        setTextViews();

        mBluetoothSocket  = ScanDevice.BS;

        recyclerView = (RecyclerView) findViewById(R.id.accepted_items_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myItem = new ArrayList<AcceptedOrderItems>();

        receipt = AcceptedOrdersAdapter.receiptImage;
        Picasso.get()
                .load(receipt)
                .placeholder(R.drawable.logo)
                .into(receiptImageIV);

        ReadFromDB();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (addressType.equals("Manual")) {
                    intent  = new Intent(getApplicationContext(), LocationByAddress.class);
                    intent.putExtra("Address", address);
                } else {
                    intent  = new Intent(getApplicationContext(), LocationByDirection.class);
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

        generateBillBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ScanDevice.CHECK) {
                    printReceipt();
                } else {
                    Intent it = new Intent(getApplicationContext(), ScanDevice.class);
                    startActivity(it);
                    finish();
                }
            }
        });

        assignOrderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), AssignOrder.class);
                it.putExtra("Username", username);
                it.putExtra("Name", name);
                it.putExtra("Phone", phone);
                it.putExtra("AddressType", addressType);

                if (addressType.equals("Manual")) {
                    it.putExtra("Address", address);
                    it.putExtra("Longitude", "");
                    it.putExtra("Latitude", "");
                } else {
                    it.putExtra("Address", "");
                    it.putExtra("Longitude", lon);
                    it.putExtra("Latitude", lat);
                }
                it.putExtra("Payment", payment);
                it.putExtra("AcceptedBy", acceptedBy);
                it.putExtra("Quantity", qty);
                it.putExtra("TotalBill", price);
                it.putExtra("ReceiptImage", AcceptedOrdersAdapter.receiptImage);
                it.putExtra("Date", AcceptedOrdersAdapter.orderDate);
                it.putExtra("time", AcceptedOrdersAdapter.orderTime);
                it.putExtra("OrderId", AcceptedOrdersAdapter.orderId);
                startActivity(it);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AcceptedOrders.class));
        finish();
    }

    public void setTextViews() {
        username = AcceptedOrdersAdapter.username;
        name = AcceptedOrdersAdapter.name;
        phone = AcceptedOrdersAdapter.phoneNo;
        addressType = AcceptedOrdersAdapter.addressType;

        address = lon = lat = "";

        if (addressType.equals("Manual")) {
            address = AcceptedOrdersAdapter.address;
            customerAddressTV.setText(address);
            receiptImageIV.setVisibility(View.GONE);
        } else {
            lon = AcceptedOrdersAdapter.longitude;
            lat = AcceptedOrdersAdapter.latitude;
            customerAddressTV.setText("(On Maps)");
        }

        payment = AcceptedOrdersAdapter.paymentType;
        acceptedBy = AcceptedOrdersAdapter.acceptedBy;
        qty = AcceptedOrdersAdapter.totalQty;
        price = AcceptedOrdersAdapter.totalBill;
        orderId = AcceptedOrdersAdapter.orderId;

        customerNameTV.setText(name);
        customerPhoneNoTV.setText(phone);
        paymentTypeTV.setText(payment);
        acceptedByTV.setText(acceptedBy);
        totalQtyTV.setText(qty);
        totalPriceTV.setText(price);
        textViewDate.setText(AcceptedOrdersAdapter.orderDate);
        textViewTime.setText(AcceptedOrdersAdapter.orderTime);

    }

    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;
        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted").
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted")
                .child(orderId).child("Items")
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
                            Toast.makeText(AcceptedOrderDetails.this, "No Item Founded ...", Toast.LENGTH_SHORT).show();

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

    public void printReceipt() {
        LayoutInflater layoutInflater1 = LayoutInflater.from(AcceptedOrderDetails.this);
        View view = layoutInflater1.inflate(R.layout.ask_print_invoice, null);
        Button printBTN = view.findViewById(R.id.printBTN);
        Button cancelBTN = view.findViewById(R.id.cancelBTN);

        final AlertDialog alertDialog = new AlertDialog.Builder(AcceptedOrderDetails.this)
                .setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);


        printBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread t = new Thread() {
                    @TargetApi(Build.VERSION_CODES.ECLAIR)
                    public void run() {
                        try {

                            OutputStream os = mBluetoothSocket
                                    .getOutputStream();
                            String BILL = "";


                            BILL = "         Sip n Snack    \n"
                                    + "      Tarammari Islamabad     \n " +
                                    "        0341-1551466      \n";
                            BILL = BILL + "\n";
                            BILL = BILL + String.format("%1$-6s %2$8s %3$10s", getTodayDate(), " ", getTime());
                            BILL = BILL + "\n";
                            BILL = BILL + "\n";

                            BILL = BILL
                                    + "--------------------------------\n";
                            BILL = BILL + String.format("%1$-6s %2$12s %3$10s", "Size", "Quantity", "Price");
                            BILL = BILL + "\n";
                            BILL = BILL
                                    + "--------------------------------";

                            for (int i = 0; i < MyAcceptedOrderData.itemId.length; i++) {

                                BILL = BILL + "\n " + String.format("%1$-6s", "Item: "+MyAcceptedOrderData.
                                        itemName[i]);

                                BILL = BILL + "\n " + String.format("%1$-10s %2$5s %3$14s",
                                        MyAcceptedOrderData.itemSize[i],
                                        MyAcceptedOrderData.itemQuantity[i],
                                        MyAcceptedOrderData.itemTotalPrice[i] + " Rs.");


                                BILL = BILL + "\n";
                                BILL = BILL
                                        + "--------------------------------";
                            }


                            BILL = BILL
                                    + "\n--------------------------------";
                            BILL = BILL + "\n\n ";


                            BILL = BILL + "        Total Qty:" + "      " + totalQtyTV.getText().toString() + "\n";
                            BILL = BILL + "        Total Price:" + "     " + totalPriceTV.getText().toString() + "\n";

                            BILL = BILL
                                    + "--------------------------------\n";

                            BILL = BILL + "Thank You for Ordering :)";


                            BILL = BILL + "\n\n\n";

                            os.write(BILL.getBytes());
                            //This is printer specific code you can comment ==== > Start

                            // Setting height
                            int gs = 29;
                            os.write(intToByteArray(gs));
                            int h = 104;
                            os.write(intToByteArray(h));
                            int n = 162;
                            os.write(intToByteArray(n));

                            // Setting Width
                            int gs_width = 29;
                            os.write(intToByteArray(gs_width));
                            int w = 119;
                            os.write(intToByteArray(w));
                            int n_width = 2;
                            os.write(intToByteArray(n_width));

                        } catch (
                                Exception e) {
                            Log.e("MainActivity", "Exe ", e);
                        }

                    }
                };
                t.start();

                Toast.makeText(AcceptedOrderDetails.this, "Bill Generated Successfully", Toast.LENGTH_SHORT).show();

                alertDialog.dismiss();

            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this::run);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(AcceptedOrderDetails.this,
                            DevicesList.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(AcceptedOrderDetails.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(AcceptedOrderDetails.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }
        return b[3];
    }

    public void wipDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.work_in_process, null);
        Button okBTN = view.findViewById(R.id.okBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}