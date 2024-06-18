package com.example.railridemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ApplyOffer extends AppCompatActivity {
    private ImageView home, msg,profile,shc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applyoffer);

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);
        String userKey = getIntent().getStringExtra("userKey");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyOffer.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyOffer.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyOffer.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyOffer.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });

    }
}