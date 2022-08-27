package com.example.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.graphics.Bitmap;
import com.example.ui.ui.group.Older;
import android.widget.Toast;

import androidx.annotation.Nullable;
import java.io.ByteArrayOutputStream;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_PEOPLE = "Create table People("
            + "people_id integer primary key autoincrement,"
            + "name text,"
            + "age text,"
            + "telephone integer,"
            + "sex text,"
            + "birthday time,"
            + "photo text)";

    public static final String CREATE_MINE = "Create table Mine("
            + "mine_id integer primary key autoincrement,"
            + "name text unique,"
            + "telephone text,"
            + "birthday text,"
            + "sex text,"
            + "password text,"
            + "workplace text,"
            + "photo text)";

    public static final String CREATE_WARN = "Create table Warn("
            + "warn_id integer primary key autoincrement,"
            + "name text unique,"
            + "address_x float(2),"
            + "address_y float(2),"
            + "radius float(2),"
            + "grade integer,"
            + "use boolean)";  //因为SQLite中并没有封装boolean类所以需要进行一定的转换,可以直接存储，但是存储的是0，1，取出来时需要进行转换

    private Context mContext;
    private String INSERT_DATA_PEOPLE = "insert into People select * from _temp_People";
    private String INSERT_DATA_MINE = "insert into Mine select * from _temp_Mine";
    private String INSERT_DATA_WARN = "insert into Warn select * from _temp_Warn";
    private String CREATE_TEMP_People = "alter table People rename to _temp_People";
    private String CREATE_TEMP_Mine = "alter table Mine rename to _temp_Mine";
    private String CREATE_TEMP_WARN = "alter table Warn rename to _temp_Warn";
    private String DROP_PEOPLE = "drop table _temp_People";
    private String DROP_MINE = "drop table _temp_Mine";
    private String DROP_WARN = "drop table _temp_Warn";
    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PEOPLE);
        db.execSQL(CREATE_MINE);
        db.execSQL(CREATE_WARN);
        //Toast.makeText(mContext,"Create succeed",Toast.LENGTH_SHORT).show();
    }

    public Cursor query(String TABLE_NAME){
        SQLiteDatabase db=getReadableDatabase();
        //获取所有行
        Cursor c=db.query(TABLE_NAME,null,null,null,null,null,null);
        return c;
    }
    public boolean insertPeople(Older older){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        //     values.put("id",older.getId().toString());
        values.put("name",older.getName().toString());
        values.put("age",older.getAge().toString());
        values.put("telephone",older.getTelephone().toString());
        values.put("sex",older.getSex().toString());
        values.put("birthday",older.getBirthtime().toString());
        Bitmap bmp=older.getPhoto();
//        final ByteArrayOutputStream os=new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG,100,os);
//        values.put("photo",os.toByteArray());
        ImageUtil iut=new ImageUtil();
        values.put("photo",iut.imageToBase64(bmp));
        long result=db.insert("People",null,values);
        return result>0?true:false;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_TEMP_People);
        db.execSQL(CREATE_TEMP_Mine);
        db.execSQL(CREATE_TEMP_WARN);
        db.execSQL(CREATE_PEOPLE);
        db.execSQL(CREATE_MINE);
        db.execSQL(CREATE_WARN);
        db.execSQL(INSERT_DATA_MINE);
        db.execSQL(INSERT_DATA_PEOPLE);
        db.execSQL(INSERT_DATA_WARN);
        db.execSQL(DROP_MINE);
        db.execSQL(DROP_PEOPLE);
        db.execSQL(DROP_WARN);
    }
}
