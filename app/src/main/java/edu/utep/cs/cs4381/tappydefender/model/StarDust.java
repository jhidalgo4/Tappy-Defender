package edu.utep.cs.cs4381.tappydefender.model;

import java.util.Random;

public class StarDust extends GameObject {

    // Detect dust leaving the screen
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private int sizeOfDust;
    private int maxSizeOfDust = 9;


    // Constructor
    public StarDust(int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        // Set a speed between 0 and 9
        Random generator = new Random();
        //speed = generator.nextInt(9)+1; //refactor
        setSpeed(generator.nextInt(9)+1);

        // Set the starting coordinates
        setX(generator.nextInt(maxX));
        //x = generator.nextInt(maxX);//refactor
        setY(generator.nextInt(maxY));
        // y = generator.nextInt(maxY); //refactor
        sizeOfDust = generator.nextInt(maxSizeOfDust);
    }
    public void update(int playerSpeed){ // Speed up when the player does x -= playerSpeed;
        // x -= speed;  //refactor
        setX(getX() - getSpeed() );

        //respawn space dust
        if(getX() < 0){
            //x = maxX; //refactor
            setX(maxX);
            Random generator = new Random();
            //y = generator.nextInt(maxY); //refactored
            setY(generator.nextInt(maxY));
            // speed = generator.nextInt(15); refactored
            setSpeed(generator.nextInt(15));
        }
    }

    public int getSizeOfDust(){
        return sizeOfDust;
    }

}
