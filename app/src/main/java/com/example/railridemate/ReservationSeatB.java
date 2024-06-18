package com.example.railridemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReservationSeatB extends AppCompatActivity {
    private DatabaseReference seatsRef;
    private Handler handler;
    private final int REFRESH_DELAY = 200;
    private int bookedSeatCount = 0;
    private final int SEAT_COST = 1200;
    private StringBuilder bookedSeatList = new StringBuilder();
    private TextView bookedSeatListView;
    private Button seatA,seatB,seatC,seatD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_seat_b);
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        seatsRef = database.getReference("seatsB");
        // Initialize handler
        handler = new Handler();
        bookedSeatListView = findViewById(R.id.bookedSeatList);
        // Attach click listeners to checkboxes
        attachCheckboxListeners();
        // Schedule periodic refresh
        scheduleSeatStatusRefresh();
        seatA = findViewById(R.id.buttonA);
        seatC = findViewById(R.id.buttonC);
        seatD = findViewById(R.id.buttonD);

        Button continueButton = findViewById(R.id.continueButton);

        // Set an onClickListener to the "Continue" button
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the payment activity
                Intent intent = new Intent(ReservationSeatB.this, Passenger.class);
                startActivity(intent);
            }
        });

        seatA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the payment activity
                Intent intent = new Intent(ReservationSeatB.this, Reservationseat.class);
                startActivity(intent);
            }
        });
        seatC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the payment activity
               Intent intent = new Intent(ReservationSeatB.this, ReservationSeatC.class);
               startActivity(intent);
            }
        });
        seatD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the payment activity
               Intent intent = new Intent(ReservationSeatB.this, ReservationSeatD.class);
                startActivity(intent);
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
                    //  String status = snapshot.child("status").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);
                    if (status != null && status.equals("booked")) {
                        bookedSeatCount++;
                    }
                    String bookingTime = snapshot.child("bookingTime").getValue(String.class);   if (status != null && bookingTime != null) {
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

    private void updateSeatStatus(String seatId, boolean isChecked) {
        String status = isChecked ? "booked" : "available"; // Update status based on checkbox state
        String bookingTime = isChecked ? String.valueOf(System.currentTimeMillis()) : ""; // Store booking time if booked
        seatsRef.child(seatId).child("status").setValue(status);
        seatsRef.child(seatId).child("bookingTime").setValue(bookingTime)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ReservationSeatB.this, "Seat status updated successfully", Toast.LENGTH_SHORT).show();
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
                            TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);
                            totalAmountTextView.setText("Total Amount: " + totalAmount); // Display total amount

                            bookedSeatListView.setText("Booked Seats: " + bookedSeatList.toString());
                        } else {
                            Log.e("Reservationseat", "Checkbox with ID " + seatId + " not found.");
                        }
                    } else {
                        Toast.makeText(ReservationSeatB.this, "Failed to update seat status", Toast.LENGTH_SHORT).show();
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