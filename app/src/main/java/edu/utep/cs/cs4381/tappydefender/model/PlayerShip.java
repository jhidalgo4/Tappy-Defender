package edu.utep.cs.cs4381.tappydefender.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import edu.utep.cs.cs4381.tappydefender.R;

public class PlayerShip extends Ship{
    private static final int GRAVITY = -25;
    private static final int MIN_SPEED = 5;
    private static final int MAX_SPEED = 42;

    private boolean boosting;

    private int maxY;
    private int minY;

    int shieldStrength = 3;

    public PlayerShip(Context context, int width, int height) {
        super();
        //x = 50; // refactored
        setX(50);
        //y = 50; // refactored
        setY(50);
        //speed = 1; // refactored
        setSpeed(1);
        setBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ship)  );

        setBitmap( scaleBitmap(getBitmap(), width+900) ); // screen res could be: 1800

        maxY = height - getBitmapHeight(); // Makes sure the whole ship is able to drag on the bottom of the screen
        minY = 0;

        hitbox = new Rect(getX(), getY(), getBitmapWidth(), getBitmapHeight() );
    }

    //PLAYERSHIP Update
    public void update() {
        if (boosting) {
            // speed += 2; //refactored
            setSpeed( getSpeed() +40);
        } else {
            //speed -= 5;//refactored
            setSpeed( getSpeed() - 10);
        }
        //refactored
        if (getSpeed() < MIN_SPEED) {
            //speed = MIN_SPEED;
            setSpeed(MIN_SPEED);
        }
        //refactored
        if (getSpeed() > MAX_SPEED) {
            //speed = MAX_SPEED;
            setSpeed(MAX_SPEED);

        }

        //y -= speed + GRAVITY; //refactored
        setY(getY() - (getSpeed() + GRAVITY) );


        //refactored
        if (getY() < minY) {
            //y = minY;
            setY(minY);

        }
        //refactored
        if (getY() > maxY) {
            //y = maxY;
            setY(maxY);
        }

        hitbox.set(getX(), getY(), getX() + getBitmapWidth(), getY() + getBitmapHeight()); //refactored


        //refactored
        hitbox.left = getX();
        hitbox.top = getY();
        hitbox.right = getX() + getBitmapWidth();
        hitbox.bottom = getY() + getBitmapHeight();
    }

    //ScaleBitmaps for size
    private Bitmap scaleBitmap(Bitmap bitmap, int screenWidth) {
        float scale = screenWidth < 1000 ? 3 : (screenWidth < 1200 ? 2 : 1.5f);
        return Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() / scale),
                (int) (bitmap.getHeight() / scale),
                false);
    }

    //GETTERS
    public int getShieldStrength(){
        return shieldStrength;
    }

    public void setShieldStrength(int i){
        this.shieldStrength = i;
    }

    //SETTERS
    public void setBoosting(boolean flag) {
        boosting = flag;
    }


}
