package com.example.dailyplanning;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dailyplanning.database.BillDBHelper;
import com.example.dailyplanning.entity.BillInfo;
import com.example.dailyplanning.util.DateUtil;
import com.example.dailyplanning.util.ToastUtil;

import java.util.Calendar;

public class BillAddActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private TextView tv_date;
    private TextView tv_date_2;
    private Calendar calendar;
    private RadioGroup rg_type;
    private EditText et_remark;
    private EditText et_amount;
    private BillDBHelper mDBHelper;
    private TextView btn_clear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_add);
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("请填写账单");
        tv_option.setText("账单列表");

        tv_date = findViewById(R.id.tv_date);
        tv_date_2 = findViewById(R.id.tv_date_2);
        rg_type = findViewById(R.id.rg_type);
        et_remark = findViewById(R.id.et_remark);//说明内容
        et_amount = findViewById(R.id.et_amount);//金额
        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);

        //显示当前日期
        calendar = Calendar.getInstance();
        tv_date.setText(DateUtil.getDate(calendar));
        //点击弹出日期对话框
        tv_date.setOnClickListener(this);//TextView也可以设置点击事件
        tv_date_2.setOnClickListener(this);//TextView也可以设置点击事件
        //当点击”添加账单”时：
        tv_option.setOnClickListener(this);
        //当点击返回图标时：
        findViewById(R.id.iv_back).setOnClickListener(this);

        //数据库的连接：
        mDBHelper = BillDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();
    }

    @Override
    public void onClick(View view) {
        String date = tv_date.getText().toString();
        String remark = et_remark.getText().toString();
        String amount = et_amount.getText().toString();
        if(view.getId() == R.id.tv_date || view.getId() == R.id.tv_date_2){//点击日期
            DatePickerDialog dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get((Calendar.DAY_OF_MONTH)));
            dialog.show();
        }
        else if(view.getId() == R.id.btn_save){//点击保存按钮
            BillInfo bill = new BillInfo();
            bill.date = tv_date.getText().toString();//此时日期用的是字符串类型的
            bill.type = rg_type.getCheckedRadioButtonId() == R.id.rb_income ?
                    BillInfo.BILL_TYPE_INCOME : BillInfo.BILL_TYPE_COST;
            bill.remark = et_remark.getText().toString();
            bill.amount = Double.parseDouble(et_amount.getText().toString());
            if(mDBHelper.save(bill) > 0){
                ToastUtil.show(this,"添加账单成功！");
            }
        }
        else if(view.getId() == R.id.tv_option){//跳转到账单列表页面
            Intent intent = new Intent(this,BillPagerActivity.class);
            //防止重复跳转，采取栈顶清空方式
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(view.getId() == R.id.iv_back){//点击返回图片
            finish();//关闭当前界面
        }
        else if(view.getId() == R.id.btn_delete){//点击删除按钮:根据事项说明remark删除账单
            if(mDBHelper.deleteByremark(remark) > 0){//>0 表示至少一行被删去
                ToastUtil.show(this,"删除账单成功！");
            }
        }
        else if(view.getId() == R.id.btn_update){//点击修改按钮
            BillInfo bill = new BillInfo(date,
                    rg_type.getCheckedRadioButtonId() == R.id.rb_income ?
                    BillInfo.BILL_TYPE_INCOME : BillInfo.BILL_TYPE_COST,
                    Double.valueOf(amount),
                    remark);
            if(mDBHelper.updata(bill) > 0){
                ToastUtil.show(this,"修改账单成功！");
            }
        }
        else if(view.getId() == R.id.btn_clear){//点击清空账单按钮
            if(mDBHelper.deleteAll() > 0){//>0 表示至少一行被删去
                ToastUtil.show(this,"清空账单成功！");
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year,int month,int dayOfMonth) {
        //设置给文本显示（显示的是年月日）
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tv_date.setText(DateUtil.getDate(calendar));
    }

//    @Override
//    protected void onDestroy() {
//        //覆写onDestroy方法
//        super.onDestroy();
//        mDBHelper.closeLink();//关闭数据库的连接
//    }
}
