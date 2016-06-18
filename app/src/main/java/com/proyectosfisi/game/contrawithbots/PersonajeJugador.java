package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

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
    protected boolean flagGano;
    private Puntajes puntajes;
    protected boolean flagBomba;

    protected int bombas;

    public PersonajeJugador(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
        puntajes = new Puntajes(escenario.getIntro());
    }

    @Override
    public void init(float relativeX, float relativeY){
        super.init(relativeX, relativeY);
        activePointerID = TouchEvent.INVALID_POINTER_ID;
        initSelectBoton();
        if(puntajes != null){
            puntajes.resetPuntaje();
        }
        flagBomba = false;
        bombas = 1;
        actualizarBombas(0);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            if ((state == STATE_Q0 || state == STATE_Q1) && validarJuegoGanado()) {
                setStateQ0();
                gane();
            } else {
                if(flagBomba){
                    float v = 7.5f;
                    float w = (float)((v * Math.sqrt(2)) / 2);
                    explosion(getX() + v, getY()    ,  v,  0);
                    explosion(getX()    , getY() + v,  0,  v);
                    explosion(getX() - v, getY()    , -v,  0);
                    explosion(getX()    , getY() - v,  0, -v);
                    explosion(getX() + w, getY() + w,  w,  w);
                    explosion(getX() + w, getY() - w,  w, -w);
                    explosion(getX() - w, getY() + w, -w,  w);
                    explosion(getX() - w, getY() - w, -w, -w);
                    actualizarBombas(-1);
                    for(Actor a : escenario.getBotFactory().getBots()){
                        if(getX() - 150 <= a.getX() && a.getX() <= getX() + 150){
                            if(a.restarVidaOMorir(100)) {
                                a.setAction(Actor.ACTION_DIE);
                                this.asesinar(a);
                            }
                        }
                    }
                    flagBomba = false;
                }
                super.onManagedUpdate(pSecondsElapsed);
            }
        }
    }

    public boolean validarJuegoGanado(){
        float anchoEscena = escenario.getParallaxLayerBackSprite().getWidth();
        return relativeX >= anchoEscena - 0.5f * (escenario.cropResolutionPolicy.getRight() - escenario.getCropResolutionPolicy().getLeft()) &&
                escenario.getBotFactory().validaBotMuertos() &&
                relativeX >= anchoEscena - Escenario.ESCENARIO_PAGGING_RIGHT - getWidth();
    }

    @Override
    public void initFlagsAndCounts() {
        super.initFlagsAndCounts();
        flagGano = false;
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
        if(!isIgnoreUpdate()) {
            if (touchEvent.isActionDown() || touchEvent.isActionMove()) {
                activePointerID = touchEvent.getPointerID();
                setAction(action);
            } else if (touchEvent.isActionUp() || touchEvent.isActionOutside()) {
                setAction(-action);
                activePointerID = TouchEvent.INVALID_POINTER_ID;
                selectBoton = -1;
            }
        }
    }

    public synchronized void setAction(TouchEvent touchEvent, float x, float y, float width, float height){
        if(!isIgnoreUpdate()) {
            if (touchEvent.getPointerID() != touchEvent.getPointerID()) {
                return;
            }
            int div = 3;
            float m = width / div;
            float n = height / div;
            int i = (int) (x / m);
            int j = (int) (y / n);
            int selectBotonAux = i + div * j;
            switch (selectBotonAux) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                    break;
                default:
                    selectBotonAux = -1;
                    break;
            }
            if (selectBoton < 0) {
                if (selectBotonAux != -1) {
                    if (touchEvent.isActionDown() || touchEvent.isActionMove()) {
                        pressBoton(selectBotonAux);
                        activePointerID = touchEvent.getPointerID();
                    }
                } else {
                    resetActionsAndStates();
                }
                selectBoton = selectBotonAux;
            } else if (selectBoton >= 0) {
                if (selectBotonAux != -1) {
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
                } else {
                    if (touchEvent.isActionMove()) {
                        unpressBoton(selectBoton);
                        activePointerID = TouchEvent.INVALID_POINTER_ID;
                        selectBoton = -1;
                    }
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

    @Override
    protected void updateLeftRight() {
        return;
    }

    public void moverEscenaRespectoPersonaje(){
        if(getVelocityX() != 0) {
            float left = escenario.getCropResolutionPolicy().getLeft();
            float right = escenario.getCropResolutionPolicy().getRight();
            float anchoEscena = escenario.getParallaxLayerBackSprite().getWidth();
            float velocityX = getVelocityX();
            if (getX() + getVelocityX() <= left + getWidth() / 2) { // Aseguramos que el personaje no salga de la escena por la izquierda
                velocityX = left + getWidth() / 2 - getX() - getVelocityX();
            } else if (getRelativeX() + getVelocityX() >= anchoEscena - Escenario.ESCENARIO_PAGGING_RIGHT - getWidth() / 2) { // y por la derecha de la pantalla
                velocityX = anchoEscena - Escenario.ESCENARIO_PAGGING_RIGHT - getWidth() / 2 - getRelativeX() - getVelocityX();
            }
            setRelativeX(getRelativeX() + velocityX);
            if (velocityX > 0) {
                if (getRelativeX() > anchoEscena - (right - left) / 2) {
                    escenario.getParallaxLayerBackSprite().setX(right - anchoEscena);
                } else if (getX() > (left + right) / 2) {
                    escenario.getParallaxLayerBackSprite().setX((left + right) / 2 - getRelativeX());
                }
            }
        }
    }

    public synchronized void activarBomba(){
        if(bombas > 0){
            if(state != STATE_Q5) {
                flagBomba = true;
            }
        }
    }

    public void centrarEscenaAPersonaje(){
        float left = escenario.getCropResolutionPolicy().getLeft();
        float right = escenario.getCropResolutionPolicy().getRight();
        escenario.getParallaxLayerBackSprite().setX(escenario.getParallaxLayerBackSprite().getX() + (left + right) / 2 - getX());
    }

    @Override
    protected void despuesDeMorir(){
        calcularScore();
        escenario.getControles().perdiste();
    }

    protected void gane(){
        calcularScore();
        escenario.getControles().ganaste();
    }

    @Override
    protected void asesinar(Actor victima){
        Puntaje puntaje = getPuntajes().getPuntaje();
        if(puntaje != null) {
            puntaje.setBotsCaidos(puntaje.getBotsCaidos() + 1);
            int puntos = 20;
            if (victima instanceof PersonajeEnemigo) {
                int tipo = ((PersonajeEnemigo) victima).getTipo();
                switch (tipo) {
                    case PersonajeEnemigo.TIPO_AGACHADO:
                        puntos = 20;
                        break;
                    case PersonajeEnemigo.TIPO_CIMA:
                        puntos = 25;
                        break;
                    case PersonajeEnemigo.TIPO_NORMAL:
                        puntos = 15;
                        break;
                }
            }
            puntaje.setPuntaje(puntaje.getPuntaje() + puntos);
        }
    }

    protected void calcularScore(){
        float anchoEscena = escenario.getParallaxLayerBackSprite().getWidth();
        Puntaje puntaje = getPuntajes().getPuntaje();
        float avance = relativeX / (anchoEscena - Escenario.ESCENARIO_PAGGING_RIGHT - getWidth());
        if(avance >= 1){
            if(isDead()) {
                avance = 0.99f;
            }else{
                avance = 1.0f;
            }
        }
        int porcentaje = (int)(100 * avance);
        escenario.getControles().setScore(puntaje.getPuntaje() + porcentaje * 4, puntaje.getBotsCaidos(), avance);
    }

    @Override
    public boolean restarVidaOMorir(float danio) {
        boolean bool = super.restarVidaOMorir(danio);
        escenario.getControles().gettVida().setText(String.format("%3d", (int) vida.getVida()).replace(" ", "0"));
        return bool;
    }

    public void actualizarBombas(int bombas){
        this.bombas += bombas;
        escenario.getControles().gettBombas().setText(String.format("x%1d", this.bombas));
    }

    public int getActivePointerID() {
        return activePointerID;
    }

    public Puntajes getPuntajes() {
        return puntajes;
    }
}
