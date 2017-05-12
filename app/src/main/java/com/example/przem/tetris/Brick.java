package com.example.przem.tetris;

import android.graphics.Color;
import android.graphics.RectF;

import java.io.Serializable;

public class Brick implements Serializable{
    private RectF rect;
    private float lenght;
    private float height;
    private float x;
    private float y;
    public int row;
    public int column;
    private int color;

    public Brick(float lenght,float height,float x,float y,int row,int column){
        this.lenght=lenght;
        this.height=height;
        this.x=x;
        this.y=y;
        rect= new MyRectFSerializable(x,y,x+lenght,y+height);
        color=Color.parseColor("#000000");
        this.row=row;
        this.column=column;
    }

    public void setColor(int crc){
        this.color=crc;
    }
    public int getColor(){
        return color;
    }
    public RectF getRect(){return rect;}

}
