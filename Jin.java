package com.example.newland.car;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Jin extends AppCompatActivity {

    private TextView red, green, carid, ontime, bian, outtime, monry, tv_show;
    private ImageView imageView;
    private String[] id1 = {"E2 80 68 94 00 00 50 22 44 B2 B8 8B", "苏A123456"};
    private String[] id2 = {"E2 80 68 94 00 00 40 15 9A 33 3D 5E", "苏C123456"};
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
    private String uhf;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private Sql sql = new Sql(this);

    private static boolean isFirstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jin);

        red = findViewById(R.id.red);
        green = findViewById(R.id.green);
        carid = findViewById(R.id.carid);
        ontime = findViewById(R.id.ontime);
        outtime = findViewById(R.id.outtime);
        bian = findViewById(R.id.bianaho);
        imageView = findViewById(R.id.men);
        monry = findViewById(R.id.money);
        tv_show = findViewById(R.id.textView5);

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();

        if (isFirstLaunch) {
            chushi();
            isFirstLaunch = false;
        }
        get();
    }

    private void get() {
        service.scheduleAtFixedRate(() -> {
            uhf = Http.get("uhf");
            String ji = Http.get("m_limit");
            runOnUiThread(() -> {
                if ("1".equals(ji)) chushi();
            });
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void jinchang(View view) {
        if (id1[0].equals(uhf)) jintuu(id1, "A1");
        else if (id2[0].equals(uhf)) jintuu(id2, "A2");
    }

    private void jintuu(String[] id, String bianNum) {
        carid.setText(id[1]);
        ontime.setText(format2.format(new Date()));
        bian.setText(bianNum);
        tv_show.setText("欢迎" + id[1] + "车主，请到" + bianNum + "车位停车！");

        editor.putString(bianNum, "1");
        editor.putLong(bianNum + "_startTime", System.currentTimeMillis());
        editor.apply();

        Http.post("m_pushrod_putt", "0");
        Http.post("m_multi_red", "0");
        Http.post("m_pushrod_back", "1");
        Http.post("m_pushrod_putt", "1");

        red.setBackgroundResource(R.drawable.dark1);
        green.setBackgroundResource(R.drawable.green1);
        imageView.setBackgroundResource(R.drawable.pic_cartoon_gate_2);
    }

    public void chuchang(View view) {
        chu();
        String name = id1[0].equals(uhf) ? id1[1] : (id2[0].equals(uhf) ? id2[1] : "");
        String bianNum = id1[0].equals(uhf) ? "A1" : "A2";

        if (!name.isEmpty()) {
            tv_show.setText(name + "车主，一路顺风！");
            editor.putString(bianNum, "0");
            editor.apply();
        }
        imageView.setBackgroundResource(R.drawable.pic_cartoon_gate_1);
    }

    private void chu() {
        outtime.setText(format2.format(new Date()));
        String currentBian = bian.getText().toString();

        long off = System.currentTimeMillis();
        long realStart = preferences.getLong(currentBian + "_startTime", off);

        long min = (off - realStart) / (60 * 1000);
        if (min <= 0) min = 1;
        monry.setText(String.valueOf(min));

        if (id1[0].equals(uhf) || id2[0].equals(uhf)) {
            sql.insert(carid.getText().toString(), ontime.getText().toString(),
                    outtime.getText().toString(), currentBian,
                    String.valueOf(min), monry.getText().toString());
        }
    }

    public void back(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void chushi() {
        Http.post("m_pushrod_back", "0");
        Http.post("m_steady_green", "0");
        Http.post("m_pushrod_putt", "1");
        Http.post("m_multi_red", "1");

        red.setBackgroundResource(R.drawable.red1);
        green.setBackgroundResource(R.drawable.dark1);
        imageView.setBackgroundResource(R.drawable.pic_cartoon_gate_1);

        tv_show.setText("");
        carid.setText("");
        ontime.setText("");
        outtime.setText("");
        bian.setText("");
        monry.setText("");
    }
}