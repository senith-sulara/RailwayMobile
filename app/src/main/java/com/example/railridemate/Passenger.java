package com.example.railridemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Passenger extends AppCompatActivity {

private EditText firstname;
    private EditText Lastname;
    private  EditText email;
    private EditText phonenumber;

    private EditText nic;
    private ImageView home, msg,profile,shc;

    private Button SaveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        firstname=(EditText) findViewById(R.id.fnametext);
        Lastname=(EditText) findViewById(R.id.lnametext);
        email=(EditText) findViewById(R.id.emailtext);
        phonenumber=(EditText) findViewById(R.id.phonetext);
        nic=(EditText) findViewById(R.id.nictext);
        SaveButton=(Button) findViewById(R.id.saveBtn);
        String userKey = getIntent().getStringExtra("userKey");

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Passenger.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Passenger.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Passenger.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Passenger.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName= firstname.getText().toString();
                String lastName=Lastname.getText().toString();
                String emailAddress = email.getText().toString().trim();
                String phone = phonenumber.getText().toString().trim();
                String nicNumber = nic.getText().toString().trim();

                // Check if any field is empty
                if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || phone.isEmpty() || nicNumber.isEmpty()) {
                    Toast.makeText(Passenger.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                addPassengerToDB(firstName, lastName, emailAddress, phone, nicNumber);

                // Navigate to payment page
                Intent intent = new Intent(Passenger.this, Payments.class);
                startActivity(intent);
            }
        });

    }

    private void addPassengerToDB(String firstName, String lastName, String emailAddress, String phone, String nicNumber) {
        HashMap<String, Object> passangerHashmap= new HashMap<>();
        passangerHashmap.put("firstName",firstName);
        passangerHashmap.put("lastName",lastName);
        passangerHashmap.put("emailAddress",emailAddress);
        passangerHashmap.put("phone",phone);
        passangerHashmap.put("nicNumber",nicNumber);


        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference passangerRef= database.getReference("passenger");

        String key=passangerRef.push().getKey();
        passangerHashmap.put("key",key);

        passangerRef.child(key).setValue(passangerHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Passenger.this,"Add Passenger",Toast.LENGTH_SHORT).show();
                firstname.getText().clear();
                Lastname.getText().clear();
                email.getText().clear();
                nic.getText().clear();
                phonenumber.getText().clear();
            }
        });
    }
}