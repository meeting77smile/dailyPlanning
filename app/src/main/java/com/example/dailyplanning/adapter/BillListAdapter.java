package com.example.dailyplanning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dailyplanning.R;
import com.example.dailyplanning.entity.BillInfo;

import java.util.List;

public class BillListAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<BillInfo> mBillList;

    public BillListAdapter(Context context, List<BillInfo> billInfoList) {
        this.mContext = context;
        this.mBillList = billInfoList;
    }

    @Override
    public int getCount() {
        return mBillList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBillList.get(position).id;
    }

    @Override
    public long getItemId(int position) {
        return mBillList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder =new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bill,null);
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            holder.tv_remark = convertView.findViewById(R.id.tv_remark);
            holder.tv_amount = convertView.findViewById(R.id.tv_amount);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        BillInfo bill = mBillList.get(position);
        holder.tv_date.setText(bill.date);
        holder.tv_remark.setText(bill.remark);
        //bill.type为0表示收入，为1表示支出
        holder.tv_amount.setText(String.format("%s%d元",bill.type == 0 ? "+" : "-",(int)bill.amount));
        return convertView;
    }

    public final class ViewHolder{
        public TextView tv_date;
        public TextView tv_remark;
        public  TextView tv_amount;
    }
}
