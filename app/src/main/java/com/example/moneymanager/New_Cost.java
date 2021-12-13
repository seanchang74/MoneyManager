package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.moneymanager.Model.MyDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class New_Cost extends AppCompatActivity implements  View.OnClickListener, DatePickerDialog.OnDateSetListener {
    Calendar mcal;
    MyDBHelper db;
    TextView editDate;
    EditText num, memo;
    Spinner expensetype;
    FloatingActionButton sendfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_costs);

        //TODO 使用tablayout區分開收入與支出的資料
        num = this.findViewById(R.id.num_input);
        memo = this.findViewById(R.id.explain_input);
        sendfab = this.findViewById(R.id.send_fab);
        sendfab.setOnClickListener(this);
        editDate = this.findViewById(R.id.datepicker);
        editDate.setOnClickListener(this);
        expensetype = this.findViewById(R.id.moneyflow_spinner);
        //TODO spinner選擇不同金流類型更改種類描述的spinner
//        expensetype.setOnItemSelectedListener(this);

    }

//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }

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
                    Intent back = new Intent(New_Cost.this,MainPage.class);
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