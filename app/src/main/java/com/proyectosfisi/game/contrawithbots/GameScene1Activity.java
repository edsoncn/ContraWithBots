package com.proyectosfisi.game.contrawithbots;

import android.widget.Toast;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.CropResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.primitive.Rectangle;
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
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import java.io.IOException;

/**
 * Created by edson on 30/03/2015.
 */
public class GameScene1Activity extends SimpleBaseGameActivity{

    // ===========================================================
    // Constants
    // ===========================================================

    private static final int CAMERA_WIDTH = 1024;
    private static final int CAMERA_HEIGHT = 256;
    private static final float SQUARE_SIZE = 32;
    private static final float MANDO_PADDING = 8;

    // ===========================================================
    // Fields
    // ===========================================================

    private ITexture mParallaxLayerBackTexture;
    private ITextureRegion mParallaxLayerBackTextureRegion;

    private ITexture mMandoDireccionalTexture;
    private ITextureRegion mMandoDireccionalTextureRegion;

    private ITexture mMandoStartSelectTexture;
    private ITextureRegion mMandoStartSelectTextureRegion;

    private ITexture mMandoAccionesTexture;
    private ITextureRegion mMandoAccionesTextureRegion;

    private Camera camera;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    CropResolutionPolicy crp;

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
        final Sprite parallaxLayerBackSprite = new Sprite(0, 0, this.mParallaxLayerBackTextureRegion, vertexBufferObjectManager);
        parallaxLayerBackSprite.setOffsetCenter(0, 0);
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(0.0f, parallaxLayerBackSprite));

        // Mando
        final Sprite spriteMandoDireccional = new Sprite(left + mMandoDireccionalTextureRegion.getWidth()/2 + MANDO_PADDING
                , bottom + mMandoDireccionalTextureRegion.getHeight()/2 + MANDO_PADDING, mMandoDireccionalTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(spriteMandoDireccional);

        final Sprite spriteMandoStartSelect = new Sprite((left+right)/2
                , bottom + mMandoStartSelectTextureRegion.getHeight()/2 + MANDO_PADDING, mMandoStartSelectTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(spriteMandoStartSelect);

        final Sprite spriteMandoAccionesSelect = new Sprite(right - mMandoAccionesTextureRegion.getWidth()/2 - MANDO_PADDING
                , bottom + mMandoAccionesTextureRegion.getHeight()/2 + MANDO_PADDING, mMandoAccionesTextureRegion, this.getVertexBufferObjectManager());
        scene.attachChild(spriteMandoAccionesSelect);

        // Touch Area
        HUD hud = new HUD();

        final Rectangle rLeft = new Rectangle(spriteMandoDireccional.getX() - spriteMandoDireccional.getWidth()/3, spriteMandoDireccional.getY(),
                spriteMandoDireccional.getWidth()/3, spriteMandoDireccional.getHeight()/3, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (touchEvent.isActionUp()){
                    spriteMandoStartSelect.setX(spriteMandoStartSelect.getX() - 5);
                }
                return true;
            };
        };
        rLeft.setAlpha(0.0f);

        final Rectangle rRight = new Rectangle(spriteMandoDireccional.getX() + spriteMandoDireccional.getWidth()/3, spriteMandoDireccional.getY(),
                spriteMandoDireccional.getWidth()/3, spriteMandoDireccional.getHeight()/3, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (touchEvent.isActionUp()){
                    spriteMandoStartSelect.setX(spriteMandoStartSelect.getX() + 5);
                }
                return true;
            };
        };
        rRight.setAlpha(0.0f);

        final Rectangle rUp = new Rectangle(spriteMandoDireccional.getX(), spriteMandoDireccional.getY() + spriteMandoDireccional.getHeight()/3,
                spriteMandoDireccional.getWidth()/3, spriteMandoDireccional.getHeight()/3, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (touchEvent.isActionUp()){
                    spriteMandoStartSelect.setY(spriteMandoStartSelect.getY() + 5);
                }
                return true;
            };
        };
        rUp.setAlpha(0.0f);

        final Rectangle rDown = new Rectangle(spriteMandoDireccional.getX(), spriteMandoDireccional.getY() - spriteMandoDireccional.getHeight()/3,
                spriteMandoDireccional.getWidth()/3, spriteMandoDireccional.getHeight()/3, getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (touchEvent.isActionUp()){
                    spriteMandoStartSelect.setY(spriteMandoStartSelect.getY() - 5);
                }
                return true;
            };
        };
        rDown.setAlpha(0.0f);

        final Rectangle rSelect = new Rectangle(spriteMandoStartSelect.getX() - spriteMandoStartSelect.getWidth()/2, spriteMandoStartSelect.getY(),
                spriteMandoStartSelect.getWidth()/2, spriteMandoStartSelect.getHeight(), getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (touchEvent.isActionUp()){
                    parallaxLayerBackSprite.setX(parallaxLayerBackSprite.getX() + 5);
                }
                return true;
            };
        };
        rSelect.setAlpha(0.0f);

        final Rectangle rStart = new Rectangle(spriteMandoStartSelect.getX() - spriteMandoStartSelect.getWidth()/2, spriteMandoStartSelect.getY(),
                spriteMandoStartSelect.getWidth()/2, spriteMandoStartSelect.getHeight(), getVertexBufferObjectManager()){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (touchEvent.isActionUp()){
                    parallaxLayerBackSprite.setX(parallaxLayerBackSprite.getX() - 5);
                }
                return true;
            };
        };
        rStart.setAlpha(0.0f);

        hud.registerTouchArea(rLeft);
        hud.registerTouchArea(rRight);
        hud.registerTouchArea(rUp);
        hud.registerTouchArea(rDown);
        hud.registerTouchArea(rSelect);
        hud.registerTouchArea(rStart);
        hud.attachChild(rLeft);
        hud.attachChild(rRight);
        hud.attachChild(rUp);
        hud.attachChild(rDown);
        hud.attachChild(rSelect);
        hud.attachChild(rStart);

        camera.setHUD(hud);

        return scene;
    }

    private Rectangle addRectangle(final Scene scene, Color color, float left, float top) {
        Rectangle r = new Rectangle(left, top, SQUARE_SIZE, SQUARE_SIZE, getVertexBufferObjectManager());
        r.registerEntityModifier(new LoopEntityModifier(new RotationByModifier(5f, 360f)));
        r.setColor(color);
        scene.attachChild(r);

        return r;
    }
}
