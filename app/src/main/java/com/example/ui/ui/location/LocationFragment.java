package com.example.ui.ui.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ui.DBUtils;
import com.example.ui.Locate;
import com.example.ui.LocateManager;
import com.example.ui.R;
import com.example.ui.WarnActivity;
import com.example.ui.ui.mine.MineFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationFragment extends Fragment {

    private LocationViewModel homeViewModel;
    private String[] people = new String[]{"顾原","李兰","王凡一","肖存","宋明志","郑志伟"};
    private int[] icons={R.drawable.photo1,R.drawable.photo2,R.drawable.photo3,R.drawable.photo4,R.drawable.photo5,R.drawable.photo6};
    private ListView listView;
    private FloatingActionButton mFbWarn;

    private LocateManager mLocateManager;
    private Locate nowLocate;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(LocationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_location, container, false);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

//        if (mLocateManager == null) {
//            mLocateManager = new LocateManager(getActivity());
//            //mLocateManager.openConnect();                             //建立本地数据库
//        }

        //数据库操作必须要在一个新的线程中
        new Thread(new Runnable() {

            @Override
            public void run() {
//                DBUtils myDBUtil = new DBUtils();
//                mLocateManager.openConnect();
//                //得到最新的一个坐标
//                Locate myLocate = mLocateManager.getLocation();
//                Log.e("myLocate:",""+myLocate.getX()+","+myLocate.getY());
            }
            }).start();

        listView=(ListView)view.findViewById(R.id.mLvPeople);
        listView.setAdapter(new LocationFragment.MyBaseAdapter());

        mFbWarn=(FloatingActionButton)view.findViewById(R.id.mBtWarn);
        mFbWarn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WarnActivity.class);
                startActivity(intent);
            }
        });


    }

    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return people.length;
        }

        @Override
        public Object getItem(int position) {
            return people[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.activity_people_item,parent,false);
            TextView textView = (TextView)view.findViewById(R.id.mTvPeople);
            ImageView imageView = (ImageView)view.findViewById(R.id.people_image);
            
            textView.setText(people[position]);
            imageView.setBackgroundResource(icons[position]);

            return view;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mLocateManager != null){
//            mLocateManager.closeConnect();
            mLocateManager = null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//        if(mLocateManager == null){
//            mLocateManager = new LocateManager(getActivity());
////            mLocateManager = new LocateManager();
//            mLocateManager.openConnect();
//        }
    }
}