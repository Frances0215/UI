package com.example.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Objects;

public class WarnManager {

    private static final String TAG = "WarnManager";
    private static final String TABLE_NAME = "Warn";
    public static final String ID = "warn_id";
    public static final String WARN_NAME = "name";
    public static final String WARN_ADDRESS_X = "address_x";
    public static final String WARN_ADDRESS_Y = "address_y";
    public static final String WARN_RADIUS = "radius";
    public static final String WARN_GRADE = "grade";
    public static final String WARN_USE = "use";
    //    public static final String SILENT = “silent”;
//    public static final String VIBRATE = “vibrate”;
    private static final int DB_VERSION = 9;
    private static final String DB_NAME = "Info9.dp";
    private Context mContext = null;

    private MyDatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public WarnManager(Context context) {
        mContext = context;
        Log.i(TAG,"WarnManager construction!");
    }
    //打开数据库
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new MyDatabaseHelper(mContext,DB_NAME,null,DB_VERSION);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }
    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    //添加新预警，即注册
    public long insertWarnData(WarnAddress warnAddress) {
        String warnName = warnAddress.getName();
        float warnAddress_x = warnAddress.getAddress_x();
        float warnAddress_y = warnAddress.getAddress_y();
        float warnRadius = warnAddress.getRadius();
        int warnGrade = warnAddress.getGrade();
        boolean warnUse = warnAddress.isUse();
        ContentValues values = new ContentValues();
        values.put(WARN_NAME, warnName);
        values.put(WARN_ADDRESS_X, warnAddress_x);
        values.put(WARN_ADDRESS_Y, warnAddress_y);
        values.put(WARN_RADIUS, warnRadius);
        values.put(WARN_GRADE,warnGrade);
        values.put(WARN_USE,warnUse);  //最开始设置时默认启动
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }

//

    //
    public Cursor fetchWarnData(int id) throws SQLException {
        Cursor mCursor = mSQLiteDatabase.query(false, TABLE_NAME, null, ID
                + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //
    public Cursor fetchAllWarnDatas() {
        return mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null,
                null);
    }

    //根据id删除用户
    public boolean deleteWarnData(int id) {
        return mSQLiteDatabase.delete(TABLE_NAME, ID + "=" + id, null) > 0;
    }

    //根据用户名注销
    public boolean deleteWarnDatabyname(String name) {
        return mSQLiteDatabase.delete(TABLE_NAME, WARN_NAME + "=" + name, null) > 0;
    }
    //删除所有用户
    public boolean deleteAllWarnDatas() {
        return mSQLiteDatabase.delete(TABLE_NAME, null, null) > 0;
    }

    //
    public String getStringByColumnName(String columnName, int id) {
        Cursor mCursor = fetchWarnData(id);
        int columnIndex = mCursor.getColumnIndex(columnName);
        String columnValue = mCursor.getString(columnIndex);
        mCursor.close();
        return columnValue;
    }
    //
    public boolean updateWarnDataById(String columnName, int id,
                                      String columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
        return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }

    //修改警告的use值
    public boolean updateWarnUseById(int id,boolean use){
        ContentValues values = new ContentValues();
        values.put("use",use);
       // String sql = "update Warn set use = ? where warn_id = ?"
        return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }
    //根据用户名找用户，可以判断注册时用户名是否已经存在
    public int findWarnByName(String warnName){
        Log.i(TAG,"findUserByName , userName="+warnName);
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
                null, null, null, null);

        while(mCursor.moveToNext()){
            boolean n = Objects.equals(mCursor.getString(mCursor.getColumnIndex("name")), warnName);
            if(n){
                mCursor.close();
                return 1;
            }
        }
        return 0;
    }
    //根据用户名和密码找用户，用于登录
//    public int findUserByNameAndPwd(String userName,String pwd){
//        Log.i(TAG,"findUserByNameAndPwd");
//
//        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
//                null, null, null, null);
//        boolean n=false;
//        boolean p = false;
//        while(mCursor.moveToNext()){
//            n = Objects.equals(mCursor.getString(mCursor.getColumnIndex("name")), userName);
//            p = Objects.equals(mCursor.getString(mCursor.getColumnIndex("password")), pwd);
//            if(n&&p){
//                mCursor.close();
//                return 1;
//            }
//        }
//
//        return 0;
//    }
}
