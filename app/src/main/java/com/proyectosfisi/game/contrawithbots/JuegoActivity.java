package com.proyectosfisi.game.contrawithbots;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;

/**
 * Created by edson on 30/03/2015.
 */
public class JuegoActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener {

    protected Camera camera;
    protected Escenario escenario;

    protected ITexture mParallaxLayerBackTexture;
    protected ITextureRegion mParallaxLayerBackTextureRegion;

    //Personajes y Bala
    protected ITexture mPlayerTexture;
    protected TiledTextureRegion mPlayerTextureRegion;
    protected ITexture mEnemyTexture;
    protected TiledTextureRegion mEnemyTextureRegion;
    protected ITexture mBulletTexture;
    protected TiledTextureRegion mBulletTextureRegion;

    //Mandos
    protected ITexture mMandoDireccionalTexture;
    protected ITextureRegion mMandoDireccionalTextureRegion;
    protected ITexture mMandoAccionesTexture;
    protected ITextureRegion mMandoAccionesTextureRegion;
    protected ITexture mMandoPausaTexture;
    protected ITextureRegion mMandoPausaTextureRegion;
    protected ITexture mBotonContinuarTexture;
    protected ITextureRegion mBotonContinuarTextureRegion;
    protected ITexture mBotonMenuTexture;
    protected ITextureRegion mBotonMenuTextureRegion;

    //Intro
    protected ITexture mIntroTituloTexture;
    protected ITextureRegion mIntroTituloTextureRegion;
    protected ITexture mIntroNivel1Texture;
    protected ITextureRegion mIntroNivel1TextureRegion;
    protected ITexture mIntroNivel2Texture;
    protected ITextureRegion mIntroNivel2TextureRegion;
    protected ITexture mIntroNivel3Texture;
    protected ITextureRegion mIntroNivel3TextureRegion;

    protected PersonajeJugador jugador;
    protected Intro intro;
    protected Controles controles;

    protected HUD hud;

    public static final float SCALE_REF_HEIGHT = 2.75f; //2.54936f;
    protected float scale;

    @Override
    protected void onCreate(final Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {

        camera = new Camera(0, 0, Escenario.CAMARA_ANCHO, Escenario.CAMARA_ALTO);
        escenario = new Escenario();
        escenario.onCreateEngineOptions();

        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, escenario.getCropResolutionPolicy(), camera);

        engineOptions.getTouchOptions().setNeedsMultiTouch(true);

        calcularEscalaDeControles();

        return engineOptions;
    }

    @Override
    public Scene onCreateScene() {

        float left = escenario.getCropResolutionPolicy().getLeft();
        float right = escenario.getCropResolutionPolicy().getRight();

        this.mEngine.registerUpdateHandler(new FPSLogger());

        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();

        final Scene scene = new Scene();

        //Creamos background y las capas
        escenario.onCreateEscene(scene, mParallaxLayerBackTextureRegion, vertexBufferObjectManager);

        BotFactory botFactory = new BotFactory(escenario, this.mEnemyTextureRegion, this.mBulletTextureRegion, vertexBufferObjectManager);
        escenario.setBotFactory(botFactory);

        // Principal
        jugador = new PersonajeJugador(escenario, (right - left)/2, 0, this.mPlayerTextureRegion, this.mBulletTextureRegion, vertexBufferObjectManager);
        jugador.setMoveLayerBackSprite(true);

        Log.i("Contra", "Jugador: " + jugador.hashCode());

        escenario.setJugador(jugador);

        botFactory.initBots();

        jugador.setEnemigos(botFactory.getBots());
        escenario.getLayerPlayer().attachChild(jugador);

        hud = new HUD();
        escenario.setHud(hud);
        // Touch Area
        intro = new Intro(escenario, mIntroTituloTextureRegion, mIntroNivel1TextureRegion, mIntroNivel2TextureRegion, mIntroNivel3TextureRegion, getVertexBufferObjectManager());
        scene.attachChild(intro);
        escenario.setIntro(intro);
        // Intro
        controles = new Controles(escenario, scale, mMandoDireccionalTextureRegion, mMandoAccionesTextureRegion, mMandoPausaTextureRegion, mBotonContinuarTextureRegion, mBotonMenuTextureRegion, getVertexBufferObjectManager());
        controles.ocultarControles();
        scene.attachChild(controles);
        escenario.setControles(controles);
        camera.setHUD(hud);
        hud.setZIndex(9);
        hud.setOnSceneTouchListener(this);

        return scene;
    }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        final int pointerID = pSceneTouchEvent.getPointerID();
        if(pointerID == jugador.getActivePointerID()){
            jugador.resetActionsAndStates();
            jugador.initSelectBoton();
        }
        return true;
    }

    private void calcularEscalaDeControles() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);


        // since SDK_INT = 1;
        int mWidthPixels = displayMetrics.widthPixels;
        int mHeightPixels = displayMetrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {
            }
        }

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                mWidthPixels = realSize.x;
                mHeightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        }

        double x = mWidthPixels/displayMetrics.xdpi;
        double y = mHeightPixels/displayMetrics.ydpi;
        double x2 = Math.pow(x,2);
        double y2 = Math.pow(y,2);
        double screenInches = Math.sqrt(x2+y2);
        if(y > SCALE_REF_HEIGHT) {
            scale = 0.75f;
        }
        Log.i("debug", "Screen inches : " + screenInches + " w:"+x+" h:"+y+" si:"+screenInches);
    }

    @Override
    public void onCreateResources() throws IOException {

        // Fondo
        this.mParallaxLayerBackTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/contrafondo.png");
        this.mParallaxLayerBackTextureRegion = TextureRegionFactory.extractFromTexture(this.mParallaxLayerBackTexture);
        this.mParallaxLayerBackTexture.load();

        // Mando
        this.mMandoDireccionalTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/mando_direccional.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mMandoDireccionalTextureRegion = TextureRegionFactory.extractFromTexture(this.mMandoDireccionalTexture);
        this.mMandoDireccionalTexture.load();

        this.mMandoAccionesTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/mando_acciones.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mMandoAccionesTextureRegion = TextureRegionFactory.extractFromTexture(this.mMandoAccionesTexture);
        this.mMandoAccionesTexture.load();

        this.mMandoPausaTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/mando_pausa.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mMandoPausaTextureRegion = TextureRegionFactory.extractFromTexture(this.mMandoPausaTexture);
        this.mMandoPausaTexture.load();

        this.mBotonContinuarTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/mando_boton_continuar.png");
        this.mBotonContinuarTextureRegion = TextureRegionFactory.extractFromTexture(this.mBotonContinuarTexture);
        this.mBotonContinuarTexture.load();

        this.mBotonMenuTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/mando_boton_menu.png");
        this.mBotonMenuTextureRegion = TextureRegionFactory.extractFromTexture(this.mBotonMenuTexture);
        this.mBotonMenuTexture.load();

        // Personaje principal
        this.mPlayerTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/sprite-personaje-principal.png");
        this.mPlayerTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mPlayerTexture, 16, 5);
        this.mPlayerTexture.load();

        // Personaje principal
        this.mEnemyTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/sprite-personaje-enemigo.png");
        this.mEnemyTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mEnemyTexture, 16, 5);
        this.mEnemyTexture.load();

        // Bullet
        this.mBulletTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/sprite-bala.png");
        this.mBulletTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mBulletTexture, 3, 1);
        this.mBulletTexture.load();

        //IntroTitulo
        this.mIntroTituloTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/intro-titulo.png");
        this.mIntroTituloTextureRegion = TextureRegionFactory.extractFromTexture(this.mIntroTituloTexture);
        this.mIntroTituloTexture.load();

        //IntroNivel1
        this.mIntroNivel1Texture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/intro-nivel1.png");
        this.mIntroNivel1TextureRegion = TextureRegionFactory.extractFromTexture(this.mIntroNivel1Texture);
        this.mIntroNivel1Texture.load();

        //IntroNivel2
        this.mIntroNivel2Texture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/intro-nivel2.png");
        this.mIntroNivel2TextureRegion = TextureRegionFactory.extractFromTexture(this.mIntroNivel2Texture);
        this.mIntroNivel2Texture.load();

        //IntroNivel3
        this.mIntroNivel3Texture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/intro-nivel3.png");
        this.mIntroNivel3TextureRegion = TextureRegionFactory.extractFromTexture(this.mIntroNivel3Texture);
        this.mIntroNivel3Texture.load();

    }

    @Override
    public void onBackPressed() {
        if(intro.getEstado() == Intro.ESTADO_Q2) {
            if(!escenario.isPausa()) {
                controles.pausa();
            }else{
                controles.reanudar();
            }
        } else {
            super.onBackPressed();
        }
    }
}
