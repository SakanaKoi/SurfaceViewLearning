package com.example.surfaceviewlearning;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class MySurface extends SurfaceView implements SurfaceHolder.Callback {
    //Переменные для рисования
    float x, y; //положение картинки
    float tx, ty; // координаты точки касания
    float dx, dy; // смещение координат
    float koeff; //коэффициент скорости

    //Переменные для картинки
    Bitmap image;
    Resources res;
    Paint paint;

    //объект потока
    DrawThread drawThread;
    public MySurface(Context context) {
        super(context);
        x = 100;
        y = 100;
        koeff = 5;
        res = getResources();
        image = BitmapFactory.decodeResource(res, R.drawable.doge);
        paint = new Paint();
        getHolder().addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            tx = event.getX();
            ty = event.getY();
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(image, x, y, paint);
        //расчёт смещения
        if (tx != 0) {
            calculate();
        }
        x += dx;
        y += dy;
    }

    private void calculate() {
        double g = Math.sqrt(Math.pow((tx - x), 2) + Math.pow((ty - y), 2));
        dx = (float)(koeff * (tx - x)/g);
        dy = (float)(koeff * (ty - y)/g);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        drawThread = new DrawThread(this, getHolder());
        drawThread.setRun(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean stop = true;
        drawThread.setRun(false);
        while (stop) {
            try {
                drawThread.join();
                stop = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
