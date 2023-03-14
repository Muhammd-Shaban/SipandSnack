package com.cust.sipnsnack.Items;

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
import android.widget.ImageButton;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AddItems extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();
    boolean dbCheck = false;
    TextInputEditText itemNameET, itemPriceET, itemDescriptionET, itemIdET;
    Button addItemBTN;
    Spinner category, sizeSpin;
    ImageView infoIcon;
    String itemName, itemPrice, itemDescription, itemCategory, itemId, itemSize;
    LoadingDialog loadingDialog;
    ImageView backBtn;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;
    CircleImageView itemImg;
    ImageButton itemImgBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        itemNameET = findViewById(R.id.item_NameET);
        itemPriceET = findViewById(R.id.item_PriceET);
        itemDescriptionET = findViewById(R.id.item_DescriptionET);
        itemIdET = findViewById(R.id.item_IdET);
        category = findViewById(R.id.categorySPN);
        sizeSpin = findViewById(R.id.sizeSPN);
        infoIcon = findViewById(R.id.infoiconIV);
        itemImg = findViewById(R.id.itemImg);
        itemImgBtn = findViewById(R.id.itemImageBTN);
        backBtn=  findViewById(R.id.backBTN);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent it=new Intent(getApplicationContext(), ManageItems.class);
               startActivity(it);
               finish();
            }
        });



        storageReference = FirebaseStorage.getInstance().getReference("/images/uploads/items");
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        loadingDialog = new LoadingDialog(AddItems.this);

        List<String> listCategory = new ArrayList<String>();
        listCategory.add("Fries");
        listCategory.add("Snacks");
        listCategory.add("Pizza");
        listCategory.add("Burgers");
        listCategory.add("Chilled Drinks");
        listCategory.add("Sea Foods");
        listCategory.add("Coffees");
        listCategory.add("Specials");


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listCategory);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(dataAdapter1);


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    itemCategory = "Fries";
                } else if (i == 1) {
                    itemCategory = "Snacks";
                } else if (i == 2) {
                    itemCategory = "Pizza";
                } else if (i == 3) {
                    itemCategory = "Burgers";
                } else if (i == 4) {
                    itemCategory = "Chilled Drinks";
                } else if (i == 5) {
                    itemCategory = "Sea Foods";
                } else if (i == 6) {
                    itemCategory = "Coffees";
                } else if (i == 7) {
                    itemCategory = "Specials";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        List<String> size = new ArrayList<String>();
        size.add("Regular");
        size.add("Small");
        size.add("Medium");
        size.add("Large");
        size.add("Extra Large");
        size.add("None");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, size);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpin.setAdapter(dataAdapter2);

        sizeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    itemSize = "Regular";
                } else if (i == 1) {
                    itemSize = "Small";
                } else if (i == 2) {
                    itemSize = "Medium";
                } else if (i == 3) {
                    itemSize = "Large";
                } else if (i == 4) {
                    itemSize = "Extra Large";
                } else if (i == 5) {
                    itemSize = "None";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });



        addItemBTN = findViewById(R.id.addItemsBtn);

        addItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItems();
            }
        });


        itemImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        itemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
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
            Picasso.get().load(imageUri).into(itemImg);
            itemImg.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    public boolean uploadFile() {
        itemId = itemIdET.getText().toString();
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
                            databaseReference.child(itemId).child("ImageUrl").setValue(downloadUrl.toString());

                            moveNext();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddItems.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void addItems() {

        loadingDialog.startLoadingDialog();

        itemName = itemNameET.getText().toString();
        itemPrice = itemPriceET.getText().toString();
        itemDescription = itemDescriptionET.getText().toString();
        itemId = itemIdET.getText().toString();

        if (itemName.equals("")) {
            loadingDialog.dismissDialog();
            itemNameET.setError("Item Name is Required");
        } else if (itemPrice.equals("")) {
            loadingDialog.dismissDialog();
            itemPriceET.setError("Item Price is Required");
        } else if (itemDescription.equals("")) {
            loadingDialog.dismissDialog();
            itemDescriptionET.setError("Item Description is Required");
        } else if (itemId.equals("")) {
            loadingDialog.dismissDialog();
            itemDescriptionET.setError("Item Id is Required");
        } else {
            FirebaseDatabase.getInstance().getReference().child("Items")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        private DataSnapshot dataSnapshot;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            this.dataSnapshot = dataSnapshot;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String tempItemName = itemName;
                                String tempItemId = itemId;
                                tempItemName = tempItemName.toLowerCase();
                                tempItemId = tempItemId.toLowerCase();
                                String dbItemName = snapshot.child("Name").getValue().toString().toLowerCase();
                                String dbItemId = snapshot.child("Id").getValue().toString().toLowerCase();
                                if (dbItemName.equals(tempItemName) || dbItemId.equals(tempItemId)) {
                                    loadingDialog.dismissDialog();
                                    dbCheck = true;
                                    itemNameET.setError("Item Already Exists");
                                    itemIdET.requestFocus();
                                    break;
                                } else {

                                    dbCheck = false;

                                }
                            }

                            if (!dbCheck) {

                                if (uploadFile()) {
                                    mDatabaseRef = mDatabase.getReference().child("Items").child(itemId);
                                    mDatabaseRef.child("Id").setValue(itemId);
                                    mDatabaseRef.child("Name").setValue(itemName);
                                    mDatabaseRef.child("Price").setValue(itemPrice);
                                    mDatabaseRef.child("Category").setValue(itemCategory);
                                    mDatabaseRef.child("Description").setValue(itemDescription);
                                    mDatabaseRef.child("Size").setValue(itemSize);


                                    itemNameET.setText("");
                                    itemPriceET.setText("");
                                    itemDescriptionET.setText("");
                                    itemIdET.setText("");

                                    itemIdET.requestFocus();

                                } else {
                                    loadingDialog.dismissDialog();
                                    Toast.makeText(AddItems.this, "Problem Occurred with Item Image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_add_item, null);
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

    public void moveNext() {
        loadingDialog.dismissDialog();

        Toast.makeText(AddItems.this, "Item Added Successfully...", Toast.LENGTH_LONG).show();
        startActivity(getIntent());
        finish();
    }
}