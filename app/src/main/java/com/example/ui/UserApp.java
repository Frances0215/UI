package com.example.ui;

import android.app.Application;

public class UserApp extends Application {
    private String mUserId;

    //全局变量用于确定当前是哪一个用户
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }
}
