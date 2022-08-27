package com.example.ui.ui.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ui.ImageUtil;
import com.example.ui.MainActivity;
import com.example.ui.MyDatabaseHelper;
import com.example.ui.R;
import com.example.ui.ui.mine.UserProfileActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddPeople extends AppCompatActivity {
    Older older=new Older(null,null,null,null,null,null,null);
    private static final int REQUEST_CODE_TAKE = 1;
    private static final int REQUEST_CODE_CHOOSE = 0;
    private ImageView mPicture;
    private TextView mName;
    private TextView mBirthday;
    private TextView mGender;
    private TextView mAge;
    private TextView mPhoneNumber;
    private Button msavebutton;
    private ImageButton ib_addback;


    private String birthday;
    private Uri imageUri; //定位资源位置
    private String imageBase64;

    private static final int DB_VERSION = 9;
    private static final String DB_NAME = "Info9.dp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //用于添加上方标题栏中的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_add_people);
        initView();
        setListeners();
    }
    private void initView(){
        mPicture = findViewById(R.id.iv_editphoto);
        mName = findViewById(R.id.tv_oldername_text);
        mBirthday = findViewById(R.id.tv_olderbirthday_text);
        mGender = findViewById(R.id.tv_oldergender_text);
        mAge = findViewById(R.id.tv_olderage_text);
        mPhoneNumber = findViewById(R.id.tv_olderphone_text);
        msavebutton=findViewById(R.id.save_button);

    }
    private void setListeners() {
        OnClick onClick = new OnClick();
        mPicture.setOnClickListener(onClick);
        mBirthday.setOnClickListener(onClick);
        mName.setOnClickListener(onClick);
        mGender.setOnClickListener(onClick);
        mAge.setOnClickListener(onClick);
        mPhoneNumber.setOnClickListener(onClick);
        msavebutton.setOnClickListener(onClick);

    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_editphoto:
                    break;
                case R.id.tv_oldergender_text:
                    final String[] gender = new String[]{"男", "女"};
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddPeople.this);
                    builder1.setTitle("选择性别").setSingleChoiceItems(gender, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mGender.setText(gender[i]);
                                    older.setSex(gender[i]);
                                    Toast.makeText(AddPeople.this, "修改成功", Toast.LENGTH_SHORT);
                                    dialogInterface.dismiss();
                                }
                            }
                    ).show();
                    break;

                case R.id.tv_oldername_text:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AddPeople.this);
                    View v1 = LayoutInflater.from(AddPeople.this).inflate(R.layout.edit_dialog, null);
                    EditText etoldername = v1.findViewById(R.id.et_text);
                    builder2.setTitle("修改昵称").setView(v1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AddPeople.this, "修改成功", Toast.LENGTH_SHORT);
                            mName.setText(etoldername.getText());
                            older.setName(etoldername.getText().toString());
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //取消修改
                        }
                    }).show();
                    break;

                case R.id.tv_olderage_text:
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(AddPeople.this);
                    View v2 = LayoutInflater.from(AddPeople.this).inflate(R.layout.edit_dialog, null);
                    EditText etolderage = v2.findViewById(R.id.et_text);
                    builder3.setTitle("修改年龄").setView(v2).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AddPeople.this, "修改成功", Toast.LENGTH_SHORT);
                            mAge.setText(etolderage.getText());
                            older.setAge(etolderage.getText().toString());
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //取消修改
                        }
                    }).show();
                    break;

                case R.id.tv_olderphone_text:
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(AddPeople.this);
                    View v3 = LayoutInflater.from(AddPeople.this).inflate(R.layout.edit_dialog, null);
                    EditText etPhone = v3.findViewById(R.id.et_text);
                    builder4.setTitle("修改手机号").setView(v3).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AddPeople.this, "修改成功", Toast.LENGTH_SHORT);
                            mPhoneNumber.setText(etPhone.getText());
                            older.setTelephone(etPhone.getText().toString());
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //取消修改
                        }
                    }).show();
                    break;

                case R.id.tv_olderbirthday_text:
                    new DatePickerDialog(AddPeople.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            int realMonth = month +1; //month默认从0开始
                            birthday = year + "/" + realMonth + "/" + dayOfMonth;
                            mBirthday.setText(birthday);
                            older.setBirthtime(birthday);
                            try {
                                older.setAge(String.valueOf(getAgeFromBirth(birthday)));
                                mAge.setText(String.valueOf(getAgeFromBirth(birthday)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    },1989,01,01).show();
                    break;
                case R.id.save_button:
                    MyDatabaseHelper openHelper= new MyDatabaseHelper(AddPeople.this, DB_NAME, null, DB_VERSION);
                    openHelper.insertPeople(older);
                    Toast.makeText(AddPeople.this,"添加成功!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AddPeople.this,MainActivity.class);
                    startActivity(intent);
                    break;

            }
        }

    }

    public int getAgeFromBirth(String birthday) throws ParseException {
        String[] strarray=birthday.split("[/]");
     //   Date date=new Date(strarray[0]+"-"+strarray[1]+"-"+strarray[2]);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date date= sdf.parse(strarray[0]+"-"+strarray[1]+"-"+strarray[2]);
        if(date!=null){
        Calendar cal= Calendar.getInstance();
        int yearNow=cal.get(Calendar.YEAR);
        int monthNow=cal.get(Calendar.MONTH)+1;
        int dayNow=cal.get(Calendar.DATE);
        cal.setTime(date);
        int selectYear = cal.get(Calendar.YEAR);
        int selectMonth = cal.get(Calendar.MONTH) + 1;
        int selectDay =cal.get(Calendar.DATE);
        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;
        int age = yearMinus;// 先大致赋值
        if (yearMinus <=0) {
            age = 0;
        }if (monthMinus < 0) {
            age=age-1;
        } else if (monthMinus == 0) {
            if (dayMinus < 0) {
                age=age-1;
            }
        }
        return age;
    }
        return 0;
}

    public void takePhoto(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            //执行拍照
            doTake();
        }else{
            //去申请权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }
    }

    //接收权限申请的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                doTake();
            }else{
                Toast.makeText(this,"您未获得摄像头权限！",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == 0){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }else{
                Toast.makeText(this,"您未获得访问相册权限！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doTake() {
        File imageTemp = new File(getExternalCacheDir(),"imageOut.jpeg");
        if(imageTemp.exists()){
            imageTemp.delete();
        }
        try {
            imageTemp.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(imageTemp);
        if(Build.VERSION.SDK_INT > 24){
            //contentProvider
            imageUri = FileProvider.getUriForFile(this,"com.example.ui.fileprovider",imageTemp);
        }else{
            imageUri = Uri.fromFile(imageTemp);
        }
        //传递文件路径
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);      //接收照片
        startActivityForResult(intent,REQUEST_CODE_TAKE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_TAKE){
            if(resultCode==RESULT_OK){
                //获取拍摄的照片
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mPicture.setImageBitmap(bitmap);//显示头像
                    final ByteArrayOutputStream os=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,2,os);
//                    Bitmap bitmap_ = ImageUtil.compressImage(bitmap);
//                    older.setPhoto(bitmap_);
//                    //保存图片
                    String imageToBase64 = ImageUtil.imageToBase64(bitmap);
                    imageBase64 = imageToBase64;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == REQUEST_CODE_CHOOSE){ //获取相册中的图片
            if(Build.VERSION.SDK_INT < 19){
                handleImageBeforeApi19(data);
            }else{
                handleImageOnApi19(data);
            }

        }
    }

    private void handleImageBeforeApi19(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnApi19(Intent data) { //通过uri拿到对应路径
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String documentId = DocumentsContract.getDocumentId(uri);

            if(TextUtils.equals(uri.getAuthority(),"com.android.providers.media.documents")){
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if(TextUtils.equals(uri.getAuthority(),"com.android.providers.downloads.documents")){
                if(documentId!=null && documentId.startsWith("msf:")){
                    resolveMSFContent(uri,documentId);
                    return;
                }
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(documentId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void resolveMSFContent(Uri uri, String documentId) {
        File file = new File(getCacheDir(),"temp_file"+getContentResolver().getType(uri).split("/"));
        //文件的读写操作
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4*1024];
            int read;
            while((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,read);
            } //循环结束之后，输入流的数据已经进入文件中
            outputStream.flush();

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mPicture.setImageBitmap(bitmap);
            imageBase64 = ImageUtil.imageToBase64(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();//关闭光标，否则会造成内存泄露
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mPicture.setImageBitmap(bitmap);
            older.setPhoto(bitmap);
            String imageToBase64 = ImageUtil.imageToBase64(bitmap);
            imageBase64 = imageToBase64;
        }
    }

    public void choosePhoto(View view) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            //打开相册
            openAlbum();
        }else{
            //去申请权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_CHOOSE);
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

//    public void back_to_group(View view) {
//        finish();
//    }

}