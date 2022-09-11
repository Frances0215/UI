package com.example.ui.ui.location;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.ui.DBUtils;
import com.example.ui.Locate;
import com.example.ui.LocateManager;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

public class MainCanvas extends View {
    private Paint mPaintLocation;//位置拖尾画笔

    private boolean Location_begin = false;//目标是否移动

    private float LocationCurrentX = 0;//当前位置X
    private float LocationCurrentY = 0;//当前位置Y
    Queue<Float> LocationX = new LinkedList<Float>();//保存位置轨迹X
    Queue<Float> LocationY = new LinkedList<Float>();//保存位置轨迹Y

    private int time = 0;//累加时间

    private LocateManager mLocateManager;

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
//要做的事情
            super.handleMessage(msg);
        }
    };

    public  class MyThread implements Runnable{

                @Override
                public void run() {

                    while (true) {
                        if (mLocateManager == null) {
                            mLocateManager = new LocateManager();
                        //mLocateManager.openConnect();                             //建立本地数据库
                        }

                        DBUtils myDBUtil = new DBUtils();
                        mLocateManager.openConnect();
                        //得到最新的一个坐标
                        Locate myLocate = mLocateManager.getLocation();
                        LocationCurrentX = (float) myLocate.getX();
                        LocationCurrentY = (float) myLocate.getY();
                        Log.e("myLocate:", "" + myLocate.getX() + "," + myLocate.getY());

                        time++;
                        invalidate();//告诉主线程重新绘制
                        if (LocationX.peek() != null) {
                            boolean is_add_Location = Math.abs(LocationX.peek() - LocationCurrentX) < 0.01;//目标不动时不记录坐标
                            if (!is_add_Location) {
                                LocationX.offer(LocationCurrentX);
                                LocationY.offer(LocationCurrentY);
                            }
                            if (LocationX.size() > 18 || is_add_Location) {
                                LocationX.poll();
                                LocationY.poll();
                            }
                        }
//                        else if (Location_begin) {
//                            LocationX.offer(LocationCurrentX);
//                            LocationY.offer(LocationCurrentY);
//                        }
//                        handler.postDelayed(this, 20);//每20ms循环一次，50fps

                        try {
                            Thread.sleep(20);//线程暂停10秒，单位毫秒
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);//发送消息
                        } catch (InterruptedException e) {
// TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

    public MainCanvas(Context context) {
        super(context);
    }

    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        new Thread(new MyThread()).start();
        mPaintLocation = new Paint();//对画笔初始化
        mPaintLocation.setColor(Color.RED);//设置画笔颜色
        mPaintLocation.setStrokeWidth(10);//设置画笔宽度
        mPaintLocation.setAntiAlias(true);//设置抗锯齿
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {//设置触摸事件，手指按下进行记录，手指抬起停止记录
//        LocationCurrentX = event.getX();
//        LocationCurrentY = event.getY();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Location_begin = true;
//                break;
//            case MotionEvent.ACTION_UP:
//                Location_begin = false;
//                break;
//        }
//        return true;
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        int[] color = Bezier.rainBow((float)time % 300 / 300); //画笔同一颜色随时间渐变

        int size = LocationX.size();
        float x1 = 0,x2 = 0,y1 = 0,y2 = 0;
        for (int i = 0; i < size; i++) {
            float percent = (float)i / size;
            float res[] = Bezier.bezier((LinkedList)LocationX, (LinkedList)LocationY, percent);
            x1 = res[0]*1000;
            y1 = res[1]*1000;
            if(i == 0){
                x2 = x1;
                y2 = y1;
                continue;
            }
            int[] color = Bezier.rainBow((time + percent * 300) % 300 / 300); //画笔不同颜色随时间渐变
            mPaintLocation.setColor(Color.argb(255, color[0], color[1], color[2]));
            mPaintLocation.setStrokeWidth((int)(percent * 20));
            Log.d("MainCanvas", "x1:"+String.valueOf(x1));
            Log.d("MainCanvas", "y1:"+String.valueOf(y1));
            Log.d("MainCanvas", "x2:"+String.valueOf(x2));
            Log.d("MainCanvas", "y2:"+String.valueOf(y2));
            canvas.drawLine(x1, y1, x2, y2, mPaintLocation);
            x2 = x1;
            y2 = y1;
            if (i == size - 1) canvas.drawLine(x1, y1, LocationCurrentX*1000, LocationCurrentY*1000, mPaintLocation);//连接最后一段与鼠标
        }
        canvas.drawCircle(LocationCurrentX*1000, LocationCurrentY*1000, 10, mPaintLocation);//绘制鼠标中心

    }
}
