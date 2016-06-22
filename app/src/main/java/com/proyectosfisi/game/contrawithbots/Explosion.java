package com.proyectosfisi.game.contrawithbots;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 29/06/2015.
 */
public class Explosion extends AnimatedSprite {

    protected Escenario escenario;

    protected float lastBackSpriteX;
    protected float lastBackSpriteY;

    protected int id;

    protected Sound sExplosion;

    protected float vx;
    protected float vy;

    public Explosion(Escenario escenario, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0.0f, 0.0f, mBulletTextureRegion, pVertexBufferObjectManager);
        this.escenario = escenario;

        id = ExplosionFactory.getInstance().getNextId();
        vx = 0;
        vy = 0;

        inactivar();
    }

    public void init(){
        lastBackSpriteX = escenario.getParallaxX();
        lastBackSpriteY = escenario.getParallaxY();
        long f = Bala.FRAME_TIME_CHISPA;
        animate(new long[]{f, f, f, f, f, f, f, f, f, f, f, f}, 0, 11, 0);
        sExplosion.play();
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            float bX = escenario.getParallaxX();
            float bY = escenario.getParallaxY();
            setX(getX() + bX - lastBackSpriteX + vx);
            setY(getY() + bY - lastBackSpriteY + vy);
            lastBackSpriteX = bX;
            lastBackSpriteY = bY;
            if(!isAnimationRunning() && getCurrentTileIndex() == 11){
                inactivar();
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    public void inactivar(){
        setVisible(false);
        setIgnoreUpdate(true);
        ExplosionFactory.getInstance().removeExplosion(this);
    }

    public boolean isInactivo(){
        return isIgnoreUpdate();
    }

    public void activar(){
        setIgnoreUpdate(false);
        setVisible(true);
        init();
    }

    public void setVelocidad(float vx, float vy){
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Explosion){
            return this.id == ((Explosion) o).getId();
        }else{
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public Sound getsExplosion() {
        return sExplosion;
    }

    public void setsExplosion(Sound sExplosion) {
        this.sExplosion = sExplosion;
    }
}
