package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
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

public class PopularItems2 extends AppCompatActivity {

    ImageView img1;
    Button bt1, bt2, bt3, bt4;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;
    LoadingDialog loadingDialog;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_items2);

        loadingDialog = new LoadingDialog(PopularItems2.this);
        loadingDialog.startLoadingDialog();

        img1 = findViewById(R.id.img2);

        bt1 = findViewById(R.id.changeImg2);

        bt2 = findViewById(R.id.btn1);
        bt3 = findViewById(R.id.btn3);
        bt4 = findViewById(R.id.btn4);

        backBtn = findViewById(R.id.backBTN);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                finish();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("/images/uploads/popular");
        databaseReference = FirebaseDatabase.getInstance().getReference("Popular");
        FirebaseDatabase.getInstance().getReference().child("Popular").child("Image2")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;
                        String url = "";
                        try {
                            url = dataSnapshot.child("ImageUrl").getValue().toString();
                            System.out.println("URL : "+ url +"\n");
                        } catch (Exception e) {}


                        if (url.equals("") || url.equals(null)) {
                            loadingDialog.dismissDialog();
                        } else {
                            loadPicture(url);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), PopularItems.class);
                startActivity(it);
                finish();
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), PopularItems3.class);
                startActivity(it);
                finish();
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), PopularItems4.class);
                startActivity(it);
                finish();
            }
        });

        img1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openFileChooser();
                return true;
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();
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
            Picasso.get().load(imageUri).into(img1);
            img1.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFile() {
        loadingDialog.startLoadingDialog();
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
                            databaseReference.child("Image2").child("ImageUrl").setValue(downloadUrl.toString());

                            loadingDialog.dismissDialog();
                            Toast.makeText(PopularItems2.this, "Successfully Uploaded...", Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(getIntent());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PopularItems2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else {
            loadingDialog.dismissDialog();
            Toast.makeText(this, "Image URI not found ...", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadPicture(String URL) {
        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.logo)
                .into(img1);
        loadingDialog.dismissDialog();
    }
}