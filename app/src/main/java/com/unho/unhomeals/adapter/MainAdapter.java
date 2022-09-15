package com.unho.unhomeals.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unho.unhomeals.R;
import com.unho.unhomeals.data.Meal;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<Meal> meals;

    public MainAdapter(ArrayList<Meal> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.tv_title.setText(meals.get(position).getTitle());
        holder.tv_content.setText(meals.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public Meal getItemAt(int position) {
        return meals.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }
}
