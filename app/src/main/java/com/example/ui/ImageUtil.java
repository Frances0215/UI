package com.example.ui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageUtil {

    //把图片变成字符串
    public static String imageToBase64(Bitmap bitmap){
        if(bitmap == null){
            return null;
        }else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream); //图片压缩变成输出流
            byte[] buffer = byteArrayOutputStream.toByteArray();
            String baseStr = Base64.encodeToString(buffer,Base64.DEFAULT); //Base64算法
            return baseStr;
        }
    }

    //把字符串还原成图片
    public static Bitmap base64ToImage(String bitmap64){
        if(bitmap64 == null){
            return null;
        }else{
            byte[] bytes = Base64.decode(bitmap64,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            return bitmap;
        }
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
//    public static Bitmap compressImage(Bitmap image) {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 2, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 2) {  //循环判断如果压缩后图片是否大于2kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        return bitmap;
//    }

//    public void bitmapFactory(Uri imageUri){
//        String[] filePathColumns = {MediaStore.Images.Media.DATA};
//        Cursor c = getContentResolver().query(imageUri, filePathColumns, null, null, null);
//        c.moveToFirst();
//        int columnIndex = c.getColumnIndex(filePathColumns[0]);
//        String imagePath = c.getString(columnIndex);
//        c.close();
//
//        // 配置压缩的参数
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true; //获取当前图片的边界大小，而不是将整张图片载入在内存中，避免内存溢出
//        BitmapFactory.decodeFile(imagePath, options);
//        options.inJustDecodeBounds = false;
//        //inSampleSize的作用就是可以把图片的长短缩小inSampleSize倍，所占内存缩小inSampleSize的平方
//        options.inSampleSize = caculateSampleSize(options,500,50);
//        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
//        //imageView.setImageBitmap(bm);
//    }
//
//    /**
//     * 计算出所需要压缩的大小
//     * @param options
//     * @param reqWidth  我们期望的图片的宽，单位px
//     * @param reqHeight 我们期望的图片的高，单位px
//     * @return
//     */
//    private int caculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        int sampleSize = 1;
//        int picWidth = options.outWidth;
//        int picHeight = options.outHeight;
//        if (picWidth > reqWidth || picHeight > reqHeight) {
//            int halfPicWidth = picWidth / 2;
//            int halfPicHeight = picHeight / 2;
//            while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
//                sampleSize *= 2;
//            }
//        }
//        return sampleSize;
//    }

}
