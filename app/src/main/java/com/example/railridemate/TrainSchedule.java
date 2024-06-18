package com.example.railridemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.railridemate.Adapters.ScheduleAdapter;
import com.example.railridemate.models.ScheduleModel;

import java.util.ArrayList;
import java.util.List;

public class TrainSchedule extends AppCompatActivity {

    private ImageView home, msg,profile,shc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_schedule);
        String userKey = getIntent().getStringExtra("userKey");


        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainSchedule.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainSchedule.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainSchedule.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainSchedule.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.trainschview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        List<ScheduleModel> dataList = new ArrayList<>();
        dataList.add(new ScheduleModel("Colombo - Kandy", "1005 - podi Manike", "Colombo", "Kandy", "05:55", "08:42"));
        dataList.add(new ScheduleModel("Colombo - Kandy", "1009 - Int Express", "Colombo", "Kandy", "07:00", "09:38"));
        dataList.add(new ScheduleModel("Colombo - Kandy", "1015 - Udarata Manike", "Colombo", "Kandy", "10:35", "14:00"));
        dataList.add(new ScheduleModel("Colombo - Kandy", "1008 - Special Train", "Colombo", "Kandy", "13:00", "16:34"));

        ScheduleAdapter adapter = new ScheduleAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }
}