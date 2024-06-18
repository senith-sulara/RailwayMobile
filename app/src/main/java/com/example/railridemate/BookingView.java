package com.example.railridemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.railridemate.Adapters.BookingAdapter;
import com.example.railridemate.Adapters.BookingPagerAdapter;
import com.example.railridemate.models.BookingModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingView extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView listViewBookings;
    private List<BookingModel> bookingList;
    private RecyclerView recyclerViewBookings;
    private ImageView home, msg,profile,shc;

    private BookingAdapter bookingAdapter;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingview);

//        // Initialize Firebase Database
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        // Retrieve user key from intent
//        String userKey = getIntent().getStringExtra("userKey");
//
//        // Initialize views
//        listViewBookings = findViewById(R.id.listViewBookings);
//        Button button1 = findViewById(R.id.button1);
//        Button button2 = findViewById(R.id.button2);
//        Button button3 = findViewById(R.id.button3);
//
//        // Initialize booking list
//        bookingList = new ArrayList<>();
//
//        // Initialize adapter
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
//        listViewBookings.setAdapter(adapter);
//
//        // Query to retrieve bookings for the user
//        Query bookingsQuery = mDatabase.child("usersC").child(userKey).child("bookings");
//
//
//        listViewBookings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Get the selected booking
//                BookingModel selectedBooking = bookingList.get(position);
//
//                // Create an Intent to navigate to the reservation page
//                Intent intent = new Intent(BookingView.this, Reservationseat.class);
//
//                // Pass the necessary data to the reservation page
//                intent.putExtra("from", selectedBooking.getFrom());
//                intent.putExtra("userKey", userKey);
//                // You can pass additional data here based on your requirements
//
//                // Start the reservation activity
//                startActivity(intent);
//            }
//        });
//
//
//        // Attach a listener to read the data at our posts reference
//        bookingsQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                bookingList.clear(); // Clear the list before adding new bookings
//                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
//                    BookingModel booking = bookingSnapshot.getValue(BookingModel.class);
//                    if (booking != null) {
//                        bookingList.add(booking); // Add booking to the list
//                    }
//                }
//                displayAllBookings(); // Display all bookings initially
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle database error
//                Toast.makeText(BookingView.this, "Failed to retrieve bookings: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Set OnClickListener for button1 (All)
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                displayAllBookings(); // Display all bookings
//            }
//        });
//
//        // Set OnClickListener for button2 (Return)
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                filterReturnOptionTrueBookings(); // Filter return option true bookings
//            }
//        });
//
//        // Set OnClickListener for button3 (One-way)
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                filterReturnOptionFalseBookings(); // Filter return option false bookings
//            }
//        });
//    }
//
//    // Method to display all bookings
//    private void displayAllBookings() {
//        adapter.clear(); // Clear the adapter
//        for (BookingModel booking : bookingList) {
//            adapter.add(formatBookingDetails(booking)); // Add all bookings
//        }
//    }
//
//    // Method to filter returnOption true bookings
//    private void filterReturnOptionTrueBookings() {
//        adapter.clear(); // Clear the adapter
//        for (BookingModel booking : bookingList) {
//            if (booking.isReturnOption()) {
//                adapter.add(formatBookingDetails(booking)); // Add return option true bookings
//            }
//        }
//    }
//
//    // Method to filter returnOption false bookings
//    private void filterReturnOptionFalseBookings() {
//        adapter.clear(); // Clear the adapter
//        for (BookingModel booking : bookingList) {
//            if (!booking.isReturnOption()) {
//                adapter.add(formatBookingDetails(booking)); // Add return option false bookings
//            }
//        }
//    }
//
//    // Method to format booking details
//    private String formatBookingDetails(BookingModel booking) {
//        return "From: " + booking.getFrom() +
//                "\nTo: " + booking.getTo() +
//                "\nDate: " + booking.getDate() +
//                "\nPassengers: " + booking.getPassengers() +
//                "\nReturn: " + (booking.isReturnOption() ? "Yes" : "No");
//    }
//}
// Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve user key from intent
        String userKey = getIntent().getStringExtra("userKey");

        // Initialize RecyclerView and its adapter
        recyclerViewBookings = findViewById(R.id.listViewBookings);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(this, bookingList, userKey);
        recyclerViewBookings.setAdapter(bookingAdapter);


        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingView.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingView.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingView.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingView.this, TrainSchedule.class);
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
                bookingAdapter.notifyDataSetChanged(); // Notify adapter about data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(BookingView.this, "Failed to retrieve bookings: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}