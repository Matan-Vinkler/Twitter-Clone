package com.matan.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register2Activity extends AppCompatActivity {

    private String name, email, birthDate, uname, password;
    private FirebaseAuth mAuth;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        birthDate = getIntent().getStringExtra("BirthDate");

        EditText edtUName = findViewById(R.id.edt_uname);
        EditText edtPassword = findViewById(R.id.edt_password);

        Button btnFinish = findViewById(R.id.btn_finish);
        ImageButton btnBack = findViewById(R.id.btn_back);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register2Activity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = edtUName.getText().toString();
                password = edtPassword.getText().toString();

                if(!validateFields(uname, password)) {
                    Toast.makeText(Register2Activity.this, "Must fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                register(email, password);
            }
        });
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register2Activity.this, "User successfully created", Toast.LENGTH_SHORT).show();
                            nextPage(user);
                        } else {
                            // Check if the user exists
                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(Register2Activity.this, "You're already registered.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register2Activity.this, LoginActivity.class);
                                intent.putExtra("Email", email);
                                intent.putExtra("Password", password);
                                startActivity(intent);
                                finish();
                            }
                            catch (Exception e) {
                                e.printStackTrace();

                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Register2Activity.this, "Authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void nextPage(FirebaseUser user) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://twitter-clone-614bd-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference userRef = database.getReference("Users");

        userRef.child(user.getUid()).child("Username").setValue(uname);
        userRef.child(user.getUid()).child("Name").setValue(name);
        userRef.child(user.getUid()).child("BirthDate").setValue(birthDate);
        userRef.child(user.getUid()).child("Email").setValue(email);

        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("is_user_logged", true).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("DisplayName", name).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Username", uname).apply();

        Intent intent = new Intent(Register2Activity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // Automatically login
        }
    }

    private boolean validateFields(String name, String password) {
        if(name.equals("") || password.equals("")) {
            // User has not entered any data
            return false;
        }
        else {
            return true;
        }
    }
}