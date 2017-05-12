package com.example.przem.tetris;

import android.graphics.RectF;

import java.io.Serializable;

/**
 * Created by przem on 10.05.2017.
 */

public class MyRectFSerializable extends RectF implements Serializable {


    public MyRectFSerializable(float x, float y, float v, float v1) {
        super(x,y,v,v1);
    }
}
