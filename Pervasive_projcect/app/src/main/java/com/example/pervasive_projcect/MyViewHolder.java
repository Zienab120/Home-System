package com.example.pervasive_projcect;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView title;
    public CardView cardView;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        image=itemView.findViewById(R.id.item_image);
        title=itemView.findViewById(R.id.item_title);
        cardView=itemView.findViewById(R.id.main_container);
    }


}
