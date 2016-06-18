package com.proyectosfisi.game.contrawithbots;

/**
 * Created by edson on 01/07/2015.
 */
public class MinMaxXY {

    protected float xMin;
    protected float xMax;
    protected float yMin;
    protected float yMax;

    public MinMaxXY(){
    }

    public MinMaxXY(float xMin, float xMax, float yMin, float yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public float getxMin() {
        return xMin;
    }

    public void setxMin(float xMin) {
        this.xMin = xMin;
    }

    public float getxMax() {
        return xMax;
    }

    public void setxMax(float xMax) {
        this.xMax = xMax;
    }

    public float getyMin() {
        return yMin;
    }

    public void setyMin(float yMin) {
        this.yMin = yMin;
    }

    public float getyMax() {
        return yMax;
    }

    public void setyMax(float yMax) {
        this.yMax = yMax;
    }

}
