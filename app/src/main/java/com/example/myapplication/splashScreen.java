package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splashScreen.this,loginPage.class);
                startActivity(intent);
                finish();
            }
        },4000);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}