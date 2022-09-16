package com.unho.unhomeals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.unho.unhomeals.adapter.RankingAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingActivity extends AppCompatActivity {

    RankingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        findViewById(R.id.exit_ranking).setOnClickListener(view -> finish());

        Map<String, Float> rates = new HashMap<>(); // TODO SERVER : Get rates(="changes" of ApplyActivity) from server
        rates.put("S2080272_친환경백미밥",  2.0f); // TODO REMOVE : SAMPLE DATA
        rates.put("S2080273_친환경백미밥",  1.0f);
        rates.put("S2080274_배추김치"   ,  1.0f);
        rates.put("S2080275_배추김치"   , -2.0f);
        rates.put("S2080276_열무감자된장", -2.0f);
        rates.put("S2080277_미나리무생채", -2.0f);
        rates.put("S2080278_미나리무생채", -2.0f);

        Map<String, Integer> scores = new HashMap<>();
        for (String key : rates.keySet()) {
            if (key.contains("_")) {
                String item = key.split("_")[1];
                int score = (int)((float)rates.get(key));

                if (!scores.containsKey(item)) scores.put(item, score);
                else scores.put(item, scores.get(item) + score);
            }
        }

        List<String> keySet = new ArrayList<>(scores.keySet());
        Collections.sort(keySet, (s1, s2) -> (scores.get(s2).compareTo(scores.get(s1))));

        String[][] items = new String[keySet.size()][3];
        for (int i = 0; i < items.length; i++) {
            items[i][0] = String.valueOf(i+1);
            items[i][1] = keySet.get(i);
            items[i][2] = String.valueOf(scores.get(items[i][1]));
        }

        // RankingAdapter
        adapter = new RankingAdapter(items);

        // RecyclerView
        RecyclerView rv_ranking = findViewById(R.id.rv_ranking);
        rv_ranking.setAdapter(adapter);
    }
}