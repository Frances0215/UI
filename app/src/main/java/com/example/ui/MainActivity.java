package com.example.ui;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.ui.ui.group.AddPeople;
import com.example.ui.ui.group.Older;
import com.example.ui.ui.mine.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.sql.Connection;


public class MainActivity extends BaseActivity {

    private MyDatabaseHelper dbHelper;
    private static final int DB_VERSION = 9;
    private static final String DB_NAME = "Info9.dp";
    private static final String DB_NAME2 = "uwb";
    private UserDataManager mUserDataManager;
    private LocateManager mLocateManager;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","sos");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_group, R.id.navigation_location, R.id.navigation_mine)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        if (mLocateManager == null) {
//            mLocateManager = new LocateManager(this);//建立本地数据库
//        }
        //mLocateManager.insertDemo();
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Locate myLocate = mLocateManager.getLocation();
//                Log.e("myLocate:",""+myLocate.getX()+myLocate.getY()+myLocate.getZ());
//            }
//        }).start();

//        if (mUserDataManager == null) {
//            mUserDataManager = new UserDataManager(this);
//            mUserDataManager.openDataBase();                              //建立本地数据库
//        }

//        UserData mUser = new UserData("邓茹心","123456789");
//        long i = mUserDataManager.insertUserData(mUser);
//
//        int id_m = mUserDataManager.findUserIdByName("邓茹心");
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo7);
//        String map=ImageUtil.imageToBase64(bitmap);
//        UserData mUser2 = new UserData("邓茹心","0",null,null,null,map);
//        boolean flag = mUserDataManager.updateUserData2(mUser2);
        MyDatabaseHelper openHelper= new MyDatabaseHelper(this, DB_NAME, null, DB_VERSION);
//
        Bitmap bitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.photo7);
        //String map=ImageUtil.imageToBase64(bitmap);
        Older people7 = new Older(null,"郑澄澄","73","13809458734","女","1954-10-09",bitmap7);
        openHelper.insertPeople(people7);

        Bitmap bitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.photo8);
        //String map=ImageUtil.imageToBase64(bitmap);
        Older people8 = new Older(null,"邓品","73","13809458734","女","1954-10-09",bitmap8);
        openHelper.insertPeople(people8);

        Bitmap bitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.photo9);
        //String map=ImageUtil.imageToBase64(bitmap);
        Older people9 = new Older(null,"王时兴","73","13809458734","男","1954-10-09",bitmap9);
        openHelper.insertPeople(people9);

        Bitmap bitmap10 = BitmapFactory.decodeResource(getResources(), R.drawable.photo10);
        //String map=ImageUtil.imageToBase64(bitmap);
        Older people10 = new Older(null,"周俊毅","73","13809458734","男","1954-10-09",bitmap10);
        openHelper.insertPeople(people10);

        Bitmap bitmap11 = BitmapFactory.decodeResource(getResources(), R.drawable.photo11);
        //String map=ImageUtil.imageToBase64(bitmap);
        Older people11 = new Older(null,"钟琴","73","13809458734","女","1954-10-09",bitmap11);
        openHelper.insertPeople(people11);

        Bitmap bitmap12 = BitmapFactory.decodeResource(getResources(), R.drawable.photo12);
        //String map=ImageUtil.imageToBase64(bitmap);
        Older people12 = new Older(null,"慕雪容","73","13809458734","女","1954-10-09",bitmap12);
        openHelper.insertPeople(people12);

    }

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }

}