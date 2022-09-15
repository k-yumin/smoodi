package com.unho.unhomeals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.unho.unhomeals.adapter.MainAdapter;
import com.unho.unhomeals.data.Meal;
import com.unho.unhomeals.data.SharedPf;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ApplyActivity extends AppCompatActivity {

    String UID;
    String monday, friday;

    MainAdapter adapter;

    Button btn_apply;
    CheckBox cb_apply;
    boolean apply_always;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy. M. d.");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        findViewById(R.id.exit_apply).setOnClickListener(view -> finish());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        friday = dateFormat1.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        monday = dateFormat1.format(calendar.getTime());

        TextView tv_weekday = findViewById(R.id.tv_weekday);
        tv_weekday.setText(monday+" ~ "+friday);

        // ViewPager2 Adapter
        Meal meal1 = new Meal("석식", "");
        Meal meal2 = new Meal("석식", "");
        Meal meal3 = new Meal("석식", "");
        Meal meal4 = new Meal("석식", "");
        Meal meal5 = new Meal("석식", "");

        ArrayList<Meal> meals = new ArrayList<>(Arrays.asList(meal1, meal2, meal3, meal4, meal5));
        adapter = new MainAdapter(meals);

        String[] contents = new String[5];
        for (int i = 0; i < 5; i++) {
            contents[i] = IntroActivity.dietInfo.get(dateFormat2.format(calendar.getTime())+"석식");
            if (contents[i] == null) contents[i] = "급식 정보가 없습니다.";
            adapter.getItemAt(i).setContent(contents[i]);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        adapter.notifyDataSetChanged();

        // ViewPager2
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer((page, position) -> page.setScaleY(0.8f+(1-Math.abs(position))*0.2f));

        ViewPager2 vp_meals = findViewById(R.id.vp_meals_apply);
        vp_meals.setAdapter(adapter);
        vp_meals.setOffscreenPageLimit(3);
        vp_meals.setPageTransformer(transformer);

        // Apply Always Checkbox
        cb_apply = findViewById(R.id.cb_apply);
        apply_always = SharedPf.getBoolean(this, "apply_always");
        if (apply_always) cb_apply.setChecked(true);

        // Apply Button
        btn_apply = findViewById(R.id.btn_apply);

        UID = SharedPf.getString(this, "UID");
        ArrayList<String> applicants = new ArrayList<>(); // TODO SERVER : Get applicants from server
        applicants.add("S2080272"); // TODO REMOVE : SAMPLE DATA

        if (UID.equals("0")) {
            cb_apply.setVisibility(View.INVISIBLE);
            btn_apply.setText("석식신청명단(XLS)");
            btn_apply.setOnClickListener(view -> {
                // UID를 학번이름으로 변환
                for (int i = 0; i < applicants.size(); i++) {
                    applicants.set(i, IntroActivity.userInfo.get(applicants.get(i)));
                }
                download(applicants);
            });
        } else {
            if (applicants.contains(UID)) {
                btn_apply.setBackground(ContextCompat.getDrawable(this, R.color.light));
                btn_apply.setText("신청완료");
            }

            btn_apply.setOnClickListener(view -> {
                String text = btn_apply.getText().toString();
                if (text.equals("신청하기")) {
                    btn_apply.setBackground(ContextCompat.getDrawable(this, R.color.light));
                    btn_apply.setText("신청완료");
                } else {
                    btn_apply.setBackground(ContextCompat.getDrawable(this, R.color.unho));
                    btn_apply.setText("신청하기");
                }
            });
        }

    }

    private void download(ArrayList<String> applicants) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row;
        Cell cell;

        row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellValue("학번");
        cell = row.createCell(1);
        cell.setCellValue("이름");

        for (int i = 0; i < applicants.size(); i++) {
            String[] applicant = applicants.get(i).split(" ");
            row = sheet.createRow(i+1);

            cell = row.createCell(0);
            cell.setCellValue(applicant[0]);
            cell = row.createCell(1);
            cell.setCellValue(applicant[1]);
        }

        try {
            String date = dateFormat2.format(dateFormat1.parse(monday));

            File parent = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(parent, date+"석식신청명단.xls");

            FileOutputStream os = new FileOutputStream(file);
            workbook.write(os);

            String message = "다운로드 폴더에 저장되었습니다.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } catch (IOException | ParseException ignored) {
            String message = "저장할 수 없습니다.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPf.put(this, "apply_always", cb_apply.isChecked());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String text = btn_apply.getText().toString();
        if (text.equals("신청완료") || apply_always) {
            // TODO SERVER : Send "UID" to server
        }
    }
}