package com.example.railridemate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railridemate.Adapters.BookingAdapter;
import com.example.railridemate.Adapters.BookingPagerAdapter;
import com.example.railridemate.models.BookingModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingListFragment extends Fragment {

    private boolean returnOption;
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<BookingModel> bookingList;

    public BookingListFragment(boolean returnOption) {
        this.returnOption = returnOption;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String userKey ="";

        // Retrieve user's key
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userKey = currentUser.getUid();
            retrieveBookings(userKey);
        }
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(getContext(), bookingList,userKey);
        recyclerView.setAdapter(adapter);



        return view;
    }

    private void retrieveBookings(String userKey) {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference()
                .child("usersC")
                .child(userKey)
                .child("bookings");

        Query query = bookingsRef.orderByChild("returnOption").equalTo(returnOption);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BookingModel booking = snapshot.getValue(BookingModel.class);
                    if (booking != null) {
                        bookingList.add(booking);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("BookingListFragment", "Database error: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Failed to retrieve bookings: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}