package com.proyectosfisi.game.contrawithbots;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by edson on 10/05/2015.
 */
public class BotFactory {

    public static float DISCTANCIA_MEDIA = 112;
    public static float DISCTANCIA_MEDIA_ERROR = 32;
    public static float BALA_TIME = 1.25f;

    private ArrayList<Actor> botsLevel1;
    private ArrayList<Actor> botsLevel2;
    private ArrayList<Actor> botsLevel3;
    private ArrayList<Actor> botsComunes;
    private ArrayList<Actor> botsUltimos;
    private ArrayList<Actor> personajeJugador;

    private Escenario escenario;
    private TiledTextureRegion pTextureEnemyRegion;
    private TiledTextureRegion pTextureEnemy2Region;
    private TiledTextureRegion pTextureEnemyFrancotiradorRegion;
    private TiledTextureRegion pTextureEnemyCorredorRegion;
    private TiledTextureRegion pTextureEnemyMetrallaRegion;
    private TiledTextureRegion pTextureEnemyPerroRegion;
    private TiledTextureRegion pTextureEnemyNaveRegion;
    private TiledTextureRegion pTextureBombaRegion;
    private TiledTextureRegion pTextureVidaRegion;
    private TiledTextureRegion mBulletTexture;
    private VertexBufferObjectManager pVertexBufferObjectManager;

    protected boolean muertos;

    public BotFactory(Escenario escenario, TiledTextureRegion pEnemyTextureRegion, TiledTextureRegion pEnemy2TextureRegion, TiledTextureRegion pTextureEnemyFrancotiradorRegion, TiledTextureRegion pTextureEnemyCorredorRegion, TiledTextureRegion pTextureEnemyMetrallaRegion, TiledTextureRegion pTextureEnemyPerroRegion, TiledTextureRegion pTextureEnemyNaveRegion, TiledTextureRegion pTextureBombaRegion, TiledTextureRegion pTextureVidaRegion, TiledTextureRegion mBulletTexture, VertexBufferObjectManager pVertexBufferObjectManager){
        this.escenario = escenario;
        this.pTextureEnemyRegion = pEnemyTextureRegion;
        this.pTextureEnemy2Region = pEnemy2TextureRegion;
        this.pTextureEnemyFrancotiradorRegion = pTextureEnemyFrancotiradorRegion;
        this.pTextureEnemyCorredorRegion = pTextureEnemyCorredorRegion;
        this.pTextureEnemyMetrallaRegion = pTextureEnemyMetrallaRegion;
        this.pTextureEnemyPerroRegion = pTextureEnemyPerroRegion;
        this.pTextureEnemyNaveRegion = pTextureEnemyNaveRegion;
        this.pTextureBombaRegion = pTextureBombaRegion;
        this.pTextureVidaRegion = pTextureVidaRegion;
        this.mBulletTexture = mBulletTexture;
        this.pVertexBufferObjectManager = pVertexBufferObjectManager;

        botsLevel1 = new ArrayList<>();
        botsLevel2 = new ArrayList<>();
        botsLevel3 = new ArrayList<>();
        botsComunes = new ArrayList<>();
        botsUltimos = new ArrayList<>();
        personajeJugador = new ArrayList<>();
    }

    public void initBots() {

        personajeJugador.add(escenario.getJugador());

        float right = escenario.getFillCropResolutionPolicy().getRight();
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float alto = escenario.getFillCropResolutionPolicy().getTop() - escenario.getFillCropResolutionPolicy().getBottom();

        //Enemigos communes

        botsComunes.add(crearBotEnemigoNave(3.5f * alto, PersonajeNave.ALTO, Actor.ORIENTATION_LEFT));// 0

        botsComunes.add(crearBotEnemigoMetralla(752, 0, Actor.ORIENTATION_LEFT));
        botsComunes.add(crearBotEnemigoMetralla(1008, 0, Actor.ORIENTATION_LEFT));
        botsComunes.add(crearBotEnemigoMetralla( 1792, 0, Actor.ORIENTATION_LEFT));
        botsComunes.add(crearBotEnemigoMetralla( 2382, 0, Actor.ORIENTATION_LEFT));
        botsComunes.add(crearBotEnemigoMetralla( 2798, 0, Actor.ORIENTATION_LEFT));
        botsComunes.add(crearBotEnemigoMetralla( 2909, 0, Actor.ORIENTATION_LEFT));
        botsComunes.add(crearBotEnemigoMetralla(3238, 0, Actor.ORIENTATION_LEFT));
        botsUltimos.add(botsComunes.get(botsComunes.size() - 1));

        botsComunes.add(crearBotEnemigoPerro(1744.5f, 0, Actor.ORIENTATION_LEFT));
        botsComunes.add(crearBotEnemigoPerro(1360.5f, 0, Actor.ORIENTATION_LEFT));
        botsComunes.add(crearBotEnemigoPerro(1104.5f, 0, Actor.ORIENTATION_LEFT));

        botsComunes.add(crearBotEnemigoFrancotirador(860, 128, Actor.ORIENTATION_LEFT));

        botsComunes.add(crearBotEnemigoFrancotirador(2016, 128, Actor.ORIENTATION_LEFT));
        botsComunes.add(crearBotEnemigoFrancotirador(1848, 96, Actor.ORIENTATION_LEFT));

        botsComunes.add(crearBotEnemigoFrancotirador(3238, 46, Actor.ORIENTATION_LEFT));
        botsUltimos.add(botsComunes.get(botsComunes.size() - 1));
        botsComunes.add(crearBotEnemigoFrancotirador(3235, 86, Actor.ORIENTATION_LEFT));
        botsUltimos.add(botsComunes.get(botsComunes.size() - 1));

        //BOTS Level 1

        botsLevel1.add(crearBotEnemigoCorredor( -2 * alto, 0, Actor.ORIENTATION_RIGHT));
        botsLevel1.add(crearBotEnemigoCorredor( 3 * alto, 0, Actor.ORIENTATION_LEFT));
        botsLevel1.add(crearBotEnemigoCorredor( 4 * alto, 0, Actor.ORIENTATION_RIGHT));
        //botsLevel1.add(crearBotEnemigoCorredor( 3.5f * alto, 0, Actor.ORIENTATION_LEFT));

        //BOTS Level 2

        botsLevel2.add(crearRNBotEnemigo(3 * alto, 0, Actor.ORIENTATION_LEFT));

        //BOTS Level 3

        botsLevel3.add(crearRNDBotEnemigo( 3 * alto, 0, Actor.ORIENTATION_LEFT));

    }

    public PersonajeEnemigo crearBotEnemigo(float x, float y, int tipo, int orientacion){
        PersonajeEnemigo enemigo = new PersonajeEnemigo(tipo, escenario, x, y, pTextureEnemyRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        escenario.getLayerPlayer().attachChild(enemigo);
        return enemigo;
    }

    public PersonajeFrancotirador crearBotEnemigoFrancotirador(float x, float y, int orientacion){
        PersonajeFrancotirador enemigo = new PersonajeFrancotirador(escenario, x, y, pTextureEnemyFrancotiradorRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        escenario.getLayerPlayer().attachChild(enemigo);
        return enemigo;
    }

    public PersonajeCorredor crearBotEnemigoCorredor(float x, float y, int orientacion){
        PersonajeCorredor enemigo = new PersonajeCorredor(escenario, x, y, pTextureEnemyCorredorRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        escenario.getLayerPlayer().attachChild(enemigo);
        return enemigo;
    }

    public PersonajeMetralla crearBotEnemigoMetralla(float x, float y, int orientacion){
        PersonajeMetralla enemigo = new PersonajeMetralla(escenario, x, y, pTextureEnemyMetrallaRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        escenario.getLayerPlayer().attachChild(enemigo);
        return enemigo;
    }

    public PersonajePerro crearBotEnemigoPerro(float x, float y, int orientacion){
        PersonajePerro enemigo = new PersonajePerro(escenario, x, y, pTextureEnemyPerroRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        escenario.getLayerPlayer().attachChild(enemigo);
        return enemigo;
    }

    public PersonajeNave crearBotEnemigoNave(float x, float y, int orientacion){
        PersonajeNave enemigo = new PersonajeNave(escenario, x, y, pTextureEnemyNaveRegion, pTextureBombaRegion, pTextureVidaRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        escenario.getLayerPlayer().attachChild(enemigo);
        return enemigo;
    }
    public PersonajeRNBot crearRNBotEnemigo(float x, float y, int orientacion){
        PersonajeRNBot enemigo = new PersonajeRNBot(escenario, x, y, pTextureEnemyRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        escenario.getLayerPlayer().attachChild(enemigo);
        return enemigo;
    }
    public PersonajeRNDBot crearRNDBotEnemigo(float x, float y, int orientacion){
        PersonajeRNDBot enemigo = new PersonajeRNDBot(escenario, x, y, pTextureEnemy2Region, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.inactivar();
        escenario.getLayerPlayer().attachChild(enemigo);
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
        muertos = false;
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
        all.addAll(botsComunes);
        return all;
    }

    public boolean validaBotMuertos(){
        if(!muertos) {
            int[] ibots;
            switch (escenario.getIntro().getNivelSelec()) {
                case 1:
                    for (Actor a : botsLevel1) {
                        if (!a.isDead()) {
                            return false;
                        }
                    }
                    break;
                case 2:
                    for (Actor a : botsLevel2) {
                        if (!a.isDead()) {
                            return false;
                        }
                    }
                    break;
                case 3:
                    for (Actor a : botsLevel3) {
                        if (!a.isDead()) {
                            return false;
                        }
                    }
                    break;
            }
            for (Actor actor : botsUltimos) {
                if (!actor.isDead()) {
                    return false;
                }
            }
            escenario.getBase().activar();
            muertos = true;
            return true;
        }else{
            return true;
        }
    }

    public ArrayList<Actor> getBots() {
        ArrayList<Actor> botsSelec = new ArrayList<>();
        switch(escenario.getIntro().getNivelSelec()) {
            case 1:
                botsSelec.addAll(botsLevel1);
                break;
            case 2:
                botsSelec.addAll(botsLevel2);
                break;
            case 3:
                botsSelec.addAll(botsLevel3);
                break;
        }
        if(botsSelec.size() == 0){
            return null;
        }else{
            botsSelec.addAll(botsComunes);
            return botsSelec;
        }
    }
}
