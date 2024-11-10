package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splashScreen extends AppCompatActivity {
    TextView tv2,tv3,tv4,tv5;
    ImageView iv;
    Animation leftAnim,rightAnim,topAnim,bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        tv2=findViewById(R.id.textView2);
        tv3=findViewById(R.id.textView3);
        tv4=findViewById(R.id.textView4);
        tv5=findViewById(R.id.textView5);
        iv=findViewById(R.id.imageView);
        leftAnim= AnimationUtils.loadAnimation(this,R.anim.left_animation);
        topAnim=AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        rightAnim= AnimationUtils.loadAnimation(this,R.anim.right_animation);
        tv2.setAnimation(leftAnim);
        tv3.setAnimation(topAnim);
        tv4.setAnimation(bottomAnim);
        tv5.setAnimation(rightAnim);
        iv.setAnimation(topAnim);

        String senderUserID="Blank";
        MyDatabaseHelper myDB=new MyDatabaseHelper(splashScreen.this);
        Cursor curs=myDB.readUserIdFromDb();
//        curs.moveToFirst();
//        senderUserID=curs.getString(1);
        if(curs.getCount()==0){
            Toast.makeText(splashScreen.this, "No userId in DB", Toast.LENGTH_SHORT).show();
        }else{
            curs.moveToFirst();
            senderUserID=curs.getString(1);
        }
        Log.d("splashscreen",senderUserID);
        String finalSenderUserID = senderUserID;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                Log.d("splashscreen",finalSenderUserID);
                if(finalSenderUserID.equals("Blank")){
                    intent=new Intent(splashScreen.this,registerPage.class);

                }else{
                    intent=new Intent(splashScreen.this,mainPage.class);
                }
                startActivity(intent);
                finish();
            }
        },2000);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}