package com.example.railridemate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class BarcodeGeneratorActivity extends AppCompatActivity {
    private DatabaseReference seatsRef;
    private ImageView imageViewBarcode;
    private TextView textViewSelectedDate;
    private DatabaseReference passengerRef;
    private TextView FnameTextView;
    private TextView PassTextView;
    private DatabaseReference bookingsRef;
    private ImageView home, msg,profile,shc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcodegeneratoractivity);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        seatsRef = database.getReference("seatsA");
        passengerRef = database.getReference("passenger");
        FnameTextView = findViewById(R.id.Fname);
        PassTextView = findViewById(R.id.passid);
        bookingsRef = database.getReference("usersC");

        String userKey = getIntent().getStringExtra("userKey");

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarcodeGeneratorActivity.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarcodeGeneratorActivity.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarcodeGeneratorActivity.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarcodeGeneratorActivity.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });

        // Initialize views
        imageViewBarcode = findViewById(R.id.imageViewBarcode);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);

        // Get the selected date from the intent extras
        String selectedDate = getIntent().getStringExtra("selectedDate");
        textViewSelectedDate.setText(selectedDate); // Display selected date

        // Fetch booked seat information from Firebase
        fetchBookedSeatsFromFirebase();
        fetchLastInsertedPassengerData();
        fetchLastBookingData();
    }
    private void fetchLastBookingData() {
        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through each user's bookings
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot bookingSnapshot : userSnapshot.child("bookings").getChildren()) {
                        String from = bookingSnapshot.child("from").getValue(String.class);
                        String to = bookingSnapshot.child("to").getValue(String.class);
                        String time = bookingSnapshot.child("date").getValue(String.class);
                        // Update TextViews with the retrieved values
                        TextView fromTextView = findViewById(R.id.fromid);
                        TextView toTextView = findViewById(R.id.tto);
                        TextView timeTextView = findViewById(R.id.depaturedateid);
                        fromTextView.setText(from);
                        toTextView.setText(to);
                        timeTextView.setText(time);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void fetchBookedSeatsFromFirebase() {
        seatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder barcodeContent = new StringBuilder();
                for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                    String seatId = seatSnapshot.getKey();
                    String status = seatSnapshot.child("status").getValue(String.class);
                    if ("booked".equals(status)) {
                        barcodeContent.append(seatId).append(", ");
                    }
                }

                // Generate barcode using the fetched seat information
                generateBarcode(barcodeContent.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void fetchLastInsertedPassengerData() {
        passengerRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot passengerSnapshot : dataSnapshot.getChildren()) {
                    String firstName = passengerSnapshot.child("firstName").getValue(String.class);
                    String passName = passengerSnapshot.child("firstName").getValue(String.class);
                    // Update Fname TextView with the retrieved first name
                    FnameTextView.setText(firstName);
                    PassTextView.setText(passName);
                    // Assuming there's only one entry, we can break out of the loop
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void generateBarcode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            // Display generated barcode
            imageViewBarcode.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}