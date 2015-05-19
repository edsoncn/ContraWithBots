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
    private ArrayList<Personaje> personajePrincipal;

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
        personajePrincipal = new ArrayList<>();
    }

    public void initBots() {

        personajePrincipal.add(escenario.getJugador());

        float right = escenario.getCropResolutionPolicy().getRight();
        float left = escenario.getCropResolutionPolicy().getLeft();
        float backLayer = escenario.getParallaxLayerBackSprite().getWidth();

        bots.add(crearBotEnemigo(-1 * (right - left) / 2, 0, PersonajeEnemigo.TIPO_NORMAL, Personaje.ORIENTATION_RIGHT));
        bots.add(crearBotEnemigo( 3 * (right - left) / 2, 0, PersonajeEnemigo.TIPO_NORMAL, Personaje.ORIENTATION_LEFT));
        bots.add(crearBotEnemigo( backLayer / 4, 0, PersonajeEnemigo.TIPO_NORMAL, Personaje.ORIENTATION_LEFT));

        bots.add(crearBotEnemigo( 715, 0, PersonajeEnemigo.TIPO_AGACHADO, Personaje.ORIENTATION_LEFT));
        bots.add(crearBotEnemigo( 1645, 0, PersonajeEnemigo.TIPO_AGACHADO, Personaje.ORIENTATION_LEFT));
        bots.add(crearBotEnemigo( 2300, 0, PersonajeEnemigo.TIPO_AGACHADO, Personaje.ORIENTATION_LEFT));
        bots.add(crearBotEnemigo( 3025, 0, PersonajeEnemigo.TIPO_AGACHADO, Personaje.ORIENTATION_LEFT));
        bots.add(crearBotEnemigo( 4263, 0, PersonajeEnemigo.TIPO_AGACHADO, Personaje.ORIENTATION_LEFT));

        bots.add(crearBotEnemigo( 860, 128, PersonajeEnemigo.TIPO_CIMA, Personaje.ORIENTATION_LEFT));

        bots.add(crearBotEnemigo( 2016, 128, PersonajeEnemigo.TIPO_CIMA, Personaje.ORIENTATION_LEFT));
        bots.add(crearBotEnemigo( 1848, 96, PersonajeEnemigo.TIPO_CIMA, Personaje.ORIENTATION_LEFT));

        bots.add(crearBotEnemigo( 3040, 128, PersonajeEnemigo.TIPO_CIMA, Personaje.ORIENTATION_LEFT));
        bots.add(crearBotEnemigo( 3080, 96, PersonajeEnemigo.TIPO_CIMA, Personaje.ORIENTATION_LEFT));

        bots.add(crearBotEnemigo( 4265, 46, PersonajeEnemigo.TIPO_CIMA, Personaje.ORIENTATION_LEFT));
        bots.add(crearBotEnemigo( 4261, 86, PersonajeEnemigo.TIPO_CIMA, Personaje.ORIENTATION_LEFT));

        for (Personaje p : bots) {
            escenario.getLayerPlayer().attachChild(p);
        }
    }

    public PersonajeEnemigo crearBotEnemigo(float x, float y, int tipo, int orientacion){
        PersonajeEnemigo enemigo = new PersonajeEnemigo(tipo, escenario, x, y, pTextureRegion, mBulletTexture, pVertexBufferObjectManager);
        enemigo.setMoveLayerBackSprite(false);
        enemigo.setOrientation(orientacion);
        enemigo.setEnemigos(personajePrincipal);
        enemigo.init();
        return enemigo;
    }

    public ArrayList<Personaje> getBots() {
        return bots;
    }
}
