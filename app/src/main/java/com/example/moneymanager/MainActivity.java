package com.example.moneymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moneymanager.Model.MyDBHelper;
import com.example.moneymanager.View.ExpenseFragment;
import com.example.moneymanager.View.FragmentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyDBHelper dbHelper;
    private SQLiteDatabase database;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private ArrayList<Fragment> pageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processViews();
        processDB();
        configViewPager();
    }

    private void processDB() {
        //初始化資料庫
        dbHelper = new MyDBHelper(MainActivity.this);
        database = dbHelper.getWritableDatabase();
        dbHelper.close(database);
    }

    private void processViews() {
        //設置FloatingActionButton
        fab = this.findViewById(R.id.add_record_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_data = new Intent(MainActivity.this,New_Cost.class);
                startActivity(add_data);
                finish();
            }
        });
    }

    private void configViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("記帳紀錄");
        titles.add("統計圖表");
        titles.add("個人頁面");


        pageList = new ArrayList<>();
        //新增DailyView
        //TODO 傳入時間區間(1d,1w,1m ...)參數，根據區間篩選出符合條件的資料
        pageList.add(new ExpenseFragment());
        //新增WeeklyView
        pageList.add(new ExpenseFragment());
        //新增MonthlyView
        pageList.add(new ExpenseFragment());

        viewPager = this.findViewById(R.id.main_viewpager);
        tabLayout = this.findViewById(R.id.main_tablayout);

        //將tablayout與viewpager綁定再一起
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),getApplicationContext(),pageList));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(titles.get(0));
        tabLayout.getTabAt(1).setText(titles.get(1));
        tabLayout.getTabAt(2).setText(titles.get(2));

        //設定監聽器
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}