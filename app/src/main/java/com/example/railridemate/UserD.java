package com.example.railridemate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.railridemate.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import java.util.HashMap;

public class UserD extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText nameEditText, emailEditText, telEditText, addressEditText;
    private RadioGroup genderRadioGroup;
    private Button saveUserButton,editUserButton,button1;
    private ImageView home, msg,profile,shc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        telEditText = findViewById(R.id.telEditText);
        addressEditText = findViewById(R.id.addressEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        saveUserButton = findViewById(R.id.saveUserButton);
        editUserButton = findViewById(R.id.EditUserButton);
        button1 = findViewById(R.id.button2);

        String userKey = getIntent().getStringExtra("userKey");

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserD.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserD.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserD.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserD.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserD.this, ApplyOffer.class);
                startActivity(intent);
            }
        });

        if (userKey != null) {
            // Load user details from Firebase
            DatabaseReference userDetailsRef = FirebaseDatabase.getInstance().getReference("usersC").child(userKey).child("UserDetails");
            userDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (userModel != null) {
                            // Set user data to EditText fields
                            nameEditText.setText(userModel.getName());
                            emailEditText.setText(userModel.getEmail());
                            telEditText.setText(userModel.getTel());
                            addressEditText.setText(userModel.getAddress());
                            // Set gender
                            String gender = userModel.getGender();
                            if ("Male".equals(gender)) {
                                genderRadioGroup.check(R.id.maleRadioButton);
                            } else {
                                genderRadioGroup.check(R.id.femaleRadioButton);
                            }
                            saveUserButton.setVisibility(View.GONE);
                            editUserButton.setVisibility(View.VISIBLE);

                        }
                    } else {
                        saveUserButton.setVisibility(View.VISIBLE);
                        editUserButton.setVisibility(View.GONE);
                        Toast.makeText(UserD.this, "UserD data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching user data: " + databaseError.getMessage());
                    Toast.makeText(UserD.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User key not found", Toast.LENGTH_SHORT).show();
        }
        saveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

        editUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser();
            }
        });

    }

    private void saveUser() {
            // Save user if button text is "Save"
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String tel = telEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String gender = "";

            int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
            if (selectedGenderId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedGenderId);
                gender = selectedRadioButton.getText().toString();
            }

            if (name.isEmpty() || email.isEmpty() || tel.isEmpty() || address.isEmpty() || gender.isEmpty()) {
                Toast.makeText(UserD.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            addUserToDatabase(name, email, tel, address, gender);
    }


    private void addUserToDatabase(String name, String email, String tel, String address, String gender) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("tel", tel);
        userMap.put("address", address);
        userMap.put("gender", gender);

        // Retrieve the userKey from the intent
        String userKey = getIntent().getStringExtra("userKey");

        // Make sure userKey is not null before proceeding
        if (userKey != null) {
            // Reference the UserDetails table under the UserC parent node using the userKey
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userDetailsRef = database.getReference("usersC").child(userKey).child("UserDetails");

//            // Generate a new key for the UserDetails entry
//            String key = userDetailsRef.push().getKey();

//            if (key != null) {
                // Set the value under the generated key
//                userDetailsRef.child(key).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            userDetailsRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserD.this, "User added successfully", Toast.LENGTH_SHORT).show();
//                            clearFields();
                            reloadActivity();
                        } else {
                            Toast.makeText(UserD.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//            }
        } else {
            // Handle the case where userKey is null
            Toast.makeText(UserD.this, "User key is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void editUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String tel = telEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String gender = "";

        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedGenderId);
            gender = selectedRadioButton.getText().toString();
        }

        if (name.isEmpty() || email.isEmpty() || tel.isEmpty() || address.isEmpty() || gender.isEmpty()) {
            Toast.makeText(UserD.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the userKey from the intent
        String userKey = getIntent().getStringExtra("userKey");

        if (userKey != null) {
            updateUserInDatabase(userKey, name, email, tel, address, gender);
        } else {
            Toast.makeText(UserD.this, "User key is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserInDatabase(String userKey, String name, String email, String tel, String address, String gender) {
        DatabaseReference userDetailsRef = FirebaseDatabase.getInstance().getReference("usersC").child(userKey).child("UserDetails");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("tel", tel);
        userMap.put("address", address);
        userMap.put("gender", gender);

        userDetailsRef.updateChildren(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserD.this, "User updated successfully", Toast.LENGTH_SHORT).show();
//                            finish(); // Close the activity after successful update
                            // Refresh the activity
                            reloadActivity();
                        } else {
                            Toast.makeText(UserD.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void reloadActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void clearFields() {
        nameEditText.setText("");
        emailEditText.setText("");
        telEditText.setText("");
        addressEditText.setText("");
        genderRadioGroup.clearCheck();
    }
}
