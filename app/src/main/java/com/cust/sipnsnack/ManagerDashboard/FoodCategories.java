package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

import java.util.ArrayList;
import java.util.List;

public class FoodCategories extends AppCompatActivity {

    TextInputEditText catNameET;
    Button add, delete;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();
    boolean dbCheck = false;
    ImageView infoIcon, itemCategoryIV;
    Spinner category;
    String name;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference, databaseReference2;
    private StorageTask uploadTask;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_categories);

        catNameET = findViewById(R.id.item_CategoryNameET);
        add = findViewById(R.id.addCategoryBtn);
        delete = findViewById(R.id.deleteCategoryBtn);
        infoIcon = findViewById(R.id.infoiconIV);
        itemCategoryIV = findViewById(R.id.addCategoryIV);
        category = findViewById(R.id.categorySPN);

        storageReference = FirebaseStorage.getInstance().getReference("/images/uploads/categories");
        databaseReference = FirebaseDatabase.getInstance().getReference("Categories");

        loadingDialog = new LoadingDialog(FoodCategories.this);

        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        itemCategoryIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openFileChooser();
                return true;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategories();
            }
        });

        List <String> categories = loadCategories();

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(dataAdapter1);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String txt = category.getSelectedItem().toString();
                Toast.makeText(FoodCategories.this, "CAT = "+txt, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public List<String> loadCategories() {
        List<String> listCategory = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference().child("Categories")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String dbItemName = snapshot.child("Name").getValue().toString().toLowerCase();

                            listCategory.add(dbItemName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return listCategory;
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
            Picasso.get().load(imageUri).into(itemCategoryIV);
            itemCategoryIV.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    public boolean uploadFile() {
        name = catNameET.getText().toString();

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
                            databaseReference.child(name).child("ImageUrl").setValue(downloadUrl.toString());

                            finish();
                            startActivity(getIntent());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FoodCategories.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
            return true;
        } else {
            Toast.makeText(this, "Image URI not found ...", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_add_category, null);
        Button okBTN = view.findViewById(R.id.okBTN);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void addCategories() {

        loadingDialog.startLoadingDialog();

        name = catNameET.getText().toString();

        if (name.equals("")) {
            loadingDialog.dismissDialog();
            catNameET.setError("Category Name is Required");
        } else {
            FirebaseDatabase.getInstance().getReference().child("Categories")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String tempItemName = name;
                                tempItemName = tempItemName.toLowerCase();

                                String dbItemName = snapshot.child("Name").getValue().toString().toLowerCase();

                                if (dbItemName.equals(tempItemName)) {
                                    loadingDialog.dismissDialog();
                                    dbCheck = true;
                                    catNameET.setError("Category Already Exists");
                                    catNameET.requestFocus();
                                    break;
                                } else {

                                    dbCheck = false;

                                }
                            }

                            if (!dbCheck) {

                                if (uploadFile()) {
                                    mDatabaseRef = mDatabase.getReference().child("Categories").child(name);
                                    mDatabaseRef.child("Name").setValue(name);

                                    catNameET.setText("");

                                    catNameET.requestFocus();

                                    Toast.makeText(FoodCategories.this, "Category Added Successfully...", Toast.LENGTH_LONG).show();
                                    loadingDialog.dismissDialog();
                                } else {
                                    loadingDialog.dismissDialog();
                                    Toast.makeText(FoodCategories.this, "Problem Occurred with Category Image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}