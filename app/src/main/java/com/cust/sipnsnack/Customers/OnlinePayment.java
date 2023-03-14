package com.cust.sipnsnack.Customers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OnlinePayment extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;
    CircleImageView receiptIMG;
    ImageButton receiptImgBTN;
    Button continueBtn;
    LoadingDialog loadingDialog;
    SharedPreferences spr;
    String username;
    ImageView BackBtn;
    TextView tv1, tv2, tv3, tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);

        loadingDialog = new LoadingDialog(OnlinePayment.this);

        tv1 = findViewById(R.id.bankAccountTitle);
        tv2 = findViewById(R.id.bankAccountNumber);
        tv3 = findViewById(R.id.EasypaisaAccNameTV);
        tv4 = findViewById(R.id.EasypaisaNumberTV);

        loadingDialog.startLoadingDialog();

        getBankAccInfo();

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");
        BackBtn = findViewById(R.id.backBTN);

        continueBtn = findViewById(R.id.continueBtn);
        receiptIMG = findViewById(R.id.receiptImg);
        receiptImgBTN = findViewById(R.id.receiptImageBTN);

        storageReference = FirebaseStorage.getInstance().getReference("/images/uploads/payments/online");
        databaseReference = FirebaseDatabase.getInstance().getReference("temp_order");


        receiptIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        receiptImgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PaymentProcess.class));
                finish();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                if (uploadFile(username)) {

                } else {
                    loadingDialog.dismissDialog();
                }
            }
        });
    }

    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(receiptIMG);
            receiptIMG.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    public boolean uploadFile(String uN) {
        final UploadTask uploadTask;
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            uploadTask = (UploadTask) fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            databaseReference.child(uN).child("ReceiptImage").setValue(downloadUrl.toString());

                            moveNext();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OnlinePayment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
            return true;
        } else {
            Toast.makeText(this, "You have not Selected any Image ...", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), PaymentProcess.class));
        finish();
    }

    public void moveNext() {
        Toast.makeText(OnlinePayment.this, "Receipt Uploaded Successfully...", Toast.LENGTH_LONG).show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("temp_order").child(username);
        mDatabaseRef.child("PaymentType").setValue("Online");

        String qtyy = getIntent().getStringExtra("Qty");
        String bill = getIntent().getStringExtra("Bill");
        Intent it = new Intent(getApplicationContext(), SetDeliveryAddress.class);
        it.putExtra("Qty", qtyy);
        it.putExtra("Bill", bill);
        startActivity(it);
        finish();

        loadingDialog.dismissDialog();
    }

    public void getBankAccInfo() {
        FirebaseDatabase.getInstance().getReference().child("OnlinePaymentDetails").child("Bank")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        tv1.setText("Account Name: "+dataSnapshot.child("AccountName").getValue().toString());
                        tv2.setText("Account Number: "+dataSnapshot.child("AccountNumber").getValue().toString());

                        getEasypaisaInfo();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getEasypaisaInfo() {
        FirebaseDatabase.getInstance().getReference().child("OnlinePaymentDetails").child("EasyPaisa")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        tv3.setText("Account Name: "+dataSnapshot.child("AccountName").getValue().toString());
                        tv4.setText("Easypaisa Number: "+dataSnapshot.child("AccountPhoneNo").getValue().toString());

                        loadingDialog.dismissDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}