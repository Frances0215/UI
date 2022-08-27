package com.example.ui;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EnrollActivity extends AppCompatActivity {
//    private EditText mAccount;                        //用户名编辑
//    private EditText mPwd;                            //密码编辑
//    private EditText mPwdCheck;                       //密码编辑
//    private Button mSureButton;                       //确定按钮

    private UserDataManager mUserDataManager;         //用户数据管理类

    private Button mBtEnroll;
    private EditText mEtName;
    private EditText mEtPass;
    private EditText mEtSure;
    private Button mBtCancel;                     //取消按钮
    private ImageView mIvEye;
    private ImageView mIvReEye;
    private boolean isOpenEye = false;
    private boolean isOpenEye2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        mBtEnroll = (Button)findViewById(R.id.mBtEnroll);
        mEtName = (EditText)findViewById(R.id.mEtName);
        mEtPass = (EditText)findViewById(R.id.mEtPassword);
        mEtSure = (EditText)findViewById(R.id.mEtSure);
        mBtCancel = (Button)findViewById(R.id.mBtCancel);
        mIvEye = (ImageView)findViewById(R.id.mIvEye);
        mIvReEye = (ImageView)findViewById(R.id.mIvReEye);
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
        
        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Register_to_Login = new Intent(EnrollActivity.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                startActivity(intent_Register_to_Login);
                finish();
            }
        });

        mBtEnroll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                register_check();
            }
        });

        mIvEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye2) {
                    mIvEye.setSelected(true);
                    isOpenEye2 = true;
                    //密码可见
                    mEtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    mIvEye.setSelected(false);
                    isOpenEye2 = false;
                    //密码不可见
                    mEtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mIvReEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye) {
                    mIvReEye.setSelected(true);
                    isOpenEye = true;
                    //密码可见
                    mEtSure.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    mIvReEye.setSelected(false);
                    isOpenEye = false;
                    //密码不可见
                    mEtSure.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //用于添加上方标题栏中的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void register_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mEtName.getText().toString().trim();
            String userPwd = mEtPass.getText().toString().trim();
            String userPwdCheck = mEtSure.getText().toString().trim();
            //检查用户是否存在
            int count=mUserDataManager.findUserByName(userName);
            //用户已经存在时返回，给出提示文字
            if(count>0){
                Toast.makeText(this, getString(R.string.name_exist),Toast.LENGTH_SHORT).show();
                return ;
            }
            if(userPwd.equals(userPwdCheck)==false){     //两次密码输入不一样
                Toast.makeText(this, getString(R.string.pwd_not_the_same),Toast.LENGTH_SHORT).show();
                return ;
            } else {
                UserData mUser = new UserData(userName, userPwd);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser); //新建用户信息
                if (flag == -1) {
                    Toast.makeText(this, getString(R.string.register_fail),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, getString(R.string.register_success),Toast.LENGTH_SHORT).show();
                    Intent intent_Register_to_Login = new Intent(EnrollActivity.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                }
            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        if (mEtName.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtPass.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mEtSure.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //返回上一个界面
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



