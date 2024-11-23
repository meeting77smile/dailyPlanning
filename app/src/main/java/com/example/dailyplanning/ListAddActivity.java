package com.example.dailyplanning;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dailyplanning.database.ListDBHelper;
import com.example.dailyplanning.entity.ListInfo;
import com.example.dailyplanning.util.DateUtil;
import com.example.dailyplanning.util.ToastUtil;

import java.util.Calendar;
import com.example.dailyplanning.ListPagerActivity;

public class ListAddActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private TextView tv_date_list;
    private TextView tv_date_list_2;
    private Calendar calendar;
    private EditText et_des_list;
    private EditText et_time;
    private ListDBHelper mDBHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("安排事项");
        tv_option.setText("待做事项");
        tv_date_list = findViewById(R.id.tv_date_list);
        tv_date_list_2 = findViewById(R.id.tv_date_list_2);
        et_des_list = findViewById(R.id.et_des_list);//事项描述
        et_time = findViewById(R.id.et_time);//所需时间
        findViewById(R.id.btn_save_list).setOnClickListener(this);
        findViewById(R.id.btn_delete_list).setOnClickListener(this);
        findViewById(R.id.btn_update_list).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        //显示当前日期
        calendar = Calendar.getInstance();
        tv_date_list.setText(DateUtil.getDate(calendar));
        //点击弹出日期对话框
        tv_date_list.setOnClickListener(this);//TextView也可以设置点击事件
        tv_date_list_2.setOnClickListener(this);//TextView也可以设置点击事件
        //当点击”待做事项”时：
        tv_option.setOnClickListener(this);
        //当点击返回图标时：
        findViewById(R.id.iv_back).setOnClickListener(this);

        //数据库的连接：
        mDBHelper = ListDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

    }

    @Override
    public void onClick(View view) {
        String date = tv_date_list.getText().toString();
        String des = et_des_list.getText().toString();
        String time = et_time.getText().toString();
        if(view.getId() == R.id.tv_date_list || view.getId() == R.id.tv_date_list_2){//点击日期
            DatePickerDialog dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get((Calendar.DAY_OF_MONTH)));
            dialog.show();
        }
        else if(view.getId() == R.id.btn_save_list){//点击保存按钮
            ListInfo listInfo = new ListInfo();
            listInfo.date = tv_date_list.getText().toString();//此时日期用的是字符串类型的
            listInfo.description = et_des_list.getText().toString();
            listInfo.time = Double.parseDouble(et_time.getText().toString());
            if(mDBHelper.save(listInfo) > 0){
                ToastUtil.show(this,"添加事项成功！");
            }
        }
        else if(view.getId() == R.id.tv_option){//跳转到事项列表页面
            Intent intent = new Intent(this,ListPagerActivity.class);
            //防止重复跳转，采取栈顶清空方式
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(view.getId() == R.id.iv_back){//点击返回图片
            finish();//关闭当前界面
        }
        else if(view.getId() == R.id.btn_delete_list){//点击删除按钮:根据事项说明remark删除账单
            long del_num = mDBHelper.deleteByDes(des);
            if(del_num > 0){//>0 表示至少一行被删去
                ToastUtil.show(this,"删除事项成功！");
                ListPagerActivity.finished_list+= (int)del_num;//计算已完成事项
            }
        }
        else if(view.getId() == R.id.btn_update_list){//点击修改按钮
            ListInfo listInfo = new ListInfo(date,Double.valueOf(time),des);
            if(mDBHelper.updata(listInfo) > 0){
                ToastUtil.show(this,"修改事项成功！");
            }
        }
        else if(view.getId() == R.id.btn_clear){//点击清空按钮
            long num = mDBHelper.deleteAll();
            if(num > 0){//>0 表示至少一行被删去
                ToastUtil.show(this,"清空事项成功！");
                ListPagerActivity.finished_list+= (int)num;//计算已完成事项
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year,int month,int dayOfMonth) {
        //设置给文本显示（显示的是年月日）
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tv_date_list.setText(DateUtil.getDate(calendar));
    }

/*    @Override
    protected void onDestroy() {
        //覆写onDestroy方法
        super.onDestroy();
        mDBHelper.closeLink();//关闭数据库的连接
    }*/
}
