package com.example.newland.car;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private TextView time, t, h, l;
    private SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
    private boolean is = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = findViewById(R.id.timee);
        t = findViewById(R.id.t);
        h = findViewById(R.id.h);
        l = findViewById(R.id.l);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> time.setText(format1.format(new Date())));
            }
        }, 0, 1000);

        if (is) {
            chushi();
            is = false;
        }
        get();
    }

    private void get() {
        service.scheduleAtFixedRate(() -> {
            String uhf = Http.get("uhf");
            String ll = Http.get("m_light");
            String te = Http.get("m_temp");
            String hh = Http.get("m_hum");

            runOnUiThread(() -> {
                try {
                    t.setText(String.format("%.0f", Float.parseFloat(te)));
                    h.setText(String.format("%.0f", Float.parseFloat(hh)));
                    l.setText(String.format("%.0f", Float.parseFloat(ll)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            });
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void jinchu(View view) {
        startActivity(new Intent(this, Jin.class));
    }

    public void tingjifei(View view) {
        startActivity(new Intent(this, Jilu.class));
    }

    public void shi(View view) {
        startActivity(new Intent(this, shiyong.class));
    }

    private void chushi() {
        Http.post("m_pushrod_back", "0");
        Http.post("m_steady_green", "0");
        Http.post("m_pushrod_putt", "1");
        Http.post("m_multi_red", "1");
    }
}