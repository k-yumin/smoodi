package com.unho.unhomeals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.unho.unhomeals.adapter.MainAdapter;
import com.unho.unhomeals.data.SharedPf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    static Map<String, String> dietInfo;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = IntroActivity.adapter;
        dietInfo = IntroActivity.dietInfo;

        ImageView iv_profile = findViewById(R.id.iv_profile);
        TextView tv_profile = findViewById(R.id.tv_profile);

        String UID = SharedPf.getString(this, "UID");

        if (UID.equals("0")) {
            iv_profile.setImageResource(R.drawable.pf_teacher);
            tv_profile.setText("선생님");

            float rating = 4.5f; // TODO SERVER : Get the average of ratings from server
            rating(rating);
        } else {
            iv_profile.setImageResource(R.drawable.pf_student);
            String[] name = IntroActivity.userInfo.get(UID).split(" ");
            tv_profile.setText(String.format("%c-%c %s", name[0].charAt(0), name[0].charAt(2), name[1]));
            barcode(UID);
        }

        // Date
        TextView tv_today = findViewById(R.id.tv_today);
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

                    String[] contents = {
                            dietInfo.get(today+"조식"),
                            dietInfo.get(today+"중식"),
                            dietInfo.get(today+"석식"),
                    };

                    for (int j = 0; j < contents.length; j++) {
                        if (contents[j] == null) contents[j] = "급식 정보가 없습니다.";
                        adapter.getItemAt(j).setContent(contents[j]);
                        adapter.notifyItemChanged(j);
                    }
                } catch (ParseException ignored) {}
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        Calendar calendar = new GregorianCalendar();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy. M. d.");
        tv_today.setText(format.format(calendar.getTime()));
        tv_today.setOnClickListener(view -> tv_today.setText(format.format(new GregorianCalendar().getTime())));

        findViewById(R.id.btn_prev).setOnClickListener(view -> {
            calendar.add(Calendar.DATE, -1);
            tv_today.setText(format.format(calendar.getTime()));
        });

        findViewById(R.id.btn_next).setOnClickListener(view -> {
            calendar.add(Calendar.DATE, 1);
            tv_today.setText(format.format(calendar.getTime()));
        });

        // ViewPager2
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer((page, position) -> page.setScaleY(0.8f+(1-Math.abs(position))*0.2f));

        ViewPager2 vp_meals = findViewById(R.id.vp_meals);
        vp_meals.setAdapter(adapter);
        vp_meals.setOffscreenPageLimit(3);
        vp_meals.setPageTransformer(transformer);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour < 8) {
            vp_meals.setCurrentItem(0);
        } else if (hour < 14) {
            vp_meals.setCurrentItem(1);
        } else {
            vp_meals.setCurrentItem(2);
        }

        // Buttons
        findViewById(R.id.btn_apply).setOnClickListener(view -> {
            Intent intent = new Intent(this, ApplyActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_rate).setOnClickListener(view -> {
            Intent intent = new Intent(this, RateActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_ranking).setOnClickListener(view -> {
            Intent intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_setting).setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        });
    }

    private void barcode(String id) {
        ImageView iv_barcode = findViewById(R.id.iv_barcode);
        TextView tv_barcode = findViewById(R.id.tv_barcode);

        iv_barcode.setVisibility(View.VISIBLE);
        tv_barcode.setVisibility(View.VISIBLE);

        try {
            Bitmap bitmap = new BarcodeEncoder().encodeBitmap(id, BarcodeFormat.CODE_39, 800, 150);
            iv_barcode.setImageBitmap(bitmap);
            tv_barcode.setText(id);
        } catch(WriterException ignored) {}
    }

    private void rating(float rating) {
        RatingBar rb_rating = findViewById(R.id.rb_rating);
        TextView tv_rating = findViewById(R.id.tv_rating);

        rb_rating.setVisibility(View.VISIBLE);
        tv_rating.setVisibility(View.VISIBLE);

        String text;

        if (rating < 1.5) {
            text = "만족도 매우 낮음";
        } else if (rating < 2.5) {
            text = "만족도 낮음";
        } else if (rating < 3.5) {
            text = "만족도 보통";
        } else if (rating < 4.5) {
            text = "만족도 높음";
        } else {
            text = "만족도 매우 높음";
        }

        rb_rating.setRating(rating);
        tv_rating.setText(text);
    }
}