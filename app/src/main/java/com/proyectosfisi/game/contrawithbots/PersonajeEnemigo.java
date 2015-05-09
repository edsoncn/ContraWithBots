package com.proyectosfisi.game.contrawithbots;

import org.andengine.engine.options.resolutionpolicy.CropResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 28/04/2015.
 */
public class PersonajeEnemigo extends Personaje {

    public static float SEPARATE = 144;

    private float countBullets;
    private float countMove;

    public PersonajeEnemigo(Escenario escenario, final float relativeX, final float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX + SEPARATE * 1.5f, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
        countBullets = 0.0f;
        countMove = 0.0f;
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        countBullets += pSecondsElapsed;
        countMove += pSecondsElapsed;
        if(countBullets > 1){
            shoot();
            countBullets = 0;
        }
        if(Math.random() > 0.98){
            setAction(ACTION_JUMP);
        }
        if(countMove > 0.25){
            if(Math.abs(getRelativeX() - enemigo.getRelativeX()) > SEPARATE){
                if(getRelativeX() > enemigo.getRelativeX()){
                    setAction(ACTION_LEFT);
                }else{
                    setAction(ACTION_RIGHT);
                }
            }else{
                setAction(-ACTION_LEFT);
                setAction(-ACTION_RIGHT);
            }
            countMove = 0;
        }
        if(getRelativeX() > enemigo.getRelativeX() && getOrientation() == ORIENTATION_RIGHT){
            setAction(ACTION_LEFT);
            setAction(-ACTION_LEFT);
        }
        if(getRelativeX() < enemigo.getRelativeX() && getOrientation() == ORIENTATION_LEFT){
            setAction(ACTION_RIGHT);
            setAction(-ACTION_RIGHT);
        }
        super.onManagedUpdate(pSecondsElapsed);
    }
}
