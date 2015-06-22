package com.proyectosfisi.game.contrawithbots;

/**
 * Created by edson on 14/06/2015.
 */
public class Puntajes {

    protected Puntaje[] puntajes;
    public Intro intro;

    public Puntajes(Intro intro){
        puntajes = new Puntaje[3];
        puntajes[0] = new Puntaje();
        puntajes[1] = new Puntaje();
        puntajes[2] = new Puntaje();
        this.intro = intro;
    }

    public void resetPuntaje(){
        Puntaje p = getPuntaje();
        if(p != null){
            p.setPuntaje(0);
            p.setBotsCaidos(0);
            p.setAvance(0);
        }
    }

    public Puntaje getPuntaje(){
        return intro.getNivelSelec() == 0 ? null : puntajes[intro.getNivelSelec() -1];
    }

}
