package com.example.dailyplanning;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.dailyplanning.adapter.BillPagerAdapter;
import com.example.dailyplanning.database.BillDBHelper;
import com.example.dailyplanning.util.DateUtil;

import java.util.Calendar;

public class BillPagerActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView tv_month;
    private Calendar calendar;
    private ViewPager vp_bill;
    private BillDBHelper mDBHelper;
    private TextView tv_income;
    private TextView tv_cost;
    private TextView tv_sum;
    private TextView tv_date;
    private TextView tv_select;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pager);

        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_income = findViewById(R.id.tv_income);
        tv_cost = findViewById(R.id.tv_cost);
        tv_sum = findViewById(R.id.tv_sum);
        tv_date = findViewById(R.id.tv_date);
        tv_select = findViewById(R.id.tv_select);
        tv_title.setText("账单列表");
        tv_option.setText("管理账单");

        tv_month = findViewById(R.id.tv_month);
        //显示当前日期
        calendar = Calendar.getInstance();
        tv_month.setText(DateUtil.getMonth(calendar));
        tv_date.setText(DateUtil.getMonth(calendar));
        //点击弹出日期对话框
        tv_month.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_select.setOnClickListener((this));
        //当点击”添加账单”时：
        tv_option.setOnClickListener(this);
        //当点击返回图标时：
        findViewById(R.id.iv_back).setOnClickListener(this);
        //数据库的连接：
        mDBHelper = BillDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();
        double income = mDBHelper.getIncomeSum(DateUtil.getMonth(calendar));
        double cost = mDBHelper.getCostSum(DateUtil.getMonth(calendar));
        double sum = income-cost;
        tv_income.setText(String.format("收入：%d",(int)income));
        tv_cost.setText(String.format("支出：%d",(int)cost));
        tv_sum.setText(String.format("净收入：%d",(int)sum));
        //初始化翻页视图
        initViewPager();
    }

    //初始化翻页视图
    private void initViewPager(){
        //从布局视图中获取名为pts_bill的翻页标签栏
        PagerTabStrip pts_bill = findViewById(R.id.pts_bill);
        //设置翻页标签栏的文本大小
        pts_bill.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        vp_bill = findViewById(R.id.vp_bill);
        //设置适配器
        BillPagerAdapter adapter = new BillPagerAdapter(getSupportFragmentManager(),calendar.get(Calendar.YEAR));
        vp_bill.setAdapter(adapter);
        vp_bill.setCurrentItem(calendar.get(Calendar.MONTH));//设置当前的月份
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_month || view.getId() == R.id.tv_select){
            //弹出日期对话框
            DatePickerDialog dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get((Calendar.DAY_OF_MONTH)));
            dialog.show();
            double income = mDBHelper.getIncomeSum(DateUtil.getMonth(calendar));
            double cost = mDBHelper.getCostSum(DateUtil.getMonth(calendar));
            double sum = income-cost;
            tv_income.setText(String.format("收入：%d",(int)income));
            tv_cost.setText(String.format("支出：%d",(int)cost));
            tv_sum.setText(String.format("净收入：%d",(int)sum));
            tv_date.setText(DateUtil.getMonth(calendar));
        }
        else if(view.getId() == R.id.tv_option){
            //跳转到管理账单界面
            Intent intent = new Intent(this,BillAddActivity.class);
            //防止重复跳转，采取栈顶清空方式
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(view.getId() == R.id.iv_back){
            finish();//关闭当前界面
        }
        else if(view.getId() == R.id.tv_date){
            //弹出日期对话框
            DatePickerDialog dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get((Calendar.DAY_OF_MONTH)));
            dialog.show();
            double income = mDBHelper.getIncomeSum(DateUtil.getMonth(calendar));
            double cost = mDBHelper.getCostSum(DateUtil.getMonth(calendar));
            double sum = income-cost;
            tv_income.setText(String.format("收入：%d",(int)income));
            tv_cost.setText(String.format("支出：%d",(int)cost));
            tv_sum.setText(String.format("净收入：%d",(int)sum));
            tv_date.setText(DateUtil.getMonth(calendar));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year,int month,int dayOfMonth) {
        //设置给文本显示（显示的是年和月）
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tv_month.setText(DateUtil.getMonth(calendar));
        //设置翻页视图显示第几页
        vp_bill.setCurrentItem(month);
    }
    @Override
    protected void onDestroy() {
        //只在主界面写一次即可
        //覆写onDestroy方法
        super.onDestroy();
        mDBHelper.closeLink();//关闭数据库的连接
    }
}
