package com.example.moneymanager.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    private Context context;
    public static final int VERSION = 1;
    private static final String db_name = "MoneyDB";
    private static final String tb_name = "MoneyTable";
    private static final String[] COLNAME = new String[] {"type", "date", "num", "memo"};

    public MyDBHelper(@Nullable Context context) {
        super(context, db_name, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTable = "CREATE TABLE IF NOT EXISTS "+ tb_name +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLNAME[0] + " VARCHAR(5), " +
                COLNAME[1] + " VARCHAR(15), " +
                COLNAME[2] + " VARCHAR(25), " +
                COLNAME[3] + " VARCHAR(100))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        final String upgradeTable = "DROP TABLE IF EXISTS "+tb_name;
        db.execSQL(upgradeTable);
        onCreate(db);
    }

    public void close(SQLiteDatabase db) {
        db.close();
    }

    public String addExpense(String type, String date, String num, String memo) {
        //檢查輸入的資料是否為空
        if(type.length() == 0 || date == "選擇日期" || num.length() == 0){
            Toast.makeText(context,"除了備註之外所有欄位皆須填寫",Toast.LENGTH_SHORT).show();
            return "failed";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(4);

        cv.put(COLNAME[0], type);
        cv.put(COLNAME[1], date);
        cv.put(COLNAME[2], num);
        cv.put(COLNAME[3], memo);
        long result = db.insert(tb_name,null,cv);

        if(result == -1){
            Toast.makeText(context, "發生錯誤", Toast.LENGTH_SHORT).show();
            return "failed";
        }else{
            Toast.makeText(context, "新增成功", Toast.LENGTH_SHORT).show();
            return "success";
        }
    }

    public String updateExpense(int id, String type, String date, String num, String memo) {
        //檢查輸入的資料是否為空
        if(type.length() == 0 || date == "選擇日期" || num.length() == 0){
            Toast.makeText(context,"除了備註之外所有欄位皆須填寫",Toast.LENGTH_SHORT).show();
            return "failed";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(4);
        cv.put(COLNAME[0], type);
        cv.put(COLNAME[1], date);
        cv.put(COLNAME[2], num);
        cv.put(COLNAME[3], memo);

        long result = db.update(tb_name, cv, "_id="+id, null);
        if(result == -1){
            Toast.makeText(context, "發生錯誤", Toast.LENGTH_SHORT).show();
            return "failed";
        }else{
            Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
            return "success";
        }
    }

    public String deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(tb_name, "_id="+id, null);
        if(result == -1){
            Toast.makeText(context, "發生錯誤", Toast.LENGTH_SHORT).show();
            return "failed";
        }else{
            Toast.makeText(context, "刪除成功", Toast.LENGTH_SHORT).show();
            return "success";
        }
    }
}
