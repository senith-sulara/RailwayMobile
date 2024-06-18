package com.example.railridemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class UserLocationActivity extends AppCompatActivity {
    private ImageView home, msg,profile,shc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        String userKey = getIntent().getStringExtra("userKey");


        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLocationActivity.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLocationActivity.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLocationActivity.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLocationActivity.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });

        Fragment fragment = new MapsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layoutuser, fragment)
                .commit();
    }
}