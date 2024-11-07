package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileWriter;
import java.io.IOException;


public class otp_page extends AppCompatActivity {
    private static final String TAG = "otp_page";
    EditText otpDigit1, otpDigit2, otpDigit3, otpDigit4;
    Button btnSubmitOtp;
    String sentOtp, email, username, password;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        otpDigit1 = findViewById(R.id.otp_digit_1);
        otpDigit2 = findViewById(R.id.otp_digit_2);
        otpDigit3 = findViewById(R.id.otp_digit_3);
        otpDigit4 = findViewById(R.id.otp_digit_4);
        btnSubmitOtp = findViewById(R.id.btn_submit_otp);

        // Retrieve data passed from registerPage
        sentOtp = getIntent().getStringExtra("otp");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        // Automatically move to the next digit after input
        otpDigit1.addTextChangedListener(new GenericTextWatcher(otpDigit1, otpDigit2));
        otpDigit2.addTextChangedListener(new GenericTextWatcher(otpDigit2, otpDigit3));
        otpDigit3.addTextChangedListener(new GenericTextWatcher(otpDigit3, otpDigit4));
        otpDigit4.addTextChangedListener(new GenericTextWatcher(otpDigit4, null));

        btnSubmitOtp.setOnClickListener(v -> {
            String enteredOtp = otpDigit1.getText().toString() +
                    otpDigit2.getText().toString() +
                    otpDigit3.getText().toString() +
                    otpDigit4.getText().toString();

            if (enteredOtp.equals(sentOtp)) {
                Log.d(TAG, "OTP verified successfully");
                // OTP is correct, proceed with authentication
                authenticateUser();
            } else {
                Log.d(TAG, "Invalid OTP entered");
                Toast.makeText(otp_page.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void authenticateUser() {
        Log.d(TAG, "Attempting to authenticate user");
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User authenticated successfully");
                            // User is already registered, navigate to GroupFragment
                            navigateToGroupFragment();
                        } else {
                            Log.d(TAG, "User not found, proceeding with registration");
                            // User is not registered, proceed with registration
                            registerUser();
                        }
                    }
                });
    }

    private void registerUser() {
        Log.d(TAG, "Attempting to register user");
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User registered successfully");
                            DatabaseReference reference = database.getReference().child("users").child(auth.getUid());
                            Users users = new Users(auth.getUid(), username, email, password);
                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User data saved to database");

                                        navigateToGroupFragment();
                                    } else {
                                        Log.e(TAG, "Error saving user data to database", task.getException());
                                        Toast.makeText(otp_page.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Log.d(TAG, "User already exists");
                                Toast.makeText(otp_page.this, "User already exists. Please login.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "Error in authentication", task.getException());
                                Toast.makeText(otp_page.this, "Error in authentication", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void navigateToGroupFragment() {
        Log.d(TAG, "Navigating to GroupFragment");
        Intent intent = new Intent(otp_page.this, mainPage.class); // Ensure this is the correct activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private class GenericTextWatcher implements TextWatcher {
        private final EditText currentView;
        private final EditText nextView;

        public GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }
    }
}
