package com.example.railridemate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class News extends AppCompatActivity {
    private Spinner newsSubjectSpinner;
    private EditText addNewsInput;
    private Button saveNewsBtn;

    private DatabaseReference newsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        // Initialize views
        newsSubjectSpinner = findViewById(R.id.newsSubjectSpinner);
        addNewsInput = findViewById(R.id.addnewsinput);
        saveNewsBtn = findViewById(R.id.savenewsbtn);

        // Initialize Firebase Database reference
        newsRef = FirebaseDatabase.getInstance().getReference().child("news");

        // Set click listener for save button
        saveNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNews();
            }
        });
    }

    private void saveNews() {
        String newsSubject = newsSubjectSpinner.getSelectedItem().toString();
        String newsContent = addNewsInput.getText().toString().trim();

        if (newsSubject.isEmpty() || newsContent.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new child node in Firebase database
        DatabaseReference newNewsRef = newsRef.push();

        // Set news data
        newNewsRef.child("subject").setValue(newsSubject);
        newNewsRef.child("content").setValue(newsContent);

        // Show success message
        Toast.makeText(this, "News added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields
        // Clear input fields
        addNewsInput.setText("");
    }
}