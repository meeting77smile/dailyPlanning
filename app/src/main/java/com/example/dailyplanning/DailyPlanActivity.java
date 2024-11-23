package com.example.dailyplanning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DailyPlanActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_list;
    private Button btn_bill;
    private ImageView iv_money;
    private ImageView iv_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_plan);
        btn_list = findViewById(R.id.btn_list);
        btn_bill = findViewById(R.id.btn_bill);
        btn_list.setOnClickListener(this);
        btn_bill.setOnClickListener(this);
        iv_money = findViewById(R.id.iv_money);
        iv_money.setOnClickListener(this);
        iv_time = findViewById(R.id.iv_time);
        iv_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_bill || view.getId() == R.id.iv_money){
            //跳转到账单列表界面
            Intent intent = new Intent(this,BillPagerActivity.class);
            //防止重复跳转，采取栈顶清空方式
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(view.getId() == R.id.btn_list || view.getId() == R.id.iv_time) {
            //跳转到行程列表界面
            Intent intent = new Intent(this,ListPagerActivity.class);
            //防止重复跳转，采取栈顶清空方式
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
