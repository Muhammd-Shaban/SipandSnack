package com.cust.sipnsnack;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ImageUpload extends AppCompatActivity {

    Button selectImg, uploadImg, viewImages;
    EditText name;
    ImageView img;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");


        selectImg = findViewById(R.id.selectImageBtn);
        uploadImg = findViewById(R.id.uploadBtn);
        viewImages = findViewById(R.id.viewImages);

        name = findViewById(R.id.imgName);

        img = findViewById(R.id.selectedImg);

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadTask != null) {
                    Toast.makeText(ImageUpload.this, "...", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

        viewImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ViewImages.class);
                startActivity(it);
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
            Picasso.get().load(imageUri).into(img);
            img.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFile() {
        final UploadTask uploadTask;
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            uploadTask = (UploadTask) fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Toast.makeText(ImageUpload.this, "Upload successful", Toast.LENGTH_LONG).show();
                            /*ImgUpload upload = new ImgUpload(name.getText().toString(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(upload);*/

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            Toast.makeText(ImageUpload.this, "URL = "+downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                            ImgUpload upload = new ImgUpload(name.getText().toString(), downloadUrl.toString());
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImageUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else {
            Toast.makeText(this, "Image URI not found ...", Toast.LENGTH_SHORT).show();
        }
    }
}