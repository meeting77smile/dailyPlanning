package com.example.dailyplanning.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dailyplanning.entity.BillInfo;

import java.util.ArrayList;
import java.util.List;

public class BillDBHelper extends SQLiteOpenHelper {

    private static  final String DB_NAME="bill.db";
    //账单信息表
    private static final String TABLE_BILLS_INFO = "bill_info";
    private static final int DB_VERSION = 1;
    private static BillDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;//用来读的
    private SQLiteDatabase mWDB = null;//用来写的
    private BillDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    //利用单例模式获取数据库帮助器的唯一实例
    public static BillDBHelper getInstance(Context context){
        if(mHelper == null){
            mHelper = new BillDBHelper(context);
        }
        return mHelper;
    }

    //打开数据库的读连接
    public SQLiteDatabase openReadLink(){
        if(mRDB == null || !mRDB.isOpen()){
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    //打开数据库的写连接
    public SQLiteDatabase openWriteLink(){
        if(mWDB == null || !mWDB.isOpen()){
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    //关闭数据库连接
    public void closeLink(){
        if(mRDB != null && mRDB.isOpen()){
            mRDB.close();
            mRDB = null;
        }
        if(mWDB != null && mWDB.isOpen()){
            mWDB.close();
            mWDB = null;
        }
    }
    //创建数据库，执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_BILLS_INFO +"("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " date VARCHAR NOT NULL," +
                " type INTEGER NOT NULL," +
                " amount DOUBLE NOT NULL," +
                " remark VARCHAR NOT NULL);" ;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库的版本更新时会执行onUpgrade函数，如DB_VERSION由1变为2时
        //一次只能增加一条字段，故要执行两次
        String sql = "ALTER TABLE " + TABLE_BILLS_INFO + " ADD COLUMN phone VARCHAR;";
        db.execSQL(sql);
        sql = "ALTER TABLE " + TABLE_BILLS_INFO + " ADD COLUMN password VARCHAR;";
        db.execSQL(sql);
    }

    //保存一条记账记录
    public long save(BillInfo bill){
        ContentValues cv = new ContentValues();
        cv.put("date",bill.date);
        cv.put("type",bill.type);
        cv.put("amount",bill.amount);
        cv.put("remark",bill.remark);
        return  mWDB.insert(TABLE_BILLS_INFO,null,cv);
    }

    @SuppressLint("Range")
    public List<BillInfo> queryByMonth(String yearMonth){//根据月份查询该月的所有账单
        List<BillInfo> list = new ArrayList<>();

        //SQL语句：select * from bill_info where date like '2035-09%'
        String sql ="select * from "+TABLE_BILLS_INFO + " where date like '"+yearMonth+"%'";
        Log.d("bill",sql);
        Cursor cursor=mRDB.rawQuery(sql,null);
        while (cursor.moveToNext()){
            BillInfo bill = new BillInfo();
            bill.id = cursor.getInt(cursor.getColumnIndex("_id"));
            bill.date = cursor.getString(cursor.getColumnIndex("date"));
            bill.type = cursor.getInt(cursor.getColumnIndex("type"));
            bill.amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            bill.remark = cursor.getString(cursor.getColumnIndex("remark"));
            list.add(bill);
        }
        return list;
    }
    public long deleteByremark(String remark){
        //删除所有数据:
        //mWDB.delete(TABLE_NAME,"1=1",null);//1肯定=1,故每一行都满足条件，都可以删除
        return mWDB.delete(TABLE_BILLS_INFO,"remark=?",new String[]{remark});//根据name来删除
    }
    public long deleteAll(){//删除所有数据
        //删除所有数据:
        return mWDB.delete(TABLE_BILLS_INFO,"1=1",null);//1肯定=1,故每一行都满足条件，都可以删除
    }
    public long updata(BillInfo bill){
        ContentValues values = new ContentValues();
        values.put("date",bill.date);
        values.put("type",bill.type);
        values.put("amount",bill.amount);
        values.put("remark",bill.remark);
        return mWDB.update(TABLE_BILLS_INFO,values,"remark=?",new String[]{bill.remark});
    }

    public double getIncomeSum(String yearMonth){
        List<BillInfo> list = queryByMonth(yearMonth);
        double sum = 0.0;
        for(int i=0;i<list.size();i++){
            BillInfo bill = list.get(i);
            if(bill.type == 0){
                sum += bill.amount;
            }
        }
        return sum;
    }
    public double getCostSum(String yearMonth){
        List<BillInfo> list = queryByMonth(yearMonth);
        double sum = 0.0;
        for(int i=0;i<list.size();i++){
            BillInfo bill = list.get(i);
            if(bill.type == 1){
                sum += bill.amount;
            }
        }
        return sum;
    }
}
