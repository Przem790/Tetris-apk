package com.example.przem.tetris;

import android.graphics.Color;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

class GameStatus implements Serializable {
    ArrayList<Brick> GameField;
    int CurrentObjectType;
    private ArrayList<ObjectBrick> CurrentObject;
    private final int CLEAR=0;
    private final int SINGLE_DOT = 1;
    private final int I_BLOCK = 2;
    private final int L_BLOCK = 3;
    private final int O_BLOCK = 4;
    private final int S_BLOCK = 5;
    private final int T_BLOCK = 6;
    private final int Z_BLOCK = 7;

    GameStatus(float XSCREEN, float YSCREEN){                                                 //inicjalizuje GRE
        GameField = new ArrayList<>();
        //float lenght =(XSCREEN/10)-1;
        //float height =(YSCREEN/20)-1;
        //float tempx=1,tempy=1;
        CurrentObject=new ArrayList<>();
        float lenght,height,tempx,tempy;
        if(XSCREEN<YSCREEN){                         //Dla Portrait;
            lenght =(XSCREEN/10)-1;
            height =(YSCREEN/20)-1;
            tempx=1;
            tempy=1;
        }else {                                                                                 //Dla Landscape
            lenght = (XSCREEN / 20) - 1;
            height = (YSCREEN / 20) - 1;
            tempx = 1+(XSCREEN / 4);
            tempy = 1;
        }
        float tempxstable=tempx;


        for(int k=0;k<20;k++) {
            for (int i = 0; i < 10; i++) {
                if(i==9&&k==19) {
                    GameField.add(new Brick(lenght-1, height-1, tempx, tempy,k+1,i+1));
                }else if(i==9){
                    GameField.add(new Brick(lenght-1, height, tempx, tempy,k+1,i+1));
                }else if(k==19){
                    GameField.add(new Brick(lenght, height-1, tempx, tempy,k+1,i+1));
                }else
                    GameField.add(new Brick(lenght,height,tempx,tempy,k+1,i+1));
                tempx+=lenght+1;
            }
            tempy+=height+1;
            tempx=tempxstable;
        }
    }

    void update(float XSCREEN, float YSCREEN){                                                //wymiary po obroceniu
        ArrayList<Brick> temporary = new ArrayList<>();
        float lenght,height,tempx,tempy;
        if(XSCREEN<GameField.get(FindIndex(1,10)).getRect().centerX()){                         //Dla Portrait;
            lenght =(XSCREEN/10)-1;
            height =(YSCREEN/20)-1;
            tempx=1;
            tempy=1;
        }else {                                                                                 //Dla Landscape
            lenght = (XSCREEN / 20) - 1;
            height = (YSCREEN / 20) - 1;
            tempx = 1+(XSCREEN / 4);
            tempy = 1;
        }
        float tempxstable=tempx;

        for(int k=0;k<20;k++) {
            for (int i = 0; i < 10; i++) {
                if(i==9&&k==19) {
                    temporary.add(new Brick(lenght-1, height-1, tempx, tempy,k+1,i+1));
                }else if(i==9){
                    temporary.add(new Brick(lenght-1, height, tempx, tempy,k+1,i+1));
                }else if(k==19){
                    temporary.add(new Brick(lenght, height-1, tempx, tempy,k+1,i+1));
                }else
                    temporary.add(new Brick(lenght,height,tempx,tempy,k+1,i+1));

                tempx+=lenght+1;
            }
            tempy+=height+1;
            tempx=tempxstable;
        }

        for(int i=0;i<GameField.size();i++){                                                        //Przepisuje kolory na odpowiednie miejsca
            temporary.get(i).setColor(GameField.get(i).getColor());
        }
        this.GameField.clear();
        this.GameField.addAll(temporary);
        temporary.clear();

    }

    private void CheckIfLineDone(){                                                                  //sprawdzam czy sie jakas linia nie wykonala
        for(int k=1;k<=20;k++) {                                                                    //i czyszcze ja
            for(int i=1;i<=10;i++){
                if(!CompareColor(k,i))
                    break;
                else {
                    if(i==10){
                        ClearLine(k);
                        k=1;
                    }
                }
            }
        }
    }

    private boolean CompareColor(int k, int i){                                                       //porownuje dane kwadraciki ze soba
        if(GameField.get(FindIndex(k,i)).getColor()!=Color.parseColor("#000000")){
            return true;
        }else{
            return false;
        }
    }

    private void ClearLine(int row){                                                                 //Czyści daną linie kolorów
        for(int i=row;i>1;i--){
            for(int k=1;k<=10;k++){
                GameField.get(FindIndex(i,k)).setColor(GameField.get(FindIndex(i-1,k)).getColor());
            }
        }

        for(int k=1;k<=10;k++){
            GameField.get(FindIndex(1,k)).setColor(Color.parseColor("#000000"));
        }

    }

    synchronized void MoveObjectDown(){
        Draw_Figure();
        for(ObjectBrick xx:CurrentObject){
            if(xx.row<20) {
                if (!CheckIfOutOfBounds(xx.row, true)||CheckIfOutOfBounds(xx.row, false)) {
                    if (GameField.get(FindIndex(xx.row + 1, xx.column)).getColor() == Color.parseColor("#000000") || CheckIfMyBrick(xx.row + 1, xx.column)) {
                        continue;
                    } else {
                        Draw_Figure();
                        CurrentObjectType = CLEAR;
                        CurrentObject = new ArrayList<>();
                        CheckIfLineDone();
                        return;
                    }
                }else continue;
            }else{
                Draw_Figure();
                CurrentObjectType=CLEAR;
                CurrentObject=new ArrayList<>();
                CheckIfLineDone();
                return;
            }
        }

        for(ObjectBrick xx:CurrentObject) {
            xx.row++;
        }
        for(ObjectBrick xx:CurrentObject) {
            if (!CheckIfOutOfBounds(xx.row, true)){
                GameField.get(FindIndex(xx.row, xx.column)).setColor(xx.color);
                if(!CheckIfMyBrick(xx.row-1,xx.column)&&xx.row-1>=1){
                GameField.get(FindIndex(xx.row-1, xx.column)).setColor(Color.parseColor("#000000"));
                }

            }

        }
    }

    private boolean CheckIfMyBrick(int row, int column){
        boolean check=false;
        for(ObjectBrick xx:CurrentObject){
            if(xx.row==row && xx.column==column){
                check=true;
            }
        }
        return check;
    }

    void MoveObjectLeft(){
        for(ObjectBrick xx:CurrentObject){
            if(xx.column>1) {
                if (!CheckIfOutOfBounds(xx.row, true)) {
                    if (GameField.get(FindIndex(xx.row, xx.column - 1)).getColor() == Color.parseColor("#000000") || CheckIfMyBrick(xx.row, xx.column - 1)) {
                        continue;
                    } else {
                        return;
                    }
                }
            }else{
                return;
            }
        }

        for(ObjectBrick xx:CurrentObject) {
            if (!CheckIfOutOfBounds(xx.row, true)) {
                GameField.get(FindIndex(xx.row, xx.column)).setColor(Color.parseColor("#000000"));
            }
            xx.column--;
        }
        for(ObjectBrick xx:CurrentObject) {
            if (!CheckIfOutOfBounds(xx.row, true)) {
                GameField.get(FindIndex(xx.row, xx.column)).setColor(xx.color);
            }
        }
    }


    void MoveObjectRight(){
        for(ObjectBrick xx:CurrentObject){
            if(xx.column<10) {
                if (!CheckIfOutOfBounds(xx.row, true)) {
                    if (GameField.get(FindIndex(xx.row, xx.column + 1)).getColor() == Color.parseColor("#000000") || CheckIfMyBrick(xx.row, xx.column + 1)) {
                        continue;
                    } else {
                        return;
                    }
                }
            }else{
                return;
            }
        }

        for(ObjectBrick xx:CurrentObject) {
            if (!CheckIfOutOfBounds(xx.row, true)) {
                GameField.get(FindIndex(xx.row, xx.column)).setColor(Color.parseColor("#000000"));
            }
            xx.column++;
        }
        for(ObjectBrick xx:CurrentObject) {
            if (!CheckIfOutOfBounds(xx.row, true)) {
                GameField.get(FindIndex(xx.row, xx.column)).setColor(xx.color);
            }
        }


     /**   for(ObjectBrick xx:CurrentObject) {
            if (!CheckIfOutOfBounds(xx.row, true)) {
                GameField.get(FindIndex(xx.row, xx.column + 1)).setColor(xx.color);
                GameField.get(FindIndex(xx.row, xx.column)).setColor(Color.parseColor("#000000"));

            }
            xx.column++;
        }   */
    }

    private boolean CheckIfOutOfBounds(int row, boolean type){                                        //TRUE - CZY W OGOLE, FALSE-CZY DLA +1 juz ok
        if(row<1&&type){       //CZY POZA??
            return true;
        }

        if(row==0&&!type)       //CZY WLASNIE WCHODZI??
            return true;

        return false;
    }

    synchronized void Draw_Figure(){
        for(ObjectBrick xx:CurrentObject) {
            if (!CheckIfOutOfBounds(xx.row, true)) {
                GameField.get(FindIndex(xx.row,xx.column)).setColor(xx.color);
            }
        }
    }
    private synchronized void Clear_Figure(){
        for(ObjectBrick xx:CurrentObject) {
            if (!CheckIfOutOfBounds(xx.row, true)) {
                GameField.get(FindIndex(xx.row,xx.column)).setColor(Color.parseColor("#000000"));
            }
        }
    }

    synchronized void RotateObject(){

        int rowcenter=0;
        int colcenter=0;
        int temprow;
        int tempcolumn;
        ArrayList<ObjectBrick> temporary=new ArrayList<>();
        for(ObjectBrick x:CurrentObject){
          temporary.add(new ObjectBrick(x.row,x.column,x.color));
        }

        if(CurrentObjectType==CLEAR||CurrentObjectType==SINGLE_DOT||CurrentObjectType==O_BLOCK){
            return;
        }
        if(CurrentObjectType==I_BLOCK||CurrentObjectType==L_BLOCK){
            rowcenter=temporary.get(3).row;
            colcenter=temporary.get(3).column;
        }
        if(CurrentObjectType==Z_BLOCK){
            Random r = new Random();
            int tt= r.nextInt(2)+1;
            rowcenter=temporary.get(tt).row;
            colcenter=temporary.get(tt).column;
        }
        if(CurrentObjectType==S_BLOCK){
            Random r = new Random();
            int tt=-5;
            while(tt!=0&&tt!=3)
            tt= r.nextInt(3)+1;
            rowcenter=temporary.get(tt).row;
            colcenter=temporary.get(tt).column;
        }
        if(CurrentObjectType==T_BLOCK){
            rowcenter=temporary.get(4).row;
            colcenter=temporary.get(4).column;
        }

        for(ObjectBrick xx:temporary) {
            //System.out.println("PRAWDZIWA DANA PRZED OBROTEM    "+xx.row+"   "+xx.column);
            if (xx.row < 0) {
                temprow = rowcenter + (-xx.row);
            } else {
                temprow = rowcenter - xx.row;
            }
            tempcolumn = colcenter - xx.column;


            xx.column =colcenter+temprow;             //jesli jest wartosc dodatnia badz ujemna to zadziala w obu przypadkach
            xx.row =rowcenter+(-tempcolumn);          //w tym wypadku tak samo (tj tutaj musze uwzglednic minusa)
        }

        for(ObjectBrick xx:temporary) {
            //System.out.println("PRAWDZIWA DANA PO OBROCIE     "+xx.row+"   "+xx.column);

            if (CheckIfOutOfBounds(xx.row, true)) {
                continue;
            }

            if(xx.row>20) {                      //wychodzi poza x-y
                return;
            }
            if(xx.column>10||xx.column<1){       //Wychodzi poza granice kolumn
                return;
            }

            if (GameField.get(FindIndex(xx.row, xx.column)).getColor() == Color.parseColor("#000000") || CheckIfMyBrick(xx.row, xx.column)) {
                continue;
            }else
                return;
        }


        Clear_Figure();
        CurrentObject.clear();
        CurrentObject.addAll(temporary);
        temporary.clear();
        Clear_Figure();
        Draw_Figure();

    }

    void GenerateObject(){
        Random random = new Random();
        CurrentObjectType=random.nextInt(7)+1;
        int color=Color.argb(255, random.nextInt(200)+55, random.nextInt(200)+55, random.nextInt(200)+55);


        if(CurrentObjectType==SINGLE_DOT) {
            CurrentObject.add(new ObjectBrick(1, 5, color));                                              //SINGLE DOT!
            Draw_Figure();
        }
        if(CurrentObjectType==I_BLOCK) {
            CurrentObject.add(new ObjectBrick(-2, 5, color));
            CurrentObject.add(new ObjectBrick(-1, 5, color));
            CurrentObject.add(new ObjectBrick(0,  5, color));
            CurrentObject.add(new ObjectBrick(1,  5, color));                                              //I - 4 blocks
            Draw_Figure();
        }
        if(CurrentObjectType==L_BLOCK) {
            CurrentObject.add(new ObjectBrick(-1, 5, color));
            CurrentObject.add(new ObjectBrick(0,  5, color));
            CurrentObject.add(new ObjectBrick(1,  5, color));
            CurrentObject.add(new ObjectBrick(1,  6, color));                                               //L - 4 bricks
            Draw_Figure();
        }
        if(CurrentObjectType==O_BLOCK) {
            CurrentObject.add(new ObjectBrick(0, 5, color));
            CurrentObject.add(new ObjectBrick(1, 5, color));
            CurrentObject.add(new ObjectBrick(0, 6, color));
            CurrentObject.add(new ObjectBrick(1, 6, color));                                                //O Block
            Draw_Figure();
        }
        if(CurrentObjectType==S_BLOCK) {
            CurrentObject.add(new ObjectBrick(0,  6, color));
            CurrentObject.add(new ObjectBrick(0,  7, color));
            CurrentObject.add(new ObjectBrick(1,  5, color));
            CurrentObject.add(new ObjectBrick(1,  6, color));                                               //S Block
            Draw_Figure();
        }
        if(CurrentObjectType==T_BLOCK) {
            CurrentObject.add(new ObjectBrick(-1, 4, color));
            CurrentObject.add(new ObjectBrick(-1, 6, color));
            CurrentObject.add(new ObjectBrick(-1, 5, color));
            CurrentObject.add(new ObjectBrick(0,  5, color));
            CurrentObject.add(new ObjectBrick(1,  5, color));                                               //T Block
            Draw_Figure();
        }
        if(CurrentObjectType==Z_BLOCK) {
            CurrentObject.add(new ObjectBrick(0,  5, color));
            CurrentObject.add(new ObjectBrick(0,  6, color));
            CurrentObject.add(new ObjectBrick(1,  6, color));
            CurrentObject.add(new ObjectBrick(1,  7, color));                                              //Z Block
            Draw_Figure();
        }
        //System.out.println(CurrentObjectType);



    }

    boolean CheckIfLoseCondition(){
        if(GameField.get(FindIndex(1,5)).getColor()!=Color.parseColor("#000000")||GameField.get(FindIndex(1,6)).getColor()!=Color.parseColor("#000000"))
            return true;
        else
            return false;
    }


    private int FindIndex(int row, int column){
        for(int xx=0;xx<GameField.size();xx++){
            if(GameField.get(xx).row==row && GameField.get(xx).column==column){
                return xx;
            }
        }
        return -1;

    }

}
