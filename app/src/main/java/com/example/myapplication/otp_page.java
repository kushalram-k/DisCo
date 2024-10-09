package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class otp_page extends AppCompatActivity {
    Button homebtn;
    EditText otpDigit1, otpDigit2, otpDigit3, otpDigit4;
    Button btnSubmitOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);

        otpDigit1 = findViewById(R.id.otp_digit_1);
        otpDigit2 = findViewById(R.id.otp_digit_2);
        otpDigit3 = findViewById(R.id.otp_digit_3);
        otpDigit4 = findViewById(R.id.otp_digit_4);
        btnSubmitOtp = findViewById(R.id.btn_submit_otp);

        // Automatically move to the next digit after input
        otpDigit1.addTextChangedListener(new GenericTextWatcher(otpDigit1, otpDigit2));
        otpDigit2.addTextChangedListener(new GenericTextWatcher(otpDigit2, otpDigit3));
        otpDigit3.addTextChangedListener(new GenericTextWatcher(otpDigit3, otpDigit4));

        btnSubmitOtp.setOnClickListener(v -> {
            String otp = otpDigit1.getText().toString() +
                    otpDigit2.getText().toString() +
                    otpDigit3.getText().toString() +
                    otpDigit4.getText().toString();

            // Verify OTP here
        });


        //Going to home page
        homebtn = findViewById(R.id.btn_submit_otp);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch OTP page activity
                Intent intent = new Intent(otp_page.this, mainPage.class);
                startActivity(intent);
            }
        });


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
            if (editable.length() == 1) {
                nextView.requestFocus();
            }
        }
    }
}