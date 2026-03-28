package com.example.demo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 演示：多个控件共用同一个处理方法。
 *
 * 涵盖：Button、Switch、CheckBox、SeekBar、
 *        Spinner、RadioGroup、EditText (TextWatcher)
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        setupButtons();
        setupCompoundButtons();
        setupSeekBars();
        setupSpinners();
        setupRadioGroups();
        setupEditTexts();
    }

    // ──────────────────────────────────────────────
    // 1. Button — 共用 OnClickListener
    // ──────────────────────────────────────────────
    private void setupButtons() {
        Button btnSave   = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        View.OnClickListener clickListener = v -> handleClick(v.getId());
        btnSave.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
    }

    private void handleClick(int id) {
        if (id == R.id.btnSave) {
            show("按钮：保存");
        } else if (id == R.id.btnCancel) {
            show("按钮：取消");
        }
    }

    // ──────────────────────────────────────────────
    // 2. Switch / CheckBox — 共用 OnCheckedChangeListener
    // ──────────────────────────────────────────────
    private void setupCompoundButtons() {
        Switch   swWifi  = findViewById(R.id.swWifi);
        CheckBox cbAgree = findViewById(R.id.cbAgree);

        CompoundButton.OnCheckedChangeListener checkedListener =
                (buttonView, isChecked) -> handleChecked(buttonView.getId(), isChecked);

        swWifi.setOnCheckedChangeListener(checkedListener);
        cbAgree.setOnCheckedChangeListener(checkedListener);
    }

    private void handleChecked(int id, boolean isChecked) {
        if (id == R.id.swWifi) {
            show("WiFi: " + (isChecked ? "开" : "关"));
        } else if (id == R.id.cbAgree) {
            show("同意协议: " + (isChecked ? "已勾选" : "未勾选"));
        }
    }

    // ──────────────────────────────────────────────
    // 3. SeekBar — 共用 OnSeekBarChangeListener
    // ──────────────────────────────────────────────
    private void setupSeekBars() {
        SeekBar sb1 = findViewById(R.id.seekBar1);
        SeekBar sb2 = findViewById(R.id.seekBar2);

        SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                handleSeekBar(seekBar.getId(), progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        sb1.setOnSeekBarChangeListener(seekListener);
        sb2.setOnSeekBarChangeListener(seekListener);
    }

    private void handleSeekBar(int id, int progress) {
        if (id == R.id.seekBar1) {
            show("音量: " + progress);
        } else if (id == R.id.seekBar2) {
            show("亮度: " + progress);
        }
    }

    // ──────────────────────────────────────────────
    // 4. Spinner — 共用 OnItemSelectedListener
    // ──────────────────────────────────────────────
    private void setupSpinners() {
        Spinner spinnerCity  = findViewById(R.id.spinnerCity);
        Spinner spinnerColor = findViewById(R.id.spinnerColor);

        spinnerCity.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"北京", "上海", "广州"}));

        spinnerColor.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"红", "绿", "蓝"}));

        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSpinner(parent.getId(), parent.getItemAtPosition(position).toString());
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerCity.setOnItemSelectedListener(spinnerListener);
        spinnerColor.setOnItemSelectedListener(spinnerListener);
    }

    private void handleSpinner(int id, String selected) {
        if (id == R.id.spinnerCity) {
            show("城市: " + selected);
        } else if (id == R.id.spinnerColor) {
            show("颜色: " + selected);
        }
    }

    // ──────────────────────────────────────────────
    // 5. RadioGroup — 共用 OnCheckedChangeListener
    // ──────────────────────────────────────────────
    private void setupRadioGroups() {
        RadioGroup rgGender = findViewById(R.id.rgGender);
        RadioGroup rgSize   = findViewById(R.id.rgSize);

        RadioGroup.OnCheckedChangeListener radioListener =
                (group, checkedId) -> handleRadioGroup(group.getId(), checkedId);

        rgGender.setOnCheckedChangeListener(radioListener);
        rgSize.setOnCheckedChangeListener(radioListener);
    }

    private void handleRadioGroup(int groupId, int checkedId) {
        if (groupId == R.id.rgGender) {
            show("性别: " + (checkedId == R.id.rbMale ? "男" : "女"));
        } else if (groupId == R.id.rgSize) {
            show("尺寸: " + (checkedId == R.id.rbSmall ? "小" : "大"));
        }
    }

    // ──────────────────────────────────────────────
    // 6. EditText — 共用 TextWatcher
    // ──────────────────────────────────────────────
    private void setupEditTexts() {
        EditText etName  = findViewById(R.id.etName);
        EditText etPhone = findViewById(R.id.etPhone);

        // makeWatcher 工厂方法把 id 闭包进去，内部调用共用方法 handleTextChanged
        etName.addTextChangedListener(makeWatcher(R.id.etName));
        etPhone.addTextChangedListener(makeWatcher(R.id.etPhone));
    }

    /** 工厂方法：生成绑定了 viewId 的 TextWatcher，内部调用共用方法 */
    private TextWatcher makeWatcher(int viewId) {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                handleTextChanged(viewId, s.toString());
            }
        };
    }

    private void handleTextChanged(int id, String text) {
        if (id == R.id.etName) {
            show("姓名输入: " + text);
        } else if (id == R.id.etPhone) {
            show("电话输入: " + text);
        }
    }

    // ──────────────────────────────────────────────
    // 工具方法
    // ──────────────────────────────────────────────
    private void show(String msg) {
        tvResult.setText(msg);
    }
}
