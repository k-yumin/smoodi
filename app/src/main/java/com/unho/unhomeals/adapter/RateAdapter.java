package com.unho.unhomeals.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unho.unhomeals.R;
import com.unho.unhomeals.RateActivity;

import java.util.ArrayList;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ViewHolder> {

    private ArrayList<String> items;

    public RateAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder {

        TextView tv_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item = itemView.findViewById(R.id.tv_item);

            Button btn_bad = itemView.findViewById(R.id.btn_bad);
            Button btn_soso = itemView.findViewById(R.id.btn_soso);
            Button btn_good = itemView.findViewById(R.id.btn_good);

            btn_bad.setOnClickListener(view -> {
                setBackgroundTint(btn_bad, R.color.pale_red);
                setBackgroundTint(btn_soso, R.color.light);
                setBackgroundTint(btn_good, R.color.light);

                String key = RateActivity.UID+"_"+ tv_item.getText().toString();
                RateActivity.changes.put(key, -2.0f);
            });

            btn_soso.setOnClickListener(view -> {
                setBackgroundTint(btn_bad, R.color.light);
                setBackgroundTint(btn_soso, R.color.pale_yellow);
                setBackgroundTint(btn_good, R.color.light);

                String key = RateActivity.UID+"_"+ tv_item.getText().toString();
                RateActivity.changes.put(key, 1.0f);
            });

            btn_good.setOnClickListener(view -> {
                setBackgroundTint(btn_bad, R.color.light);
                setBackgroundTint(btn_soso, R.color.light);
                setBackgroundTint(btn_good, R.color.pale_green);

                String key = RateActivity.UID+"_"+ tv_item.getText().toString();
                RateActivity.changes.put(key, 2.0f);
            });
        }

        public TextView getTextView() {
            return tv_item;
        }

        @SuppressLint("UseCompatLoadingForColorStateLists")
        private void setBackgroundTint(Button button, int id) {
            button.setBackgroundTintList(RateActivity.context.getResources().getColorStateList(id));
        }
    }
}
