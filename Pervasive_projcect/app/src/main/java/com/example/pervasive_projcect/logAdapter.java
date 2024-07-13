package com.example.pervasive_projcect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class logAdapter extends RecyclerView.Adapter<logHolder> {


    private Context context;
    private List<Event> list;
    public logAdapter(Context context, List<Event> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public logHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new logHolder(LayoutInflater.from(context).inflate(R.layout.log_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull logHolder holder, int position) {
        holder.Name.setText(list.get(position).getActivityName());
        holder.State.setText(list.get(position).getEvent());
        holder.Time.setText(list.get(position).getTimeStamp());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
