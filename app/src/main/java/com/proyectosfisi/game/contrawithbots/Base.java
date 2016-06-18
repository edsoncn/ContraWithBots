package com.proyectosfisi.game.contrawithbots;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 11/07/2015.
 */
public class Base extends Sprite {

    protected float count;
    protected float v;
    protected Escenario escenario;

    public Base(Escenario escenario, final ITextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0.0f, 0.0f, mBulletTextureRegion, pVertexBufferObjectManager);
        this.escenario = escenario;
        inactivar();
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            if(count + v >= 1){
                count = 1;
                v = -1 * Math.abs(v);
            }else if(count + v <= 0){
                count = 0;
                v = Math.abs(v);
            }else{
                count += v;
            }
            setAlpha(count * 0.59f + 0.05f);
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    public void inactivar(){
        setVisible(false);
        setIgnoreUpdate(true);
    }

    public boolean isInactivo(){
        return isIgnoreUpdate();
    }

    public void activar(){
        count = 0;
        v = 0.125f;
        float right = escenario.getCropResolutionPolicy().getRight();
        float bottom = escenario.getCropResolutionPolicy().getBottom();
        setPosition(right - 90, bottom + 51.5f);
        setIgnoreUpdate(false);
        setVisible(true);
    }

}
