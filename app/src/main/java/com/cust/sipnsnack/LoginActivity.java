package com.cust.sipnsnack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import com.cust.sipnsnack.Admin.AdminView;
import com.cust.sipnsnack.Bikers.BikersView;
import com.cust.sipnsnack.Customers.CustomerView;
import com.cust.sipnsnack.ManagerDashboard.DashBoard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView goToSignup, forgotTV;
    TextInputEditText usernameET, passwordET;
    Button login_Button;
    LoadingDialog loadingDialog;
    private FirebaseDatabase mDatabase2 = FirebaseDatabase.getInstance();
    private FirebaseDatabase mDatabase3 = FirebaseDatabase.getInstance();
    private FirebaseDatabase mDatabase4 = FirebaseDatabase.getInstance();
    boolean flag1 = false;
    boolean flag2 = false;
    boolean flag3 = false;
    boolean flag4 = false;
    boolean emailFlag = false;
    String dbUsername, dbPassword, dbName, status, phoneNo, address, email;
    SharedPreferences mySharedPreferences;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        goToSignup = findViewById(R.id.goToSignup);

        loadingDialog = new LoadingDialog(LoginActivity.this);

        mAuth = FirebaseAuth.getInstance();

        usernameET = findViewById(R.id.login_email);
        passwordET = findViewById(R.id.login_password);
        forgotTV = findViewById(R.id.forgotPassword);

        login_Button = findViewById(R.id.logIn);


        goToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(it);
                finish();
            }
        });

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIN();
            }
        });

        forgotTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wipDialog();
            }
        });
    }
    public void logIN() {
        loadingDialog.startLoadingDialog();
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        mySharedPreferences = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();

        if (username.equals("admin@1.com") && password.equals("admin1")) {

            editor.putString("Status", "LoggedIn");
            editor.putString("User", "Manager");
            editor.apply();

            loadingDialog.dismissDialog();

            Intent it = new Intent(getApplicationContext(), DashBoard.class);
            startActivity(it);
        } else {

            if (username.isEmpty()) {
                loadingDialog.dismissDialog();
                usernameET.setError("Username is required !");
                usernameET.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                loadingDialog.dismissDialog();
                passwordET.setError("Password is required !");
                passwordET.requestFocus();
                return;
            }


            if (password.length() < 6) {
                loadingDialog.dismissDialog();
                passwordET.setError("Minimum password length is 6 !");
                passwordET.requestFocus();
                return;
            }


            FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        private DataSnapshot dataSnapshot;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            this.dataSnapshot = snapshot;

                            for(DataSnapshot DSP : dataSnapshot.getChildren()) {

                                dbUsername = DSP.child("Username").getValue().toString();
                                dbPassword = DSP.child("Password").getValue().toString();
                                status = DSP.child("Status").getValue().toString();
                                dbName = DSP.child("Name").getValue().toString();
                                phoneNo = DSP.child("PhoneNo").getValue().toString();
                                address = DSP.child("Address").getValue().toString();
                                email = DSP.child("Email").getValue().toString();

                                if (dbUsername.equals(username) && dbPassword.equals(password)) {
                                    if (!status.equals("Blocked")) {
                                        flag1 = true;
                                        dbName = DSP.child("Name").getValue().toString();
                                        break;
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sorry, " + dbName +
                                                "Your Account is Blocked by Manager.", Toast.LENGTH_SHORT).show();

                                        accBlockedDialog();
                                    }

                                }  else if (email.equals(username) && dbPassword.equals(password)) {
                                    if (!status.equals("Blocked")) {
                                        flag1 = true;
                                        dbName = DSP.child("Name").getValue().toString();
                                        break;
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sorry, " + dbName +
                                                "Your Account is Blocked by Manager.", Toast.LENGTH_SHORT).show();

                                        accBlockedDialog();
                                    }
                                }
                            }

                            if (flag1) {

                                System.out.println("INSIDE FLAG 1");

                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {

                                                    System.out.println("INSIDE IFFF");
                                                    // Sign in success, update UI with the signed-in user's information
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    String userId = user.getUid();

                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                                                DataSnapshot dataSnapshot;

                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                    this.dataSnapshot = snapshot;


                                                                    if (user.isEmailVerified()) {
                                                                        emailFlag = true;
                                                                        editor.putString("CustomerVerified", "true");
                                                                    } else {
                                                                        editor.putString("CustomerVerified", "false");
                                                                    }

                                                                    editor.putString("Status", "LoggedIn");
                                                                    editor.putString("User", "Customer");
                                                                    editor.putString("Username", dbUsername);
                                                                    editor.putString("Name", dbName);
                                                                    editor.putString("PhoneNo", phoneNo);
                                                                    editor.putString("Address", address);
                                                                    editor.putString("Password", dbPassword);
                                                                    editor.putString("Email", email);
                                                                    editor.apply();


                                                                    loadingDialog.dismissDialog();

                                                                    if (emailFlag) {
                                                                        Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                                                                        Intent it;
                                                                        it = new Intent(getApplicationContext(), CustomerView.class);
                                                                        startActivity(it);
                                                                    } else {
                                                                        Toast.makeText(LoginActivity.this, "Email not Verified.", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(getApplicationContext(), SendEmail.class);
                                                                        intent.putExtra("Email", email);
                                                                        startActivity(intent);
                                                                    }
                                                                    finish();


                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });

                                                } else {
                                                    mDatabase2.getReference().child("Users").child("Bikers")
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                                                DataSnapshot dataSnapshot;

                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    this.dataSnapshot = snapshot;

                                                                    for (DataSnapshot DSP : dataSnapshot.getChildren()) {

                                                                        dbUsername = DSP.child("Username").getValue().toString();
                                                                        dbPassword = DSP.child("Password").getValue().toString();

                                                                        if (dbUsername.equals(username) && dbPassword.equals(password)) {
                                                                            flag2 = true;
                                                                            dbName = DSP.child("Name").getValue().toString();
                                                                            break;
                                                                        }

                                                                    }

                                                                    if (flag2) {

                                                                        editor.putString("Status", "LoggedIn");
                                                                        editor.putString("User", "Biker");
                                                                        editor.putString("Username", username);
                                                                        editor.apply();

                                                                        loadingDialog.dismissDialog();
                                                                        Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();

                                                                        Intent it = new Intent(getApplicationContext(), BikersView.class);
                                                                        it.putExtra("BikerName", dbName);
                                                                        startActivity(it);
                                                                        finish();
                                                                    } else {
                                                                        mDatabase3.getReference().child("Users").child("Managers")
                                                                                .addListenerForSingleValueEvent(new ValueEventListener() {

                                                                                    DataSnapshot dataSnapshot;

                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                        this.dataSnapshot = snapshot;

                                                                                        for (DataSnapshot DSP : dataSnapshot.getChildren()) {

                                                                                            dbUsername = DSP.child("Username").getValue().toString();
                                                                                            dbPassword = DSP.child("Password").getValue().toString();

                                                                                            System.out.println("Manager : "+ dbUsername +"\n");

                                                                                            if (dbUsername.equals(username) && dbPassword.equals(password)) {
                                                                                                flag3 = true;
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                        if (flag3) {

                                                                                            editor.putString("Status", "LoggedIn");
                                                                                            editor.putString("User", "Manager");
                                                                                            editor.putString("Username", username);
                                                                                            editor.apply();

                                                                                            loadingDialog.dismissDialog();
                                                                                            Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();

                                                                                            Intent it = new Intent(getApplicationContext(), DashBoard.class);
                                                                                            startActivity(it);
                                                                                        } else {

                                                                                            mDatabase4.getReference().child("Users").child("Admin")
                                                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {

                                                                                                        DataSnapshot dataSnapshot;

                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                                            this.dataSnapshot = snapshot;


                                                                                                            dbUsername = dataSnapshot.child("Username").getValue().toString();
                                                                                                            dbPassword = dataSnapshot.child("Password").getValue().toString();

                                                                                                            System.out.println("Admin : "+ dbUsername +"\n");

                                                                                                            if (dbUsername.equals(username) && dbPassword.equals(password)) {
                                                                                                                flag4 = true;
                                                                                                            }

                                                                                                            if (flag4) {

                                                                                                                editor.putString("Status", "LoggedIn");
                                                                                                                editor.putString("User", "Admin");
                                                                                                                editor.putString("Username", username);
                                                                                                                editor.apply();

                                                                                                                loadingDialog.dismissDialog();
                                                                                                                Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();

                                                                                                                Intent it = new Intent(getApplicationContext(), AdminView.class);
                                                                                                                startActivity(it);
                                                                                                            } else {
                                                                                                                loadingDialog.dismissDialog();
                                                                                                                Toast.makeText(LoginActivity.this, "Error Occurred, TRY AGAIN ...", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

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

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                }
                                            }
                                        });

                            } else {
                                System.out.println("INSIDE BIKER ELSE");
                                mDatabase2.getReference().child("Users").child("Bikers")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {

                                            DataSnapshot dataSnapshot;

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                this.dataSnapshot = snapshot;

                                                for (DataSnapshot DSP : dataSnapshot.getChildren()) {

                                                    dbUsername = DSP.child("Username").getValue().toString();
                                                    dbPassword = DSP.child("Password").getValue().toString();

                                                    if (dbUsername.equals(username) && dbPassword.equals(password)) {
                                                        flag2 = true;
                                                        dbName = DSP.child("Name").getValue().toString();
                                                        break;
                                                    }

                                                }

                                                if (flag2) {

                                                    editor.putString("Status", "LoggedIn");
                                                    editor.putString("User", "Biker");
                                                    editor.putString("Username", username);
                                                    editor.apply();

                                                    loadingDialog.dismissDialog();
                                                    Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();

                                                    Intent it = new Intent(getApplicationContext(), BikersView.class);
                                                    it.putExtra("BikerName", dbName);
                                                    startActivity(it);
                                                    finish();
                                                } else {
                                                    mDatabase3.getReference().child("Users").child("Managers")
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                                                DataSnapshot dataSnapshot;

                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                    this.dataSnapshot = snapshot;

                                                                    for (DataSnapshot DSP : dataSnapshot.getChildren()) {

                                                                        dbUsername = DSP.child("Username").getValue().toString();
                                                                        dbPassword = DSP.child("Password").getValue().toString();

                                                                        System.out.println("Manager : "+ dbUsername +"\n");

                                                                        if (dbUsername.equals(username) && dbPassword.equals(password)) {
                                                                            flag3 = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (flag3) {

                                                                        editor.putString("Status", "LoggedIn");
                                                                        editor.putString("User", "Manager");
                                                                        editor.putString("Username", username);
                                                                        editor.apply();

                                                                        loadingDialog.dismissDialog();
                                                                        Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();

                                                                        Intent it = new Intent(getApplicationContext(), DashBoard.class);
                                                                        startActivity(it);
                                                                    } else {

                                                                        mDatabase4.getReference().child("Users").child("Admin")
                                                                                .addListenerForSingleValueEvent(new ValueEventListener() {

                                                                                    DataSnapshot dataSnapshot;

                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                        this.dataSnapshot = snapshot;


                                                                                        dbUsername = dataSnapshot.child("Username").getValue().toString();
                                                                                        dbPassword = dataSnapshot.child("Password").getValue().toString();

                                                                                        System.out.println("Admin : "+ dbUsername +"\n");

                                                                                        if (dbUsername.equals(username) && dbPassword.equals(password)) {
                                                                                            flag4 = true;
                                                                                        }

                                                                                        if (flag4) {

                                                                                            editor.putString("Status", "LoggedIn");
                                                                                            editor.putString("User", "Admin");
                                                                                            editor.putString("Username", username);
                                                                                            editor.apply();

                                                                                            loadingDialog.dismissDialog();
                                                                                            Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();

                                                                                            Intent it = new Intent(getApplicationContext(), AdminView.class);
                                                                                            startActivity(it);
                                                                                        } else {
                                                                                            loadingDialog.dismissDialog();
                                                                                            Toast.makeText(LoginActivity.this, "Invalid Credentials Provided ...", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

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

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

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

    public void accBlockedDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.acc_blocked_dialog, null);
        Button enterBTN = view.findViewById(R.id.enterBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        enterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
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