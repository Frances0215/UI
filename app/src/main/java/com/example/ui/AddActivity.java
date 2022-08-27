package com.example.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    public WarnManager mWarnManager;
    public EditText mEtName;
    public EditText mEtAddX;
    public EditText mEtAddY;
    public EditText mEtRadius;
    public RadioButton mRbLow;
    public RadioButton mRbMid;
    public RadioButton mRbHigh;
    public Button mBtAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (mWarnManager == null) {
            mWarnManager = new WarnManager(this);
            mWarnManager.openDataBase();                              //建立本地数据库
        }

        mEtName = (EditText)findViewById(R.id.mEtName);
        mEtAddX = (EditText)findViewById(R.id.mEtAddX);
        mEtAddY = (EditText)findViewById(R.id.mEtAddY);
        mEtRadius = (EditText)findViewById(R.id.mEtRadius);

        mRbLow = (RadioButton)findViewById(R.id.mRbLow);
        mRbMid = (RadioButton)findViewById(R.id.mRbMid);
        mRbHigh = (RadioButton)findViewById(R.id.mRbHigh);
        mBtAdd = (Button)findViewById(R.id.mBtAdd);

        mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_check();
            }
        });

    }

    public void register_check() {                                //确认按钮的监听事件
        if (isWarnValid()) {
            String warnName = mEtName.getText().toString().trim();
            String _warnAddX = mEtAddX.getText().toString().trim();
            String _warnAddY = mEtAddY.getText().toString().trim();
            String _warnRadius = mEtRadius.getText().toString().trim();
            int warnGrade = 0;

            float warnAddX = Float.parseFloat(_warnAddX);
            float warnAddY = Float.parseFloat(_warnAddY);
            float warnRadius = Float.parseFloat(_warnRadius);

            //检查风险等级
            if(mRbLow.isChecked()){
                warnGrade = 1;
            }
            else if(mRbMid.isChecked()){
                warnGrade = 2;
            }
            else if(mRbHigh.isChecked()){
                warnGrade = 3;
            }else{
                Toast.makeText(this,"请选择风险等级",Toast.LENGTH_SHORT).show();
                return;
            }
            WarnAddress mWarnAddress = new WarnAddress(warnName,warnAddX,warnAddY,warnRadius,warnGrade,true);

            //检查用户是否存在
            int count=mWarnManager.findWarnByName(warnName);
            //用户已经存在时返回，给出提示文字
            if(count>0){
                Toast.makeText(this, "该地区已存在",Toast.LENGTH_SHORT).show();
                return ;
            }

            long flag = mWarnManager.insertWarnData(mWarnAddress); //新建用户信息
            if (flag == -1) {
                Toast.makeText(this, "创建新预警失败",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "创建新预警成功", Toast.LENGTH_SHORT).show();
                Intent intent_Register_to_Login = new Intent(AddActivity.this, WarnActivity.class);    //切换User Activity至Login Activity
                startActivity(intent_Register_to_Login);
                finish();
            }

        }
    }
    public boolean isWarnValid() {
        if (mEtName.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtAddX.getText().toString().trim().equals("")) {
            Toast.makeText(this, "名称不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mEtAddY.getText().toString().trim().equals("")) {
            Toast.makeText(this, "地址_x不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mEtRadius.getText().toString().trim().equals("")) {
            Toast.makeText(this, "地址_y不能为空",
                Toast.LENGTH_SHORT).show();
            return false;
        }else if(mEtRadius.getText().toString().trim().equals("")) {
            Toast.makeText(this, "范围不能为空",
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


    @Override
    protected void onResume() {
        if (mWarnManager == null) {
            mWarnManager = new WarnManager(this);
            mWarnManager.openDataBase();
        }
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        if (mWarnManager != null) {
            mWarnManager.closeDataBase();
            mWarnManager = null;
        }
        super.onPause();
    }

}