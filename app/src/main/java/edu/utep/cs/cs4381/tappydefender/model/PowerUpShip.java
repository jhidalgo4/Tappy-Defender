package edu.utep.cs.cs4381.tappydefender.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import edu.utep.cs.cs4381.tappydefender.R;

public class PowerUpShip extends Ship {
    private static final Random random = new Random();
    private int maxX, minX; // move horizontally from right to left
    private int maxY, minY;
    private boolean collided;

    public PowerUpShip(Context ctx, int screenX, int screenY){
        setSpeed(getSpeed() );

        //bitmap = BitmapFactory.decodeResource(ctx.getResources(), pickImage() ); //refactored
        setBitmap(  BitmapFactory.decodeResource(ctx.getResources(), R.drawable.health_logo));
        //bitmap = scaleBitmap(bitmap, 1800); //refactored
        setBitmap( scaleBitmap(getBitmap(),300) ); //Could be 1800
        maxX = screenX;
        maxY = screenY-getBitmapHeight();
        minX = 0;
        minY = getBitmapHeight();
        //speed = random.nextInt(6) + 9; //refactored
        setSpeed(random.nextInt(6) + 9 );
        //x = screenX;  //refactored
        setX(screenX + 1000);
        //y = random.nextInt(maxY) - bitmap.getHeight(); //refactored
        setY(random.nextInt(maxY) - getBitmap().getHeight() );

        //hitbox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight()); //refactored
        setHitbox( new Rect(getX(), getY(), getBitmap().getWidth(), getBitmap().getHeight() ) );

        collided = false;
    }

    public void update(int playerSpeed) {
        setX( getX() - getSpeed() );
//        x -= playerSpeed; //refactored
        setX( getX() - getSpeed() );
        // x -= speed; //refactored
        if (getX() < minX - getBitmapWidth() ) {
            //speed = random.nextInt(10)+10; //refactored
            setSpeed(random.nextInt(10)+10);
            //x = maxX;
            setX(maxX );
            //y = random.nextInt(maxY) - bitmap.getHeight();
            setY(random.nextInt(maxY) - getBitmapWidth() );
        }

        hitbox.set(getX(), getY(), getX() + getBitmapWidth(), getY() + getBitmapHeight() );

        hitbox.left = getX();
        hitbox.top = getY();
        hitbox.right = getX() + getBitmapWidth();
        hitbox.bottom = getY() + getBitmapHeight();
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int screenWidth) {
        float scale = screenWidth < 1000 ? 3 : (screenWidth < 1200 ? 2 : 1.5f);
        return Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() / scale),
                (int) (bitmap.getHeight() / scale),
                false);
    }

    public void setHasCollided(boolean b){
        this.collided = b;
    }



}
