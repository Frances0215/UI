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

import java.util.Objects;

public class UserDataManager {             //用户数据管理类
        //一些宏定义和声明
        private static final String TAG = "UserDataManager";
        private static final String TABLE_NAME = "Mine";
        public static final String ID = "mine_id";
        public static final String USER_NAME = "name";
        public static final String USER_PWD = "password";
        public static final String USER_TELEPHONE = "telephone";
        public static final String USER_SEX = "sex";
        public static final String USER_WORKPLACE = "workplace";
        public static final String USER_PHOTO = "photo";
        public static final String USER_BIRTHDAY = "birthday";

        private static final int DB_VERSION = 9;
        private static final String DB_NAME = "Info9.dp";
        private Context mContext = null;

        private MyDatabaseHelper mDatabaseHelper;
        private SQLiteDatabase mSQLiteDatabase;

        public UserDataManager(Context context) {
            mContext = context;
            Log.i(TAG,"UserDataManager construction!");
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
        //添加新用户，即注册
        public long insertUserData(UserData userData) {
            String userName=userData.getUserName();
            String userPwd=userData.getUserPwd();
            ContentValues values = new ContentValues();
            values.put(USER_NAME, userName);
            values.put(USER_PWD, userPwd);
            return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
        }
        //更新用户信息，如修改密码
        public boolean updateUserData(UserData userData) {
            //int id = userData.getUserId();
            String userName = userData.getUserName();
            String userPwd = userData.getUserPwd();
            ContentValues values = new ContentValues();
            values.put(USER_NAME, userName);
            values.put(USER_PWD, userPwd);
            return mSQLiteDatabase.update(TABLE_NAME, values,"name == ?", new String[]{userName}) > 0;
            //return mSQLiteDatabase.update(TABLE_NAME, values, ID + ”=” + id, null) > 0;
        }


        //更新用户资料
        public boolean updateUserData2(UserData userData) {
            //int id = userData.getUserId();
            String userSex = userData.getUserSex();
            String userTelephone = userData.getUserTelephone();
            String userWorkplace = userData.getUserWorkplace();
            String userPhoto = userData.getUserPhoto();
            String userName = userData.getUserName();
            String userBirthday = userData.getBirthday();
            ContentValues values = new ContentValues();
            values.put(USER_NAME, userName);
            values.put(USER_SEX, userSex);
            values.put(USER_TELEPHONE, userTelephone);
            values.put(USER_WORKPLACE, userWorkplace);
            values.put(USER_PHOTO, userPhoto);
            values.put(USER_BIRTHDAY, userBirthday);
            return mSQLiteDatabase.update(TABLE_NAME, values,"name == ?", new String[]{userName}) > 0;
            //return mSQLiteDatabase.update(TABLE_NAME, values, ID + ”=” + id, null) > 0;
        }

        //根据id得到用户数据
        @RequiresApi(api = Build.VERSION_CODES.P)
        public Cursor fetchUserData(int id) throws SQLException {
            Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
                    null, null, null, null);

            CursorWindow cw = new CursorWindow("test", 500000000);
            AbstractWindowedCursor ac = (AbstractWindowedCursor) mCursor;
            ac.setWindow(cw);

            while(mCursor.moveToNext()){
                if(mCursor.getInt(mCursor.getColumnIndex("mine_id"))==id){
                    return mCursor;
                }
            }
            return null;
        }

        //得到所有用户的数据
        public Cursor fetchAllUserDatas() {
            return mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null,
                    null);
        }
        //根据id删除用户
        public boolean deleteUserData(int id) {
            return mSQLiteDatabase.delete(TABLE_NAME, ID + "=" + id, null) > 0;
        }
        //根据用户名注销
        public boolean deleteUserDatabyname(String name) {
            return mSQLiteDatabase.delete(TABLE_NAME, USER_NAME + "=" + name, null) > 0;
        }
        //删除所有用户
        public boolean deleteAllUserDatas() {
            return mSQLiteDatabase.delete(TABLE_NAME, null, null) > 0;
        }

        //
        @RequiresApi(api = Build.VERSION_CODES.P)
        public String getStringByColumnName(String columnName, int id) {
            Cursor mCursor = fetchUserData(id);
            int columnIndex = mCursor.getColumnIndex(columnName);
            String columnValue = mCursor.getString(columnIndex);
            mCursor.close();
            return columnValue;
        }


        //修改用户的一个属性
        public boolean updateUserDataById(String columnName, int id,
                                          String columnValue) {
            ContentValues values = new ContentValues();
            values.put(columnName, columnValue);
            return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
        }

        //根据用户名找用户，可以判断注册时用户名是否已经存在
        @RequiresApi(api = Build.VERSION_CODES.P)
        public int findUserByName(String userName){
            Log.i(TAG,"findUserByName , userName="+userName);
            Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
                    null, null, null, null);

            CursorWindow cw = new CursorWindow("test", 500000000);
            AbstractWindowedCursor ac = (AbstractWindowedCursor) mCursor;
            ac.setWindow(cw);

            while(mCursor.moveToNext()){
                boolean n = Objects.equals(mCursor.getString(mCursor.getColumnIndex("name")), userName);
                if(n){
                    mCursor.close();
                    return 1;
                }
            }
            return 0;
        }

        //根据用户名返回用户id
        @RequiresApi(api = Build.VERSION_CODES.P)
        public int findUserIdByName(String userName){
            Log.i(TAG,"findUserByName , userName="+userName);
            Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
                    null, null, null, null);

            CursorWindow cw = new CursorWindow("test", 500000000);
            AbstractWindowedCursor ac = (AbstractWindowedCursor) mCursor;
            ac.setWindow(cw);
            while(mCursor.moveToNext()){
                boolean n = Objects.equals(mCursor.getString(mCursor.getColumnIndex("name")), userName);
                if(n){
                    int id = mCursor.getInt(mCursor.getColumnIndex("mine_id"));
                    mCursor.close();
                    return id;
                }
            }
            return 0;
        }

        //根据用户名和密码找用户，用于登录
        @RequiresApi(api = Build.VERSION_CODES.P)
        public int findUserByNameAndPwd(String userName, String pwd){
            Log.i(TAG,"findUserByNameAndPwd");

            Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, null,
                    null, null, null, null);

            CursorWindow cw = new CursorWindow("test", 500000000);
            AbstractWindowedCursor ac = (AbstractWindowedCursor) mCursor;
            ac.setWindow(cw);
            while(mCursor.moveToNext()){
                boolean n = Objects.equals(mCursor.getString(mCursor.getColumnIndex("name")), userName);
                boolean p = Objects.equals(mCursor.getString(mCursor.getColumnIndex("password")), pwd);
                if(n&&p){
                    mCursor.close();
                    return 1;
                }
            }

            return 0;
        }

}
