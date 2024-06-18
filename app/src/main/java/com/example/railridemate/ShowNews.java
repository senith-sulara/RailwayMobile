package com.example.railridemate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.railridemate.Adapters.NewsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ShowNews extends AppCompatActivity {
    private DatabaseReference newsRef;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private Button addNews;
    private ImageView home, msg,profile,shc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsRef = FirebaseDatabase.getInstance().getReference().child("news");
        recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String userKey = getIntent().getStringExtra("userKey");

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowNews.this, MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowNews.this, ChatActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowNews.this, UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowNews.this, TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
//        addNews=findViewById(R.id.addNewsBtn);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadNewsItems();

            // After the refresh action is complete, call setRefreshing(false) to indicate that the refresh operation is finished
            swipeRefreshLayout.setRefreshing(false);
        });

        // Set click listener for addNews button
//        addNews.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to the news activity page
//                Intent intent = new Intent(ShowNews.this, News.class);
//                startActivity(intent);
//            }
//        });
        loadNewsItems();
    }

    private void loadNewsItems() {
        newsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DataSnapshot> newsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    newsList.add(snapshot);
                }
                newsAdapter = new NewsAdapter(newsList, newsRef);
                recyclerView.setAdapter(newsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}