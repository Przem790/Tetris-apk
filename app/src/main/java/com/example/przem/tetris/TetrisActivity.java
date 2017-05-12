package com.example.przem.tetris;

import android.app.Activity;
import android.os.Bundle;

import java.io.Serializable;

public class TetrisActivity extends Activity {
    GameStatus GS;
    TetrisView tv;
    final String OBJECT="Object";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv = new TetrisView(this);
        if(savedInstanceState!=null) {
            this.GS=(GameStatus) savedInstanceState.getSerializable(OBJECT);
            tv.setGS(this.GS);
        }
        setContentView(tv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tv.pause();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(OBJECT,(Serializable)tv.getGS());
    }
}
