package com.unho.unhomeals.adapter;

import android.annotation.SuppressLint;
import android.provider.Telephony;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

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
        String item = items.get(position);

        for (String key : RateActivity.changes.keySet()) {
            if (key.contains(item)) {
                float v = RateActivity.changes.get(key);
                if (v == -2.0) {
                    holder.getBad().callOnClick();
                } else if (v == 1.0) {
                    holder.getSoso().callOnClick();
                } else if (v == 2.0) {
                    holder.getGood().callOnClick();
                }
            }
        }

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

        private final TextView tv_item;
        private final Button btn_bad, btn_soso, btn_good;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item = itemView.findViewById(R.id.tv_item_rate);

            btn_bad = itemView.findViewById(R.id.btn_bad);
            btn_soso = itemView.findViewById(R.id.btn_soso);
            btn_good = itemView.findViewById(R.id.btn_good);

            btn_bad.setOnClickListener(view -> {
                setBackgroundTint(btn_bad, R.color.pale_red);
                setBackgroundTint(btn_soso, R.color.light);
                setBackgroundTint(btn_good, R.color.light);

                String text = tv_item.getText().toString();
                if (RateActivity.changes.containsKey(text)) {
                    String key = RateActivity.UID+"_"+text;
                    RateActivity.changes.put(key, -2.0f);
                }
            });

            btn_soso.setOnClickListener(view -> {
                setBackgroundTint(btn_bad, R.color.light);
                setBackgroundTint(btn_soso, R.color.pale_yellow);
                setBackgroundTint(btn_good, R.color.light);

                String text = tv_item.getText().toString();
                String key = RateActivity.UID+"_"+text;
                if (RateActivity.changes.containsKey(text)) {
                    RateActivity.changes.put(key, 1.0f);
                }
            });

            btn_good.setOnClickListener(view -> {
                setBackgroundTint(btn_bad, R.color.light);
                setBackgroundTint(btn_soso, R.color.light);
                setBackgroundTint(btn_good, R.color.pale_green);

                String text = tv_item.getText().toString();
                if (RateActivity.changes.containsKey(text)) {
                    String key = RateActivity.UID+"_"+text;
                    RateActivity.changes.put(key, 2.0f);
                }
            });
        }

        public TextView getTextView() {
            return tv_item;
        }

        public Button getBad() {
            return btn_bad;
        }

        public Button getSoso() {
            return btn_soso;
        }

        public Button getGood() {
            return btn_good;
        }

        @SuppressLint("UseCompatLoadingForColorStateLists")
        private void setBackgroundTint(Button button, int id) {
            button.setBackgroundTintList(RateActivity.context.getResources().getColorStateList(id));
        }
    }
}
