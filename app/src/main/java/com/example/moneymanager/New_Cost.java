package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.moneymanager.Model.MyDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class New_Cost extends AppCompatActivity implements  View.OnClickListener, DatePickerDialog.OnDateSetListener {
    Calendar mcal;
    MyDBHelper db;
    TextView editDate;
    EditText num, memo;
    Spinner expensetype;
    String inflow[] = new String[] {"薪水","獎金", "投資", "買賣"};
    String outflow[] = new String[] {"飲食", "交通", "娛樂", "醫療", "社交"};
    ArrayAdapter<String> adapter;
    FloatingActionButton sendfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_costs);
        //選擇不同金流類型更改種類描述的spinner
        String type = getIntent().getExtras().getString("Flow_type");
        switch (type){
            case "inflow":
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,inflow);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                break;
            case "outflow":
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,outflow);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                break;
        }
        expensetype = this.findViewById(R.id.moneyflow_spinner);
        expensetype.setAdapter(adapter);
        expensetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //不用觸發任何效果
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //不用觸發任何效果
            }
        });

        num = this.findViewById(R.id.num_input);
        memo = this.findViewById(R.id.explain_input);
        sendfab = this.findViewById(R.id.send_fab);
        sendfab.setOnClickListener(this);
        editDate = this.findViewById(R.id.datepicker);
        editDate.setOnClickListener(this);
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
            case R.id.send_fab:
                //將資料寫入DB
                db = new MyDBHelper(New_Cost.this);
                String status = db.addExpense(expensetype.getSelectedItem().toString().trim(),
                              editDate.getText().toString().trim(),
                              num.getText().toString().trim(),
                              memo.getText().toString().trim());
                if(status == "success") {
                    Intent back = new Intent(New_Cost.this,MainActivity.class);
                    startActivity(back);
                    finish();
                }
            break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        mcal.set(year,monthOfYear,dayOfMonth);
        editDate.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
    }
}