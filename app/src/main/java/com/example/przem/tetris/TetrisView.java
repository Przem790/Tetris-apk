package com.example.przem.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Random;



public class TetrisView extends View implements Runnable,Serializable{
    Handler handler;
    volatile boolean running;
    Thread gameThread = null;
    GameStatus GS;
    float screenX;
    float screenY;
    Paint paint;
    Random random;
    TetrisView myinstance;
    float downX,downY,upX,upY;
    float MIN_DISTANCE=50;
    int orientation;
    RectF dummy1,dummy2;
    float initX,initY;
    long tstart,tstop;


    public TetrisView(Context context) {
        super(context);
        screenY = context.getResources().getDisplayMetrics().heightPixels;
        screenX = context.getResources().getDisplayMetrics().widthPixels;
        GS = new GameStatus(screenX,screenY);
        paint = new Paint();
        random=new Random();
        this.myinstance=this;
        orientation=context.getResources().getConfiguration().orientation;
        dummy1=new RectF(0,0,screenX/4,screenY);
        dummy2=new RectF(3*(screenX/4),0,3*(screenX/4)+screenX/4,screenY);
    }



    public GameStatus getGS(){
         return GS;
    }
    public void setGS(GameStatus G){
        G.update(screenX,screenY);
        this.GS=G;
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.argb(255,255,255,255));
        for(Brick x:GS.GameField){
            paint.setColor(x.getColor());
            canvas.drawRect(x.getRect(),paint);
        }
        paint.setColor(Color.argb(255,255,255,255));
        if(orientation==2){
            paint.setColor(Color.parseColor("#000000"));
            canvas.drawRect(dummy1,paint);
            canvas.drawRect(dummy2,paint);

        }

    }


    public void resume(){
        running=true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void pause() {
        running=false;
        try {
           gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();

        if(action == MotionEvent.ACTION_DOWN){

            downX = motionEvent.getX();
            downY = motionEvent.getY();
            initX=downX;
            initY=downY;
            if(downX<screenX/10)
                GS.MoveObjectLeft();

            if(downX>screenX-screenX/10)
                GS.MoveObjectRight();

            if(downY>screenY-screenY/10)
                GS.MoveObjectDown();

            return true;
        }
        else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
            upX = motionEvent.getX();
            upY = motionEvent.getY();

            float deltaX = initX - upX;
            float deltaY = initY - upY;

            if (Math.abs(deltaX) > MIN_DISTANCE) {

                if (deltaX < 0) {
                    //GS.MoveObjectRight();
                    return false;
                }
                if (deltaX > 0) {
                    //GS.MoveObjectLeft();
                    return false;
                }
            } else if (Math.abs(deltaY) > MIN_DISTANCE) { // vertical swipe
                if (deltaY < 0) {

                    return false;
                }
                if (deltaY > 0) {
                    return false;
                }
            }
            if(downX>screenX/10&&downX<screenX-screenX/10&&downY<screenY-screenY/10){
                if(deltaX<0)
                    deltaX=-deltaX;
                if(deltaY<0)
                    deltaY=-deltaY;
                if(orientation!=2) {
                    if (deltaX < screenX / 10 && deltaY < screenY / 20) {
                        GS.RotateObject();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                myinstance.invalidate();
                            }
                        });
                    }
                }else{
                    if (deltaX < screenX / 20 && deltaY < screenY / 20) {
                        GS.RotateObject();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                myinstance.invalidate();
                            }
                        });
                    }
                }
            }
            return false;
        }
        else if(action == MotionEvent.ACTION_MOVE){
            upY = motionEvent.getY();
            upX = motionEvent.getX();
            float deltaX = downX - upX;
            float deltaY = downY - upY;
            if(orientation!=2) {
                if (deltaX < 0 && -deltaX > screenX / 10) {
                    GS.MoveObjectRight();
                    downX = upX;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myinstance.invalidate();
                        }
                    });
                    return false;
                }
                if (deltaX > 0 && deltaX > screenX / 10) {
                    downX = upX;
                    GS.MoveObjectLeft();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myinstance.invalidate();
                        }
                    });
                    return false;
                }


                if (deltaY < 0 && -deltaY > screenY / 20) {
                    downY = upY;
                    GS.MoveObjectDown();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myinstance.invalidate();
                        }
                    });
                    return false;
                }
            }else{
                if (deltaX < 0&& -deltaX>screenX/20) {
                    GS.MoveObjectRight();
                    downX=upX;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myinstance.invalidate();
                        }
                    });
                    return false;
                }
                if (deltaX > 0 && deltaX>screenX/20) {
                    downX=upX;
                    GS.MoveObjectLeft();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myinstance.invalidate();
                        }
                    });
                    return false;
                }


                if(deltaY<0 && -deltaY>screenY/20){
                    downY=upY;
                    GS.MoveObjectDown();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myinstance.invalidate();
                        }
                    });
                    return false;
                }
            }

            return true;
        }
        return false;
    }


    @Override
    public synchronized void run() {
        while (running) {

            if(handler==null){
                handler=new Handler(Looper.getMainLooper());
            }



            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }/**
            tstart=System.currentTimeMillis();
            tstop=System.currentTimeMillis();
            while (tstop-tstart<500){
                tstop=System.currentTimeMillis();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myinstance.invalidate();
                    }
                });
            }
*/
            if(GS.CurrentObjectType==0){
                if(GS.CheckIfLoseCondition()){
                    running=false;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "You Lost", Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
                }
                GS.GenerateObject();
            }else{
                GS.Draw_Figure();
                GS.MoveObjectDown();
            }



            handler.post(new Runnable() {
                @Override
                public void run() {
                    myinstance.invalidate();
                }
            });

        }
    }
}
