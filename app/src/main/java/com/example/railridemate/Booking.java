package com.example.railridemate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.railridemate.models.BookingModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Booking extends AppCompatActivity {

    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private DatabaseReference mDatabase;
    private EditText editTextDate;
    private Button buttonContinue, buttoncancel;
    private Switch switchReturn;
    private EditText editTextPassengers;
    private ImageView home, msg,profile,shc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Retrieve the user key
        String userKey = getIntent().getStringExtra("userKey");
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editTextDate = findViewById(R.id.editTextDate);
        buttonContinue = findViewById(R.id.buttonContinue);
        switchReturn = findViewById(R.id.switchReturn);
        editTextPassengers = findViewById(R.id.editTextPassengers);
        buttoncancel =findViewById(R.id.buttoncancel);

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });

        Button buttonLearnMore = findViewById(R.id.button2);
        buttonLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the activity for "Learn More"
                Intent intent = new Intent(Booking.this, StaySave.class);
                startActivity(intent);
            }
        });

        // Set click listener on the EditText to show DatePickerDialog
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // List of places for spinners
        List<String> places = new ArrayList<>(Arrays.asList("Kandy", "Colombo", "Anuradhapura"));

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, places);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerFrom.setAdapter(adapter);

        // When an item is selected in the 'From' spinner, update the 'To' spinner
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item
                String selectedPlace = spinnerFrom.getSelectedItem().toString();

                // Remove the selected item from the list for the 'To' spinner
                List<String> toPlaces = new ArrayList<>(places);
                toPlaces.remove(selectedPlace);

                // Update the 'To' spinner
                ArrayAdapter<String> toAdapter = new ArrayAdapter<>(Booking.this, android.R.layout.simple_spinner_item, toPlaces);
                toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTo.setAdapter(toAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking.this, MainActivity.class);
                intent.putExtra("userKey", userKey); // userKey is the key of the user whose bookings you want to view
                startActivity(intent);
            }
        });

        // Set click listener on Continue button
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Existing logic to get from, to, date, and returnOption
                String from = spinnerFrom.getSelectedItem().toString();
                String to = spinnerTo.getSelectedItem().toString();
                String date = editTextDate.getText().toString();
                boolean returnOption = switchReturn.isChecked();

                // Retrieve the passengers count
                int passengers = 0; // Default to 0
                try {
                    passengers = Integer.parseInt(editTextPassengers.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(Booking.this, "Please enter a valid number of passengers", Toast.LENGTH_SHORT).show();
                    return; // Prevent further execution if the number is invalid
                }

                // Insert booking with passengers count
                insertBooking(userKey, from, to, date, returnOption, passengers);
            }
        });
    }

    // Method to insert booking into the Firebase database
    private void insertBooking(String userKey, String from, String to, String date, boolean returnOption, int passengers) {
        if (userKey != null) {
            BookingModel booking = new BookingModel(from, to, date, returnOption, passengers); // Updated to include passengers
            mDatabase.child("usersC").child(userKey).child("bookings").push().setValue(booking);
            Toast.makeText(this, "Booking successfully inserted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Booking.this, BookingView.class);
            intent.putExtra("from", from);
            intent.putExtra("userKey", userKey); // userKey is the key of the user whose bookings you want to view
            startActivity(intent);

        } else {
            Toast.makeText(this, "UserD key is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the date on the EditText
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Restrict selection of past dates
                        if (selectedDate.before(Calendar.getInstance())) {
                            // Display a toast message indicating past date selection is not allowed
                            Toast.makeText(Booking.this, "Please select a future date", Toast.LENGTH_SHORT).show();
                        } else {
                            // Set the date on the EditText
                            String formattedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            editTextDate.setText(formattedDate);
                        }
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}