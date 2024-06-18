package com.example.railridemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.railridemate.Adapters.MessageAdapter;
import com.example.railridemate.models.MessageModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseReference chatsReference;
    MessageAdapter messageAdapter;
    List<MessageModel> messageList;
    PopupWindow popupWindow;
    ImageButton track, reserve, book, chat,news,histry;
   // ImageButton track, reserve,book, chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        track =findViewById(R.id.imageButton7);
        reserve=findViewById(R.id.imageButton8);
        book = findViewById(R.id.imageButton11);
        histry = findViewById(R.id.imageButton81);
        news = findViewById(R.id.imageButton118);
        chat = findViewById(R.id.imageButton812);
        ImageView imageView9 = findViewById(R.id.imageView9);
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the popup layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_layout, null);

                // Create the popup window
                int width = ViewGroup.LayoutParams.WRAP_CONTENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                popupWindow = new PopupWindow(popupView, width, height, true);

                // Set a transparent background
                popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                // Display the popup at a specified location on the screen
                // After showing the popup window
                popupWindow.showAtLocation(v, Gravity.END | Gravity.TOP, 0, 0);

// Create a handler
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Dismiss the popup window
                        popupWindow.dismiss();
                    }
                }, 3000); // 3000 milliseconds = 3 seconds




                // Set text to TextView inside popup layout
                TextView phoneTextView = popupView.findViewById(R.id.phoneTextView);
                phoneTextView.setText("0702900565");
            }
        });

        // Receive the username from the Intent
        String username = getIntent().getStringExtra("username");

        // Construct the room ID
        String roomId = username + "CL8N71afiOc2nHz6uvggKlB1ryq2";

        // Initialize Firebase reference for the constructed room ID
        chatsReference = FirebaseDatabase.getInstance().getReference("chats").child(roomId);

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserLocationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve userKey from Intent
                String userKey = getIntent().getStringExtra("userKey");
                Intent intent = new Intent(MainActivity.this, Booking.class);
                intent.putExtra("userKey", userKey);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("username", username); // Pass the username to ChatActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);



              /*  Intent intent = new Intent(getApplicationContext(), ShowNews.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
               /* String userKey = getIntent().getStringExtra("userKey");
                Intent intent = new Intent(getApplicationContext(), UserD.class);
                intent.putExtra("userKey", userKey);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }
        });

       book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(getApplicationContext(), ShowNews.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                Intent intent = new Intent(getApplicationContext(), ShowNews.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);



            }
        });

        /*book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve userKey from Intent
                String userKey = getIntent().getStringExtra("userKey");
                if (userKey == null) {
                    Toast.makeText(MainActivity.this, "No user key provided.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, BookingView.class);
                intent.putExtra("userKey", userKey); // userKey is the key of the user whose bookings you want to view
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);



            }
        });*/

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userKey = getIntent().getStringExtra("userKey");
                Intent intent = new Intent(getApplicationContext(), UserD.class);
                intent.putExtra("userKey", userKey);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        histry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userKey = getIntent().getStringExtra("userKey");
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                intent.putExtra("userKey", userKey); // Pass the username to ChatActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}