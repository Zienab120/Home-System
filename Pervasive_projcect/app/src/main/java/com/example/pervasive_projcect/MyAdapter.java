package com.example.pervasive_projcect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<items> itemsList;
    private SelectListener listener;

    public MyAdapter(Context context, List<items> itemsList,SelectListener listener) {
        this.context = context;
        this.itemsList = itemsList;
        this.listener=listener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(itemsList.get(position).getTitle());
        holder.image.setImageResource(itemsList.get(position).getImage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(itemsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public void filterList(List<items> filteredList){
        itemsList=filteredList;
        notifyDataSetChanged();

    }



}
