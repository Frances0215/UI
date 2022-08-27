package com.example.ui;

import android.app.Application;

public class UserApp extends Application {
    private int mUserId;

    //全局变量用于确定当前是哪一个用户
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }
}
