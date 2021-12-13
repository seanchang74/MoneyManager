package com.example.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.moneymanager.Model.MyDBHelper;
import com.example.moneymanager.View.ExpenseFragment;
import com.example.moneymanager.View.FragmentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private MyDBHelper dbHelper;
    private SQLiteDatabase database;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private ArrayList<Fragment> pageList;
    private ActionBarDrawerToggle mToggle;//元件觸發

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        processViews();
        processDB();
        configViewPager();
    }

    private void processDB() {
        //初始化資料庫
        dbHelper = new MyDBHelper(MainPage.this);
        database = dbHelper.getWritableDatabase();
        dbHelper.close(database);
    }

    private void processViews() {
        //設置Navigation Drawer
        drawerLayout = this.findViewById(R.id.main_drawerlayout);
        mToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);//必須用字串資源檔
        drawerLayout.addDrawerListener(mToggle);//工具欄監聽事件
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//隱藏顯示箭頭返回
        NavigationView navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);//清單觸發監聽事件

        //設置FloatingActionButton
        fab = this.findViewById(R.id.add_record_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_data = new Intent(MainPage.this,New_Cost.class);
                startActivity(add_data);
                finish();
            }
        });
    }

    private void configViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("Daily");
        titles.add("Weekly");
        titles.add("Monthly");
        titles.add("Total");

        pageList = new ArrayList<>();
        //新增DailyView
        //TODO 傳入時間區間(1d,1w,1m ...)參數，根據區間篩選出符合條件的資料
        pageList.add(new ExpenseFragment());
        //新增WeeklyView
        pageList.add(new ExpenseFragment());
        //新增MonthlyView
        pageList.add(new ExpenseFragment());
        //TODO 還需要新增一個totalview，另外寫class
        pageList.add(new ExpenseFragment());

        viewPager = this.findViewById(R.id.main_viewpager);
        tabLayout = this.findViewById(R.id.main_tablayout);

        //將tablayout與viewpager綁定再一起
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),getApplicationContext(),pageList));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(titles.get(0));
        tabLayout.getTabAt(1).setText(titles.get(1));
        tabLayout.getTabAt(2).setText(titles.get(2));
        tabLayout.getTabAt(3).setText(titles.get(3));

        //設定監聽器
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent it = new Intent(MainPage.this, MainPage.class);
                startActivity(it);
                finish();
                return true;
            case R.id.navigation_record:
                Intent it1 = new Intent(MainPage.this, Record.class);
                startActivity(it1);
                finish();
                return true;
            case R.id.navigation_info:
                Intent it2 = new Intent(MainPage.this, Info.class);
                startActivity(it2);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){//當按下左上三條線或顯示工具列
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}