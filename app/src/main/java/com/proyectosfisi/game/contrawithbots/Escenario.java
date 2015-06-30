package com.proyectosfisi.game.contrawithbots;

import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.resolutionpolicy.CropResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 01/05/2015.
 */
public class Escenario {

    public static final int CAMARA_ANCHO = 1024;
    public static final int CAMARA_ALTO = 256;
    public static final float MANDO_PADDING = 6;
    public static final float ESCENARIO_PAGGING_RIGHT = 58;

    protected CropResolutionPolicy cropResolutionPolicy;

    protected AutoParallaxBackground autoParallaxBackground;
    protected Sprite parallaxLayerBackSprite;
    protected Entity layerPlayer;
    protected Entity layerBullets;
    protected HUD hud;
    
    protected PersonajeJugador jugador;
    protected Intro intro;
    protected Controles controles;
    protected BotFactory botFactory;
    protected boolean pausa;

    protected Sound sDisparo;
    protected Sound sExplosion;
    protected Music mMusic;

    protected TiledTextureRegion mBulletTextureRegion; // Textura de la bala
    protected TiledTextureRegion mExplosionTextureRegion; // Textura de la explosion

    public static final float PISO_ALTO = 84;
    public static final float ESCALONES_ALTO[] = new float[]{/*1*/ 56, 128, 96,/*2*/ 56, 96, 56, 128, 96, 56,/*3*/ 96, 56, 128, 96, 56,/*4*/ 46, 86};
    public static final float ESCALONES_DISTANCIA_X_MIN[] = new float[]{/*1*/ 785, 817, 873 ,/*2*/ 1809, 1841, 1905, 1937, 1993, 2097,/*3*/ 2865, 2929, 2961, 3017, 3121,/*4*/ 4231, 4231};
    public static final float ESCALONES_DISTANCIA_X_MAX[] = new float[]{/*1*/ 832, 904, 1000,/*2*/ 1856, 1928, 1952, 2096, 2120, 2144,/*3*/ 2952, 2976, 3120, 3144, 3168,/*4*/ 4299, 4291};

    public void onCreateEngineOptions(){
        cropResolutionPolicy = new CropResolutionPolicy(CAMARA_ANCHO, CAMARA_ALTO);
        pausa = true;
    }

    public void onCreateEscene(Scene scene, ITextureRegion mParallaxLayerBackTextureRegion,VertexBufferObjectManager vertexBufferObjectManager){

        autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
        scene.setBackground(autoParallaxBackground);

        // Fondo
        parallaxLayerBackSprite = new Sprite(cropResolutionPolicy.getLeft(), cropResolutionPolicy.getBottom(), mParallaxLayerBackTextureRegion, vertexBufferObjectManager);
        parallaxLayerBackSprite.setOffsetCenter(0, 0);
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(0.0f, parallaxLayerBackSprite));

        layerPlayer = new Entity();
        layerPlayer.setZIndex(7);

        layerBullets = new Entity();
        layerBullets.setZIndex(5);

        scene.attachChild(layerBullets);
        scene.attachChild(layerPlayer);

    }

    public int tocoPisoOEscalon(Personaje personaje){
        if(personaje.getVelocityY() <= 0){
            if(personaje.getRelativeY() >= 0 && personaje.getRelativeY() + personaje.getVelocityY() <= 0){
                return 0; // toco el piso
            }else{
                for (int i = 0; i < ESCALONES_ALTO.length; i++){
                    float x_min = ESCALONES_DISTANCIA_X_MIN[i];
                    float x_max = ESCALONES_DISTANCIA_X_MAX[i];
                    if(x_min <= personaje.getRelativeX() && personaje.getRelativeX() <= x_max){
                        float alto = ESCALONES_ALTO[i];
                        if(personaje.getRelativeY() >= alto && alto >= personaje.getRelativeY() + personaje.getVelocityY()){
                            return i+1; //toco un escalon
                        }
                    }
                }
                return -2; // esta cayendo aun sin tocar un piso o escalon
            }
        }else{
            return -1; // esta subiendo aun
        }
    }

    public CropResolutionPolicy getCropResolutionPolicy() {
        return cropResolutionPolicy;
    }

    public Entity getLayerPlayer() {
        return layerPlayer;
    }

    public Entity getLayerBullets() {
        return layerBullets;
    }

    public Sprite getParallaxLayerBackSprite() {
        return parallaxLayerBackSprite;
    }

    public PersonajeJugador getJugador() {
        return jugador;
    }

    public void setJugador(PersonajeJugador jugador) {
        this.jugador = jugador;
    }

    public BotFactory getBotFactory() {
        return botFactory;
    }

    public void setBotFactory(BotFactory botFactory) {
        this.botFactory = botFactory;
    }

    public boolean isPausa() {
        return pausa;
    }

    public synchronized void setPausa(boolean pausa) {
        this.pausa = pausa;
    }

    public HUD getHud() {
        return hud;
    }

    public void setHud(HUD hud) {
        this.hud = hud;
    }

    public Intro getIntro() {
        return intro;
    }

    public void setIntro(Intro intro) {
        this.intro = intro;
    }

    public Controles getControles() {
        return controles;
    }

    public void setControles(Controles controles) {
        this.controles = controles;
    }

    public Sound getsDisparo() {
        return sDisparo;
    }

    public void setsDisparo(Sound sDisparo) {
        this.sDisparo = sDisparo;
    }

    public Music getmMusic() {
        return mMusic;
    }

    public void setmMusic(Music mMusic) {
        this.mMusic = mMusic;
    }

    public TiledTextureRegion getmBulletTextureRegion() {
        return mBulletTextureRegion;
    }

    public void setmBulletTextureRegion(TiledTextureRegion mBulletTextureRegion) {
        this.mBulletTextureRegion = mBulletTextureRegion;
    }

    public TiledTextureRegion getmExplosionTextureRegion() {
        return mExplosionTextureRegion;
    }

    public void setmExplosionTextureRegion(TiledTextureRegion mExplosionTextureRegion) {
        this.mExplosionTextureRegion = mExplosionTextureRegion;
    }

    public Sound getsExplosion() {
        return sExplosion;
    }

    public void setsExplosion(Sound sExplosion) {
        this.sExplosion = sExplosion;
    }
}
