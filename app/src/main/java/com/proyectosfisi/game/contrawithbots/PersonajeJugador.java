package com.proyectosfisi.game.contrawithbots;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by edson on 07/04/2015.
 */
public class PersonajeJugador extends Personaje {

    protected int selectBoton;
    protected int activePointerID;

    public PersonajeJugador(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);

    }

    @Override
    public void init(float relativeX, float relativeY){
        super.init(relativeX, relativeY);
        activePointerID = TouchEvent.INVALID_POINTER_ID;
        initSelectBoton();
    }

    @Override
    public void resetActionsAndStates(){
        super.resetActionsAndStates();
        activePointerID = TouchEvent.INVALID_POINTER_ID;
    }

    public void initSelectBoton(){
        selectBoton = -1;
    }

    public synchronized void setAction(int action, TouchEvent touchEvent){
        if(touchEvent.isActionDown() || touchEvent.isActionMove()){
            activePointerID = touchEvent.getPointerID();
            setAction(action);
        } else if (touchEvent.isActionUp() || touchEvent.isActionOutside()){
            setAction(-action);
            activePointerID = TouchEvent.INVALID_POINTER_ID;
            selectBoton = -1;
        }
    }

    public synchronized void setAction(TouchEvent touchEvent, float x, float y, float width, float height){
        if(touchEvent.getPointerID() != touchEvent.getPointerID()){
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
        if(selectBoton < 0) {
            if(selectBotonAux != -1) {
                if (touchEvent.isActionDown() || touchEvent.isActionMove()) {
                    pressBoton(selectBotonAux);
                    activePointerID = touchEvent.getPointerID();
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
                        activePointerID = touchEvent.getPointerID();
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

    public int getActivePointerID() {
        return activePointerID;
    }

}
