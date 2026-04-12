package com.example.newland.car;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Sql extends SQLiteOpenHelper {
    private static String NAME = "data";
    private static String NAME_TABLE = "data";
    private static int VERSION = 1;

    public Sql(Context context) {
        super(context, NAME_TABLE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + NAME + "(id integer primary key autoincrement, carid text, ontime text, offtime text, bian text, time text, money text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insert(String name, String banji, String odd, String num, String jin, String money) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("carid", name);
        values.put("ontime", banji);
        values.put("offtime", odd);
        values.put("bian", num);
        values.put("time", jin);
        values.put("money", money);
        db.insert(NAME, null, values);
        db.close();
    }

    public List<String> query(String bian) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE bian = ?", new String[]{bian});
        return parseCursor(cursor, db);
    }

    public List<String> querytime(String on, String off) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NAME + " WHERE offtime BETWEEN ? AND ?", new String[]{on, off});
        return parseCursor(cursor, db);
    }

    // 将游标遍历逻辑合并为通用方法
    private List<String> parseCursor(Cursor cursor, SQLiteDatabase db) {
        List<String> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nn = cursor.getString(cursor.getColumnIndex("carid"));
                String bb = cursor.getString(cursor.getColumnIndex("ontime"));
                String nnn = cursor.getString(cursor.getColumnIndex("offtime"));
                String jj = cursor.getString(cursor.getColumnIndex("bian"));
                String tt = cursor.getString(cursor.getColumnIndex("time"));
                String mm = cursor.getString(cursor.getColumnIndex("money"));

                // 使用 String.format 替代繁琐的 + "\t\t\t" + 拼接
                String xx = String.format("%s\t\t\t%s\t\t\t%s\t\t\t%s\t\t\tt%s\t\t\t%s", nn, bb, nnn, jj, tt, mm);
                list.add(xx);
            }
            cursor.close();
        }
        db.close();
        return list;
    }
}