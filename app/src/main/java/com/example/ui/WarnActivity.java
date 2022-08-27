package com.example.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.ui.WarnAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WarnActivity<actionBar> extends AppCompatActivity  implements WarnAddressAdapter.Callback , AdapterView.OnItemClickListener {

    private ListView mLvWarn;
    private List<WarnAddress> warnAddresses = new ArrayList<>();
    private Button mBtAdd;
    //private SlideButton mSbWarn;
    public WarnManager mWarnManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn);

        if (mWarnManager == null) {
            mWarnManager = new WarnManager(this);
            mWarnManager.openDataBase();                              //建立本地数据库
        }

        initWarnAddress();
        WarnAddressAdapter adapter = new WarnAddressAdapter(WarnActivity.this,warnAddresses,this);
        mLvWarn = (ListView) findViewById(R.id.mLvWarn);
        mLvWarn.setAdapter(adapter);
        mLvWarn.setOnItemClickListener(this);

        mBtAdd = (Button) findViewById(R.id.mBtAdd);
        mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarnActivity.this, AddActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });


        //用于添加上方标题栏中的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initWarnAddress(){
        mWarnManager.openDataBase();
        Cursor mCursor = mWarnManager.fetchAllWarnDatas();
        while(mCursor.moveToNext()){
            int warnId = mCursor.getInt(mCursor.getColumnIndex("warn_id"));
            String warnName = mCursor.getString(mCursor.getColumnIndex("name"));
            float warnAddX = mCursor.getFloat(mCursor.getColumnIndex("address_x"));
            float warnAddY = mCursor.getFloat(mCursor.getColumnIndex("address_y"));
            float warnRadius = mCursor.getFloat(mCursor.getColumnIndex("radius"));
            int warnGrade = mCursor.getInt(mCursor.getColumnIndex("grade"));
            //因为SQLite中并没有封装boolean类所以需要进行一定的转换
            boolean warnUse = mCursor.getString(mCursor.getColumnIndex("use")).equals("1");
            WarnAddress warnAddress = new WarnAddress(warnId,warnName,warnAddX,warnAddY,warnRadius,warnGrade,warnUse);
            warnAddresses.add(warnAddress);
        }
    }

    //用于添加上方标题栏中的返回按钮
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //整体的点击事件
    }

    //listview中的点击事件
    @Override
    public void click(View v) {
        //注意这里的数字必须要向左移动24位
        WarnAddress mWarnAddress = warnAddresses.get((Integer) v.getTag(R.id.one));
        boolean _use = (boolean)v.getTag(R.id.two);
        boolean use;
        if(_use){
            use = false;
        }else {
            use = true;
        }
        int warn_id = mWarnAddress.getId();
        mWarnManager.updateWarnUseById(warn_id,use);
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