package com.example.ui.ui.location;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
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
    private Paint mPaintMouse;//鼠标拖尾画笔

    private boolean mouse_begin = false;//鼠标是否按下

    private float mouseCurrentX = 0;//当前鼠标位置X
    private float mouseCurrentY = 0;//当前鼠标位置Y
    Queue<Float> mouseX = new LinkedList<Float>();//保存鼠标轨迹X
    Queue<Float> mouseY = new LinkedList<Float>();//保存鼠标轨迹Y

    private int time = 0;//累加时间

    private LocateManager mLocateManager;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
//            if (mLocateManager == null) {
//                mLocateManager = new LocateManager();
//                //mLocateManager.openConnect();                             //建立本地数据库
//            }

//            DBUtils myDBUtil = new DBUtils();
//            mLocateManager.openConnect();
//            //得到最新的一个坐标
//            Locate myLocate = mLocateManager.getLocation();
//            Log.e("myLocate:",""+myLocate.getX()+","+myLocate.getY());

            time++;
            invalidate();//告诉主线程重新绘制
            if (mouseX.peek() != null) {
                boolean is_add_mouse = Math.abs(mouseX.peek() - mouseCurrentX) < 0.01;//鼠标不动时不记录坐标
                if (!is_add_mouse) {
                    mouseX.offer(mouseCurrentX);
                    mouseY.offer(mouseCurrentY);
                }
                if (mouseX.size() > 20 || is_add_mouse) {
                    mouseX.poll();
                    mouseY.poll();
                }
            } else if (mouse_begin) {
                mouseX.offer(mouseCurrentX);
                mouseY.offer(mouseCurrentY);
            }
            handler.postDelayed(this, 20);//每20ms循环一次，50fps
        }
    };

    public MainCanvas(Context context) {
        super(context);
    }

    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler.postDelayed(runnable, 20);
        mPaintMouse = new Paint();//对画笔初始化
        mPaintMouse.setColor(Color.RED);//设置画笔颜色
        mPaintMouse.setStrokeWidth(10);//设置画笔宽度
        mPaintMouse.setAntiAlias(true);//设置抗锯齿
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//设置触摸事件，手指按下进行记录，手指抬起停止记录
        mouseCurrentX = event.getX();
        mouseCurrentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mouse_begin = true;
                break;
            case MotionEvent.ACTION_UP:
                mouse_begin = false;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        int[] color = Bezier.rainBow((float)time % 300 / 300); //画笔同一颜色随时间渐变

        int size = mouseX.size();
        float x1 = 0,x2 = 0,y1 = 0,y2 = 0;
        for (int i = 0; i < size; i++) {
            float percent = (float)i / size;
            float res[] = Bezier.bezier((LinkedList)mouseX, (LinkedList)mouseY, percent);
//            Log.d("MainCanvas",String.valueOf(res));
            x1 = res[0];
            y1 = res[1];
            if(i == 0){
                x2 = x1;
                y2 = y1;
                continue;
            }
            int[] color = Bezier.rainBow((time + percent * 300) % 300 / 300); //画笔不同颜色随时间渐变
            mPaintMouse.setColor(Color.argb(255, color[0], color[1], color[2]));
            mPaintMouse.setStrokeWidth((int)(percent * 20));
            Log.d("MainCanvas", "x1:"+String.valueOf(x1));
            Log.d("MainCanvas", "y1:"+String.valueOf(y1));
            Log.d("MainCanvas", "x2:"+String.valueOf(x2));
            Log.d("MainCanvas", "y2:"+String.valueOf(y2));
            canvas.drawLine(x1, y1, x2, y2, mPaintMouse);
            x2 = x1;
            y2 = y1;
            if (i == size - 1) canvas.drawLine(x1, y1, mouseCurrentX, mouseCurrentY, mPaintMouse);//连接最后一段与鼠标
        }
        canvas.drawCircle(mouseCurrentX, mouseCurrentY, 10, mPaintMouse);//绘制鼠标中心

    }
}
