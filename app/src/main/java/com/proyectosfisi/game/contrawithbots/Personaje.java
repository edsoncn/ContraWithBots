package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.engine.options.resolutionpolicy.CropResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 07/04/2015.
 */
public class Personaje extends AnimatedSprite {

    public static final long FRAME_TIME = 150;

    public static final float PADDING = 84;

    public static final int ACTION_LEFT = 1;
    public static final int ACTION_RIGHT = 2;
    public static final int ACTION_DOWN = 3;
    public static final int ACTION_UP = 4;
    public static final int ACTION_JUMP = 5;

    public static final int STATE_Q0 = 0;
    public static final int STATE_Q1 = 1;
    public static final int STATE_Q2 = 2;
    public static final int STATE_Q3 = 3;
    public static final int STATE_Q4 = 4;

    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_RIGHT = 2;

    public static final float VELOCITY_X = 1.5f;
    public static final float VELOCITY_Y = 6f;
    public static final float GRAVEDAD = -0.25f;

    public static int ACTIVE_POINTER_ID = 0;

    protected boolean actionLeft;
    protected boolean actionRight;
    protected boolean actionDown;
    protected boolean actionUp;
    protected boolean actionJump;

    protected int selectBoton;
    protected int orientation;
    protected int state;

    protected float relativeX;
    protected float relativeY;
    protected float velocityX;
    protected float velocityY;

    protected final Entity layerPlayer;
    protected final Entity layerBullets;

    protected CropResolutionPolicy crp; // Datos de la pantalla
    protected Sprite parallaxLayerBackSprite; // Sprite del fondo
    protected TiledTextureRegion mBulletTextureRegion; // Textura de la bala
    protected boolean moveLayerBackSprite;

    protected int activePointerID;

    public Personaje(final Entity layerPlayer, final Entity layerBullets, CropResolutionPolicy crp, Sprite parallaxLayerBackSprite, final float relativeX, final float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, 0, pTextureRegion, pVertexBufferObjectManager);

        this.layerPlayer = layerPlayer;
        this.layerBullets = layerBullets;
        this.crp = crp;
        this.parallaxLayerBackSprite = parallaxLayerBackSprite;
        this.mBulletTextureRegion = mBulletTextureRegion;
        moveLayerBackSprite = false;

        setRelativeX(relativeX);
        setRelativeY(relativeY);
        setVelocityX(0);
        setVelocityY(0);

        orientation = ORIENTATION_RIGHT;
        setStateQ0();

        initSelectBoton();

        activePointerID = TouchEvent.INVALID_POINTER_ID;
        
    }

    public synchronized void setAction(int action){
        switch (state){
            case STATE_Q0:
                switch (action){
                    case ACTION_LEFT:
                        if(!actionLeft) {
                            actionLeft = true;
                            setStateQ2();
                        }
                        break;
                    case ACTION_RIGHT:
                        if(!actionRight) {
                            actionRight = true;
                            setStateQ2();
                        }
                        break;
                    case ACTION_JUMP:
                        setStateQ1();
                        setVelocityY(VELOCITY_Y);
                        break;
                    case ACTION_DOWN:
                        setStateQ3();
                        break;
                    case ACTION_UP:
                        setStateQ4();
                        break;
                }
                break;
            case STATE_Q1:
                switch (action) {
                    case ACTION_LEFT:
                        if(!actionLeft) {
                            actionLeft = true;
                            actionJump = false;
                            orientation = ORIENTATION_LEFT;
                            setStateQ1();
                            setVelocityX(-VELOCITY_X);
                        }
                        break;
                    case ACTION_RIGHT:
                        if(!actionRight) {
                            actionRight = true;
                            actionJump = false;
                            orientation = ORIENTATION_RIGHT;
                            setStateQ1();
                            setVelocityX(VELOCITY_X);
                        }
                        break;
                    case ACTION_UP:
                        actionUp = true;
                        break;
                    case ACTION_DOWN:
                        actionDown = true;
                        break;
                    case -ACTION_LEFT:
                    case -ACTION_RIGHT:
                        actionLeft = false;
                        actionRight = false;
                        setVelocityX(0);
                        break;
                    case -ACTION_UP:
                    case -ACTION_DOWN:
                        actionUp = false;
                        actionDown = false;
                        break;
                }
                break;
            case STATE_Q2:
                switch (action){
                    case -ACTION_LEFT:
                    case -ACTION_RIGHT:
                        setStateQ0();
                        break;
                    case ACTION_UP:
                        if(!actionUp) {
                            actionUp = true;
                            setStateQ2();
                        }
                        break;
                    case ACTION_DOWN:
                        if(!actionDown) {
                            actionDown = true;
                            setStateQ2();
                        }
                        break;
                    case -ACTION_UP:
                    case -ACTION_DOWN:
                        resetActionsUpDown();
                        setStateQ2();
                        break;
                    case ACTION_JUMP:
                        setStateQ1();
                        setVelocityY(VELOCITY_Y);
                        break;
                }
                break;
            case STATE_Q3:
                switch (action) {
                    case -ACTION_DOWN:
                        setStateQ0();
                        break;
                    case ACTION_JUMP:
                        setStateQ1();
                        setVelocityY(VELOCITY_Y);
                        break;
                }
                break;
            case STATE_Q4:
                switch (action) {
                    case -ACTION_UP:
                        setStateQ0();
                        break;
                    case ACTION_JUMP:
                        setStateQ1();
                        setVelocityY(VELOCITY_Y);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        switch (state){
            case STATE_Q2:
                updateLeftRight();
                break;
            case STATE_Q1:
                setRelativeY(getRelativeY() + getVelocityY());
                setVelocityY(getVelocityY() + GRAVEDAD);
                updateLeftRight();
                if(getVelocityY() < -VELOCITY_Y){
                    actionJump = false;
                    state = STATE_Q0;
                    if(actionLeft){
                        actionLeft = false;
                        setAction(ACTION_LEFT);
                    }else if(actionRight){
                        actionRight = false;
                        setAction(ACTION_RIGHT);
                    }else if(actionUp){
                        actionUp = false;
                        setAction(ACTION_UP);
                    }else if(actionDown) {
                        actionDown = false;
                        setAction(ACTION_DOWN);
                    }else{
                        setStateQ0();
                    }
                }
        }
        super.onManagedUpdate(pSecondsElapsed);
    }

    public void resetActionsAndStates(){
        Log.i("Contra", "Reset Actions, State : " + state);
        switch (state){
            case STATE_Q0:
                break;
            case STATE_Q1:
                resetActions();
                break;
            case STATE_Q2:
            case STATE_Q3:
            case STATE_Q4:
                setStateQ0();
                break;
        }
        activePointerID = TouchEvent.INVALID_POINTER_ID;
    }

    public void setStateQ0(){
        state = STATE_Q0;
        switch (orientation){
            case ORIENTATION_LEFT:
                stopAnimation(7);
                break;
            case ORIENTATION_RIGHT:
                stopAnimation(8);
                break;
        }
        resetActions();
        setVelocityX(0);
    }

    public void setStateQ1(){
        if(!actionJump) {
            actionJump = true;
            state = STATE_Q1;
            if(orientation == ORIENTATION_RIGHT) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 56, 59, true);
            }else{
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{55, 54, 53, 52}, true);
            }
        }
    }

    public void setStateQ2(){
        if(actionLeft) {
            resetActionsLeftRight();
            actionLeft = true;
            orientation = ORIENTATION_LEFT;
            if(actionUp) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{21, 20, 19, 18, 17, 16}, true);
            }else if(actionDown) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{37, 36, 35, 34, 33, 32}, true);
            }else{
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{5, 4, 3, 2, 1, 0}, true);
            }
            setVelocityX(-VELOCITY_X);
        }else if(actionRight) {
            resetActionsLeftRight();
            actionRight = true;
            orientation = ORIENTATION_RIGHT;
            if(actionUp){
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 26, 31, true);
            }else if(actionDown){
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 42, 47, true);
            }else{
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 10, 15, true);
            }
            setVelocityX(VELOCITY_X);
        }
        state = STATE_Q2;
    }

    public void setStateQ3(){
        if(!actionDown){
            resetActionsUpDown();
            actionDown = true;
            state = STATE_Q3;
            if(orientation == ORIENTATION_RIGHT) {
                stopAnimation(40);
            }else{
                stopAnimation(39);
            }
            setVelocityX(0);
        }
    }

    public void setStateQ4(){
        if(!actionUp){
            resetActionsUpDown();
            actionUp = true;
            state = STATE_Q4;
            if(orientation == ORIENTATION_RIGHT) {
                stopAnimation(24);
            }else{
                stopAnimation(23);
            }
            setVelocityX(0);
        }
    }

    private void updateLeftRight(){
        if(getVelocityX() != 0){
            setRelativeX(getRelativeX() + getVelocityX());
            if(moveLayerBackSprite) {
                if (getRelativeX() < (crp.getRight() - crp.getLeft()) / 2) {
                    parallaxLayerBackSprite.setX(crp.getLeft());
                } else if (getRelativeX() > parallaxLayerBackSprite.getWidth() - (crp.getRight() - crp.getLeft()) / 2) {
                    parallaxLayerBackSprite.setX(crp.getRight() - parallaxLayerBackSprite.getWidth());
                } else {
                    parallaxLayerBackSprite.setX((crp.getLeft() + crp.getRight()) / 2 - getRelativeX());
                }
            }
        }
    }

    public void resetActions(){
        actionLeft = false;
        actionRight = false;
        actionDown = false;
        actionUp = false;
    }

    public void resetActionsLeftRight(){
        actionLeft = false;
        actionRight = false;
    }
    public void resetActionsUpDown(){
        actionDown = false;
        actionUp = false;
    }

    public void initSelectBoton(){
        selectBoton = -1;
    }

    public synchronized void setAction(int action, TouchEvent touchEvent){
        if(touchEvent.isActionDown() || touchEvent.isActionMove()){
            setAction(action);
            activePointerID = ACTIVE_POINTER_ID;
        } else if (touchEvent.isActionUp() || touchEvent.isActionOutside()){
            setAction(-action);
            activePointerID = TouchEvent.INVALID_POINTER_ID;
            selectBoton = -1;
        }
    }

    public synchronized void setAction(TouchEvent touchEvent, float x, float y, float width, float height){
        if(touchEvent.getPointerID() > 0){
            return;
        }
        int div = 3;
        float m = width / div;
        float n = height / div;
        int i = (int)(x / m);
        int j = (int)(y / n);
        int selectBotonAux = i + div * j;
        switch (selectBotonAux){
            case 0: case 1: case 2: case 3: case 5: case 6: case 7: case 8:
                break;
            default:
                selectBotonAux = -1;
                break;
        }
        Log.i("Contra", "Select: " + selectBoton);
        if(selectBoton < 0) {
            if(selectBotonAux != -1) {
                if (touchEvent.isActionDown() || touchEvent.isActionMove()) {
                    pressBoton(selectBotonAux);
                    activePointerID = ACTIVE_POINTER_ID;
                }
            }else{
                resetActionsAndStates();
            }
            selectBoton = selectBotonAux;
        }else if(selectBoton >= 0){
            if(selectBotonAux != -1) {
                if (selectBoton == selectBotonAux) {
                    if (touchEvent.isActionUp() || touchEvent.isActionOutside()) {
                        unpressBoton(selectBoton);
                        activePointerID = TouchEvent.INVALID_POINTER_ID;
                        selectBoton = -1;
                    }
                } else {
                    if (touchEvent.isActionMove()) {
                        unpressBoton(selectBoton);
                        pressBoton(selectBotonAux);
                        selectBoton = selectBotonAux;
                        activePointerID = ACTIVE_POINTER_ID;
                    }
                }
            }else{
                if (touchEvent.isActionMove()) {
                    unpressBoton(selectBoton);
                    activePointerID = TouchEvent.INVALID_POINTER_ID;
                    selectBoton = -1;
                }
            }
        }
    }

    public void pressBoton(int selectBotonAux){
        switch (selectBotonAux) {
            case 0:
                setAction(ACTION_LEFT);
                setAction(ACTION_DOWN);
                break;
            case 1:
                setAction(ACTION_DOWN);
                break;
            case 2:
                setAction(ACTION_RIGHT);
                setAction(ACTION_DOWN);
                break;
            case 3:
                setAction(ACTION_LEFT);
                break;
            case 5:
                setAction(ACTION_RIGHT);
                break;
            case 6:
                setAction(ACTION_LEFT);
                setAction(ACTION_UP);
                break;
            case 7:
                setAction(ACTION_UP);
                break;
            case 8:
                setAction(ACTION_RIGHT);
                setAction(ACTION_UP);
                break;
        }
    }

    public void unpressBoton(int selectBotonAux){
        switch (selectBotonAux) {
            case 0:
                setAction(-ACTION_LEFT);
                setAction(-ACTION_DOWN);
                break;
            case 1:
                setAction(-ACTION_DOWN);
                break;
            case 2:
                setAction(-ACTION_RIGHT);
                setAction(-ACTION_DOWN);
                break;
            case 3:
                setAction(-ACTION_LEFT);
                break;
            case 5:
                setAction(-ACTION_RIGHT);
                break;
            case 6:
                setAction(-ACTION_LEFT);
                setAction(-ACTION_UP);
                break;
            case 7:
                setAction(-ACTION_UP);
                break;
            case 8:
                setAction(-ACTION_RIGHT);
                setAction(-ACTION_UP);
                break;
        }
    }

    public void addBullet(){
        this.layerBullets.attachChild(new Bala(crp, parallaxLayerBackSprite, this, mBulletTextureRegion, getVertexBufferObjectManager()));
    }

    public Sprite getParallaxLayerBackSprite() {
        return parallaxLayerBackSprite;
    }

    public void setParallaxLayerBackSprite(Sprite parallaxLayerBackSprite) {
        this.parallaxLayerBackSprite = parallaxLayerBackSprite;
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
        setY(relativeY + crp.getBottom() +  PADDING);
    }

    public int getActivePointerID() {
        return activePointerID;
    }

    public void setActivePointerID(int activePointerID) {
        this.activePointerID = activePointerID;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public boolean isMoveLayerBackSprite() {
        return moveLayerBackSprite;
    }

    public void setMoveLayerBackSprite(boolean moveLayerBackSprite) {
        this.moveLayerBackSprite = moveLayerBackSprite;
    }

    public int getState() {
        return state;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean isActionLeft() {
        return actionLeft;
    }

    public boolean isActionRight() {
        return actionRight;
    }

    public boolean isActionDown() {
        return actionDown;
    }

    public boolean isActionUp() {
        return actionUp;
    }

    public boolean isActionJump() {
        return actionJump;
    }
}
