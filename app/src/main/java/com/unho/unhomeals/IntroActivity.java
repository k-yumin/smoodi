package com.unho.unhomeals;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

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

        userInfo = new HashMap<>(); // TODO SERVER 사용자 정보를 담고 있는 HashMap (서버 대체)
        userInfo.put("S2080272", "10405 김규민");
        userInfo.put("S2080131", "10428 황인재");

        new MealServiceDietInfo().execute();
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
            // Params
            String URL = "https://open.neis.go.kr/hub/mealServiceDietInfo";
            String KEY = "23c08f8654a74ec9984cd75eafdfdea0";
            String ATPT_OFCDC_SC_CODE = "M10";
            String SD_SCHUL_CODE = "8000091";

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();

            // MLSV_FROM_YMD : The first day of last month
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            String MLSV_FROM_YMD = dateFormat.format(calendar.getTime());

            // MLSV_TO_YMD : The last day of next month
            calendar.add(Calendar.MONTH, 2);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
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

            Button btn_student = findViewById(R.id.btn_student);
            Button btn_teacher = findViewById(R.id.btn_teacher);

            if (SharedPf.getString(IntroActivity.this, "UID").length() == 0) {
                Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(200);

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