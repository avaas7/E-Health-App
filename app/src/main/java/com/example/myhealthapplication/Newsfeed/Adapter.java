package com.example.myhealthapplication.Newsfeed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myhealthapplication.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<com.example.myhealthapplication.Newsfeed.Adapter.ViewHolder> {

    Context context;
    ArrayList<modelClass> modelClassArrayList;

    public Adapter(Context context, ArrayList<modelClass> modelClassArrayList) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
    }

    @NonNull
    @Override
    public com.example.myhealthapplication.Newsfeed.Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.myhealthapplication.Newsfeed.Adapter.ViewHolder holder, int position) {

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebView.class);
                intent.putExtra("url",modelClassArrayList.get(position).getUrl());
                context.startActivity(intent);

            }
        });

        holder.mTime.setText("Published At: " + modelClassArrayList.get(position).getPublishedAt());
        holder.mAuthor.setText(modelClassArrayList.get(position).getAuthor());
        holder.mHeading.setText(modelClassArrayList.get(position).getTitle());
        holder.mContent.setText(modelClassArrayList.get(position).getDescription());
        Glide.with(context).load(modelClassArrayList.get(position).getUrlToImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mHeading, mContent, mAuthor, mTime;
        CardView cardView;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mHeading = itemView.findViewById(R.id.li_tv_heading);
            mContent = itemView.findViewById(R.id.li_tv_content);
            mAuthor = itemView.findViewById(R.id.li_tv_author);
            mTime = itemView.findViewById(R.id.li_tv_time);
            cardView = itemView.findViewById(R.id.li_card_view);
            imageView = itemView.findViewById(R.id.li_image_view);



        }
    }
}
