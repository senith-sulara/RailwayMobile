package com.example.railridemate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railridemate.R;
import com.example.railridemate.models.BookingModel;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private List<BookingModel> bookingList;

    public HistoryAdapter(Context context, List<BookingModel> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (bookingList != null && !bookingList.isEmpty()) {
            BookingModel booking = bookingList.get(position);
            if (booking != null) {
                holder.bind(booking);
            }
        }
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fromTextView, toTextView, dateTextView, passengersTextView, returnTextView;

        private int bookedSeatCount;
        private final int SEAT_COST = 1200;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fromTextView = itemView.findViewById(R.id.textViewfrom);
            // Initialize other TextViews
            toTextView = itemView.findViewById(R.id.textViewto);
            dateTextView = itemView.findViewById(R.id.textViewdate);
            passengersTextView = itemView.findViewById(R.id.textViewtotal);
        }

        public void bind(BookingModel booking) {
            // Bind booking data to TextViews
            fromTextView.setText(booking.getFrom());
            toTextView.setText(booking.getTo());
            dateTextView.setText(booking.getDate());
            // Get the number of passengers and display it in the appropriate TextView
            int passengersCount = booking.getPassengers();
            String passengersString = "Passengers: " + passengersCount;
            int totalAmount = passengersCount * SEAT_COST;
            String totStrin = String.valueOf(totalAmount);

            passengersTextView.setText("LKR "+totStrin+".00");


        }
    }
}
