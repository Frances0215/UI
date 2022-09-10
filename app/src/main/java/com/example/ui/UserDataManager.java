package com.example.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;

public class UserDataManager {             //用户数据管理类
        //一些宏定义和声明
        private static final String TAG = "UserDataManager";
        private static final String TABLE_NAME = "users";
        public static final String USER_NAME = "name";
        public static final String USER_PWD = "password";
        public static final String USER_TELEPHONE = "telephone";
        public static final String USER_SEX = "sex";
        public static final String USER_WORKPLACE = "workplace";
        public static final String USER_PHOTO = "photo";
        public static final String USER_BIRTHDAY = "birthday";

        private static final int DB_VERSION = 9;
        private static final String DB_NAME = "uwb";
        private Context mContext = null;

        private MyDatabaseHelper mDatabaseHelper;
        private SQLiteDatabase mSQLiteDatabase;

        private DBUtils myDBUtil = new DBUtils();

        public UserDataManager(Context context) {
            mContext = context;
            Log.i(TAG,"UserDataManager construction!");
        }
        //打开数据库
//        public void openDataBase() throws SQLException {
//            mDatabaseHelper = new MyDatabaseHelper(mContext,DB_NAME,null,DB_VERSION);
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//        }
//        //关闭数据库
//        public void closeDataBase() throws SQLException {
//            mDatabaseHelper.close();
//        }

        //添加新用户，即注册
        public int insertUserData(UserData userData) {
//            String userName=userData.getUserName();
//            String userPwd=userData.getUserPwd();
//            ContentValues values = new ContentValues();
//            values.put(USER_NAME, userName);
//            values.put(USER_PWD, userPwd);
//            return mSQLiteDatabase.insert(TABLE_NAME, ID, values);

//            new Thread(new Runnable() {

//                @Override
//                public void run() {
            int value = 0;
                    Connection myConn = myDBUtil.getConn(DB_NAME);
                    String sql = "insert into users(telephone,password) values(?,?);";
                    try {
                        PreparedStatement pstm = myConn.prepareStatement(sql);
                        //赋值
                        pstm.setString(1,userData.getUserTelephone());
                        pstm.setString(2,userData.getUserPwd());

                        //执行更新数据库
                        value = pstm.executeUpdate();
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

//                }
//            }).start();
                return value;
        }

        //更新用户信息，如修改密码
        public boolean updatePassword(UserData userData) {
            //int id = userData.getUserId();
//            String userTelephone = userData.getUserTelephone();
//            String userPwd = userData.getUserPwd();
//            ContentValues values = new ContentValues();
//            values.put(USER_NAME, userName);
//            values.put(USER_PWD, userPwd);
//            return mSQLiteDatabase.update(TABLE_NAME, values,"name == ?", new String[]{userName}) > 0;
            //return mSQLiteDatabase.update(TABLE_NAME, values, ID + ”=” + id, null) > 0;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
            int value = 0;
                    Connection myConn = myDBUtil.getConn(DB_NAME);
                    String newPwd = userData.getUserPwd();
                    String telephone = userData.getUserTelephone();
                    String sql = "update users set pawd = '"+newPwd+"'"+"where telephone = '"+ telephone +"'";
                    try {
                        PreparedStatement pstm = myConn.prepareStatement(sql);
                        value = pstm.executeUpdate();//更新数据库中的数据
                        //closeDataBase();
                        pstm.close();
                        myConn.close();

                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                    }
//                }
//            }).start();
            return value > 0 ;
        }


        //更新用户资料
        public boolean updateUserData2(UserData userData) {
            //int id = userData.getUserId();
//            String userSex = userData.getUserSex();
//            String userTelephone = userData.getUserTelephone();
//            String userWorkplace = userData.getUserWorkplace();
//            String userPhoto = userData.getUserPhoto();
//            String userName = userData.getUserName();
//            String userBirthday = userData.getBirthday();
//            ContentValues values = new ContentValues();
//            values.put(USER_NAME, userName);
//            values.put(USER_SEX, userSex);
//            values.put(USER_TELEPHONE, userTelephone);
//            values.put(USER_WORKPLACE, userWorkplace);
//            values.put(USER_PHOTO, userPhoto);
//            values.put(USER_BIRTHDAY, userBirthday);
//            return mSQLiteDatabase.update(TABLE_NAME, values,"name == ?", new String[]{userName}) > 0;
            //return mSQLiteDatabase.update(TABLE_NAME, values, ID + ”=” + id, null) > 0;

            int result = 0;
            String userSex = userData.getUserSex();
            String userTelephone = userData.getUserTelephone();
            String userWorkplace = userData.getUserWorkplace();
            String userPhoto = userData.getUserPhoto();
            String userName = userData.getUserName();
            String userBirthday = userData.getBirthday();
            Connection myConn = myDBUtil.getConn(DB_NAME);
            String sql = "update users set birthday = '"+userBirthday+"',sex = '"+userSex+"', name = '"+userName+"', workplace = '"+userWorkplace+"', photo = '"+userPhoto+"' where telephone = '"+userTelephone+"'";
            try {
                PreparedStatement pstm = myConn.prepareStatement(sql);
                result = pstm.executeUpdate();//更新数据库中的数据
                //closeDataBase();

                pstm.close();
                myConn.close();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

            return result>0;

        }

        //根据id得到用户数据

        public UserData fetchUserData(String telephone) throws SQLException {
//            Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
//                    null, null, null, null);
//
//            CursorWindow cw = new CursorWindow("test", 500000000);
//            AbstractWindowedCursor ac = (AbstractWindowedCursor) mCursor;
//            ac.setWindow(cw);
//
//            while(mCursor.moveToNext()){
//                if(mCursor.getInt(mCursor.getColumnIndex("mine_id"))==id){
//                    return mCursor;
//                }
//            }
//            return null;
            UserData user = new UserData(null,null,null,null,null,null);
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
            Connection myConn = myDBUtil.getConn(DB_NAME);

            String sql = "select * from users where telephone = '"+ telephone+"'";
            try {
                PreparedStatement pstm = myConn.prepareStatement(sql);
                ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
                while (rSet.next()) {
                    //columnLabel是属性名
                    user.setUserTelephone(rSet.getString("telephone"));
                    user.setUserName(rSet.getString("name"));
                    user.setUserPwd(rSet.getString("password"));
                    user.setUserSex(rSet.getString("sex"));
                    user.setBirthday(rSet.getString("birthday"));
                    user.setUserWorkplace(rSet.getString("workplace"));
                }

                //closeDataBase();
                pstm.close();
                myConn.close();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }


//            }
//        }).start();
            return user;

        }

        //得到所有用户的数据
        public ArrayList<UserData> fetchAllUserDatas() {
//            return mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null,
//                    null);
            ArrayList<UserData> users = new ArrayList<>();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    Connection myConn = myDBUtil.getConn(DB_NAME);
                    String sql = "select * from users";
                    try {
                        PreparedStatement pstm = myConn.prepareStatement(sql);
                        ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
                        while (rSet.next()) {
                            UserData user = new UserData(null,null,null,null,null,null);

                            //columnLabel是属性名
                            user.setUserTelephone(rSet.getString("telephone"));
                            user.setUserName(rSet.getString("name"));
                            user.setUserPwd(rSet.getString("password"));
                            user.setUserSex(rSet.getString("sex"));
                            user.setBirthday(rSet.getString("birthday"));
                            user.setUserWorkplace(rSet.getString("workplace"));
                            users.add(user);
                        }

                        //closeDataBase();
                        pstm.close();
                        myConn.close();
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                    }


//                }
//            }).start();
            return users;
        }
//        //根据id删除用户
//        public boolean deleteUserData(int id) {
//            return mSQLiteDatabase.delete(TABLE_NAME, ID + "=" + id, null) > 0;
//        }
//        //根据用户名注销
//        public boolean deleteUserDatabyname(String name) {
//            return mSQLiteDatabase.delete(TABLE_NAME, USER_NAME + "=" + name, null) > 0;
//        }
//        //删除所有用户
//        public boolean deleteAllUserDatas() {
//            return mSQLiteDatabase.delete(TABLE_NAME, null, null) > 0;
//        }

        //
//        @RequiresApi(api = Build.VERSION_CODES.P)
//        public String getStringByColumnName(String columnName, int id) {
//            Cursor mCursor = fetchUserData(id);
//            int columnIndex = mCursor.getColumnIndex(columnName);
//            String columnValue = mCursor.getString(columnIndex);
//            mCursor.close();
//            return columnValue;
//        }


        //修改用户的一个属性
        public boolean updateUserDataById(String columnName, String telephone,
                                          String columnValue) {
//            ContentValues values = new ContentValues();
//            values.put(columnName, columnValue);
//            return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
            int value =0;
            Connection myConn = myDBUtil.getConn(DB_NAME);
            String sql = "update users set " + columnName+"='"+columnValue+"' where telephone = '"+telephone+"'";
            try {
                PreparedStatement pstm = myConn.prepareStatement(sql);
                value = pstm.executeUpdate();//更新数据库中的数据
                //closeDataBase();
                pstm.close();
                myConn.close();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
            return value>0;
        }

        //根据用户名找用户，可以判断注册时用户名是否已经存在

        public int findUserByName(String telephone){
//            Log.i(TAG,"findUserByName , userName="+userName);
//            Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
//                    null, null, null, null);
//
//            CursorWindow cw = new CursorWindow("test", 500000000);
//            AbstractWindowedCursor ac = (AbstractWindowedCursor) mCursor;
//            ac.setWindow(cw);
//
//            while(mCursor.moveToNext()){
//                boolean n = Objects.equals(mCursor.getString(mCursor.getColumnIndex("name")), userName);
//                if(n){
//                    mCursor.close();
//                    return 1;
//                }
//            }
//            return 0;
            int value = 0;
            Connection myConn = myDBUtil.getConn(DB_NAME);
            String sql = "select * from users where telephone = '"+ telephone+"'";
            try {
                PreparedStatement pstm = myConn.prepareStatement(sql);
                ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
                if(rSet.next()) value = 1;

                //closeDataBase();
                pstm.close();
                myConn.close();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

            return value;
        }

        //根据用户名返回用户id
//        @RequiresApi(api = Build.VERSION_CODES.P)
//        public int findUserIdByName(String userName){
//            Log.i(TAG,"findUserByName , userName="+userName);
//            Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
//                    null, null, null, null);
//
//            CursorWindow cw = new CursorWindow("test", 500000000);
//            AbstractWindowedCursor ac = (AbstractWindowedCursor) mCursor;
//            ac.setWindow(cw);
//            while(mCursor.moveToNext()){
//                boolean n = Objects.equals(mCursor.getString(mCursor.getColumnIndex("name")), userName);
//                if(n){
//                    int id = mCursor.getInt(mCursor.getColumnIndex("mine_id"));
//                    mCursor.close();
//                    return id;
//                }
//            }
//            return 0;
//        }

        //根据用户名和密码找用户，用于登录
        //@RequiresApi(api = Build.VERSION_CODES.P)
        public int findUserByNameAndPwd(String telephone, String pwd){
//            Log.i(TAG,"findUserByNameAndPwd");
//
//            Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
//                    null, null, null, null);
//
//            CursorWindow cw = new CursorWindow("test", 500000000);
//            AbstractWindowedCursor ac = (AbstractWindowedCursor) mCursor;
//            ac.setWindow(cw);
//            while(mCursor.moveToNext()){
//                boolean n = Objects.equals(mCursor.getString(mCursor.getColumnIndex("name")), userName);
//                boolean p = Objects.equals(mCursor.getString(mCursor.getColumnIndex("password")), pwd);
//                if(n&&p){
//                    mCursor.close();
//                    return 1;
//                }
//            }
//
//            return 0;

            int value = 0;
            Connection myConn = myDBUtil.getConn(DB_NAME);
            String sql = "select * from users where telephone = '"+ telephone+"' and password = '"+pwd+"'";
            try {
                PreparedStatement pstm = myConn.prepareStatement(sql);
                ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
                if(rSet.next()) value = 1;

                //closeDataBase();
                pstm.close();
                myConn.close();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

            return value;
        }

}
