package com.example.ui.ui.mine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.graphics.drawable.BitmapDrawable;


import com.example.ui.R;
import com.example.ui.UserApp;
import com.example.ui.UserData;
import com.example.ui.UserDataManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UserProfileActivity extends BaseActivity {
    private static final int REQUEST_CODE_TAKE = 1;
    private static final int REQUEST_CODE_CHOOSE = 0;
    private ImageView mPicture;
    private TextView mName;
    private TextView mBirthday;
    private TextView mGender;
    private TextView mWorkplace;
    private TextView mPhoneNumber;
    private Button mBtSave;
    private String birthday;
    private String birthdayTime;

    private Context mContext = null;

    private Uri imageUri; //??????????????????
    private String imageBase64;

    private UserDataManager mUserDataManager;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //mUserDataManager.openDataBase();                              //?????????????????????
        }
        setContentView(R.layout.activity_user_profile);
        initView();
        initData();
        setListeners();
    }
    private void initView(){
        mPicture = findViewById(R.id.iv_avatar);
        mName = findViewById(R.id.tv_username_text);
        mBirthday = findViewById(R.id.tv_birthday_text);
        mGender = findViewById(R.id.tv_gender_text);
        mWorkplace = findViewById(R.id.tv_workplace_text);
        mPhoneNumber = findViewById(R.id.tv_phone_text);
        mBtSave = findViewById(R.id.btn_button);
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initData(){
        getDataFromSpf();
    }

    public void save(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = mName.getText().toString();

                String birthday = mBirthday.getText().toString();
                String workplace = mWorkplace.getText().toString();
                String phone = mPhoneNumber.getText().toString();
                String gender = mGender.getText().toString();
        //        Bitmap bitmap_ = ((BitmapDrawable)mPicture.getDrawable()).getBitmap();
        //        Bitmap bitmap = ImageUtil.compressImage(bitmap_);
                String photo = ImageUtil.imageToBase64(((BitmapDrawable)mPicture.getDrawable()).getBitmap());
                //??????????????????

                UserData userData = new UserData(name,gender,birthday,phone,workplace,photo);
                mUserDataManager.updateUserData2(userData);
                Looper.prepare();
                Toast.makeText(UserProfileActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
                Looper.loop();
                //        this.finish(); //???????????????
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void getDataFromSpf(){
        final UserApp app =(UserApp)getApplication();
        String userId = app.getmUserId();

        new Thread(new Runnable() {
            @Override
            public void run() {
                UserData myUser = mUserDataManager.fetchUserData(userId);
                Message message = new Message();
                message.obj =myUser;
                message.what = 0;
                mHandler.sendMessage(message);
            }

        }).start();
//        CursorWindow cw = new CursorWindow("test", 500000000);
//        AbstractWindowedCursor ac = (AbstractWindowedCursor) mCursor;
//        ac.setWindow(cw);

//        String name = mCursor.getString(mCursor.getColumnIndex("name"));
//        String birthday = mCursor.getString(mCursor.getColumnIndex("birthday"));
//        String workplace = mCursor.getString(mCursor.getColumnIndex("workplace"));
//        String phone = mCursor.getString(mCursor.getColumnIndex("telephone"));
//        String gender = mCursor.getString(mCursor.getColumnIndex("sex"));
//        String image64 = mCursor.getString(mCursor.getColumnIndex("photo"));

//        mName.setText(myUser.getUserName());
//        mBirthday.setText(myUser.getBirthday());
//        mWorkplace.setText(myUser.getUserWorkplace());
//        mPhoneNumber.setText(myUser.getUserTelephone());
//        mGender.setText(myUser.getUserSex());
//        mPicture.setImageBitmap(ImageUtil.base64ToImage(myUser.getUserPhoto()));
    }

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    UserData myUser = (UserData) msg.obj;
                    mName.setText(myUser.getUserName());
                    mBirthday.setText(myUser.getBirthday());
                    mWorkplace.setText(myUser.getUserWorkplace());
                    mPhoneNumber.setText(myUser.getUserTelephone());
                    mGender.setText(myUser.getUserSex());
                    mPicture.setImageBitmap(ImageUtil.base64ToImage(myUser.getUserPhoto()));
                    break;
                default:
                    break;
            }
        }

    };

    private void setListeners() {
        OnClick onClick = new OnClick();
        mPicture.setOnClickListener(onClick);
        mBirthday.setOnClickListener(onClick);
        //mName.setOnClickListener(onClick);
        mGender.setOnClickListener(onClick);
        mWorkplace.setOnClickListener(onClick);
        mPhoneNumber.setOnClickListener(onClick);
        mBtSave.setOnClickListener(onClick);
    }

    public void takePhoto(View view) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            //????????????
            doTake();
        }else{
            //???????????????
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }
    }

    //???????????????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                doTake();
            }else{
                Toast.makeText(this,"??????????????????????????????",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == 0){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }else{
                Toast.makeText(this,"?????????????????????????????????",Toast.LENGTH_SHORT).show();
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
        //??????????????????
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);      //????????????
        startActivityForResult(intent,REQUEST_CODE_TAKE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_TAKE){
            if(resultCode==RESULT_OK){
                //?????????????????????
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mPicture.setImageBitmap(bitmap);//????????????
//                    //????????????
                    String imageToBase64 = ImageUtil.imageToBase64(bitmap);
                    imageBase64 = imageToBase64;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == REQUEST_CODE_CHOOSE){ //????????????????????????
            if(Build.VERSION.SDK_INT < 19){
                try {
                    handleImageBeforeApi19(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    handleImageOnApi19(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static boolean getRootPath(Context context) {

// ?????????SD???

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)

                || !Environment.isExternalStorageRemovable()) {

            //return context.getExternalCacheDir().getPath(); // ???
            return true;

        } else {

           // return context.getCacheDir().getPath(); // ???
            return false;
        }

    }

    private void handleImageBeforeApi19(Intent data) throws FileNotFoundException {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnApi19(Intent data) throws FileNotFoundException { //??????uri??????????????????
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
        //?????????????????????
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4*1024];
            int read;
            while((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,read);
            } //????????????????????????????????????????????????????????????
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
            cursor.close();//??????????????????????????????????????????
        }
        return path;
    }

    private void displayImage(String imagePath) throws FileNotFoundException {
        if(imagePath != null){
            boolean flag = getRootPath(mContext);
            FileInputStream fis = new FileInputStream(imagePath);
            Bitmap bitmap  = BitmapFactory.decodeStream(fis);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mPicture.setImageBitmap(bitmap);
            String imageToBase64 = ImageUtil.imageToBase64(bitmap);
            imageBase64 = imageToBase64;
        }
    }

    public void choosePhoto(View view) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            //????????????
            openAlbum();
        }else{
            //???????????????
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_CHOOSE);
    }

    public void back_to_mine(View view) {
        finish();
    }

    //???????????????
    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_avatar:
                    break;
                case R.id.tv_gender_text:
                    final String[] gender = new String[]{"???", "???"};
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(UserProfileActivity.this);
                    builder1.setTitle("????????????").setSingleChoiceItems(gender, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mGender.setText(gender[i]);
                                    Toast.makeText(UserProfileActivity.this, "????????????", Toast.LENGTH_SHORT);
                                    dialogInterface.dismiss();
                                }
                            }
                    ).show();
                    break;

//                case R.id.tv_username_text:
//                    AlertDialog.Builder builder2 = new AlertDialog.Builder(UserProfileActivity.this);
//                    View v1 = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.edit_dialog, null);
//                    EditText etUsername = v1.findViewById(R.id.et_text);
//                    builder2.setTitle("????????????").setView(v1).setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(UserProfileActivity.this, "????????????", Toast.LENGTH_SHORT);
//                            mName.setText(etUsername.getText());
//                        }
//                    }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            //????????????
//                        }
//                    }).show();
//                    break;

                case R.id.tv_workplace_text:
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(UserProfileActivity.this);
                    View v2 = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.edit_dialog, null);
                    EditText etWorkplace = v2.findViewById(R.id.et_text);
                    builder3.setTitle("??????????????????").setView(v2).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(UserProfileActivity.this, "????????????", Toast.LENGTH_SHORT);
                            mWorkplace.setText(etWorkplace.getText());
                        }
                    }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //????????????
                        }
                    }).show();
                    break;

                case R.id.tv_phone_text:
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(UserProfileActivity.this);
                    View v3 = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.edit_dialog, null);
                    EditText etPhone = v3.findViewById(R.id.et_text);
                    builder4.setTitle("???????????????").setView(v3).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(UserProfileActivity.this, "????????????", Toast.LENGTH_SHORT);
                            mPhoneNumber.setText(etPhone.getText());
                        }
                    }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //????????????
                        }
                    }).show();
                    break;

                case R.id.tv_birthday_text:
                    new DatePickerDialog(UserProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            int realMonth = month +1; //month?????????0??????
                            birthday = year + "/" + realMonth + "/" + dayOfMonth;
                            mBirthday.setText(birthday);
                        }
                    },1989,01,01).show();
                    break;

                case R.id.btn_button:
                    save();
                    break;
            }
        }

    }

    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            //mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //mUserDataManager.openDataBase();
        }
        super.onResume();
    }
}