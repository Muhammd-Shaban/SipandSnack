package com.cust.sipnsnack;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    TextView goToLogin;
    Button singUp_Button;
    TextInputEditText nameET, usernameET, passwordET, re_passwordET, phoneET, addressET, emailET;
    public String name, username, password, re_password, phone, address, email;
    Boolean dbCheck = false;

    SharedPreferences mySharedPreferences;
    LoadingDialog loadingDialog;

    FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loadingDialog = new LoadingDialog(SignupActivity.this);

        mAuth = FirebaseAuth.getInstance();

        goToLogin = findViewById(R.id.goToLogin);
        singUp_Button = findViewById(R.id.signUp);

        nameET = findViewById(R.id.signup_Name);
        usernameET = findViewById(R.id.signup_Username);
        passwordET = findViewById(R.id.signup_Password);
        re_passwordET = findViewById(R.id.signup_RePassword);
        phoneET = findViewById(R.id.signup_PhoneNo);
        addressET = findViewById(R.id.signup_Address);
        emailET = findViewById(R.id.signup_Email);

        // GO TO LOGIN PAGE
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(it);
            }
        });

        // SIGN UP BUTTON CLICK EVENT
        singUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup();
            }
        });
    }

    public void Signup() {
        loadingDialog.startLoadingDialog();

        name = nameET.getText().toString();
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();
        phone = phoneET.getText().toString();
        address = addressET.getText().toString();
        re_password = re_passwordET.getText().toString();
        email = emailET.getText().toString();

        mySharedPreferences = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();


        if (name.isEmpty() && username.isEmpty() && password.isEmpty() && phone.isEmpty() && address.isEmpty()) {
            Toast.makeText(this, "Please fill out all the fields first", Toast.LENGTH_SHORT).show();
        }

        if (name.isEmpty()) {

            nameET.setError("Name is required !");
            nameET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (username.isEmpty()) {

            usernameET.setError("Username is required !");
            usernameET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (password.isEmpty()) {

            passwordET.setError("Password is required !");
            passwordET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (phone.isEmpty()) {

            phoneET.setError("Phone No is required !");
            phoneET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (!phone.startsWith("03")) {

            phoneET.setError("Phone No must Starts with 03 !");
            phoneET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (phone.length() < 11) {

            phoneET.setError("Phone No Length must be 11 !");
            phoneET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (address.isEmpty()) {

            addressET.setError("Address is required !");
            addressET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (username.length() < 5) {

            usernameET.setError("Username min length should be 5 !");
            usernameET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (password.length() < 6) {

            passwordET.setError("Password min length should be 6 !");
            passwordET.requestFocus();

        } else if (re_password.length() < 6) {

            re_passwordET.setError("Password min length should be 6 !");
            re_passwordET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (!password.equals(re_password)) {

            passwordET.setError("Passwords are not same !");
            re_passwordET.setError("Passwords are not same !");
            re_passwordET.requestFocus();
            loadingDialog.dismissDialog();

        } else if (!validateEmailAddress()) {

            loadingDialog.dismissDialog();

        } else {

            FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String tempUserName = username;
                                tempUserName = tempUserName.toLowerCase();
                                String dbUserName = snapshot.child("Username").getValue().toString().toLowerCase();
                                String dbPhoneNo = snapshot.child("PhoneNo").getValue().toString();
                                if (dbUserName.equals(tempUserName) || dbPhoneNo.equals(phone)) {
                                    if (dbUserName.equals(tempUserName)) {

                                        loadingDialog.dismissDialog();
                                        usernameET.setError("Username Already Exists");
                                        usernameET.requestFocus();
                                    }
                                    if (dbPhoneNo.equals(phone)) {

                                        loadingDialog.dismissDialog();
                                        phoneET.setError("Phone No Already Used");
                                        phoneET.requestFocus();
                                    }
                                    dbCheck = true;
                                    break;
                                } else {

                                    dbCheck = false;

                                }
                            }

                            if (!dbCheck) {

                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {

                                                    // SEND EMAIL CONFIRMATION LINK ...
                                                    FirebaseUser myUser = mAuth.getCurrentUser();
                                                    myUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(SignupActivity.this, "Signed Up Successfully & Verification Email has been sent ...", Toast.LENGTH_LONG).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SignupActivity.this, "Email was not sent ...", Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                                    // Sign in success, update UI with the signed-in user's information
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    String userId = user.getUid();

                                                    mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                                            .child("Users").child("Customers").child(username);

                                                    mDatabaseRef.child("Name").setValue(name);
                                                    mDatabaseRef.child("Username").setValue(username);
                                                    mDatabaseRef.child("Password").setValue(password);
                                                    mDatabaseRef.child("PhoneNo").setValue(phone);
                                                    mDatabaseRef.child("Address").setValue(address);
                                                    mDatabaseRef.child("Role").setValue("Customer");
                                                    mDatabaseRef.child("Status").setValue("Unblocked");
                                                    mDatabaseRef.child("CancelledOrders").setValue("0");
                                                    mDatabaseRef.child("FirebaseUID").setValue(userId);
                                                    mDatabaseRef.child("Email").setValue(email);

                                                    loadingDialog.dismissDialog();
                                                    sendNotification(name);
                                                    signupSuccessDialog();


                                                } else {
                                                    loadingDialog.dismissDialog();
                                                    // If sign in fails, display a message to the user.
                                                    Toast.makeText(SignupActivity.this, "Invalid Email or Email / PhoneNo already used.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private boolean validateEmailAddress()
    {
        email = emailET.getText().toString();

        if(email.isEmpty())
        {
            emailET.setError("Email is required to login.");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailET.setError("Invalid Email Address Entered !");
            return false;
        }
        else
        {
            return true;
        }

    }


    public void signupSuccessDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.signup_success_dialog, null);
        Button enterBTN = view.findViewById(R.id.enterBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void sendNotification(String nam) {

        String textTitle = "EMAIL SENT !";
        String textContent = "Dear "+ nam + ", Verification Email has been sent to your email... Thanks!";

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


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}