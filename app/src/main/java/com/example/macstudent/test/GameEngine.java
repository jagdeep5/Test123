package com.example.macstudent.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {
    int screenHeight;
    int screenWidth;

    boolean gameIsRunning;

    Thread gameThread;

    SurfaceHolder holder;
    Canvas canvas;
    Paint paintBrush;

    private int AXPosition;
    private int AYPosition;

    private  int snakeLength;

    private int BXPositions;
    private int BYPositions;

    private int snakeDirection;

    private int score = 0;
    private  int CXPosition;
    private int CYPosition;

    private int cLength;
    private int ADirection;
    private int blockSize;

    private  final int NUM_BLOCKS_WIDE = 50;
    private  int numBlocksHigh;


    int maxX;
    int maxY;
    int minX;
    int minY;

    public  GameEngine(Context context, int h, int w, Point p){
        super(context);

        this.screenHeight = p.y;
        this.screenWidth = p.x;


        holder = this.getHolder();
        paintBrush = new Paint();


        this.blockSize = this.screenWidth/this.NUM_BLOCKS_WIDE;
        this.numBlocksHigh = this.screenHeight/ this.blockSize;



        this.minX = 8;
        this.minY = 1;
        this.maxX = (NUM_BLOCKS_WIDE -8);
        this.maxY =  (numBlocksHigh -70);




        this.newGame();

    }
    @Override
    public void run() {
        while (gameIsRunning == true)  {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }

    private void newGame(){
        spawnSnake();

        // Add the first apple
        spawnA();
        spawnC();
        // Reset the score
        score = 0;
    }

    private void spawnSnake(){

        this.snakeLength =1;
        this.BXPositions = NUM_BLOCKS_WIDE /2;
        this.BYPositions = numBlocksHigh-20;

        this.snakeDirection = 0;
    }

    private void spawnA(){


        AXPosition = NUM_BLOCKS_WIDE/2;
        AYPosition = numBlocksHigh-80;

    }
    private void spawnC(){


        this.CXPosition = NUM_BLOCKS_WIDE /2;
        this.CYPosition = numBlocksHigh-15;



    }

    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    public void redrawSprites(){
        if (this.holder.getSurface().isValid()){
            this.canvas = this.holder.lockCanvas();

            canvas.drawColor(Color.argb(255,0,0,0));

            paintBrush.setColor(Color.argb(255, 255, 0, 0));

            // Draw apple
            canvas.drawRect(AXPosition * (blockSize) ,
                    (AYPosition * blockSize),
                    (AXPosition * blockSize) + (2*blockSize),
                    (AYPosition * blockSize) + blockSize,
                    paintBrush);

            // Draw apple

            paintBrush.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawRect(CXPosition * blockSize,
                    (CYPosition * blockSize),
                    (CXPosition * blockSize) + (4*blockSize),
                    (CYPosition * blockSize) + blockSize,
                    paintBrush);


            paintBrush.setColor(Color.argb(255,255,255,255));

            for(int i = 0; i< snakeLength; i++) {
                canvas.drawRect(BXPositions * blockSize, BYPositions * blockSize, (BXPositions * blockSize) + blockSize, (BYPositions * blockSize) + blockSize, paintBrush);


            }



            paintBrush.setTextSize(50);
            paintBrush.setColor(Color.argb(255, 255, 255, 0));
            this.canvas.drawText("Score: " + score, 50, 300, paintBrush);


            // top
            this.canvas.drawLine(this.minX*blockSize, this.minY*blockSize, this.maxX*blockSize, this.minY*blockSize, paintBrush );
            // bottom
            //this.canvas.drawLine(this.minX*blockSize, this.maxY*blockSize, this.maxX*blockSize, this.maxY*blockSize, paintBrush );
            // left wall
            this.canvas.drawLine(this.minX*blockSize,this.minY*blockSize,this.minX*blockSize,this.maxY*blockSize,paintBrush);

            // right wall
            this.canvas.drawLine(this.maxX*blockSize,this.minY*blockSize, this.maxX*blockSize,this.maxY*blockSize,paintBrush);
            this.holder.unlockCanvasAndPost(canvas);
        }


    }

    public void updatePositions() {

        if(BXPositions <= minX || BXPositions >= maxX || BYPositions <= minY )
        {
            score = 0;
            this.spawnSnake();


        }

        if ( (BXPositions == AXPosition)
                && (BYPositions == AYPosition)) {
            snakeDirection = 2;
            score++;

        }
        else if((BXPositions == CXPosition)
                && (BYPositions == CYPosition)){
            snakeDirection = 0;
        }
        if (snakeDirection == 0) {
            //up
            BYPositions = BYPositions - 1;
        }
        else if (snakeDirection == 1) {
            // right
            BXPositions = BXPositions + 1;
        }
        else if (snakeDirection == 2) {
            // down
            BYPositions = BYPositions + 1;
        }
        else if (snakeDirection == 3) {
            // left
            BXPositions = BXPositions - 1;
        }

        if (ADirection == 1) {
            //up
            AXPosition = AXPosition + 1;
        }

        else if (ADirection == 3) {
            // right
            AXPosition = AXPosition - 1;
        }



    }

    public void setFPS(){
        try{
            gameThread.sleep(75);
        }
        catch (Exception e){

        }
    }

    public boolean onTouchEvent(MotionEvent event){

        if(event.getAction() == MotionEvent.ACTION_UP){
            if(this.ADirection == 1){
                this.ADirection = 3;
            }
            else this.ADirection =1;

        }
        return true;
    }
}