package com.proyectosfisi.game.contrawithbots.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class MovingBallExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 240;
	private static final int CAMERA_HEIGHT = 180;

	private static final float DEMO_VELOCITY = 200.0f;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mFaceTextureRegion;

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
		final Camera camera = new Camera(0, 0, MovingBallExample.CAMERA_WIDTH, MovingBallExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(MovingBallExample.CAMERA_WIDTH, MovingBallExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 0, 2, 1);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final float centerX1 = (MovingBallExample.CAMERA_WIDTH/2 - this.mFaceTextureRegion.getWidth()) / 2;
		final float centerY1 = (MovingBallExample.CAMERA_HEIGHT/2 - this.mFaceTextureRegion.getHeight()) / 2;
		final Ball ball1 = new Ball(centerX1, centerY1, this.mFaceTextureRegion, this.getVertexBufferObjectManager());

        final float centerX2 = MovingBallExample.CAMERA_WIDTH/2 + (MovingBallExample.CAMERA_WIDTH/2 - this.mFaceTextureRegion.getWidth()) / 2;
        final float centerY2 = MovingBallExample.CAMERA_HEIGHT/2 + (MovingBallExample.CAMERA_HEIGHT/2 - this.mFaceTextureRegion.getHeight()) / 2;
        final Ball ball2 = new Ball(centerX2, centerY2, this.mFaceTextureRegion, this.getVertexBufferObjectManager());

        ball1.setOtherBall(ball2);
        //ball2.setOtherBall(ball1);

		scene.attachChild(ball1);
        scene.attachChild(ball2);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class Ball extends AnimatedSprite {
		private final PhysicsHandler mPhysicsHandler;

        private Ball otherBall;

		public Ball(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
			super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
			this.mPhysicsHandler = new PhysicsHandler(this);
			this.registerUpdateHandler(this.mPhysicsHandler);
			this.mPhysicsHandler.setVelocity(MovingBallExample.DEMO_VELOCITY, MovingBallExample.DEMO_VELOCITY);
            setOtherBall(null);
		}

		@Override
		protected void onManagedUpdate(final float pSecondsElapsed) {

            if(otherBall != null) {
                float dx = otherBall.getX() - getX();
                float dy = otherBall.getY() - getY();
                float d = (float)Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
                if(d <= otherBall.getWidth()/2 + getWidth()/2){
                    float vx = this.mPhysicsHandler.getVelocityX();
                    float vy = this.mPhysicsHandler.getVelocityY();

                    float wx = otherBall.mPhysicsHandler.getVelocityX();
                    float wy = otherBall.mPhysicsHandler.getVelocityY();

                    float vd = (vx*dx+vy*dy)/d;
                    float wd = (wx*dx*-1+wy*dy*-1)/d;

                    float dux = dx/d;
                    float duy = dy/d;

                    float vx1 = vd*dux;
                    float vy1 = vd*duy;
                    float vx2 = vx - vx1;
                    float vy2 = vy - vy1;
                    this.mPhysicsHandler.setVelocityX(vx1 * -1 + vx2);
                    this.mPhysicsHandler.setVelocityY(vy1 * -1 + vy2);

                    float wx1 = wd*dux*-1;
                    float wy1 = wd*duy*-1;
                    float wx2 = wx - wx1;
                    float wy2 = wy - wy1;
                    otherBall.mPhysicsHandler.setVelocityX(wx1 * -1 + wx2);
                    otherBall.mPhysicsHandler.setVelocityY(wy1 * -1 + wy2);
                }
            }

			if(this.mX < 0) {
                this.mPhysicsHandler.setVelocityX(Math.abs(this.mPhysicsHandler.getVelocityX()));
            } else if(this.mX + this.getWidth() > MovingBallExample.CAMERA_WIDTH) {
				this.mPhysicsHandler.setVelocityX(Math.abs(this.mPhysicsHandler.getVelocityX())*-1);
			}

			if(this.mY < 0) {
                this.mPhysicsHandler.setVelocityY(Math.abs(this.mPhysicsHandler.getVelocityY()));
            }else if(this.mY + this.getHeight() > MovingBallExample.CAMERA_HEIGHT) {
                this.mPhysicsHandler.setVelocityY(Math.abs(this.mPhysicsHandler.getVelocityY())*-1);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}

        public Ball getOtherBall() {
            return otherBall;
        }

        public void setOtherBall(Ball otherBall) {
            this.otherBall = otherBall;
        }
    }
}
