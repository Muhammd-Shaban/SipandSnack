package com.cust.sipnsnack.ManagerDashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class Test extends AppCompatActivity {

    EditText et;
    Button btn;
    String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        et = findViewById(R.id.et);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = et.getText().toString();

                if (str.equals("")) {
                    Toast.makeText(Test.this, "PLEASE FILL", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(str).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Test.this, "SENT", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}