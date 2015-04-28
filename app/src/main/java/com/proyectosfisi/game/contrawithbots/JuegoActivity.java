package com.proyectosfisi.game.contrawithbots;

import android.widget.Toast;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.CropResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
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

    // ===========================================================
    // Constants
    // ===========================================================

    public static final int CAMERA_WIDTH = 1024;
    public static final int CAMERA_HEIGHT = 256;
    public static final float MANDO_PADDING = 4;

    // ===========================================================
    // Fields
    // ===========================================================

    private Camera camera;

    private ITexture mPlayerTexture;
    private TiledTextureRegion mPlayerTextureRegion;

    private ITexture mBulletTexture;
    private TiledTextureRegion mBulletTextureRegion;

    private ITexture mParallaxLayerBackTexture;
    private ITextureRegion mParallaxLayerBackTextureRegion;

    private ITexture mMandoDireccionalTexture;
    private ITextureRegion mMandoDireccionalTextureRegion;

    private ITexture mMandoStartSelectTexture;
    private ITextureRegion mMandoStartSelectTextureRegion;

    private ITexture mMandoAccionesTexture;
    private ITextureRegion mMandoAccionesTextureRegion;

    private CropResolutionPolicy crp;

    private Personaje jugador;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        crp = new CropResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, crp, camera);

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

        this.mMandoStartSelectTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/mando_start_select.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mMandoStartSelectTextureRegion = TextureRegionFactory.extractFromTexture(this.mMandoStartSelectTexture);
        this.mMandoStartSelectTexture.load();

        this.mMandoAccionesTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/mando_acciones.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mMandoAccionesTextureRegion = TextureRegionFactory.extractFromTexture(this.mMandoAccionesTexture);
        this.mMandoAccionesTexture.load();

        // Personaje principal
        this.mPlayerTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/sprite-personaje-principal.png");
        this.mPlayerTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mPlayerTexture, 16, 5);
        this.mPlayerTexture.load();

        // Bullet
        this.mBulletTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/sprite-bala.png");
        this.mBulletTextureRegion = TextureRegionFactory.extractTiledFromTexture(this.mBulletTexture, 3, 1);
        this.mBulletTexture.load();

    }

    @Override
    public Scene onCreateScene() {

        final float centerX = CAMERA_WIDTH / 2;
        final float centerY = CAMERA_HEIGHT / 2;
        float left = crp.getLeft();
        float bottom = crp.getBottom();
        float right = crp.getRight();
        float top = crp.getTop();

        this.mEngine.registerUpdateHandler(new FPSLogger());

        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();

        final Scene scene = new Scene();
        final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
        scene.setBackground(autoParallaxBackground);

        // Fondo
        final Sprite parallaxLayerBackSprite = new Sprite(left, 0, this.mParallaxLayerBackTextureRegion, vertexBufferObjectManager);
        parallaxLayerBackSprite.setOffsetCenter(0, 0);
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(0.0f, parallaxLayerBackSprite));

        final Entity layerPlayer = new Entity();
        layerPlayer.setZIndex(7);
        final Entity layerBullets = new Entity();
        layerBullets.setZIndex(5);

        // Principal
        jugador = new Personaje(layerPlayer, layerBullets, crp, parallaxLayerBackSprite, (right - left)/2, 0, this.mPlayerTextureRegion, this.mBulletTextureRegion, vertexBufferObjectManager);
        jugador.setMoveLayerBackSprite(true);
        layerPlayer.attachChild(jugador);

        scene.attachChild(layerBullets);
        scene.attachChild(layerPlayer);

        // Mando
        final Sprite spriteMandoDireccional = new Sprite(left + mMandoDireccionalTextureRegion.getWidth()/2 + MANDO_PADDING
                , bottom + mMandoDireccionalTextureRegion.getHeight()/2 + MANDO_PADDING, mMandoDireccionalTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(spriteMandoDireccional);

        final Sprite spriteMandoStart = new Sprite((left+right)/2
                , bottom + mMandoStartSelectTextureRegion.getHeight()/2 + MANDO_PADDING, mMandoStartSelectTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(spriteMandoStart);

        final Sprite spriteMandoAcciones = new Sprite(right - mMandoAccionesTextureRegion.getWidth()/2 - MANDO_PADDING
                , bottom + mMandoAccionesTextureRegion.getHeight()/2 + MANDO_PADDING, mMandoAccionesTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(spriteMandoAcciones);

        // Touch Area
        HUD hud = new HUD();

        // Control Direccional
        final Rectangle rDireccional = new Rectangle(spriteMandoDireccional.getX(), spriteMandoDireccional.getY(),
                spriteMandoDireccional.getWidth(), spriteMandoDireccional.getHeight(), getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                jugador.setAction(touchEvent, X, Y, spriteMandoDireccional.getWidth(), spriteMandoDireccional.getHeight());
                return true;
            };
        };
        rDireccional.setAlpha(0.0f);

        // Control Select Start
        final Rectangle rSelect = new Rectangle(spriteMandoStart.getX() - spriteMandoStart.getWidth()/4, spriteMandoStart.getY(),
                spriteMandoStart.getWidth()/2, spriteMandoStart.getHeight(), getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){

                return true;
            };
        };
        rSelect.setAlpha(0.0f);
        final Rectangle rStart = new Rectangle(spriteMandoStart.getX() + spriteMandoStart.getWidth()/4, spriteMandoStart.getY(),
                spriteMandoStart.getWidth()/2, spriteMandoStart.getHeight(), getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){

                return true;
            };
        };
        rStart.setAlpha(0.00f);

        // Control Acciones
        final Rectangle rY = new Rectangle(spriteMandoAcciones.getX() - spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getY(),
                spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(touchEvent.isActionDown()) {
                    jugador.addBullet();
                }
                return true;
            };
        };
        rY.setAlpha(0.0f);
        final Rectangle rA = new Rectangle(spriteMandoAcciones.getX() + spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getY(),
                spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                return true;
            };
        };
        rA.setAlpha(0.0f);
        final Rectangle rX = new Rectangle(spriteMandoAcciones.getX(), spriteMandoAcciones.getY() + spriteMandoAcciones.getHeight()/3 - MANDO_PADDING/2,
                spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3 + MANDO_PADDING, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){

                return true;
            };
        };
        rX.setAlpha(0.0f);
        final Rectangle rB = new Rectangle(spriteMandoAcciones.getX(), spriteMandoAcciones.getY() - spriteMandoAcciones.getHeight()/3 + MANDO_PADDING/2,
                spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3 + MANDO_PADDING, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                jugador.setAction(Personaje.ACTION_JUMP, touchEvent);
                return true;
            };
        };
        rB.setAlpha(0.0f);

        hud.registerTouchArea(rDireccional);
        hud.registerTouchArea(rSelect);
        hud.registerTouchArea(rStart);
        hud.registerTouchArea(rY);
        hud.registerTouchArea(rA);
        hud.registerTouchArea(rX);
        hud.registerTouchArea(rB);
        hud.attachChild(rDireccional);
        hud.attachChild(rSelect);
        hud.attachChild(rStart);
        hud.attachChild(rY);
        hud.attachChild(rA);
        hud.attachChild(rX);
        hud.attachChild(rB);

        hud.setZIndex(9);

        camera.setHUD(hud);
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
}