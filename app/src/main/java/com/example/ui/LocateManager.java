package com.example.ui;

import android.content.Context;
import android.util.Log;

import com.example.ui.ui.mine.ToolClass.L;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LocateManager {
    private static final String TAG = "LocateManager";
    private static final String DB_NAME = "uwb";
    private Context mContext = null;
    private DBUtils myDBUtil = new DBUtils();
    private Connection myConn;
//    public LocateManager(Context context){
//        mContext = context;
//    }

    public LocateManager() {

    }

    public void openConnect(){
        myConn = myDBUtil.getConn(DB_NAME);
    }

    public void closeConnect() {
        try {
            myConn.close();
        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }
    public Locate getLocation(){
        Locate myLocate = new Locate(0,0,0,0);
        //Connection myConn = myDBUtil.getConn(DB_NAME);

        String sql = "select * from positions order by id desc limit 1";
        try {
            PreparedStatement pstm = myConn.prepareStatement(sql);
            ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
            while (rSet.next()) {
                //columnLabel是属性名
                myLocate.setId(rSet.getInt("id"));
                myLocate.setX(rSet.getDouble("x"));
                myLocate.setY(rSet.getDouble("y"));
                myLocate.setZ(rSet.getDouble("z"));
            }

            //closeDataBase();
            pstm.close();
            myConn.close();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return myLocate;
    }

    public void insertDemo(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                if(myConn == null){
                    Log.e("test", "连接数据库失败");
                }
                String sql = "insert into location(x,y,z) values(?,?,?);";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    //赋值
                    pstm.setString(1,"2");
                    pstm.setString(2,"3");
                    pstm.setString(3,"4");

                    //执行更新数据库
                    int value = pstm.executeUpdate();
                    if (value > 0) {
                        Log.e("test", "register: 注册成功");
                    }else {
                        Log.e("test", "register: 注册失败");
                    }
                    myConn.close();
                    //closeDataBase();
                    pstm.close();


                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
