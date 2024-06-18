package com.example.railridemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.railridemate.Adapters.MessageAdapter;
import com.example.railridemate.databinding.ActivityChatBinding;
import com.example.railridemate.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    DatabaseReference chatsReference;
    MessageAdapter messageAdapter;
    List<MessageModel> messageList;
    private ImageView home, msg,profile,shc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Receive the username from the Intent
        String username = getIntent().getStringExtra("username");

        // Construct the room ID
        String roomId = username + "6Y9bSL2Ayla4AKD1wYWCUBVhiSH3";

        // Initialize Firebase reference for the constructed room ID
        chatsReference = FirebaseDatabase.getInstance().getReference("chats").child(roomId);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        binding.recycler.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recycler.setLayoutManager(linearLayoutManager);
        String userKey = getIntent().getStringExtra("userKey");

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        // Set up listener for messages in the "nisalCL8N71afiOc2nHz6uvggKlB1ryq2" room
        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messageList.add(messageModel);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        binding.sendMessageLayout.setVisibility(View.VISIBLE);

        // Set click listener for the send button
        binding.sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hardcoding sender ID to "CL8N71afiOc2nHz6uvggKlB1ryq2"
                String senderId = "6Y9bSL2Ayla4AKD1wYWCUBVhiSH3";

                String messageText = binding.messageEd.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    sendMessage(senderId, messageText);
                }
            }
        });
    }

    private void sendMessage(String senderId, String messageText) {
        String messageId = chatsReference.push().getKey();
        MessageModel message = new MessageModel(messageId, senderId, messageText, false, null);
        if (messageId != null) {
            chatsReference.child(messageId).setValue(message)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Clear the EditText after sending the message
                            binding.messageEd.setText("");
                            // Display a toast message indicating the message was sent
                            Toast.makeText(ChatActivity.this, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
