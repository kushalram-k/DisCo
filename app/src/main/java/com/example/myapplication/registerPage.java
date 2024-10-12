package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class registerPage extends AppCompatActivity {
    TextView tv9;
    Button sendOTPbtn;
    EditText u_name,em,pass,conpass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    // Generate 4-digit OTP function
    private String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); // Generate 4-digit OTP
        return String.valueOf(otp);
    }
    private void sendOTPEmail(String recipientEmail, String otp) {
        final String username = "leelajogeendarsaireddi@gmail.com"; // Your Gmail address
        final String password = "myhfbtjovoxwqjnu"; // Your Gmail app password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP for Registration");
            message.setText("Your OTP is: " + otp);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(registerPage.this, "OTP sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(registerPage.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);

        tv9=findViewById(R.id.textView9);
        tv9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerPage.this, loginPage.class);
                startActivity(intent);
            }
        });

        u_name=findViewById(R.id.editTextText4);
        em=findViewById(R.id.editTextText5);
        pass=findViewById(R.id.editTextText6);
        conpass=findViewById(R.id.editTextText7);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        sendOTPbtn = findViewById(R.id.button2);


        sendOTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username=u_name.getText().toString();
                String email=em.getText().toString();
                String password=pass.getText().toString();
                String cpassword=conpass.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword)){

                    Toast.makeText(registerPage.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                }else  if (!email.matches(emailPattern)){

                    em.setError("Type A Valid Email Here");
                }else if (password.length()<6){

                    pass.setError("Password Must Be 6 Characters Or More");
                }else if (!password.equals(cpassword)){
                    pass.setError("The Password Doesn't Match");
                }else{
                    // Generate OTP
                    String otp = generateOTP();
                    // Send OTP via email
                    sendOTPEmail(email, otp);

                    // Navigate to OTP page, pass the OTP along with other details
                    Intent intent = new Intent(registerPage.this, otp_page.class);
                    intent.putExtra("otp", otp); // Pass the generated OTP
                    intent.putExtra("email", email);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}