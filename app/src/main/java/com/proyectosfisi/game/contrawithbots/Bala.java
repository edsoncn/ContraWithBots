package com.proyectosfisi.game.contrawithbots;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by edson on 24/04/2015.
 */
public class Bala extends TiledSprite{

    public static final float VELOCITY_X = 4.5f;
    public static final float VELOCITY_Y = 4.5f;
    public static final long FRAME_TIME_CHISPA = 30;

    protected int id;

    protected float velocityX;
    protected float velocityY;

    protected Escenario escenario;
    protected Actor actor;
    protected ArrayList<Actor> enemigos;

    protected AnimatedSprite chispa;
    protected float countChispa;
    protected boolean active;

    protected Sound sDisparo;

    protected float lastBackSpriteX;
    protected float lastBackSpriteY;

    public Bala(Escenario escenario, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0.0f, 0.0f, mBulletTextureRegion, pVertexBufferObjectManager);

        this.escenario = escenario;
        this.actor = null;
        this.enemigos = null;

        this.active = false;
        setVisible(false);

        chispa = new AnimatedSprite(0, 0, mBulletTextureRegion, pVertexBufferObjectManager);
        chispa.setVisible(false);
        escenario.getLayerPlayer().attachChild(chispa);

        id = BalaFactory.getInstance().getNextId();

        this.sDisparo = escenario.getsDisparo();
    }

    public void initBala(){
        this.active = true;

        PosicionYVelocidad pv = actor.getPosicionYVelocidadDeBala();
        setX(pv.getX());
        setY(pv.getY());
        lastBackSpriteX = escenario.getParallaxLayerBackSprite().getX();
        lastBackSpriteY = escenario.getParallaxLayerBackSprite().getY();
        velocityX = pv.getVx();
        velocityY = pv.getVy();

        chispa.setPosition(getX(), getY());
        chispa.animate(new long[]{FRAME_TIME_CHISPA, FRAME_TIME_CHISPA}, 1, 2, true);
        countChispa = 0;

        setVisible(true);
        chispa.setVisible(true);
    }

    public void inactivar(){
        setVisible(false);
        chispa.setVisible(false);
        this.active = false;
        BalaFactory.getInstance().removeBala(this);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            if (active) {
                float left = escenario.getCropResolutionPolicy().getLeft();
                float right = escenario.getCropResolutionPolicy().getRight();
                float top = escenario.getCropResolutionPolicy().getTop();
                float bottom = escenario.getCropResolutionPolicy().getBottom();
                if (left - actor.getWidth() / 2 > getX() || getX() > right + actor.getWidth() / 2
                        || top + actor.getHeight() / 2 < getY() || getY() < bottom
                        || validarColisiones()) {
                    inactivar();
                } else {
                    float bX = escenario.getParallaxLayerBackSprite().getX();
                    float bY = escenario.getParallaxLayerBackSprite().getY();
                    setX(getX() + velocityX + bX - lastBackSpriteX);
                    setY(getY() + velocityY + bY - lastBackSpriteY);
                    lastBackSpriteX = bX;
                    lastBackSpriteY = bY;
                    if (chispa != null) {
                        if (countChispa > 0.05f) {
                            chispa.setVisible(false);
                        } else {
                            PosicionYVelocidad pv = actor.getPosicionYVelocidadDeBala();
                            chispa.setX(pv.getX());
                            chispa.setY(pv.getY());
                            countChispa += pSecondsElapsed;
                            chispa.setIgnoreUpdate(false);
                        }
                    }
                    super.onManagedUpdate(pSecondsElapsed);
                }
            }
        }else{
            chispa.setIgnoreUpdate(true);
        }
    }

    protected boolean validarColisiones(){
        for(Actor enemigo : enemigos){
            if(!enemigo.isInactivo() && !enemigo.isActionDie()){
                if (enemigo.colisionBala(this)){
                    float restar = 5.00f;
                    if(enemigo instanceof PersonajeEnemigo){
                        restar = 25.0f;
                    }else if(enemigo instanceof PersonajeFrancotirador){
                        restar = 33.34f;
                    }
                    if(enemigo.restarVidaOMorir(restar)) {
                        enemigo.setAction(Actor.ACTION_DIE);
                        actor.asesinar(enemigo);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Bala){
            return this.id == ((Bala) o).getId();
        }else{
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }

    public void setEnemigos(ArrayList<Actor> enemigos) {
        this.enemigos = enemigos;
    }

    public Sound getsDisparo() {
        return sDisparo;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }
}
