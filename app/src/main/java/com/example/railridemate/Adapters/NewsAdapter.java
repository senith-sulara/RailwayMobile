package com.example.railridemate.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railridemate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<DataSnapshot> newsList;
    private DatabaseReference newsRef;

    public NewsAdapter(List<DataSnapshot> newsList, DatabaseReference newsRef) {
        this.newsList = newsList;
        this.newsRef = newsRef;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(newsList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView newsSubjectTextView;
        private TextView newsContentTextView;
        private Button editNewsBtn;
        private Button deleteNewsBtn;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsSubjectTextView = itemView.findViewById(R.id.newsSubjectTextView);
            newsContentTextView = itemView.findViewById(R.id.newsContentTextView);
//            editNewsBtn = itemView.findViewById(R.id.editNewsBtn);
//            deleteNewsBtn = itemView.findViewById(R.id.deleteNewsBtn);
        }

        void bind(DataSnapshot snapshot) {
            String newsSubject = snapshot.child("subject").getValue(String.class);
            String newsContent = snapshot.child("content").getValue(String.class);

            newsSubjectTextView.setText(newsSubject);
            newsContentTextView.setText(newsContent);

//            editNewsBtn.setOnClickListener(v -> {
//                String newsKey = snapshot.getKey();
//                editNews(newsKey, newsSubjectTextView, newsContentTextView);
//            });

//            deleteNewsBtn.setOnClickListener(v -> {
//                String newsKey = snapshot.getKey();
//                newsRef.child(newsKey).removeValue()
//                        .addOnSuccessListener(aVoid -> {
//                            // Item deleted successfully
////                            notifyDataSetChanged();
//                            int position = getAdapterPosition();
//                            if (position != RecyclerView.NO_POSITION) {
//                                newsList.remove(position); // Remove the item from the dataset
//                                notifyItemRemoved(position); // Notify adapter about the removal
//                                notifyItemRangeChanged(position, newsList.size()); // Notify adapter about the range change
//                            }
//                            Toast.makeText(itemView.getContext(), "News deleted successfully", Toast.LENGTH_SHORT).show();
//                        })
//                        .addOnFailureListener(e -> {
//                            // Failed to delete item
//                            Toast.makeText(itemView.getContext(), "Failed to delete news", Toast.LENGTH_SHORT).show();
//                        });
//            });
        }

        private void editNews(String newsKey, TextView newsSubjectTextView, TextView newsContentTextView) {
            String newSubject = newsSubjectTextView.getText().toString();
            String newContent = newsContentTextView.getText().toString();

            newsRef.child(newsKey).child("subject").setValue(newSubject)
                    .addOnSuccessListener(aVoid -> {
                        // Subject updated successfully
                        newsRef.child(newsKey).child("content").setValue(newContent)
                                .addOnSuccessListener(aVoid1 -> {
                                    // Content updated successfully
                                    // Notify adapter about the change
                                    Toast.makeText(itemView.getContext(), "News updated successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to update content
                                    Toast.makeText(itemView.getContext(), "Failed to update news content", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Failed to update subject
                        Toast.makeText(itemView.getContext(), "Failed to update news subject", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}



