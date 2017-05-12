package com.example.przem.tetris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    Button start,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        start=(Button) this.findViewById(R.id.ngbutton);
        exit=(Button) this.findViewById(R.id.exitbutton);
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    start.setBackgroundResource(R.drawable.button_clicked);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    start.setBackgroundResource(R.drawable.button_shape);
                    Intent intent = new Intent(getApplicationContext(),TetrisActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

        exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    exit.setBackgroundResource(R.drawable.button_clicked);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    System.exit(0);
                }

                return true;
            }
        });


    }
}
