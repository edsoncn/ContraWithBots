package com.proyectosfisi.game.contrawithbots.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.CropResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;

import java.io.IOException;

/**
 * Created by edson on 29/03/2015.
 */
public class ScreenExample extends SimpleBaseGameActivity {
    private static final int CAMERA_WIDTH = 1024;
    private static final int CAMERA_HEIGHT = 256;
    private static final int SQUARE_SIZE = 32;

    CropResolutionPolicy crp;

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        crp = new CropResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                crp, camera);
    }

    @Override
    public void onCreateResources() throws IOException {
    }

    @Override
    public Scene onCreateScene() {

        this.mEngine.registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene();
        scene.getBackground().setColor(Color.YELLOW);

        final float centerX = CAMERA_WIDTH / 2;
        final float centerY = CAMERA_HEIGHT / 2;
        float left = crp.getLeft();
        float bottom = crp.getBottom();
        float right = crp.getRight();
        float top = crp.getTop();
/*
        addRectangle(scene, Color.RED, left, top);
        addRectangle(scene, Color.RED, left, bottom);
        addRectangle(scene, Color.RED, right, top);
        addRectangle(scene, Color.RED, right, bottom);

        addRectangle(scene, Color.GREEN, left, centerY);
        addRectangle(scene, Color.GREEN, right, centerY);
        addRectangle(scene, Color.GREEN, centerX, top);
        addRectangle(scene, Color.GREEN, centerX, bottom);
*/
        addRectangle(scene, Color.BLUE, left, bottom);
        addRectangle(scene, Color.BLACK, right, top);

        return scene;
    }

    private void addRectangle(final Scene scene, Color color, float left, float top) {
        Rectangle r = new Rectangle(left, top, SQUARE_SIZE, SQUARE_SIZE, getVertexBufferObjectManager());
        r.registerEntityModifier(new LoopEntityModifier(new RotationByModifier(5f, 360f)));
        r.setColor(color);
        scene.attachChild(r);
    }

}
