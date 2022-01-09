package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.moneymanager.Model.MyDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class Edit_New_Cost extends AppCompatActivity implements  View.OnClickListener, DatePickerDialog.OnDateSetListener{
    int id;
    Calendar mcal;
    MyDBHelper db;
    TextView editDate;
    EditText editNum, editMemo;
    Spinner expensetype;
    String type, date, num, memo;
    FloatingActionButton updatefab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_new_cost);

        id = getIntent().getExtras().getInt("id");
        type = getIntent().getExtras().getString("type");
        date = getIntent().getExtras().getString("date");
        num = getIntent().getExtras().getString("num");
        memo = getIntent().getExtras().getString("memo");

        editNum = this.findViewById(R.id.num_input);
        editMemo = this.findViewById(R.id.explain_input);
        editDate = this.findViewById(R.id.datepicker);
        editDate.setOnClickListener(this);
        expensetype = this.findViewById(R.id.moneyflow_spinner);
        updatefab = this.findViewById(R.id.update_fab);
        updatefab.setOnClickListener(this);

        editDate.setText(date);
        editNum.setText(num);
        editMemo.setText(memo);
        switch(type){ //TODO 存入DB時或者寫法有問題，寫法不能顯示支出選項
            case "收入":
                expensetype.setSelection(0);
            case "支出":
                expensetype.setSelection(1);
            default:
                expensetype.setSelection(0);
        }
    }

    //修改觸控監聽方法
    @Override
    public boolean onTouchEvent(MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            View view = this.getCurrentFocus();
            if (view != null) {
                //如果有開啟虛擬鍵盤則關閉
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        mcal.set(year,monthOfYear,dayOfMonth);
        editDate.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.datepicker:
                //觸發DatePickerDialog
                mcal = Calendar.getInstance();
                new DatePickerDialog(this,this,
                        mcal.get(Calendar.YEAR),
                        mcal.get(Calendar.MONTH),
                        mcal.get(Calendar.DATE)).show();
                break;
            case R.id.update_fab:
                //將資料寫入DB
                db = new MyDBHelper(Edit_New_Cost.this);
                String status = db.updateExpense(id,
                        expensetype.getSelectedItem().toString().trim(),
                        editDate.getText().toString().trim(),
                        editNum.getText().toString().trim(),
                        editMemo.getText().toString().trim());
                if(status == "success") {
                    Intent back = new Intent(Edit_New_Cost.this,MainActivity.class);
                    startActivity(back);
                    finish();
                }
                break;
        }
    }
}