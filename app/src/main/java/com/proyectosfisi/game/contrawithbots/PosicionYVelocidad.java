package com.proyectosfisi.game.contrawithbots;

/**
 * Created by edson on 24/06/2015.
 */
public class PosicionYVelocidad {

    private float x;
    private float y;
    private float vx;
    private float vy;

    public PosicionYVelocidad(float x, float y, float vx, float vy){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public PosicionYVelocidad(){
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }
}
