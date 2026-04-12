package com.example.newland.car;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Jilu extends AppCompatActivity {

    private ListView listView;
    private Spinner spinner;
    private Sql sql = new Sql(this);
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jilu);
        listView = findViewById(R.id.lisss);
        spinner = findViewById(R.id.sppp);

        String[] i = {"A1", "A2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, i);
        spinner.setAdapter(adapter);
    }

    public void to24(View view) {
        Calendar calendar = Calendar.getInstance();
        String off = format2.format(calendar.getTime());
        calendar.add(Calendar.HOUR, -24);
        setListView(sql.querytime(format2.format(calendar.getTime()), off));
    }

    public void to30(View view) {
        Calendar calendar = Calendar.getInstance();
        String off = format2.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        setListView(sql.querytime(format2.format(calendar.getTime()), off));
    }

    public void quer(View view) {
        String xx = spinner.getSelectedItem().toString();
        Log.d("TAG", "quer: ================" + xx);
        setListView(sql.query(xx));
    }

    private void setListView(List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    public void backl(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}