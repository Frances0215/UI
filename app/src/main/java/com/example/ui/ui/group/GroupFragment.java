package com.example.ui.ui.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.AbstractWindowedCursor;
import android.database.CrossProcessCursorWrapper;
import android.database.Cursor;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ui.ImageUtil;
import com.example.ui.MyDatabaseHelper;
import com.example.ui.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupFragment extends Fragment {

    private GroupViewModel dashboardViewModel;
    private ListView listview;
    private FloatingActionButton fab;
    private ImageButton ib_search;
    List<Older> personList;
    ImageUtil iut=new ImageUtil();

    private static final int DB_VERSION = 9;
    private static final String DB_NAME = "Info9.dp";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(GroupViewModel.class);
        View root = inflater.inflate(R.layout.group_fragment, container, false);

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Older> list=getPersonList();
        //ListViewAdapter adapter=new ListViewAdapter(getActivity(),R.layout.fragment_group,list);
        listview = (ListView)view.findViewById(R.id.listview);
        List<HashMap<String,Object>> data=new ArrayList<HashMap<String,Object>>();
        if(personList!=null){
            for(Older older:personList){
                HashMap<String,Object> items=new HashMap<String,Object>();
                items.put("photo",older.getPhoto());
                items.put("id",older.getId());
                items.put("name",older.getName());

                data.add(items);
            }
            SimpleAdapter adapter=new SimpleAdapter(getActivity(),data,R.layout.people_listview,new String[]{"photo","name"},new int[]{R.id.img,R.id.name});
            adapter.setViewBinder(new MyViewBinder());
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //用Bundle传数据
                    Bundle bundle=new Bundle();
                    bundle.putString("_id",personList.get(i).getId());
                    bundle.putString("name",personList.get(i).getName());
                    bundle.putString("age",personList.get(i).getAge());
                    bundle.putString("telephone",personList.get(i).getTelephone());
                    bundle.putString("sex",personList.get(i).getSex());
                    bundle.putString("birthday",personList.get(i).getBirthtime());
                    // bundle.putString("photo",iut.imageToBase64(personList.get(i).getPhoto()));
                    //bundle.putParcelable("photo",personList.get(i).getPhoto());
                /*    ByteArrayOutputStream os=new ByteArrayOutputStream();
                    personList.get(i).getPhoto().compress(Bitmap.CompressFormat.PNG,50,os);
                    byte[] photoByte=os.toByteArray();
                    bundle.putByteArray("photo",photoByte); */
                    Intent intent=new Intent();
                    intent.putExtras(bundle);
                /*    Older od=personList.get(i);
                    intent.putExtra("_id",od.getId());
                    intent.putExtra("name",od.getName());
                    intent.putExtra("age",od.getAge());
                    intent.putExtra("telephone",od.getTelephone());
                    intent.putExtra("sex",od.getSex());
                    intent.putExtra("birthday",od.getBirthtime());
                    ByteArrayOutputStream os=new ByteArrayOutputStream();
                    od.getPhoto().compress(Bitmap.CompressFormat.PNG,100,os);
                    byte[] photoByte=os.toByteArray();
                    intent.putExtra("photo",photoByte);*/
                    intent.setClass(getActivity(),PeopleInformation.class);
                    startActivity(intent);
                }
            });
            fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),AddPeople.class);
                    startActivity(intent);
                }
            });
            //  ib_search=(ImageButton)root.findViewById(R.id.ib_search);
        }
        //      listview.setAdapter(adapter);
  /*      final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public List<Older> getPersonList() {
        personList = new ArrayList<Older>();
      Bitmap photow=BitmapFactory.decodeResource(this.getResources(),R.drawable.wanglaotou);
        /*Older od=new Older("1","王老头","66","123456789","男","195600101",photow);
        od.setId("5");
        od.setName("王老头");
        od.setAge("80");
        od.setTelephone("123456789");
        od.setSex("男");
        od.setBirthtime("1932/6/29");
        od.setPhoto(photow); */

        MyDatabaseHelper dbh = new MyDatabaseHelper(getActivity(),DB_NAME , null, DB_VERSION);
        //dbh.insertPeople(od);


        Cursor cursor = dbh.query("People");

        CursorWindow cw = new CursorWindow("test", 999999999);
        AbstractWindowedCursor ac = (AbstractWindowedCursor) cursor;
        ac.setWindow(cw);
        while (cursor.moveToNext()) {
//            @SuppressLint("Range") byte[] in=cursor.getBlob(cursor.getColumnIndex("photo"));
//            Bitmap bmpout= BitmapFactory.decodeByteArray(in,0,in.length);

            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("people_id"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String age = cursor.getString(cursor.getColumnIndex("age"));
            @SuppressLint("Range") String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
            @SuppressLint("Range") String sex = cursor.getString(cursor.getColumnIndex("sex"));
            @SuppressLint("Range") String birthtime = cursor.getString(cursor.getColumnIndex("birthday"));
            @SuppressLint("Range") String photo = cursor.getString(cursor.getColumnIndex("photo"));
            Older person = new Older();
            person.setId(id);
            person.setName(name);
            person.setAge(age);
            person.setTelephone(telephone);
            person.setSex(sex);
            person.setBirthtime(birthtime);
            person.setPhoto(iut.base64ToImage(photo));
    //        person.setPhoto(bmpout);
            personList.add(person);
        }
        return personList;
    }

    class MyViewBinder implements SimpleAdapter.ViewBinder {
        public boolean setViewValue(View view,Object data,String textRepresentation){
            if((view instanceof ImageView)&(data instanceof Bitmap))
            {
                ImageView iv=(ImageView)view;
                Bitmap bmp=(Bitmap)data;
                iv.setImageBitmap(bmp);
                return true;
            }
            return false;
        }
    }

}