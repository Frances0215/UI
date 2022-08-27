package com.example.ui.ui.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
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
import android.view.View;
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

public class PeopleInformation extends AppCompatActivity {
    private Older older;
    private TextView tv_name1;
    private ImageView iv;
    private TextView tv_name2;
    private TextView tv_name;
    private TextView tv_tele;
    private TextView tv_olderbirthday;
    private TextView tv_age;
    private TextView tv_sex;
    private ImageButton ib_back;
    private TextView tv_delete;
    ImageUtil iut;
    private Uri imageUri; //定位资源位置
    private String imageBase64;
    private static final int REQUEST_CODE_TAKE = 1;
    private static final int REQUEST_CODE_CHOOSE = 0;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_people_information);
        initView();
        setListeners();
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initView(){
        Intent intent=getIntent();
        older=new Older();
        iut=new ImageUtil();

        /*older.setName(intent.getStringExtra("name"));
        older.setAge(intent.getStringExtra("age"));
        older.setTelephone(intent.getStringExtra("telephone"));
        older.setSex(intent.getStringExtra("sex"));
        older.setBirthtime(intent.getStringExtra("birthday"));
        byte[] res=intent.getByteArrayExtra("photo");
        older.setPhoto(getPicFromBytes(res,null));*/
        //Bundle
       Bundle bundle=intent.getExtras();
        older.setId(bundle.getString("_id"));
        older.setName(bundle.getString("name"));
        older.setAge(bundle.getString("age"));
        older.setTelephone(bundle.getString("telephone"));
        older.setSex(bundle.getString("sex"));
        older.setBirthtime(bundle.getString("birthday"));
    //    older.setPhoto(iut.base64ToImage(bundle.getString("photo")));
   /*     byte[] bis=bundle.getByteArray("photo");
        older.setPhoto(BitmapFactory.decodeByteArray(bis,0,bis.length));
 */
        MyDatabaseHelper dbh=new MyDatabaseHelper(PeopleInformation.this, "People.db", null, 1);
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor scursor=db.query("People",new String[]{"photo"},"id like ?", new String[]{bundle.getString("_id")},null,null,null,null);

        CursorWindow cw = new CursorWindow("test", 999999999);
        AbstractWindowedCursor ac = (AbstractWindowedCursor) scursor;
        ac.setWindow(cw);
        while(scursor.moveToNext()){
            older.setPhoto(iut.base64ToImage(scursor.getString(0)));
        }
        //        tv_name1=(TextView) findViewById(R.id.people_infor);
//        tv_name1.setText(older.getName());
        iv=(ImageView)findViewById(R.id.iv_photo);
        iv.setImageBitmap(older.getPhoto());
        tv_name2=(TextView) findViewById(R.id.tv_name1);
        tv_name2.setText(older.getName());
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_name.setText(older.getName());
        tv_tele=(TextView) findViewById(R.id.tv_tele);
        tv_tele.setText(older.getTelephone());
        tv_olderbirthday=(TextView) findViewById(R.id.tv_olderbirthday);
        tv_olderbirthday.setText(older.getBirthtime());
        tv_age=(TextView) findViewById(R.id.tv_age);
        tv_age.setText(older.getAge());
        tv_sex=(TextView) findViewById(R.id.tv_sex);
        tv_sex.setText(older.getSex());
        ib_back=(ImageButton)findViewById(R.id.ib_back);
        tv_delete=(TextView)findViewById(R.id.delete);
    }
    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {

        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,  opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;

    }

    private void setListeners(){
        OnClick onclick=new OnClick();
        tv_name.setOnClickListener(onclick);
        tv_sex.setOnClickListener(onclick);
        tv_tele.setOnClickListener(onclick);
        tv_olderbirthday.setOnClickListener(onclick);
        ib_back.setOnClickListener(onclick);
        tv_delete.setOnClickListener(onclick);
    }
    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View view){
            MyDatabaseHelper openHelper= new MyDatabaseHelper(PeopleInformation.this, "People.db", null, 1);
            SQLiteDatabase db = openHelper.getWritableDatabase();
            switch (view.getId()){
                case R.id.iv_photo:
                    break;
                case R.id.tv_name:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(PeopleInformation.this);
                    View v1 = LayoutInflater.from(PeopleInformation.this).inflate(R.layout.edit_dialog, null);
                    EditText etname = v1.findViewById(R.id.et_text);
                    etname.setHint(older.getName());
                    builder2.setTitle("修改昵称").setView(v1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(PeopleInformation.this, "修改成功", Toast.LENGTH_SHORT);
                            ContentValues cv=new ContentValues();
                            cv.put("name",etname.getText().toString());
                            db.update("People", cv, "id=?", new String[]{older.getId()});
                            older.setName(etname.getText().toString());
                            tv_name2.setText(older.getName());
                            tv_name.setText(older.getName());
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //取消修改
                        }
                    }).show();
                    break;
                case R.id.tv_sex:
                    final String[] gender = new String[]{"男", "女"};
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PeopleInformation.this);
                    builder1.setTitle("选择性别").setSingleChoiceItems(gender, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    older.setSex(gender[i]);
                                    tv_sex.setText(gender[i]);
                                    Toast.makeText(PeopleInformation.this, "修改成功", Toast.LENGTH_SHORT);
                                    dialogInterface.dismiss();
                                    ContentValues cv=new ContentValues();
                                    cv.put("sex",gender[i]);
                                    db.update("People", cv, "id=?", new String[]{older.getId()});
                                }
                            }
                    ).show();
                    break;
                case R.id.tv_tele:
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(PeopleInformation.this);
                    View v2 = LayoutInflater.from(PeopleInformation.this).inflate(R.layout.edit_dialog, null);
                    EditText ettele = v2.findViewById(R.id.et_text);
                    ettele.setHint(older.getTelephone());
                    builder3.setTitle("修改电话号码").setView(v2).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(PeopleInformation.this, "修改成功", Toast.LENGTH_SHORT);
                            ContentValues cv=new ContentValues();
                            cv.put("telephone",Integer.parseInt(ettele.getText().toString()));
                            db.update("People", cv, "id=?", new String[]{older.getId()});
                            older.setTelephone(ettele.getText().toString());
                            tv_tele.setText(ettele.getText().toString());
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //取消修改
                        }
                    }).show();
                    break;
                case R.id.tv_olderbirthday:
                    new DatePickerDialog(PeopleInformation.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            int realMonth = month +1; //month默认从0开始
                            String birthday = year + "/" + realMonth + "/" + dayOfMonth;
                            tv_olderbirthday.setText(birthday);
                            ContentValues cv=new ContentValues();
                            cv.put("birthday",birthday);
                            db.update("People", cv, "id=?", new String[]{older.getId()});
                            try {
                                String tempage=String.valueOf(getAgeFromBirth(birthday));
                                older.setAge(tempage);
                                tv_age.setText(tempage);
                                ContentValues cv2=new ContentValues();
                                cv2.put("age",tempage);
                                db.update("People", cv2, "id=?", new String[]{older.getId()});
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    },1943,01,01).show();
                    break;
                case R.id.delete:
                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(PeopleInformation.this);
                    alertdialogbuilder.setMessage("您确认解除绑定？");
                    alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.delete("People","id=?",new String[]{older.getId()});
                            Intent intent=new Intent(PeopleInformation.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertdialogbuilder.setNeutralButton("取消", null);
                    final AlertDialog alertdialog1 = alertdialogbuilder.create();
                    alertdialog1.show();
                    break;
                case R.id.ib_back:
                    Intent intent=new Intent(PeopleInformation.this, MainActivity.class);
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
                    iv.setImageBitmap(bitmap);//显示头像
                    final ByteArrayOutputStream os=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
                    older.setPhoto(bitmap);
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
            iv.setImageBitmap(bitmap);
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
            iv.setImageBitmap(bitmap);
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
}