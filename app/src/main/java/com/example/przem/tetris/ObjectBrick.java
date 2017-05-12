package com.example.przem.tetris;

import java.io.Serializable;

/**
 * Created by przem on 10.05.2017.
 */

public class ObjectBrick implements Serializable {
    public int row;
    public int column;
    public int color;
    public ObjectBrick(int row,int column,int color){
        this.row=row;
        this.column=column;
        this.color=color;
    }

}
