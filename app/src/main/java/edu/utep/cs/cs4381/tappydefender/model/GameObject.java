package edu.utep.cs.cs4381.tappydefender.model;

public class GameObject {
    private int x, y, speed;

    //SETTERS
    public void setSpeed(int i){
        this.speed = i;
    }
    public void setX(int i){
        this.x = i;
    }
    public void setY(int i){
        this.y = i;
    }

    //GETTERS
    public int getSpeed(){
        return speed;
    }
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

}
