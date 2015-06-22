package com.proyectosfisi.game.contrawithbots;

/**
 * Created by edson on 14/06/2015.
 */
public class Puntaje {

    protected int puntaje;
    protected int botsCaidos;
    protected float avance;

    public Puntaje(){
        puntaje = 0;
        botsCaidos = 0;
        avance = 0;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getBotsCaidos() {
        return botsCaidos;
    }

    public void setBotsCaidos(int botsCaidos) {
        this.botsCaidos = botsCaidos;
    }

    public float getAvance() {
        return avance;
    }

    public void setAvance(float avance) {
        this.avance = avance;
    }
}
