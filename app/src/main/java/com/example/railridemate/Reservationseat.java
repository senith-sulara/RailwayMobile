package com.example.railridemate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.railridemate.Utils.NotificationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Reservationseat extends AppCompatActivity {
    private DatabaseReference seatsRef;
    private DatabaseReference mDatabase;
    private Handler handler;
    private final int REFRESH_DELAY = 200;
    private int bookedSeatCount = 0;
    private int bookingCount =0;
    private final int SEAT_COST = 1200;
    private StringBuilder bookedSeatList = new StringBuilder();
    private TextView bookedSeatListView, stationN;

    private Button seatA,seatB,seatC,seatD;

    private ImageView home, msg,profile,shc;

    private NotificationHelper notificationHelper;
    private ImageButton bakbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservationseat);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        seatsRef = database.getReference("seatsA");
        // Initialize handler
        handler = new Handler();
        bookedSeatListView = findViewById(R.id.bookedSeatList);
        // Attach click listeners to checkboxes
        attachCheckboxListeners();
        // Schedule periodic refresh
        scheduleSeatStatusRefresh();
        seatB = findViewById(R.id.buttonB);
        seatC = findViewById(R.id.buttonC);
        seatD = findViewById(R.id.buttonD);
        stationN = findViewById(R.id.stview);
        bakbtn = findViewById(R.id.imageButtonbak);

        notificationHelper = new NotificationHelper(Reservationseat.this);
// Retrieve user key from intent
        String userKey = getIntent().getStringExtra("userKey");
        String stationfrom = getIntent().getStringExtra("from");

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reservationseat.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reservationseat.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reservationseat.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reservationseat.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        stationN.setText(stationfrom);
        Button continueButton = findViewById(R.id.continueButton);

        // Set an onClickListener to the "Continue" button
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the payment activity
                Intent intent = new Intent(Reservationseat.this, Passenger.class);
                startActivity(intent);
            }
        });

        seatB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the payment activity
                Intent intent = new Intent(Reservationseat.this, ReservationSeatB.class);
                startActivity(intent);
            }
        });
        seatC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the payment activity
                Intent intent = new Intent(Reservationseat.this, ReservationSeatC.class);
               startActivity(intent);
            }
        });
        seatD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the payment activity
              /*  Intent intent = new Intent(Reservationseat.this, ReservationSeatD.class);
                startActivity(intent);*/
            }
        });

        bakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reservationseat.this, Booking.class);
                intent.putExtra("userKey", userKey);
                startActivity(intent);
            }
        });

        Query bookingsQuery = mDatabase.child("usersC").child(userKey).child("bookings");
        bookingsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    bookingCount = (int) dataSnapshot.getChildrenCount(); // Get the count of bookings
                    Log.d("BookingCount", "Booking count for user " + userKey + ": " + bookingCount);
                    // You can use bookingCount as needed, maybe display it in your UI or perform some other actions
                } else {
                    Log.d("BookingCount", "No bookings found for user " + userKey);
                    bookingCount = 0; // Set bookingCount to 0 if no bookings found
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Log.e("BookingCount", "Error getting booking count for user " + userKey, databaseError.toException());
            }
        });

    }


    private void attachCheckboxListeners() {
        // Example: Listen for changes in Seat 1 checkbox
        CheckBox seat1 = findViewById(R.id.seat1);
        seat1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat1", isChecked);
        });

        CheckBox seat2 = findViewById(R.id.seat2);
        seat2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat2", isChecked);
        });

        CheckBox seat3 = findViewById(R.id.seat3);
        seat3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat3", isChecked);
        });

        CheckBox seat4 = findViewById(R.id.seat4);
        seat4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat4", isChecked);
        });

        CheckBox seat5 = findViewById(R.id.seat5);
        seat5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat5", isChecked);
        });

        CheckBox seat6 = findViewById(R.id.seat6);
        seat6.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat6", isChecked);
        });

        CheckBox seat7 = findViewById(R.id.seat7);
        seat7.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat7", isChecked);
        });

        CheckBox seat8 = findViewById(R.id.seat8);
        seat8.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat8", isChecked);
        });

        CheckBox seat9 = findViewById(R.id.seat9);
        seat9.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat9", isChecked);
        });

        CheckBox seat10 = findViewById(R.id.seat10);
        seat10.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat10", isChecked);
        });

        CheckBox seat11 = findViewById(R.id.seat11);
        seat11.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat11", isChecked);
        });

        CheckBox seat12 = findViewById(R.id.seat12);
        seat12.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat12", isChecked);
        });
        CheckBox seat13 = findViewById(R.id.seat13);
        seat13.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat13", isChecked);
        });
        CheckBox seat14 = findViewById(R.id.seat14);
        seat14.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat14", isChecked);
        });
        CheckBox seat15 = findViewById(R.id.seat15);
        seat15.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat15", isChecked);
        });
        CheckBox seat16 = findViewById(R.id.seat16);
        seat16.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat16", isChecked);
        });
        CheckBox seat17 = findViewById(R.id.seat17);
        seat17.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat17", isChecked);
        });
        CheckBox seat18 = findViewById(R.id.seat18);
        seat18.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat18", isChecked);
        });
        CheckBox seat19 = findViewById(R.id.seat19);
        seat19.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat19", isChecked);
        });
        CheckBox seat20 = findViewById(R.id.seat20);
        seat20.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSeatStatus("seat20", isChecked);
        });
        // Fetch and update seat statuses from Firebase
        fetchAndUpdateSeatStatus("seat1", seat1);
        fetchAndUpdateSeatStatus("seat2", seat2);
        fetchAndUpdateSeatStatus("seat3", seat3);
        fetchAndUpdateSeatStatus("seat4", seat4);
        fetchAndUpdateSeatStatus("seat5", seat5);
        fetchAndUpdateSeatStatus("seat6", seat6);
        fetchAndUpdateSeatStatus("seat7", seat7);
        fetchAndUpdateSeatStatus("seat8", seat8);
        fetchAndUpdateSeatStatus("seat9", seat9);
        fetchAndUpdateSeatStatus("seat10", seat10);
        fetchAndUpdateSeatStatus("seat11", seat11);
        fetchAndUpdateSeatStatus("seat12", seat12);
        fetchAndUpdateSeatStatus("seat13", seat13);
        fetchAndUpdateSeatStatus("seat14", seat14);
        fetchAndUpdateSeatStatus("seat15", seat15);
        fetchAndUpdateSeatStatus("seat16", seat16);
        fetchAndUpdateSeatStatus("seat17", seat17);
        fetchAndUpdateSeatStatus("seat18", seat18);
        fetchAndUpdateSeatStatus("seat19", seat19);
        fetchAndUpdateSeatStatus("seat20", seat20);

    }
    private void scheduleSeatStatusRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Fetch and update seat statuses from Firebase periodically
                fetchAndUpdateSeatStatus("seat1", findViewById(R.id.seat1));
                fetchAndUpdateSeatStatus("seat2", findViewById(R.id.seat2));
                fetchAndUpdateSeatStatus("seat3", findViewById(R.id.seat3));
                fetchAndUpdateSeatStatus("seat4", findViewById(R.id.seat4));
                fetchAndUpdateSeatStatus("seat5", findViewById(R.id.seat5));
                fetchAndUpdateSeatStatus("seat6", findViewById(R.id.seat6));
                fetchAndUpdateSeatStatus("seat7", findViewById(R.id.seat7));
                fetchAndUpdateSeatStatus("seat8", findViewById(R.id.seat8));
                fetchAndUpdateSeatStatus("seat9", findViewById(R.id.seat9));
                fetchAndUpdateSeatStatus("seat10", findViewById(R.id.seat10));
                fetchAndUpdateSeatStatus("seat11", findViewById(R.id.seat11));
                fetchAndUpdateSeatStatus("seat12", findViewById(R.id.seat12));
                fetchAndUpdateSeatStatus("seat13", findViewById(R.id.seat13));
                fetchAndUpdateSeatStatus("seat14", findViewById(R.id.seat14));
                fetchAndUpdateSeatStatus("seat15", findViewById(R.id.seat15));
                fetchAndUpdateSeatStatus("seat16", findViewById(R.id.seat16));
                fetchAndUpdateSeatStatus("seat17", findViewById(R.id.seat17));
                fetchAndUpdateSeatStatus("seat18", findViewById(R.id.seat18));
                fetchAndUpdateSeatStatus("seat19", findViewById(R.id.seat19));
                fetchAndUpdateSeatStatus("seat20", findViewById(R.id.seat20));

                // Calculate and display total amount
                // Calculate and display total amount
                int totalAmount = bookedSeatCount * SEAT_COST;
                Log.d("TotalAmount", "Total Amount: " + totalAmount);
               // Display the total amount multiplied by 1200
                TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);
              ////  totalAmountTextView.setText("Total Amounts: " + totalAmount);
                // Schedule next refresh
                handler.postDelayed(this, REFRESH_DELAY);
            }
        }, REFRESH_DELAY);
    }
    private void fetchAndUpdateSeatStatus(String seatId, CheckBox checkBox) {
        seatsRef.child(seatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child("status").getValue(String.class);
                    String previousStatus = ""; // Store previous status
                    if (checkBox.isChecked()) {
                        previousStatus = "booked"; // Assuming the checkbox is checked when the seat is booked
                    }
                    if (status != null && status.equals("booked")) {
                        if (!previousStatus.equals("booked")) {
                            bookedSeatCount++; // Seat is newly booked
                        }
                    } else {
                        if (previousStatus.equals("booked")) {
                            bookedSeatCount--; // Seat is unbooked
                        }
                    }
                    String bookingTime = snapshot.child("bookingTime").getValue(String.class);
                    if (status != null && bookingTime != null && status != "unavailable") {
                        Log.d("SeatStatus", "Seat ID: " + seatId + ", Status: " + status);
                        updateCheckboxAppearance(checkBox, status, bookingTime);
                    } else {
                        Log.e("SeatStatus", "Invalid data for seat ID: " + seatId);
                    }
                } else {
                    Log.d("SeatStatus", "Snapshot does not exist for seat ID: " + seatId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SeatStatus", "Error fetching seat status for seat ID: " + seatId, error.toException());
                // Handle onCancelled event if needed
            }
        });
    }

//    private void fetchAndUpdateSeatStatus(String seatId, CheckBox checkBox) {
//        seatsRef.child(seatId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                  //  String status = snapshot.child("status").getValue(String.class);
//                    String status = snapshot.child("status").getValue(String.class);
//                    if (status != null && status.equals("booked")) {
//                        bookedSeatCount++;
//                    }
//                    String bookingTime = snapshot.child("bookingTime").getValue(String.class);   if (status != null && bookingTime != null) {
//                        Log.d("SeatStatus", "Seat ID: " + seatId + ", Status: " + status);
//                        updateCheckboxAppearance(checkBox, status, bookingTime);
//                    } else {
//                        Log.e("SeatStatus", "Invalid data for seat ID: " + seatId);
//                    }
//                } else {
//                    Log.d("SeatStatus", "Snapshot does not exist for seat ID: " + seatId);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("SeatStatus", "Error fetching seat status for seat ID: " + seatId, error.toException());
//                // Handle onCancelled event if needed
//            }
//        });
//    }

    private void updateSeatStatus(String seatId, boolean isChecked) {
        String status = isChecked ? "booked" : "available"; // Update status based on checkbox state
        String bookingTime = isChecked ? String.valueOf(System.currentTimeMillis()) : ""; // Store booking time if booked
        seatsRef.child(seatId).child("status").setValue(status);
        seatsRef.child(seatId).child("bookingTime").setValue(bookingTime)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Reservationseat.this, "Seat status updated successfully", Toast.LENGTH_SHORT).show();
                        CheckBox checkBox = findViewById(getResources().getIdentifier(seatId, "id", getPackageName()));
                        if (checkBox != null) {
                            updateCheckboxAppearance(checkBox, status, bookingTime);

                            // Update booked seat list and total amount
                            if (isChecked) {
                                bookedSeatCount++;
                                bookedSeatList.append(seatId).append(", ");
                            } else {
                                bookedSeatCount--;
                                bookedSeatList.delete(bookedSeatList.indexOf(seatId), bookedSeatList.indexOf(seatId) + seatId.length() + 2);
                            }
                            int totalAmount = bookedSeatCount * SEAT_COST; // Calculate total amount
                            int totnet = 0;
                            TextView totalnetTextView = findViewById(R.id.netAmountTextView);
                            TextView discountTextView = findViewById(R.id.discountAmountTextView);
                            // Apply discount if booking count is more than 5
                            if (bookingCount > 5) {
                                // Calculate discounted amount
                                double discountedAmount = totalAmount * 0.8; // 20% discount
                                Log.d("DiscountedAmount", "Discounted Amount: " + discountedAmount);
                                totnet = (int) discountedAmount; // Convert back to integer

                                // Display total net amount and discount
                                totalnetTextView.setVisibility(View.VISIBLE);
                                discountTextView.setVisibility(View.VISIBLE);
                                totalnetTextView.setText("Total Net Amount: " + totnet); // Display total net amount
                                discountTextView.setText("Discount Amount: " + (totalAmount - totnet)); // Display discount amount
                                notificationHelper.sendNotification("Offer Alert", "Congratulations You have won a 20% discount!");

                            }else {
                                // Hide total net amount and discount
                                totalnetTextView.setVisibility(View.GONE);
                                discountTextView.setVisibility(View.GONE);
                            }
                            TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);


                            totalAmountTextView.setText("Total Gross Amount: " + totalAmount); // Display total amount
                            totalnetTextView.setText("Total Net Amount: " + totnet); // Display total net amount


                            bookedSeatListView.setText("Your Seats: " + bookedSeatList.toString());
                        } else {
                            Log.e("Reservationseat", "Checkbox with ID " + seatId + " not found.");
                        }
                    } else {
                        Toast.makeText(Reservationseat.this, "Failed to update seat status", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void updateCheckboxAppearance(CheckBox checkBox, String status, String bookingTime) {
        if (checkBox == null) {
            Log.e("Reservationseat", "Checkbox is null");
            return;
        }

        switch (status) {
            case "booked":
                checkBox.setChecked(true);
                long currentTime = System.currentTimeMillis();
                if (bookingTime == null) {
                    Log.e("Reservationseat", "Booking time is null");
                    return;
                }
                long timeDifference = currentTime - Long.parseLong(bookingTime);
                long timeLimit = 60 * 1000; // 1 minute in milliseconds
                if (timeDifference < timeLimit) {
                    // Booking expired
                    checkBox.setEnabled(true);
                    checkBox.setBackgroundColor(Color.GRAY);
                } else {
                    // Booking still valid
                    checkBox.setEnabled(false);
                    checkBox.setBackgroundColor(Color.WHITE);
                }
                break;
            case "available":
                checkBox.setChecked(false);
                checkBox.setEnabled(true);
                checkBox.setBackgroundColor(Color.GREEN);
                break;
            case "unavailable":
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
                checkBox.setBackgroundColor(Color.RED);
                break;
            default:
                break;
        }
    }
    // Override onDestroy() to remove callbacks from handler
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}