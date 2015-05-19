package com.proyectosfisi.game.contrawithbots;

import android.graphics.Color;
import android.widget.Toast;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
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

    //Intro
    protected ITexture mIntroTituloTexture;
    protected ITextureRegion mIntroTituloTextureRegion;
    protected ITexture mIntroNivel1Texture;
    protected ITextureRegion mIntroNivel1TextureRegion;
    protected ITexture mIntroNivel2Texture;
    protected ITextureRegion mIntroNivel2TextureRegion;
    protected ITexture mIntroNivel3Texture;
    protected ITextureRegion mIntroNivel3TextureRegion;
    protected Rectangle rIntroFondo;

    protected Sprite spriteMandoDireccional;
    protected Sprite spriteMandoAcciones;
    protected Rectangle rDireccional;

    protected Personaje jugador;
    protected Personaje enemigo;

    protected HUD hud;

    @Override
    public EngineOptions onCreateEngineOptions() {

        camera = new Camera(0, 0, Escenario.CAMARA_ANCHO, Escenario.CAMARA_ALTO);
        escenario = new Escenario();
        escenario.onCreateEngineOptions();

        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, escenario.getCropResolutionPolicy(), camera);

        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        if(MultiTouch.isSupported(this)) {
            if(MultiTouch.isSupportedDistinct(this)) {
                Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
        }

        return engineOptions;
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
    public Scene onCreateScene() {

        float left = escenario.getCropResolutionPolicy().getLeft();
        float bottom = escenario.getCropResolutionPolicy().getBottom();
        float right = escenario.getCropResolutionPolicy().getRight();

        this.mEngine.registerUpdateHandler(new FPSLogger());

        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();

        final Scene scene = new Scene();

        //Creamos background y las capas
        escenario.onCreateEscene(scene, mParallaxLayerBackTextureRegion, vertexBufferObjectManager);

        BalaFactory.getInstance().inactivar();

        BotFactory botFactory = new BotFactory(escenario, this.mEnemyTextureRegion, this.mBulletTextureRegion, vertexBufferObjectManager);
        escenario.setBotFactory(botFactory);

        // Principal
        jugador = new Personaje(escenario, (right - left)/2, 0, this.mPlayerTextureRegion, this.mBulletTextureRegion, vertexBufferObjectManager);
        jugador.setMoveLayerBackSprite(true);
        escenario.setJugador(jugador);

        botFactory.initBots();

        jugador.setEnemigos(botFactory.getBots());

        escenario.getLayerPlayer().attachChild(jugador);

        hud = new HUD();
        // Touch Area
        initControles(scene);
        // Intro
        initIntro(scene);
        camera.setHUD(hud);
        hud.setZIndex(9);
        hud.setOnSceneTouchListener(this);

        return scene;
    }

    protected void initIntro(Scene scene){

        float left = escenario.getCropResolutionPolicy().getLeft();
        float bottom = escenario.getCropResolutionPolicy().getBottom();
        float top = escenario.getCropResolutionPolicy().getTop();
        float right = escenario.getCropResolutionPolicy().getRight();
        float alto = top - bottom;
        final float ancho = right - left;
        float separaAlto = (alto - mIntroTituloTextureRegion.getHeight() - mIntroNivel1TextureRegion.getHeight())/3;
        float separaAncho = (ancho - 3*mIntroNivel1TextureRegion.getWidth())/4;

        this.rIntroFondo = new Rectangle(left, bottom, ancho, alto, getVertexBufferObjectManager());
        this.rIntroFondo.setOffsetCenter(0, 0);
        this.rIntroFondo.setColor(Color.BLACK);
        this.rIntroFondo.setAlpha(0.4f);
        scene.attachChild(rIntroFondo);

        //Sprites
        final Sprite spriteIntroTitulo = new Sprite(
                left + (ancho - mIntroTituloTextureRegion.getWidth())/2,
                top - mIntroTituloTextureRegion.getHeight() - separaAlto,
                mIntroTituloTextureRegion, this.getVertexBufferObjectManager());
        spriteIntroTitulo.setOffsetCenter(0, 0);
        scene.attachChild(spriteIntroTitulo);

        //Nivel1
        final Sprite spriteIntroNivel1 = new Sprite(
                left + separaAncho,
                top - mIntroTituloTextureRegion.getHeight() - 2*separaAlto - mIntroNivel1TextureRegion.getHeight(),
                mIntroNivel1TextureRegion, this.getVertexBufferObjectManager());
        spriteIntroNivel1.setOffsetCenter(0, 0);
        scene.attachChild(spriteIntroNivel1);

        //Nivel2
        final Sprite spriteIntroNivel2 = new Sprite(
                left + 2*separaAncho + mIntroNivel1TextureRegion.getWidth(),
                top - mIntroTituloTextureRegion.getHeight() - 2*separaAlto - mIntroNivel2TextureRegion.getHeight(),
                mIntroNivel2TextureRegion, this.getVertexBufferObjectManager());
        spriteIntroNivel2.setOffsetCenter(0, 0);
        scene.attachChild(spriteIntroNivel2);

        //Nivel3
        final Sprite spriteIntroNivel3 = new Sprite(
                left + 3*separaAncho + mIntroNivel1TextureRegion.getWidth() + mIntroNivel2TextureRegion.getWidth(),
                top - mIntroTituloTextureRegion.getHeight() - 2*separaAlto - mIntroNivel3TextureRegion.getHeight(),
                mIntroNivel3TextureRegion, this.getVertexBufferObjectManager());
        spriteIntroNivel3.setOffsetCenter(0, 0);
        scene.attachChild(spriteIntroNivel3);

        // Control Direccional
        final Rectangle rNivel1 = new Rectangle(spriteIntroNivel1.getX()+spriteIntroNivel1.getWidth()/2, spriteIntroNivel1.getY()+ spriteIntroNivel1.getHeight()/2,
                spriteIntroNivel1.getWidth(), spriteIntroNivel1.getHeight(), getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(escenario.isPausa()) {
                    if (touchEvent.isActionDown()) {
                        rIntroFondo.setAlpha(0.0f);
                        spriteIntroTitulo.setAlpha(0.0f);
                        spriteIntroNivel1.setAlpha(0.0f);
                        spriteIntroNivel2.setAlpha(0.0f);
                        spriteIntroNivel3.setAlpha(0.0f);
                        spriteMandoDireccional.setX(spriteMandoDireccional.getX() - ancho);
                        spriteMandoAcciones.setX(spriteMandoAcciones.getX() - ancho);
                        rDireccional.setX(rDireccional.getX() - ancho);
                        hud.setAlpha(1.0f);
                        escenario.setPausa(false);
                        this.detachSelf();
                        this.dispose();
                    }
                }
                return true;
            };
        };
        rNivel1.setAlpha(0.0f);

        hud.registerTouchArea(rNivel1);
        hud.attachChild(rNivel1);
    }

    protected void initControles(Scene scene){

        float left = escenario.getCropResolutionPolicy().getLeft();
        float bottom = escenario.getCropResolutionPolicy().getBottom();
        float right = escenario.getCropResolutionPolicy().getRight();
        float ancho = right - left;

        //Sprites
        spriteMandoDireccional = new Sprite(left + mMandoDireccionalTextureRegion.getWidth()/2 + Escenario.MANDO_PADDING
                , bottom + mMandoDireccionalTextureRegion.getHeight()/2 + Escenario.MANDO_PADDING, mMandoDireccionalTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(spriteMandoDireccional);

        spriteMandoAcciones = new Sprite(right - mMandoAccionesTextureRegion.getWidth()/2 - Escenario.MANDO_PADDING
                , bottom + mMandoAccionesTextureRegion.getHeight()/2 + Escenario.MANDO_PADDING, mMandoAccionesTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(spriteMandoAcciones);

        // Control Direccional
        rDireccional = new Rectangle(spriteMandoDireccional.getX(), spriteMandoDireccional.getY(),
                spriteMandoDireccional.getWidth(), spriteMandoDireccional.getHeight(), getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(!escenario.isPausa()) {
                    jugador.setAction(touchEvent, X, Y, spriteMandoDireccional.getWidth(), spriteMandoDireccional.getHeight());
                }
                return true;
            };
        };
        rDireccional.setAlpha(0.0f);

        // Control Acciones
        final Rectangle rY = new Rectangle(spriteMandoAcciones.getX() - spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getY(),
                spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(!escenario.isPausa()) {
                    if (touchEvent.isActionDown()) {
                        jugador.shoot();
                    }
                }
                return true;
            };
        };
        rY.setAlpha(0.0f);
        final Rectangle rA = new Rectangle(spriteMandoAcciones.getX() + spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getY(),
                spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3, getVertexBufferObjectManager());
        rA.setAlpha(0.0f);
        final Rectangle rX = new Rectangle(spriteMandoAcciones.getX(), spriteMandoAcciones.getY() + spriteMandoAcciones.getHeight()/3 - Escenario.MANDO_PADDING/2,
                spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3 + Escenario.MANDO_PADDING, getVertexBufferObjectManager());
        rX.setAlpha(0.0f);
        final Rectangle rB = new Rectangle(spriteMandoAcciones.getX(), spriteMandoAcciones.getY() - spriteMandoAcciones.getHeight()/3 + Escenario.MANDO_PADDING/2,
                spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3 + Escenario.MANDO_PADDING, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(!escenario.isPausa()) {
                    if (touchEvent.isActionDown()) {
                        jugador.setAction(Personaje.ACTION_JUMP, touchEvent);
                    }
                }
                return true;
            };
        };
        rB.setAlpha(0.0f);

        spriteMandoDireccional.setX(spriteMandoDireccional.getX() + ancho);
        spriteMandoAcciones.setX(spriteMandoAcciones.getX() + ancho);
        rDireccional.setX(rDireccional.getX() + ancho);

        hud.registerTouchArea(rDireccional);
        hud.registerTouchArea(rY);
        hud.registerTouchArea(rA);
        hud.registerTouchArea(rX);
        hud.registerTouchArea(rB);
        hud.attachChild(rDireccional);
        hud.attachChild(rY);
        hud.attachChild(rA);
        hud.attachChild(rX);
        hud.attachChild(rB);

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


}
