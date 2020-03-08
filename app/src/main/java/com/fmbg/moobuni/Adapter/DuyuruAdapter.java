package com.fmbg.moobuni.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbg.moobuni.Model.Duyuru;
import com.fmbg.moobuni.R;
import com.fmbg.moobuni.webViewActivity;

import java.util.List;

public class DuyuruAdapter extends RecyclerView.Adapter<DuyuruAdapter.ViewHolder>{
    public Context mContext;
    public List<Duyuru> mDUyuru;

    public DuyuruAdapter(Context mContext, List<Duyuru> mDUyuru) {
        this.mContext = mContext;
        this.mDUyuru = mDUyuru;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_view, parent, false);
        return new DuyuruAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Duyuru duyuru = mDUyuru.get(position);
        holder.date.setText(duyuru.getDate());
        holder.day.setText(duyuru.getDay());
        holder.title.setText(duyuru.getTitle());
        holder.university_name.setText("İstanbul Medeniyet Üniversitesi");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, webViewActivity.class);
                intent.putExtra("url", duyuru.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDUyuru.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, date, title, university_name;
        ImageView university_logo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
            university_name = itemView.findViewById(R.id.university_name);
            university_logo = itemView.findViewById(R.id.university_logo);
        }
    }
}