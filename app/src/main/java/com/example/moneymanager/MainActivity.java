package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.moneymanager.Model.MyDBHelper;
import com.example.moneymanager.View.ExpenseFragment;
import com.example.moneymanager.View.FragmentAdapter;
import com.example.moneymanager.View.PersonalFragment;
import com.example.moneymanager.View.StatisticFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyDBHelper dbHelper;
    private SQLiteDatabase database;
    private boolean isOpen = false;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Animation fabclose, fabopen, rotateforward, rotatebackward;
    private FloatingActionButton fab, fab1, fab2;
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
        fab1 = this.findViewById(R.id.inflow_fab);
        fab2 = this.findViewById(R.id.outflow_fab);
        //設置點擊動畫
        fabopen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabclose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateforward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotatebackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Intent input_inflow = new Intent(MainActivity.this,New_Cost.class);
                //傳遞Flow_type資料去改變New_Cost頁面的spinner選項
                input_inflow.putExtra("Flow_type","inflow");
                startActivity(input_inflow);
                finish();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Intent input_outflow = new Intent(MainActivity.this,New_Cost.class);
                //傳遞Flow_type資料去改變New_Cost頁面的spinner選項
                input_outflow.putExtra("Flow_type","outflow");
                startActivity(input_outflow);
                finish();
            }
        });
    }

    private void animateFab(){
        if(isOpen){
            fab.startAnimation(rotatebackward);
            fab1.startAnimation(fabclose);
            fab2.startAnimation(fabclose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen=false;
        }
        else{
            fab.startAnimation(rotateforward);
            fab1.startAnimation(fabopen);
            fab2.startAnimation(fabopen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen=true;
        }
    }

    private void configViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("記帳紀錄");
        titles.add("統計圖表");
        titles.add("作者資訊");

        pageList = new ArrayList<>();
        //記帳資料頁面
        pageList.add(new ExpenseFragment());
        //統計資料頁面
        pageList.add(new StatisticFragment());
        //個人資訊頁面
        pageList.add(new PersonalFragment());

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