package com.cust.sipnsnack.Items;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class UpdateItems extends AppCompatActivity {

    TextInputEditText itemIdET, itemNameET, itemPriceET, itemDescriptionET;
    Spinner categorySpinner, sizeSpinner;
    String itemName, itemPrice, itemDescription, itemCategory, itemId, itemSize;
    Button updateBtn;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference();
    ImageView itemImg;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference, databaseReference2;
    private StorageTask uploadTask;
    LoadingDialog loadingDialog;
    String id, name, price, desc, category, SIZE;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_items);

        loadingDialog = new LoadingDialog(UpdateItems.this);

        itemIdET = findViewById(R.id.updateItem_IdET);
        itemNameET = findViewById(R.id.updateItem_NameET);
        itemPriceET = findViewById(R.id.updateItem_PriceET);
        itemDescriptionET = findViewById(R.id.updateItem_DescriptionET);
        categorySpinner = findViewById(R.id.categoryUpdateSPN);
        sizeSpinner = findViewById(R.id.sizeUpdateSPN);
        updateBtn = findViewById(R.id.itemsUpdateButton);
        itemImg = findViewById(R.id.updateItem_ImageIV);
        backBtn = findViewById(R.id.backBTN);

        itemIdET.setEnabled(false);

        storageReference = FirebaseStorage.getInstance().getReference("/images/uploads/items");
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ViewItems.class));
                finish();
            }
        });

        itemImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openFileChooser();
                return true;
            }
        });

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
        categorySpinner.setAdapter(dataAdapter1);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        sizeSpinner.setAdapter(dataAdapter2);

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        itemIdET.setText(MyItemsAdapter.id);
        itemNameET.setText(MyItemsAdapter.name);
        itemPriceET.setText(MyItemsAdapter.price);
        itemDescriptionET.setText(MyItemsAdapter.description);

        loadPicture(MyItemsAdapter.uri);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id = itemIdET.getText().toString();
                name = itemNameET.getText().toString();
                price = itemPriceET.getText().toString();
                desc = itemDescriptionET.getText().toString();
                category = categorySpinner.getSelectedItem().toString();
                SIZE = sizeSpinner.getSelectedItem().toString();

                updateItem(id, name, price, desc, category, SIZE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), ViewItems.class);
        startActivity(it);
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
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateItems.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void updateItem(String iD, String nAme, String pRice, String dEsc,
                           String cAtegory, String sIze) {

        loadingDialog.startLoadingDialog();

        itemId = itemIdET.getText().toString();
        itemName = itemNameET.getText().toString();
        itemPrice = itemPriceET.getText().toString();
        itemDescription = itemDescriptionET.getText().toString();


        if (itemName.equals("")) {
            itemNameET.setError("Item Name is Required");
        } else if (itemPrice.equals("")) {
            itemPriceET.setError("Item Price is Required");
        } else if (itemDescription.equals("")) {
            itemDescriptionET.setError("Item Description is Required");
        } else if (itemId.equals("")) {
            itemDescriptionET.setError("Item Id is Required");
        } else {
            if (itemId.equals(MyItemsAdapter.id)) {
                FirebaseDatabase.getInstance().getReference().child("Items")
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            private DataSnapshot dataSnapshot;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                this.dataSnapshot = snapshot;

                                if (uploadFile()) {

                                    mRef = mDatabase.getReference().child("Items").child(itemId);
                                    mRef.child("Id").setValue(itemId);
                                    mRef.child("Name").setValue(itemName);
                                    mRef.child("Price").setValue(itemPrice);
                                    mRef.child("Size").setValue(itemSize);
                                    mRef.child("Category").setValue(itemCategory);
                                    mRef.child("Description").setValue(itemDescription);

                                    loadingDialog.dismissDialog();
                                    Toast.makeText(UpdateItems.this, "Item Updated Successfully ... ", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), ViewItems.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            } else {
                Toast.makeText(UpdateItems.this, "Item ID is not Matching ...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadPicture(String URL) {
        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.logo)
                .into(itemImg);
    }
}