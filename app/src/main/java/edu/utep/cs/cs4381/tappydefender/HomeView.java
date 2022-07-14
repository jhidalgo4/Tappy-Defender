package edu.utep.cs.cs4381.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

public class HomeView extends View {
    private int width, height;
    Paint paint;
    Rect rect;
    ButtonListener buttonlis;


    //Constructor
    public HomeView(Context context) {
        super(context);
        calculateWidthAndHeight();
        paint = new Paint();
    }

    @Override
    public void onDraw(Canvas canvas){
        drawBackgroundImage(canvas); //draw background image

        drawButton(canvas); //draw button
    }

    //Calculate Height and width
    private void calculateWidthAndHeight() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    width = Math.min(getWidth(), getHeight());
                    height = Math.max(getWidth(), getHeight());
                }
            });
        }
    }

    //Detect if in Sqaure
    private static boolean isIn(float x, float y, float left, float top, float right, float bottom) {
        return new RectF(left, top, right, bottom).contains(x, y);
    }

    //If Button Click, Call Interface method
    public void detectPushOfButton(float x, float y){
        if (isIn(x,y,rect.left,rect.top,rect.right,rect.bottom) ){
            buttonlis.squButtonClicked();
        }
    }
    public void setDiscClickListener(ButtonListener listener) {
        buttonlis = listener;
    }

    //Detect touch
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                detectPushOfButton(event.getX(), event.getY() );
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }


    // DRAW
    public void drawBackgroundImage(Canvas canvas){
        paint.setColor(Color.BLACK);

        //SET SQUARE SPACE
        rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = height;
        rect.bottom = width;

        //Bitmap
        Bitmap backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        canvas.drawBitmap(backBitmap, null, rect, paint);
    }

    public void drawButton(Canvas canvas){
        //BUTTON SQUARE
        paint.setColor(Color.WHITE);
        canvas.drawRect(height-500,width-(width/4),height,width,paint);

        //TEXT
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        canvas.drawText("START",height-400,width-(width/12), paint);
    }
}
