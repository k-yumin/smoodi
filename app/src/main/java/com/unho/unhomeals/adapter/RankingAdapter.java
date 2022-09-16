package com.unho.unhomeals.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unho.unhomeals.R;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    String[][] items;

    public RankingAdapter(String[][] items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String rank = items[position][0];
        switch (rank) {
            case "1":
                holder.getImageView().setImageResource(R.drawable.rank_1st);
                break;
            case "2":
                holder.getImageView().setImageResource(R.drawable.rank_2nd);
                break;
            case "3":
                holder.getImageView().setImageResource(R.drawable.rank_3rd);
                break;
        }

        holder.getRankView().setText(rank);
        holder.getItemView().setText(items[position][1]);
        holder.getScoreView().setText(items[position][2]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_rank;
        private final TextView tv_rank, tv_item, tv_score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_rank = itemView.findViewById(R.id.iv_rank);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_item = itemView.findViewById(R.id.tv_item_ranking);
            tv_score = itemView.findViewById(R.id.tv_score);
        }

        public ImageView getImageView() { return iv_rank; }

        public TextView getRankView() { return tv_rank; }

        public TextView getItemView() {
            return tv_item;
        }

        public TextView getScoreView() {
            return tv_score;
        }
    }
}
