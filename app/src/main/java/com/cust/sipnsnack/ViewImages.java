package com.cust.sipnsnack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewImages extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ImageAdapter imageAdapter;

    public DatabaseReference databaseReference;
    public List<ImgUpload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images);

        recyclerView = findViewById(R.id.imgRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploads = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImgUpload imgUpload = postSnapshot.getValue(ImgUpload.class);
                    uploads.add(imgUpload);
                }

                imageAdapter = new ImageAdapter(ViewImages.this, uploads);
                recyclerView.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}