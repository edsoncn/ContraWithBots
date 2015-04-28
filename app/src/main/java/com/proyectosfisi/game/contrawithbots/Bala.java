package com.proyectosfisi.game.contrawithbots;

import org.andengine.engine.options.resolutionpolicy.CropResolutionPolicy;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 24/04/2015.
 */
public class Bala extends TiledSprite{

    public static final float VELOCITY_X = 1.0f;
    public static final float VELOCITY_Y = 1.0f;

    protected float relativeX;
    protected float relativeY;
    protected float velocityX;
    protected float velocityY;

    protected final CropResolutionPolicy crp;
    protected final Sprite parallaxLayerBackSprite;

                                                // L&R,    Up,   Down, L&R&U, L&R&D,
    private final float MOVING_X[] = new float[]{15.0f,  1.0f,  19.0f, 11.0f, 11.0f};
    private final float MOVING_Y[] = new float[]{-0.5f, 25.5f, -21.5f, 14.5f, -15.5f};

    public Bala(final CropResolutionPolicy crp, final Sprite parallaxLayerBackSprite, final Personaje personaje, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(personaje.getX(), personaje.getY(), mBulletTextureRegion, pVertexBufferObjectManager);
        this.crp = crp;
        this.parallaxLayerBackSprite = parallaxLayerBackSprite;

        setRelativeX(personaje.getRelativeX());
        setRelativeY(personaje.getRelativeY());

        int pi = 0;
        switch (personaje.getState()){
            case Personaje.STATE_Q0:
                velocityY = 0.0f;
                if(personaje.getOrientation() == Personaje.ORIENTATION_LEFT){
                    velocityX = -VELOCITY_X;
                }else {
                    velocityX = VELOCITY_X;
                }
                break;
            case Personaje.STATE_Q2:
                if(personaje.isActionUp()){
                    pi = 3;
                    velocityY = VELOCITY_Y;
                }else if(personaje.isActionDown()){
                    pi = 4;
                    velocityY = -VELOCITY_Y;
                }else{
                    pi = 0;
                    velocityY = 0.0f;
                }
                if(personaje.getOrientation() == Personaje.ORIENTATION_LEFT){
                    velocityX = -VELOCITY_X;
                }else {
                    velocityX = VELOCITY_X;
                }
                break;
            case Personaje.STATE_Q3: // Down
                pi = 2;
                velocityX = 0.0f;
                velocityY = -VELOCITY_Y;
                break;
            case Personaje.STATE_Q4: // Up
                pi = 1;
                velocityX = 0.0f;
                velocityY = VELOCITY_Y;
                break;
        }
        setRelativeY(getRelativeY() + MOVING_Y[pi]);
        if(personaje.getOrientation() == Personaje.ORIENTATION_LEFT){
            setRelativeX(getRelativeX() - MOVING_X[pi]);
        }else{
            setRelativeX(getRelativeX() + MOVING_X[pi]);
        }
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        setRelativeX(getRelativeX() + velocityX);
        setRelativeY(getRelativeY() + velocityY);
        super.onManagedUpdate(pSecondsElapsed);
    }

    public float getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
        setX(parallaxLayerBackSprite.getX() + relativeX);
    }

    public float getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        setY(relativeY + crp.getBottom() +  Personaje.PADDING);
    }

}
