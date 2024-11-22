package com.example.dailyplanning.entity;

public class BillInfo {
//账单对象
    public int id;
    public String date;
    public int type;
    public double amount;
    public String remark;

    //账单类型：0代表收入，1代表支出
    public static final int BILL_TYPE_INCOME = 0;
    public static final int BILL_TYPE_COST = 1;

    @Override
    public String toString() {
        return "BillInfo{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", remark='" + remark + '\'' +
                '}';
    }
    public BillInfo(){};

    public BillInfo(String date, int type, double amount, String remark) {
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
    }
}
