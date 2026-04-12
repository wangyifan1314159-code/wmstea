package com.example.newland.car;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class shiyong extends AppCompatActivity {

    private TextView num, a1, a11, a2, a22;
    private SharedPreferences preferences;
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiyong);

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        num = findViewById(R.id.num);
        a1 = findViewById(R.id.a1id);
        a11 = findViewById(R.id.a1shijian);
        a2 = findViewById(R.id.a2);
        a22 = findViewById(R.id.a22);

        get();
    }

    private void get() {
        int kong = 6;
        String currentTime = format2.format(new Date());

        if ("1".equals(preferences.getString("A1", ""))) {
            kong--;
            a1.setText("苏A123456");
            a11.setText(currentTime);
            a1.setBackgroundResource(R.drawable.green);
            a11.setBackgroundResource(R.drawable.green);
        } else {
            a1.setText("");
            a11.setText("");
            a1.setBackgroundResource(R.drawable.dark);
            a11.setBackgroundResource(R.drawable.dark);
        }

        if ("1".equals(preferences.getString("A2", ""))) {
            kong--;
            a2.setText("苏C123456");
            a22.setText(currentTime);
            a2.setBackgroundResource(R.drawable.green);
            a22.setBackgroundResource(R.drawable.green);
        } else {
            a2.setText("");
            a22.setText("");
            a2.setBackgroundResource(R.drawable.dark);
            a22.setBackgroundResource(R.drawable.dark);
        }

        num.setText(String.valueOf(kong));
    }

    public void backk(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}