package com.example.moneymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Record extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;//元件觸發

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        drawerLayout = this.findViewById(R.id.record_drawerlayout);
        mToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);//必須用字串資源檔
        drawerLayout.addDrawerListener(mToggle);//工具欄監聽事件

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//隱藏顯示箭頭返回

        NavigationView navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);//清單觸發監聽事件
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent it = new Intent(Record.this, MainPage.class);
                startActivity(it);
                finish();
                return true;
            case R.id.navigation_record:
                Intent it1 = new Intent(Record.this, Record.class);
                startActivity(it1);
                finish();
                return true;
            case R.id.navigation_info:
                Intent it2 = new Intent(Record.this, Info.class);
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