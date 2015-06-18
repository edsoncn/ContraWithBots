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

    private ArrayList<Personaje> bots;
    private ArrayList<Personaje> personajeJugador;

    private Escenario escenario;
    private TiledTextureRegion pTextureRegion;
    private TiledTextureRegion mBulletTexture;
    private VertexBufferObjectManager pVertexBufferObjectManager;

    public BotFactory(Escenario escenario, TiledTextureRegion pTextureRegion, TiledTextureRegion mBulletTexture, VertexBufferObjectManager pVertexBufferObjectManager){
        this.escenario = escenario;
        this.pTextureRegion = pTextureRegion;
        this.mBulletTexture = mBulletTexture;
        this.pVertexBufferObjectManager = pVertexBufferObjectManager;

        bots = new ArrayList<>();
        personajeJugador = new ArrayList<>();
    }

    public void initBots() {

        personajeJugador.add(escenario.getJugador());

        float right = escenario.getCropResolutionPolicy().getRight();
        float left = escenario.getCropResolutionPolicy().getLeft();
        float backLayer = escenario.getParallaxLayerBackSprite().getWidth();

        //bots.add(crearBotEnemigo(-1 * (right - left) / 2, 0, PersonajeEnemigo.TIPO_NORMAL, PersonajeJugador.ORIENTATION_RIGHT)); //0
        //bots.add(crearBotEnemigo( 3 * (right - left) / 2, 0, PersonajeEnemigo.TIPO_NORMAL, PersonajeJugador.ORIENTATION_LEFT)); //1
        //bots.add(crearBotEnemigo( backLayer / 4, 0, PersonajeEnemigo.TIPO_NORMAL, PersonajeJugador.ORIENTATION_LEFT)); //2

        bots.add(crearRNBotEnemigo( 3 * (right - left) / 2, 0, PersonajeJugador.ORIENTATION_LEFT)); //1

        /*bots.add(crearBotEnemigo( 715, 0, PersonajeEnemigo.TIPO_AGACHADO, PersonajeJugador.ORIENTATION_LEFT)); //3
        bots.add(crearBotEnemigo( 1645, 0, PersonajeEnemigo.TIPO_AGACHADO, PersonajeJugador.ORIENTATION_LEFT)); //4
        bots.add(crearBotEnemigo( 2300, 0, PersonajeEnemigo.TIPO_AGACHADO, PersonajeJugador.ORIENTATION_LEFT)); //5
        bots.add(crearBotEnemigo( 3025, 0, PersonajeEnemigo.TIPO_AGACHADO, PersonajeJugador.ORIENTATION_LEFT)); //6
        bots.add(crearBotEnemigo( 4263, 0, PersonajeEnemigo.TIPO_AGACHADO, PersonajeJugador.ORIENTATION_LEFT)); //7

        bots.add(crearBotEnemigo( 860, 128, PersonajeEnemigo.TIPO_CIMA, PersonajeJugador.ORIENTATION_LEFT)); //8

        bots.add(crearBotEnemigo( 2016, 128, PersonajeEnemigo.TIPO_CIMA, PersonajeJugador.ORIENTATION_LEFT)); //9
        bots.add(crearBotEnemigo( 1848, 96, PersonajeEnemigo.TIPO_CIMA, PersonajeJugador.ORIENTATION_LEFT)); //10

        bots.add(crearBotEnemigo( 3040, 128, PersonajeEnemigo.TIPO_CIMA, PersonajeJugador.ORIENTATION_LEFT)); //11
        bots.add(crearBotEnemigo( 3080, 96, PersonajeEnemigo.TIPO_CIMA, PersonajeJugador.ORIENTATION_LEFT)); //12

        bots.add(crearBotEnemigo( 4265, 46, PersonajeEnemigo.TIPO_CIMA, PersonajeJugador.ORIENTATION_LEFT)); //13
        bots.add(crearBotEnemigo( 4261, 86, PersonajeEnemigo.TIPO_CIMA, PersonajeJugador.ORIENTATION_LEFT)); //14
*/
        for (Personaje p : bots) {
            escenario.getLayerPlayer().attachChild(p);
        }
    }

    public PersonajeEnemigo crearBotEnemigo(float x, float y, int tipo, int orientacion){
        PersonajeEnemigo enemigo = new PersonajeEnemigo(tipo, escenario, x, y, pTextureRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.init();
        return enemigo;
    }

    public PersonajeRNBot crearRNBotEnemigo(float x, float y, int orientacion){
        PersonajeRNBot enemigo = new PersonajeRNBot(escenario, x, y, pTextureRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajeJugador);
        enemigo.init();
        return enemigo;
    }

    public void init(){
        for (Personaje p : bots) {
            p.init();
        }
    }

    public boolean validaBotMuertos(){
        int[] ibots = new int[]{14, 13, 7, 0, 1, 2};
        for(int i : ibots){
            if(!bots.get(i).isDead()){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Personaje> getBots() {
        return bots;
    }
}
