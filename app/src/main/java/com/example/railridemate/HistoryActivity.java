package com.example.railridemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.railridemate.Adapters.BookingAdapter;
import com.example.railridemate.Adapters.HistoryAdapter;
import com.example.railridemate.models.BookingModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView listViewBookings;
    private List<BookingModel> bookingList;
    private RecyclerView recyclerViewBookings;
    private ImageView home, msg,profile,shc;

    private HistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve user key from intent
        String userKey = getIntent().getStringExtra("userKey");

        // Initialize RecyclerView and its adapter
        recyclerViewBookings = findViewById(R.id.recyViewBookings);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, bookingList);
        recyclerViewBookings.setAdapter(historyAdapter);


        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });

        // Query to retrieve bookings for the user
        Query bookingsQuery = mDatabase.child("usersC").child(userKey).child("bookings");

        // Attach a listener to read the data at our posts reference
        bookingsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear(); // Clear the list before adding new bookings
                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                    BookingModel booking = bookingSnapshot.getValue(BookingModel.class);
                    if (booking != null) {
                        bookingList.add(booking); // Add booking to the list
                    }
                }
                historyAdapter.notifyDataSetChanged(); // Notify adapter about data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(HistoryActivity.this, "Failed to retrieve bookings: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}