package com.example.railridemate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railridemate.R;


import com.example.railridemate.models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.BreakIterator;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageModel> messageList;
    private Context context;

    public MessageAdapter(List<MessageModel> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.message_row, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel message = messageList.get(position);
        holder.msg.setText(message.getMessage());

        if (message.isAdmin()) {
            // If the message is from an admin
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            // If the message is from a regular user
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));
        }
    }



    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView msg;
        LinearLayout main;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message);
            main = itemView.findViewById(R.id.mainMessageLayout);
        }
    }
}
