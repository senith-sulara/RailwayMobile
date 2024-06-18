package com.example.railridemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.railridemate.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetails extends AppCompatActivity {

    private TextView nameTextView, emailTextView, telTextView, addressTextView, genderTextView;
    private Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // Initialize views
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        telTextView = findViewById(R.id.telTextView);
        addressTextView = findViewById(R.id.addressTextView);
        genderTextView = findViewById(R.id.genderTextView);
        editProfileButton = findViewById(R.id.button);

        // Retrieve the userKey from the intent
        String userKey = getIntent().getStringExtra("userKey");

        // Make sure userKey is not null before proceeding
        if (userKey != null) {
            // Reference the UserDetails table under the UserC parent node using the userKey
            DatabaseReference userDetailsRef = FirebaseDatabase.getInstance().getReference("usersC").child(userKey).child("UserDetails");

            userDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Loop through each child node under UserDetails
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // Map each child node to a UserD object
                            UserModel user = dataSnapshot.getValue(UserModel.class);
                            if (user != null) {
                                // Display user details in TextViews
                                nameTextView.setText("Name: " + user.getName());
                                emailTextView.setText("Email: " + user.getEmail());
                                telTextView.setText("Tel: " + user.getTel());
                                addressTextView.setText("Address: " + user.getAddress());
                                genderTextView.setText("Gender: " + user.getGender());
                                break; // Exit loop after displaying first user details
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UserDetails.this, "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // Handle the case where userKey is null
            Toast.makeText(UserDetails.this, "UserD key is null", Toast.LENGTH_SHORT).show();
        }

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if any of the TextViews are null
                if (nameTextView != null && emailTextView != null &&
                        telTextView != null && addressTextView != null &&
                        genderTextView != null) {
                    // Get all the user details
                    String name = nameTextView.getText().toString().substring(6); // Remove "Name: " prefix
                    String email = emailTextView.getText().toString().substring(7); // Remove "Email: " prefix
                    String tel = telTextView.getText().toString().substring(5); // Remove "Tel: " prefix
                    String address = addressTextView.getText().toString().substring(9); // Remove "Address: " prefix
                    String gender = genderTextView.getText().toString().substring(8); // Remove "Gender: " prefix

                    // Create an intent to go to the UserD activity
                    Intent intent = new Intent(UserDetails.this, UserD.class);
                    // Pass all the user details to the UserD activity
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("tel", tel);
                    intent.putExtra("address", address);
                    intent.putExtra("gender", gender);
                    // Start the UserD activity
                    startActivity(intent);
                } else {
                    // Handle the case where any of the TextViews is null
                    Toast.makeText(UserDetails.this, "UserD details are not available", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
