package com.example.railridemate.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railridemate.R;
import com.example.railridemate.models.ScheduleModel;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    private List<ScheduleModel> dataList;

    public ScheduleAdapter(List<ScheduleModel> dataList) {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        ScheduleModel data = dataList.get(position);
        holder.textViewRoute.setText(data.getRoute());
        holder.textViewTrain.setText(data.getTrain());
        holder.textViewIn.setText(data.getIn());
        holder.textViewOut.setText(data.getOut());
        holder.textViewAt1.setText(data.getAt1());
        holder.textViewAt2.setText(data.getAt2());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRoute, textViewTrain, textViewIn, textViewOut, textViewAt1, textViewAt2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoute = itemView.findViewById(R.id.textView31);
            textViewTrain = itemView.findViewById(R.id.textView321);
            textViewIn = itemView.findViewById(R.id.textView41);
            textViewOut = itemView.findViewById(R.id.textView43);
            textViewAt1 = itemView.findViewById(R.id.textView414);
            textViewAt2 = itemView.findViewById(R.id.textView434);
        }
    }
}
