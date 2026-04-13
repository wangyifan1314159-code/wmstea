# Android 多控件共用一个方法 — 完整示例

本示例演示如何让 **多个控件共用同一个处理方法**，适用场景：减少重复代码、统一事件分发。

## 覆盖控件

| 控件 | 监听器接口 | 共用方法 |
|------|-----------|---------|
| `Button` | `View.OnClickListener` | `handleClick(int id)` |
| `Switch` / `CheckBox` | `CompoundButton.OnCheckedChangeListener` | `handleChecked(int id, boolean isChecked)` |
| `SeekBar` | `SeekBar.OnSeekBarChangeListener` | `handleSeekBar(int id, int progress)` |
| `Spinner` | `AdapterView.OnItemSelectedListener` | `handleSpinner(int id, String selected)` |
| `RadioGroup` | `RadioGroup.OnCheckedChangeListener` | `handleRadioGroup(int groupId, int checkedId)` |
| `EditText` | `TextWatcher` (工厂方法) | `handleTextChanged(int id, String text)` |

## 核心思路

```
一个监听器实例  →  绑定多个控件  →  在回调里用 getId() 区分是谁触发
```

### 1. Button（点击）

```java
View.OnClickListener clickListener = v -> handleClick(v.getId());
btnSave.setOnClickListener(clickListener);
btnCancel.setOnClickListener(clickListener);

private void handleClick(int id) {
    if (id == R.id.btnSave)   { /* 保存 */ }
    else if (id == R.id.btnCancel) { /* 取消 */ }
}
```

### 2. Switch / CheckBox（选中变化）

```java
CompoundButton.OnCheckedChangeListener checkedListener =
        (buttonView, isChecked) -> handleChecked(buttonView.getId(), isChecked);

swWifi.setOnCheckedChangeListener(checkedListener);
cbAgree.setOnCheckedChangeListener(checkedListener);

private void handleChecked(int id, boolean isChecked) {
    if (id == R.id.swWifi)  { /* WiFi 开/关 */ }
    else if (id == R.id.cbAgree) { /* 同意协议 */ }
}
```

### 3. SeekBar（进度变化）

```java
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

private void handleSeekBar(int id, int progress) {
    if (id == R.id.seekBar1) { /* 音量 */ }
    else if (id == R.id.seekBar2) { /* 亮度 */ }
}
```

### 4. Spinner（下拉选择）

```java
AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        handleSpinner(parent.getId(), parent.getItemAtPosition(position).toString());
    }
    @Override public void onNothingSelected(AdapterView<?> parent) {}
};

spinnerCity.setOnItemSelectedListener(spinnerListener);
spinnerColor.setOnItemSelectedListener(spinnerListener);

private void handleSpinner(int id, String selected) {
    if (id == R.id.spinnerCity)  { /* 城市 */ }
    else if (id == R.id.spinnerColor) { /* 颜色 */ }
}
```

### 5. RadioGroup（单选变化）

```java
RadioGroup.OnCheckedChangeListener radioListener =
        (group, checkedId) -> handleRadioGroup(group.getId(), checkedId);

rgGender.setOnCheckedChangeListener(radioListener);
rgSize.setOnCheckedChangeListener(radioListener);

private void handleRadioGroup(int groupId, int checkedId) {
    if (groupId == R.id.rgGender) { /* 男/女 */ }
    else if (groupId == R.id.rgSize) { /* 大/小 */ }
}
```

### 6. EditText（文字变化 TextWatcher）

> `TextWatcher` 不携带控件引用，所以用**工厂方法**把 `id` 闭包进去，内部调用同一个 `handleTextChanged`。

```java
etName.addTextChangedListener(makeWatcher(R.id.etName));
etPhone.addTextChangedListener(makeWatcher(R.id.etPhone));

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
    if (id == R.id.etName)  { /* 姓名 */ }
    else if (id == R.id.etPhone) { /* 电话 */ }
}
```

## 文件结构

```
android-widgets-demo/
└── app/src/main/
    ├── java/com/example/demo/
    │   └── MainActivity.java     ← 所有控件的完整实现
    └── res/layout/
        └── activity_main.xml     ← 对应的布局文件
```
