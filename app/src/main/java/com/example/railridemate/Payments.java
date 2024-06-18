package com.example.railridemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Payments extends AppCompatActivity {

    private EditText cardNumberEditText, cardHolderNameEditText, expireMonthEditText, expireYearEditText, securityCodeEditText, offerCodeEditText;
    private RadioGroup paymentMethodRadioGroup;
    private Button savePaymentButton;
    private ImageView home, msg,profile,shc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        // Initialize views
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        cardHolderNameEditText = findViewById(R.id.cardHolderNameEditText);
        expireMonthEditText = findViewById(R.id.expireMonthEditText);
        expireYearEditText = findViewById(R.id.expireYearEditText);
        securityCodeEditText = findViewById(R.id.securityCodeEditText);
        offerCodeEditText = findViewById(R.id.offerCodeEditText);
        paymentMethodRadioGroup = findViewById(R.id.paymentMethodRadioGroup);
        savePaymentButton = findViewById(R.id.savePaymentButton);

        String userKey = getIntent().getStringExtra("userKey");

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payments.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payments.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payments.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payments.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });

        // Set onClickListener for savePaymentButton
        savePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savePaymentDetails();

                // Get the current date
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = dateFormat.format(calendar.getTime());

// Start BarcodeGeneratorActivity and pass the current date
                Intent intent = new Intent(Payments.this, BarcodeGeneratorActivity.class);
                intent.putExtra("selectedDate", currentDate);
                startActivity(intent);


            }
        });
    }

    private void savePaymentDetails() {
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String cardHolderName = cardHolderNameEditText.getText().toString().trim();
        String expireMonth = expireMonthEditText.getText().toString().trim();
        String expireYear = expireYearEditText.getText().toString().trim();
        String securityCode = securityCodeEditText.getText().toString().trim();
        String offerCode = offerCodeEditText.getText().toString().trim();
        String paymentMethod = ((RadioButton) findViewById(paymentMethodRadioGroup.getCheckedRadioButtonId())).getText().toString();

        // Check if any field is empty
        if (cardNumber.isEmpty() || cardHolderName.isEmpty() || expireMonth.isEmpty() || expireYear.isEmpty() || securityCode.isEmpty() || offerCode.isEmpty()) {
            Toast.makeText(Payments.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare payment details HashMap
        HashMap<String, Object> paymentDetails = new HashMap<>();
        paymentDetails.put("cardNumber", cardNumber);
        paymentDetails.put("cardHolderName", cardHolderName);
        paymentDetails.put("expireMonth", expireMonth);
        paymentDetails.put("expireYear", expireYear);
        paymentDetails.put("securityCode", securityCode);
        paymentDetails.put("offerCode", offerCode);
        paymentDetails.put("paymentMethod", paymentMethod);

        // Save payment details to Firebase
        DatabaseReference paymentRef = FirebaseDatabase.getInstance().getReference().child("payments");
        String paymentKey = paymentRef.push().getKey();
        paymentDetails.put("key", paymentKey);

        paymentRef.child(paymentKey).setValue(paymentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Payments.this, "Payment details saved successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(Payments.this, "Failed to save payment details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearFields() {
        cardNumberEditText.setText("");
        cardHolderNameEditText.setText("");
        expireMonthEditText.setText("");
        expireYearEditText.setText("");
        securityCodeEditText.setText("");
        offerCodeEditText.setText("");
    }
}
