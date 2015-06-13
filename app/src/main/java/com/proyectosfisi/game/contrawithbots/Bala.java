package com.proyectosfisi.game.contrawithbots;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by edson on 24/04/2015.
 */
public class Bala extends TiledSprite{

                                                      // L&R,    Up,   Down, L&R&U,  L&R&D,  Jump, Cayen. D
    public static final float MOVING_X[] = new float[]{18.5f,  1.0f,  22.0f, 12.0f,  12.0f,  0.0f,  -2.0f};
    public static final float MOVING_Y[] = new float[]{-1.0f, 26.5f, -22.5f, 16.5f, -18.0f, -8.5f, -25.0f};
                                                           // L&R,     Up,   Down,  L&R&U,  L&R&D,  Jump, Cayen. D
    public static final float CHOQUE_X_MIN[] = new float[]{-10.5f, -10.5f, -20.5f, -10.5f, -10.5f,  -8.5f, -10.5f};
    public static final float CHOQUE_X_MAX[] = new float[]{  9.5f,   9.5f,  16.5f,   9.5f,   9.5f,   8.5f,   9.5f};
    public static final float CHOQUE_Y_MIN[] = new float[]{-28.0f, -28.0f, -28.0f, -28.0f, -28.0f, -17.0f, -28.0f};
    public static final float CHOQUE_Y_MAX[] = new float[]{ 13.0f,  13.0f, -12.0f,  13.0f,  13.0f,   0.0f,  13.0f};

    public static final float VELOCITY_X = 4.5f;
    public static final float VELOCITY_Y = 4.5f;
    public static final long FRAME_TIME_CHISPA = 30;

    protected int id;

    protected float relativeX;
    protected float relativeY;
    protected float velocityX;
    protected float velocityY;

    protected Escenario escenario;
    protected Personaje personaje;
    protected ArrayList<Personaje> enemigos;

    protected AnimatedSprite chispa;
    protected float countChispa;
    protected boolean active;

    public Bala(Escenario escenario, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0.0f, 0.0f, mBulletTextureRegion, pVertexBufferObjectManager);

        this.escenario = escenario;
        this.personaje = null;
        this.enemigos = null;

        this.active = false;
        setVisible(false);

        chispa = new AnimatedSprite(0, 0, mBulletTextureRegion, pVertexBufferObjectManager);
        chispa.setVisible(false);
        escenario.getLayerPlayer().attachChild(chispa);

        id = BalaFactory.getInstance().getNextId();

    }

    public void initBala(){
        this.active = true;

        setRelativeX(personaje.getRelativeX());
        setRelativeY(personaje.getRelativeY());

        float[] position = initPositionsAndVelocity(personaje);
        setRelativeX(getRelativeX() + position[0]);
        setRelativeY(getRelativeY() + position[1]);
        velocityX = position[2];
        velocityY = position[3];

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

    public static int positionPersonaje(Personaje personaje){
        int pi = 0;
        switch (personaje.getState()){
            case Personaje.STATE_Q0:
                break;
            case Personaje.STATE_Q1:
                pi = 5;
                break;
            case Personaje.STATE_Q6:
                if(personaje.isActionUp()){
                    if(!personaje.isActionLeft() && !personaje.isActionRight()){
                        pi = 1;
                    }else{
                        pi = 3;
                    }
                }else if(personaje.isActionDown()){
                    if(!personaje.isActionLeft() && !personaje.isActionRight()){
                        pi = 6;
                    }else{
                        pi = 4;
                    }
                }else{
                    pi = 0;
                }
                break;
            case Personaje.STATE_Q2:
                if(personaje.isActionUp()){
                    pi = 3;
                }else if(personaje.isActionDown()){
                    pi = 4;
                }else{
                    pi = 0;
                }
                break;
            case Personaje.STATE_Q3: // Down
                if(personaje instanceof PersonajeEnemigo && ((PersonajeEnemigo) personaje).getTipo() == PersonajeEnemigo.TIPO_CIMA){
                    pi = 1;
                }else {
                    pi = 2;
                }
                break;
            case Personaje.STATE_Q4: // Up
                pi = 1;
                break;
        }
        return pi;
    }

    public static float[] initPositionsAndVelocity(Personaje personaje){
        int pi = 0;
        float velocityX = 0.0f;
        float velocityY = 0.0f;
        switch (personaje.getState()){
            case Personaje.STATE_Q0:
                velocityY = 0.0f;
                if(personaje.getOrientation() == personaje.ORIENTATION_LEFT){
                    velocityX = -VELOCITY_X;
                }else {
                    velocityX = VELOCITY_X;
                }
                break;
            case Personaje.STATE_Q1:
                pi = 5;
                if(personaje.isActionLeft()){
                    velocityX = -VELOCITY_X;
                }else if(personaje.isActionRight()){
                    velocityX = VELOCITY_X;
                }
                if(personaje.isActionUp()){
                    velocityY = VELOCITY_Y;
                }else if(personaje.isActionDown()){
                    velocityY = -VELOCITY_Y;
                }else{
                    velocityY = 0.0f;
                    if(personaje.getOrientation() == personaje.ORIENTATION_LEFT){
                        velocityX = -VELOCITY_X;
                    }else {
                        velocityX = VELOCITY_X;
                    }
                }
                break;
            case Personaje.STATE_Q6:
                if(personaje.isActionUp()){
                    if(!personaje.isActionLeft() && !personaje.isActionRight()){
                        pi = 1;
                    }else{
                        pi = 3;
                    }
                    velocityY = VELOCITY_Y;
                }else if(personaje.isActionDown()){
                    if(!personaje.isActionLeft() && !personaje.isActionRight()){
                        pi = 6;
                    }else{
                        pi = 4;
                    }
                    velocityY = -VELOCITY_Y;
                }else{
                    pi = 0;
                    velocityY = 0.0f;
                    if(personaje.getOrientation() == personaje.ORIENTATION_LEFT){
                        velocityX = -VELOCITY_X;
                    }else {
                        velocityX = VELOCITY_X;
                    }
                }
                if(personaje.isActionLeft()){
                    velocityX = -VELOCITY_X;
                }else if(personaje.isActionRight()){
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
                if(personaje.getOrientation() == personaje.ORIENTATION_LEFT){
                    velocityX = -VELOCITY_X;
                }else {
                    velocityX = VELOCITY_X;
                }
                break;
            case Personaje.STATE_Q3: // Down
                if(personaje instanceof PersonajeEnemigo && ((PersonajeEnemigo) personaje).tipo == PersonajeEnemigo.TIPO_CIMA){
                    pi = 6;
                    velocityY = -VELOCITY_Y;
                }else {
                    pi = 2;
                    if (personaje.getOrientation() == personaje.ORIENTATION_LEFT) {
                        velocityX = -VELOCITY_X;
                    } else {
                        velocityX = VELOCITY_X;
                    }
                    velocityY = 0.0f;
                }
                break;
            case Personaje.STATE_Q4: // Up
                pi = 1;
                velocityX = 0.0f;
                velocityY = VELOCITY_Y;
                break;
        }
        float[] position = new float[4];
        if(personaje.getOrientation() == personaje.ORIENTATION_LEFT){
            position[0] = -MOVING_X[pi];
        }else{
            position[0] = MOVING_X[pi];
        }
        position[1] = MOVING_Y[pi];
        position[2] = velocityX;
        position[3] = velocityY;
        if(personaje.getState() == personaje.STATE_Q1){
            position[0] += (velocityX != 0 ? (-velocityX/Math.abs(velocityX)) : 0) * MOVING_Y[pi];
            position[1] += (velocityY != 0 ? (-velocityY/Math.abs(velocityY)) : 0) * MOVING_Y[pi];
        }
        return position;
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            if (active) {
                float left = escenario.getCropResolutionPolicy().getLeft();
                float right = escenario.getCropResolutionPolicy().getRight();
                float top = escenario.getCropResolutionPolicy().getTop();
                float bottom = escenario.getCropResolutionPolicy().getBottom();
                if (left - personaje.getWidth() / 2 > getX() || getX() > right + personaje.getWidth() / 2
                        || top + personaje.getHeight() / 2 < getY() || getY() < bottom
                        || validCollisions()) {
                    inactivar();
                } else {
                    setRelativeX(getRelativeX() + velocityX);
                    setRelativeY(getRelativeY() + velocityY);
                    if (chispa != null) {
                        if (countChispa > 0.05f) {
                            chispa.setVisible(false);
                        } else {
                            float[] position = initPositionsAndVelocity(personaje);
                            chispa.setX(personaje.getX() + position[0]);
                            chispa.setY(personaje.getY() + position[1]);
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

    private boolean validCollisions(){
        for(Personaje enemigo : enemigos){
            if(enemigo.getState() != Personaje.STATE_Q5){
                if (validarColision(enemigo)){
                    float restar = 2.50f;
                    if(enemigo instanceof PersonajeEnemigo){
                        restar = 25.0f;
                    }
                    if(enemigo.getVida().restarVidaOMorir(restar)) {
                        enemigo.setStateQ5();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validarColision(Personaje personaje){
        int pi = positionPersonaje(personaje);
        float xMin = CHOQUE_X_MIN[pi];
        float xMax = CHOQUE_X_MAX[pi];
        float yMin = CHOQUE_Y_MIN[pi];
        float yMax = CHOQUE_Y_MAX[pi];
        return personaje.getX() + xMin < getX() && getX() < personaje.getX() + xMax
                && personaje.getY() + yMin < getY() && getY() < personaje.getY() + yMax;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Bala){
            return this.id == ((Bala) o).getId();
        }else{
            return false;
        }
    }

    public float getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
        setX(escenario.getParallaxLayerBackSprite().getX() + relativeX);
    }

    public float getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        setY(relativeY + escenario.getCropResolutionPolicy().getBottom() +  Escenario.PISO_ALTO);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje = personaje;
    }

    public void setEnemigos(ArrayList<Personaje> enemigos) {
        this.enemigos = enemigos;
    }
}
