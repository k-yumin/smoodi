package com.unho.unhomeals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.unho.unhomeals.adapter.RateAdapter;
import com.unho.unhomeals.data.SharedPf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RateActivity extends AppCompatActivity {

    public static String UID;

    public static Map<String, Float> changes;
    public static Context context;
    RateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        findViewById(R.id.exit_rate).setOnClickListener(view -> finish());
        Map<String, String> dietInfo = IntroActivity.dietInfo;

        UID = SharedPf.getString(this, "UID");

        changes = new HashMap<>();
        context = getBaseContext();
        adapter = new RateAdapter(new ArrayList<>());

        // Load previous "changes" from SharedPreferences
        for (String rating : SharedPf.getStringSet(this, "MY_RATINGS")) {
            String key = rating.split("#")[0];
            float value = Float.parseFloat(rating.split("#")[1]);
            changes.put(key, value);
        }

        // RatingBar
        TextView tv_rate = findViewById(R.id.tv_rate);
        RatingBar rb_rate = findViewById(R.id.rb_rate);

        rb_rate.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            String text = "만족도";
            if (v <= 1.0) {
                text = "만족도 매우 낮음";
            } else if (v <= 2.0) {
                text = "만족도 낮음";
            } else if (v <= 3.0) {
                text = "만족도 보통";
            } else if (v <= 4.0) {
                text = "만족도 높음";
            } else if (v <= 5.0){
                text = "만족도 매우 높음";
            }
            tv_rate.setText(text);
        });

        // Date
        TextView tv_today = findViewById(R.id.tv_today_rate);
        tv_today.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy. M. d.");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");

                try {
                    String today = dateFormat2.format(Objects.requireNonNull(dateFormat1.parse(charSequence.toString())));

                    float v = rb_rate.getRating();
                    if (v != 0.0f) changes.put(UID+"*"+today, v);

                    String[] contents = {
                            dietInfo.get(today+"조식"),
                            dietInfo.get(today+"중식"),
                            dietInfo.get(today+"석식"),
                    };

                    ArrayList<String> items = new ArrayList<>();
                    for (String content : contents) {
                        if (content == null) continue;
                        String[] itemArray = content.split("\n");
                        items.addAll(Arrays.asList(itemArray));
                    }

                    items = new ArrayList<>(new HashSet<>(items));

                    adapter.setItems(items);
                    adapter.notifyDataSetChanged();
                } catch (ParseException ignored) {}
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        Calendar calendar = new GregorianCalendar();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy. M. d.");
        String today = format.format(calendar.getTime());
        tv_today.setText(today);
        tv_today.setOnClickListener(view -> tv_today.setText(format.format(new GregorianCalendar().getTime())));

        Button btn_next_rate = findViewById(R.id.btn_next_rate);
        btn_next_rate.setOnClickListener(view -> {
            calendar.add(Calendar.DATE, 1);
            tv_today.setText(format.format(calendar.getTime()));

            String text = tv_today.getText().toString();
            if (text.equals(today)) btn_next_rate.setVisibility(View.INVISIBLE);
        });

        findViewById(R.id.btn_prev_rate).setOnClickListener(view -> {
            btn_next_rate.setVisibility(View.VISIBLE);
            calendar.add(Calendar.DATE, -1);
            tv_today.setText(format.format(calendar.getTime()));
        });

        // RecyclerView
        RecyclerView rv_rate = findViewById(R.id.rv_rate);
        rv_rate.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Save "chagnes" in SharedPreferences
        Set<String> changesSet = new HashSet<>();
        for (String key : changes.keySet()) {
            changesSet.add(key+"#"+changes.get(key));
        }
        SharedPf.put(this, "MY_RATINGS", changesSet);

        // TODO SERVER : Send "changes" to server
    }
}