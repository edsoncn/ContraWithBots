package com.proyectosfisi.game.contrawithbots;

/**
 * Created by edson on 10/05/2015.
 */
public class BotFactory {

private ArrayList<PersonajeEnemigo> bots;

private BotFactory(){
bots = new ArrayList<PersonajeEnemigo>();
}

    public static BotFactory getInstance(){
        if(instance == null){
            synchronized (BotFactory.class){
                if (instance == null){
                    instance = new BotFactory();
                }
            }
        }
        return instance;
    }

}
