package com.unho.unhomeals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.unho.unhomeals.adapter.MainAdapter;
import com.unho.unhomeals.data.Meal;
import com.unho.unhomeals.data.SharedPf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IntroActivity extends AppCompatActivity {

    public static Map<String, String> userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        userInfo = new HashMap<>(); // TODO SERVER : Move this data to server
        userInfo.put("S2080272", "10405 김규민");
        userInfo.put("S2080131", "10428 황인재");

        if (dietInfo == null) new MealServiceDietInfo().execute();
        else new MealServiceDietInfo().onPostExecute(null);
    }

    public static Map<String, String> dietInfo;
    public static MainAdapter adapter;

    private class MealServiceDietInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dietInfo = new HashMap<>();

            Meal meal1 = new Meal("조식", "");
            Meal meal2 = new Meal("중식", "");
            Meal meal3 = new Meal("석식", "");

            ArrayList<Meal> meals = new ArrayList<>(Arrays.asList(meal1, meal2, meal3));
            adapter = new MainAdapter(meals);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try { Thread.sleep(2_000); } catch (InterruptedException ignored) {}

            // Params
            String URL = "https://open.neis.go.kr/hub/mealServiceDietInfo";
            String KEY = "23c08f8654a74ec9984cd75eafdfdea0";
            String ATPT_OFCDC_SC_CODE = "M10";
            String SD_SCHUL_CODE = "8000091";

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();

            // MLSV_FROM_YMD : Minus 15 days
            calendar.add(Calendar.DAY_OF_MONTH, -15);
            String MLSV_FROM_YMD = dateFormat.format(calendar.getTime());

            // MLSV_TO_YMD : Plus 15 days
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            String MLSV_TO_YMD = dateFormat.format(calendar.getTime());

            try {
                Document document = Jsoup.connect(URL)
                        .timeout(10_000)
                        .data("KEY", KEY)
                        .data("ATPT_OFCDC_SC_CODE", ATPT_OFCDC_SC_CODE)
                        .data("SD_SCHUL_CODE", SD_SCHUL_CODE)
                        .data("MLSV_FROM_YMD", MLSV_FROM_YMD)
                        .data("MLSV_TO_YMD", MLSV_TO_YMD)
                        .get();

                for (Element element : document.select("row")) {
                    String MLSV_YMD    = element.select("MLSV_YMD").text();
                    String MMEAL_SC_NM = element.select("MMEAL_SC_NM").text();
                    String DDISH_NM    = element.select("DDISH_NM").text();

                    StringBuilder builder = new StringBuilder();

                    String[] dishes = DDISH_NM.split("<br/>");
                    for (String dish : dishes) builder.append(dish.split(" ")[0]).append("\n");

                    dietInfo.put(MLSV_YMD + MMEAL_SC_NM, builder.toString());
                }
            } catch (Exception ignored) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            TextView tv_loading = findViewById(R.id.tv_loading);
            tv_loading.setVisibility(View.INVISIBLE);

            Button btn_student = findViewById(R.id.btn_student);
            Button btn_teacher = findViewById(R.id.btn_teacher);

            if (SharedPf.getString(IntroActivity.this, "UID").length() == 0) {
                Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(600);

                btn_student.setVisibility(View.VISIBLE);
                btn_student.setAnimation(animation);
                btn_student.setOnClickListener(view -> {
                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent);
                });

                btn_teacher.setVisibility(View.VISIBLE);
                btn_teacher.setAnimation(animation);
                btn_teacher.setOnClickListener(view -> {
                    // TODO SHOW FRAGMENT TO INPUT PASSCODE
                    SharedPf.put(IntroActivity.this, "UID", "0");

                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                });
            } else {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        }
    }
}