package com.proyectosfisi.game.contrawithbots;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by edson on 10/05/2015.
 */
public class BotFactory {

    public static float DISCTANCIA_MEDIA = 144;
    public static float DISCTANCIA_MEDIA_ERROR = 20;

    private ArrayList<Actor> botsLevel1;
    private ArrayList<Actor> botsLevel2;
    private ArrayList<Actor> botsLevel3;
    private ArrayList<Actor> personajeJugador;

    private Escenario escenario;
    private TiledTextureRegion pTextureEnemyRegion;
    private TiledTextureRegion pTextureEnemyFrancotiradorRegion;
    private TiledTextureRegion mBulletTexture;
    private VertexBufferObjectManager pVertexBufferObjectManager;

    public BotFactory(Escenario escenario, TiledTextureRegion pEnemyTextureRegion, TiledTextureRegion pTextureEnemyFrancotiradorRegion, TiledTextureRegion mBulletTexture, VertexBufferObjectManager pVertexBufferObjectManager){
        this.escenario = escenario;
        this.pTextureEnemyRegion = pEnemyTextureRegion;
        this.pTextureEnemyFrancotiradorRegion = pTextureEnemyFrancotiradorRegion;
        this.mBulletTexture = mBulletTexture;
        this.pVertexBufferObjectManager = pVertexBufferObjectManager;

        botsLevel1 = new ArrayList<>();
        botsLevel2 = new ArrayList<>();
        botsLevel3 = new ArrayList<>();
        personajeJugador = new ArrayList<>();
    }

    public void initBots() {

        personajeJugador.add(escenario.getJugador());

        float right = escenario.getCropResolutionPolicy().getRight();
        float left = escenario.getCropResolutionPolicy().getLeft();
        float backLayer = escenario.getParallaxLayerBackSprite().getWidth();

        //BOTS Level 1
        /*
        botsLevel1.add(crearBotEnemigo(-1 * (right - left) / 2, 0, PersonajeEnemigo.TIPO_NORMAL, Actor.ORIENTATION_RIGHT)); //0
        botsLevel1.add(crearBotEnemigo( 3 * (right - left) / 2, 0, PersonajeEnemigo.TIPO_NORMAL, Actor.ORIENTATION_LEFT)); //1
        botsLevel1.add(crearBotEnemigo(backLayer / 4, 0, PersonajeEnemigo.TIPO_NORMAL, Actor.ORIENTATION_LEFT)); //2

        botsLevel1.add(crearBotEnemigo( 715, 0, PersonajeEnemigo.TIPO_AGACHADO, Actor.ORIENTATION_LEFT)); //3
        botsLevel1.add(crearBotEnemigo( 1645, 0, PersonajeEnemigo.TIPO_AGACHADO, Actor.ORIENTATION_LEFT)); //4
        botsLevel1.add(crearBotEnemigo( 2300, 0, PersonajeEnemigo.TIPO_AGACHADO, Actor.ORIENTATION_LEFT)); //5
        botsLevel1.add(crearBotEnemigo( 3025, 0, PersonajeEnemigo.TIPO_AGACHADO, Actor.ORIENTATION_LEFT)); //6
        botsLevel1.add(crearBotEnemigo( 4263, 0, PersonajeEnemigo.TIPO_AGACHADO, Actor.ORIENTATION_LEFT)); //7
*/

        botsLevel1.add(crearBotEnemigoFrancotirador(860, 128, Actor.ORIENTATION_LEFT)); //8

        botsLevel1.add(crearBotEnemigoFrancotirador(2016, 128, Actor.ORIENTATION_LEFT)); //9
        botsLevel1.add(crearBotEnemigoFrancotirador(1848, 96, Actor.ORIENTATION_LEFT)); //10

        botsLevel1.add(crearBotEnemigoFrancotirador(3040, 128, Actor.ORIENTATION_LEFT)); //11
        botsLevel1.add(crearBotEnemigoFrancotirador(3080, 96, Actor.ORIENTATION_LEFT)); //12

        botsLevel1.add(crearBotEnemigoFrancotirador(4265, 46, Actor.ORIENTATION_LEFT)); //13
        botsLevel1.add(crearBotEnemigoFrancotirador(4261, 86, Actor.ORIENTATION_LEFT)); //14
        //BOTS Level2

        botsLevel2.add(crearRNBotEnemigo( 3 * (right - left) / 2, 0, Actor.ORIENTATION_LEFT)); //1

        for (Actor p : botsLevel1) {
            escenario.getLayerPlayer().attachChild(p);
        }
        for (Actor p : botsLevel2) {
            escenario.getLayerPlayer().attachChild(p);
        }
        for (Actor p : botsLevel3) {
            escenario.getLayerPlayer().attachChild(p);
        }
    }

    public PersonajeEnemigo crearBotEnemigo(float x, float y, int tipo, int orientacion){
        PersonajeEnemigo enemigo = new PersonajeEnemigo(tipo, escenario, x, y, pTextureEnemyRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        return enemigo;
    }

    public PersonajeFrancotirador crearBotEnemigoFrancotirador(float x, float y, int orientacion){
        PersonajeFrancotirador enemigo = new PersonajeFrancotirador(escenario, x, y, pTextureEnemyFrancotiradorRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        return enemigo;
    }

    public PersonajeRNBot crearRNBotEnemigo(float x, float y, int orientacion){
        PersonajeRNBot enemigo = new PersonajeRNBot(escenario, x, y, pTextureEnemyRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        return enemigo;
    }

    public void init(){
        ArrayList<Actor> botsSelec = getBots();
        if(botsSelec != null) {
            for (Actor bot : botsSelec) {
                bot.init();
                bot.activar();
            }
        }
    }

    public void inactivar(){
        for (Actor p : getAllBots()) {
            p.inactivar();
        }
    }

    public ArrayList<Actor> getAllBots(){
        ArrayList<Actor> all = new ArrayList<>();
        all.addAll(botsLevel1);
        all.addAll(botsLevel2);
        all.addAll(botsLevel3);
        return all;
    }

    public boolean validaBotMuertos(){
        switch(escenario.getIntro().getNivelSelec()){
            case 1:
                int[] ibots = new int[]{14, 13, 7, 0, 1, 2};
                for(int i : ibots){
                    if(!botsLevel1.get(i).isDead()){
                        return false;
                    }
                }
                break;
            case 2:
                for (Actor bot : botsLevel2){
                    if(!bot.isDead()){
                        return false;
                    }
                }
                break;
            case 3:
                break;
        }
        return true;
    }

    public ArrayList<Actor> getBots() {
        ArrayList<Actor> botsSelec = null;
        switch(escenario.getIntro().getNivelSelec()) {
            case 1:
                botsSelec = botsLevel1;
                break;
            case 2:
                botsSelec = botsLevel2;
                break;
            case 3:
                botsSelec = botsLevel3;
                break;
        }
        return botsSelec;
    }
}
