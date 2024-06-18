package com.example.railridemate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railridemate.R;
import com.example.railridemate.Reservationseat;
import com.example.railridemate.models.BookingModel;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private final String userKey;
    private Context context;
    private List<BookingModel> bookingList;

    public BookingAdapter(Context context, List<BookingModel> bookingList, String userKey) {
        this.context = context;
        this.bookingList = bookingList;
        this.userKey = userKey; // Initialize userKey
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (bookingList != null && !bookingList.isEmpty()) {
            BookingModel booking = bookingList.get(position);
            if (booking != null) {
                holder.bind(booking);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Navigate to the reservation page
                        Intent intent = new Intent(context, Reservationseat.class);
                        intent.putExtra("from", booking.getFrom());
                        intent.putExtra("userKey", userKey); // assuming userKey is available in BookingModel
                        // Add additional data as needed
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fromTextView, toTextView, dateTextView, passengersTextView, returnTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fromTextView = itemView.findViewById(R.id.fromTextView);
            // Initialize other TextViews
            toTextView = itemView.findViewById(R.id.toTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            passengersTextView = itemView.findViewById(R.id.passengersTextView);
            returnTextView = itemView.findViewById(R.id.returnTextView);
        }

        public void bind(BookingModel booking) {
            // Bind booking data to TextViews
            fromTextView.setText(booking.getFrom());
            toTextView.setText(booking.getTo());
            dateTextView.setText("Date: " + booking.getDate());
            returnTextView.setText("Return: " + (booking.isReturnOption() ? "Yes" : "No"));

            // Get the number of passengers and display it in the appropriate TextView
            int passengersCount = booking.getPassengers();
            String passengersString = "Passengers: " + passengersCount;
            passengersTextView.setText(passengersString);
        }
    }
}
