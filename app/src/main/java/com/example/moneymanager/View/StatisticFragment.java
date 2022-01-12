package com.example.moneymanager.View;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.moneymanager.Model.Expense;
import com.example.moneymanager.Model.MyDBHelper;
import com.example.moneymanager.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StatisticFragment extends Fragment implements TextWatcher {
    static final String tb_name = "MoneyTable";
    private SQLiteDatabase db;
    private PieChart pieChart;

    private Cursor cur;
    private Calendar mcal;
    private TextView no_data_txv, selectdate, inflow_txv, outflow_txv;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistic_fragment, container, false);
        String timeStamp = new SimpleDateFormat("yyyy/M/d").format(Calendar.getInstance().getTime());
        pieChart = view.findViewById(R.id.pie_chart);
        inflow_txv = view.findViewById(R.id.show_inflow_txv);
        outflow_txv = view.findViewById(R.id.show_outflow_txv);
        no_data_txv = view.findViewById(R.id.no_matching_txv);

        selectdate = view.findViewById(R.id.choose_date);
        selectdate.setText(timeStamp);
        //日期字串的監聽函式
        selectdate.addTextChangedListener(this);
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //觸發DatePickerDialog
                mcal = Calendar.getInstance();
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mcal.set(year,monthOfYear,dayOfMonth);
                        selectdate.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
                    }
                },
                        mcal.get(Calendar.YEAR),
                        mcal.get(Calendar.MONTH),
                        mcal.get(Calendar.DATE)).show();
            }
        });

        //初始化整數陣列存放資料
        int numbers[] = {0,0,0,0,0,0,0};
        //存取DB的資料
        MyDBHelper dbHelper = new MyDBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        cur = db.rawQuery("SELECT * FROM "+tb_name,null);
        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                //篩選符合日期的資料
                if(cur.getString(2).equals(selectdate.getText().toString())){
                    //第0筆資料是索引值
                    switch (cur.getString(1)){
                        case "飲食":
                            numbers[0] = numbers[0] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        case "交通":
                            numbers[1] = numbers[1] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        case "娛樂":
                            numbers[2] = numbers[2] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        case "醫療":
                            numbers[3] = numbers[3] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        case "社交":
                            numbers[4] = numbers[4] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        default:
                            numbers[5] = numbers[5] + Integer.parseInt(cur.getString(3));
                            break;
                    }
                }
            }
        }

        if(numbers[5] == 0 && numbers[6] == 0){
            //顯示無符合資料字樣
            no_data_txv.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
        }else{
            no_data_txv.setVisibility(View.GONE);
            //更新textview資料
            inflow_txv.setText(String.valueOf(numbers[5]));
            outflow_txv.setText(String.valueOf(numbers[6]));
            //設置圓餅圖
            setupPieChart();
            //將資料傳給PieChart
            loadPieChartData(numbers[0],numbers[1],numbers[2],numbers[3],numbers[4] );
        }
        return view;
    }

    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("支出類別");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }

    private void loadPieChartData(Integer food, Integer transport, Integer entertainment, Integer medical, Integer social){
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        //將各類別加入圓餅圖中
        if(food != 0)
            entries.add(new PieEntry(food,"飲食"));
        if(transport != 0)
            entries.add(new PieEntry(transport,"交通"));
        if(entertainment != 0)
            entries.add(new PieEntry(entertainment,"娛樂"));
        if(medical != 0)
            entries.add(new PieEntry(medical,"醫療"));
        if(social !=0)
            entries.add(new PieEntry(social,"社交"));

        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "支出類別");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        //初始化整數陣列存放資料
        int numbers[] = {0, 0, 0, 0, 0, 0, 0};
        //存取DB的資料
        MyDBHelper dbHelper = new MyDBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        cur = db.rawQuery("SELECT * FROM " + tb_name, null);
        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                //篩選符合日期的資料
                if (cur.getString(2).equals(selectdate.getText().toString())) {
                    //第0筆資料是索引值
                    switch (cur.getString(1)) {
                        case "飲食":
                            numbers[0] = numbers[0] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        case "交通":
                            numbers[1] = numbers[1] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        case "娛樂":
                            numbers[2] = numbers[2] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        case "醫療":
                            numbers[3] = numbers[3] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        case "社交":
                            numbers[4] = numbers[4] + Integer.parseInt(cur.getString(3));
                            numbers[6] = numbers[6] + Integer.parseInt(cur.getString(3));
                            break;
                        default:
                            numbers[5] = numbers[5] + Integer.parseInt(cur.getString(3));
                            break;
                    }
                }
            }
        }

        if (numbers[5] == 0 && numbers[6] == 0) {
            inflow_txv.setText(String.valueOf(numbers[5]));
            outflow_txv.setText(String.valueOf(numbers[6]));
            //顯示無符合資料字樣
            no_data_txv.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
        } else {
            no_data_txv.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
            //更新textview資料
            inflow_txv.setText(String.valueOf(numbers[5]));
            outflow_txv.setText(String.valueOf(numbers[6]));
            //設置圓餅圖
            setupPieChart();
            //將資料透過arrayList傳給PieChart
            loadPieChartData(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4]);
        }
    }
}