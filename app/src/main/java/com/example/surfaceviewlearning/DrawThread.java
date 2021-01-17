package com.example.surfaceviewlearning;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
    //AsyncTask, Thread, Runnable - интерфейс
    MySurface mySurface;
    SurfaceHolder surfaceHolder;
    boolean isRun = false;
    long nowTime, prevTime, ellapsedTime;

    public DrawThread(MySurface mySurface, SurfaceHolder surfaceHolder) {
        this.mySurface = mySurface;
        this.surfaceHolder = surfaceHolder;
        prevTime = System.currentTimeMillis();
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (isRun) {
            nowTime = System.currentTimeMillis();
            ellapsedTime = nowTime - prevTime;
            if (ellapsedTime > 33) {
                prevTime = nowTime;
                canvas = null;
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    mySurface.draw(canvas);
                }
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
