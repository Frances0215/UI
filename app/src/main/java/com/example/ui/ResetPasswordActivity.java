package com.example.ui;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd_old;                        //密码编辑
    private EditText mPwd_new;                        //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private UserDataManager mUserDataManager;         //用户数据管理类
    private ImageView mIvEye;
    private ImageView mIvReEye;
    private ImageView mIvReEyeSure;
    private boolean isOpenEye = false;
    private boolean isOpenEye2 = false;
    private boolean isOpenEye3 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAccount = (EditText) findViewById(R.id.mEtName);
        mPwd_old = (EditText) findViewById(R.id.mEtOldPassword);
        mPwd_new = (EditText) findViewById(R.id.mEtNewPwd);
        mPwdCheck = (EditText) findViewById(R.id.mEtNewPwdSure);

        mSureButton = (Button) findViewById(R.id.mBtSure);
        mCancelButton = (Button) findViewById(R.id.mBtCancel);

        mIvEye = (ImageView)findViewById(R.id.mIvEye) ;
        mIvReEye = (ImageView)findViewById(R.id.mIvReEye);
        mIvReEyeSure = (ImageView)findViewById(R.id.mIvReEyeSure);

        mSureButton.setOnClickListener(m_resetpwd_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_resetpwd_Listener);
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //mUserDataManager.openDataBase();                              //建立本地数据库
        }

        mIvEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye2) {
                    mIvEye.setSelected(true);
                    isOpenEye2 = true;
                    //密码可见
                    mPwd_old.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    mIvEye.setSelected(false);
                    isOpenEye2 = false;
                    //密码不可见
                    mPwd_old.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
                    mPwd_new.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    mIvReEye.setSelected(false);
                    isOpenEye = false;
                    //密码不可见
                    mPwd_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mIvReEyeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye) {
                    mIvReEyeSure.setSelected(true);
                    isOpenEye3 = true;
                    //密码可见
                    mPwdCheck.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    mIvReEyeSure.setSelected(false);
                    isOpenEye3 = false;
                    //密码不可见
                    mPwdCheck.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
    View.OnClickListener m_resetpwd_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        @RequiresApi(api = Build.VERSION_CODES.P)
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mBtSure:                       //确认按钮的监听事件
                    resetpwd_check();
                    break;
                case R.id.mBtCancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_ResetPwd_to_Login = new Intent(ResetPasswordActivity.this,LoginActivity.class) ;    //切换Resetpwd Activity至Login Activity
                    startActivity(intent_ResetPwd_to_Login);
                    finish();
                    break;
            }
        }


    };
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void resetpwd_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
            String userName = mAccount.getText().toString().trim();
            String userPwd_old = mPwd_old.getText().toString().trim();
            String userPwd_new = mPwd_new.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();
            int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd_old);
            if(result==1){                                             //返回1说明用户名和密码均正确,继续后续操作
                if(userPwd_new.equals(userPwdCheck)==false){           //两次密码输入不一样
                    Looper.prepare();
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.pwd_not_the_same),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return ;
                } else {
                    UserData mUser = new UserData(userName, userPwd_new);
                    //mUserDataManager.openDataBase();
                    boolean flag = mUserDataManager.updatePassword(mUser);
                    if (flag == false) {
                        Looper.prepare();
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.resetpwd_fail),Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else{
                        Looper.prepare();
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.resetpwd_success),Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        mUser.pwdResetFlag=1;
                        Intent intent_Register_to_Login = new Intent(ResetPasswordActivity.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                        startActivity(intent_Register_to_Login);
                        finish();
                    }
                }
            }else if(result==0){
                Looper.prepare();//返回0说明用户名和密码不匹配，重新输入
                Toast.makeText(ResetPasswordActivity.this, getString(R.string.pwd_not_fit_user),Toast.LENGTH_SHORT).show();
                Looper.loop();
                return;
            }
                }

            }).start();
        }
    }
//    @RequiresApi(api = Build.VERSION_CODES.P)
    public boolean isUserNameAndPwdValid() {

                String userName = mAccount.getText().toString().trim();
                //检查用户是否存在
//                int count=mUserDataManager.findUserByName(userName);
//                //用户不存在时返回，给出提示文字
//                if(count<=0){
//                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.name_not_exist),Toast.LENGTH_SHORT).show();
//                    return false;
//                    //result = false;
//                }
                if (mAccount.getText().toString().trim().equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.account_empty),Toast.LENGTH_SHORT).show();
                    return false;
                } else if (mPwd_old.getText().toString().trim().equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.pwd_empty),Toast.LENGTH_SHORT).show();
                    return false;
                } else if (mPwd_new.getText().toString().trim().equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.pwd_new_empty),Toast.LENGTH_SHORT).show();
                    return false;
                }else if(mPwdCheck.getText().toString().trim().equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.pwd_check_empty),Toast.LENGTH_SHORT).show();
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