package com.proyectosfisi.game.contrawithbots;

import com.proyectosfisi.game.andengine.resolutionpolicy.FillCropResolutionPolicy;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

import java.io.IOException;

/**
 * Created by edson on 01/05/2015.
 */
public class Escenario {

    public static final int CAMARA_ANCHO = 480;//1024;
    public static final int CAMARA_ALTO = 256;
    public static final float MANDO_PADDING = 6;
    public static final float ESCENARIO_PAGGING_RIGHT = 58;

    protected FillCropResolutionPolicy fillCropResolutionPolicy;

    protected Sprite[] parallaxLayerBackSprites;
    protected Entity layerPlayer;
    protected Entity layerBullets;
    protected HUD hud;
    
    protected PersonajeJugador jugador;
    protected Intro intro;
    protected Controles controles;
    protected BotFactory botFactory;
    protected boolean pausa;

    protected Base base;

    protected Font fontTitulo;
    protected Font fontParrafo;
    protected Font fontNivel;
    protected Font fontScoreNivel;
    protected Font fontVida;
    protected Font fontIconoVida;

    protected Sound sDisparo;
    protected Sound sExplosion;
    protected Sound sLive;
    protected Music mMusic;

    protected TiledTextureRegion mBulletTextureRegion; // Textura de la bala
    protected TiledTextureRegion mExplosionTextureRegion; // Textura de la explosion

    protected ITexture mIntroBaseTexture;
    protected ITextureRegion mIntroBaseTextureRegion;

    public static final float PISO_ALTO = 84;
    public static final float ESCALONES_ALTO[] = new float[]{/*1*/ 56, 128, 96,/*2*/ 56, 96, 56, 128, 96, 56,/*3*/ 96, 56, 128, 96, 56,/*4*/ 46, 86};
    public static final float ESCALONES_DISTANCIA_X_MIN[] = new float[]{/*1*/ 785, 817, 873 ,/*2*/ 1809, 1841, 1905, 1937, 1993, 2097,/*3*/ 2865, 2929, 2961, 3017, 3121,/*4*/ 4231, 4231};
    public static final float ESCALONES_DISTANCIA_X_MAX[] = new float[]{/*1*/ 832, 904, 1000,/*2*/ 1856, 1928, 1952, 2096, 2120, 2144,/*3*/ 2952, 2976, 3120, 3144, 3168,/*4*/ 4299, 4291};

    public void onCreateEngineOptions(){
        fillCropResolutionPolicy = new FillCropResolutionPolicy(CAMARA_ANCHO, CAMARA_ALTO);
        pausa = true;
    }

    public void onCreateEscene(Scene scene, ITextureRegion[] mParallaxLayerBackTextureRegions,VertexBufferObjectManager vertexBufferObjectManager){

        final ParallaxBackground parallaxBackground = new ParallaxBackground(0, 0, 0);
        scene.setBackground(parallaxBackground);

        parallaxLayerBackSprites = new Sprite[mParallaxLayerBackTextureRegions.length];
        for(int i = 0; i < mParallaxLayerBackTextureRegions.length; i++){
            parallaxLayerBackSprites[i] = new Sprite(fillCropResolutionPolicy.getLeft() + i * mParallaxLayerBackTextureRegions[i].getWidth(), 0, mParallaxLayerBackTextureRegions[i], vertexBufferObjectManager);
            parallaxLayerBackSprites[i].setOffsetCenter(0, 0);
            parallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, parallaxLayerBackSprites[i]));
        }

        // Fondo

        base = new Base(this, mIntroBaseTextureRegion, vertexBufferObjectManager);

        layerPlayer = new Entity();
        layerPlayer.setZIndex(7);

        layerBullets = new Entity();
        layerBullets.setZIndex(15);

        scene.attachChild(layerPlayer);
        scene.attachChild(layerBullets);

        layerPlayer.attachChild(base);

    }

    public int tocoPisoOEscalon(Actor actor){
        if(actor.getVelocityY() <= 0){
            if(actor.getRelativeY() >= 0 && actor.getRelativeY() + actor.getVelocityY() <= 0){
                return 0; // toco el piso
            }else{
                for (int i = 0; i < ESCALONES_ALTO.length; i++){
                    float x_min = ESCALONES_DISTANCIA_X_MIN[i];
                    float x_max = ESCALONES_DISTANCIA_X_MAX[i];
                    if(x_min <= actor.getRelativeX() && actor.getRelativeX() <= x_max){
                        float alto = ESCALONES_ALTO[i];
                        if(actor.getRelativeY() >= alto && alto >= actor.getRelativeY() + actor.getVelocityY()){
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

    public void onCreateResources(BaseGameActivity baseGameActivity, Engine mEngine) throws IOException{

        final ITexture fontTextureTitulo = new BitmapTextureAtlas(baseGameActivity.getTextureManager(), 256, 256, TextureOptions.NEAREST);
        fontTitulo = FontFactory.createFromAsset(baseGameActivity.getFontManager(), fontTextureTitulo, baseGameActivity.getAssets(), "font/ufonts.com_showcard-gothic.ttf", 32, true, new Color(0.963522f, 0.271782f, 0.079002f).getARGBPackedInt());
        fontTitulo.load();

        final ITexture fontTextureParrafo = new BitmapTextureAtlas(baseGameActivity.getTextureManager(), 256, 256, TextureOptions.NEAREST);
        fontParrafo = FontFactory.createFromAsset(baseGameActivity.getFontManager(), fontTextureParrafo, baseGameActivity.getAssets(), "font/atari_full.ttf", 10, true, new Color(1.0f, 1.0f, 1.0f).getARGBPackedInt());
        fontParrafo.load();

        final ITexture fontTextureNivel = new BitmapTextureAtlas(baseGameActivity.getTextureManager(), 256, 256, TextureOptions.NEAREST);
        fontNivel = FontFactory.createFromAsset(baseGameActivity.getFontManager(), fontTextureNivel, baseGameActivity.getAssets(), "font/ufonts.com_showcard-gothic.ttf", 12, true, new Color(1.0f, 1.0f, 1.0f).getARGBPackedInt());
        fontNivel.load();

        final ITexture fontTextureScoreNivel = new BitmapTextureAtlas(baseGameActivity.getTextureManager(), 256, 256, TextureOptions.NEAREST);
        fontScoreNivel = FontFactory.createFromAsset(baseGameActivity.getFontManager(), fontTextureScoreNivel, baseGameActivity.getAssets(), "font/atari_full.ttf", 6, true, new Color(0.0f, 0.0f, 0.0f).getARGBPackedInt());
        fontScoreNivel.load();

        final ITexture fontTextureVida = new BitmapTextureAtlas(baseGameActivity.getTextureManager(), 256, 256, TextureOptions.NEAREST);
        fontVida = FontFactory.createFromAsset(baseGameActivity.getFontManager(), fontTextureVida, baseGameActivity.getAssets(), "font/atari_full.ttf", 6, true, new Color(1.0f, 1.0f, 1.0f).getARGBPackedInt());
        fontVida.load();

        final ITexture fontTextureIconoVida = new BitmapTextureAtlas(baseGameActivity.getTextureManager(), 256, 256, TextureOptions.NEAREST);
        fontIconoVida = FontFactory.createFromAsset(baseGameActivity.getFontManager(), fontTextureIconoVida, baseGameActivity.getAssets(), "font/atari_full.ttf", 8, true, new Color(1.0f, 1.0f, 1.0f).getARGBPackedInt());
        fontIconoVida.load();

        //Base
        this.mIntroBaseTexture = new AssetBitmapTexture(baseGameActivity.getTextureManager(), baseGameActivity.getAssets(), "gfx/sprite-base.png");
        this.mIntroBaseTextureRegion = TextureRegionFactory.extractFromTexture(this.mIntroBaseTexture);
        this.mIntroBaseTexture.load();

        sDisparo = SoundFactory.createSoundFromAsset(baseGameActivity.getSoundManager(), baseGameActivity.getApplicationContext(), "sounds/disparo2.ogg");
        sExplosion = SoundFactory.createSoundFromAsset(baseGameActivity.getSoundManager(), baseGameActivity.getApplicationContext(), "sounds/explosion2.wav");
        sLive = SoundFactory.createSoundFromAsset(baseGameActivity.getSoundManager(), baseGameActivity.getApplicationContext(), "sounds/live-down.wav");
        mMusic = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), baseGameActivity, "sounds/korn-somebody_someone.ogg");
        mMusic.setLooping(true);

    }

    public FillCropResolutionPolicy getFillCropResolutionPolicy() {
        return fillCropResolutionPolicy;
    }

    public Entity getLayerPlayer() {
        return layerPlayer;
    }

    public Entity getLayerBullets() {
        return layerBullets;
    }

    public float getParallaxX(){
        return parallaxLayerBackSprites[0].getX();
    }

    public float getParallaxY(){
        return parallaxLayerBackSprites[0].getY();
    }

    public float getParallaxWidth(){
        return parallaxLayerBackSprites[0].getWidth()*parallaxLayerBackSprites.length;
    }

    public void setParallaxX(float x){
        for(int i = 0; i < parallaxLayerBackSprites.length; i++){
            parallaxLayerBackSprites[i].setX(x + parallaxLayerBackSprites[0].getWidth() * i);
        }
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

    public Sound getsLive() {
        return sLive;
    }

    public void setsLive(Sound sLive) {
        this.sLive = sLive;
    }

    public Font getFontVida() {
        return fontVida;
    }

    public void setFontVida(Font fontVida) {
        this.fontVida = fontVida;
    }

    public Font getFontTitulo() {
        return fontTitulo;
    }

    public void setFontTitulo(Font fontTitulo) {
        this.fontTitulo = fontTitulo;
    }

    public Font getFontParrafo() {
        return fontParrafo;
    }

    public void setFontParrafo(Font fontParrafo) {
        this.fontParrafo = fontParrafo;
    }

    public Font getFontNivel() {
        return fontNivel;
    }

    public void setFontNivel(Font fontNivel) {
        this.fontNivel = fontNivel;
    }

    public Font getFontScoreNivel() {
        return fontScoreNivel;
    }

    public void setFontScoreNivel(Font fontScoreNivel) {
        this.fontScoreNivel = fontScoreNivel;
    }

    public Font getFontIconoVida() {
        return fontIconoVida;
    }

    public void setFontIconoVida(Font fontIconoVida) {
        this.fontIconoVida = fontIconoVida;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }
}
