package edu.utep.cs.cs4381.tappydefender.model;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Ship extends GameObject {
    private Bitmap bitmap;
    Rect hitbox;


    //SETTERS
    public void setBitmap(Bitmap b ){
        this.bitmap = b;
    }
    public void setHitbox(Rect h){
        this.hitbox = h;
    }


    //GETTERS
    public Bitmap getBitmap() {
        return this.bitmap; }

    public Rect getHitbox() {
        return hitbox;
    }

    public int getBitmapWidth(){
        return bitmap.getWidth();
    }
    public int getBitmapHeight(){
        return bitmap.getHeight();
    }
}
