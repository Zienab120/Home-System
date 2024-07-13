package com.example.pervasive_projcect;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class logHolder extends RecyclerView.ViewHolder {
    public TextView Name,State,Time;
    public logHolder(@NonNull View itemView) {
        super(itemView);
        Name=itemView.findViewById(R.id.name);
        State=itemView.findViewById(R.id.state);
        Time=itemView.findViewById(R.id.time);
    }
}
