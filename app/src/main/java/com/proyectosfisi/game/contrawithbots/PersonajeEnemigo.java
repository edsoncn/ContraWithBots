package com.proyectosfisi.game.contrawithbots;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 28/04/2015.
 */
public class PersonajeEnemigo extends Personaje {

    protected float countBullets;
    protected float countMove;
    public static final int TIPO_CIMA = 1;
    public static final int TIPO_AGACHADO = 2;
    public static final int TIPO_NORMAL = 3;
    protected final int tipo;
    protected float destanciaMedia;

    protected int flagCima;

    public PersonajeEnemigo(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
        tipo = TIPO_NORMAL;
    }

    public PersonajeEnemigo(int tipo, Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
        this.tipo = tipo;
    }

    public void initEnemigo(){
        countBullets = (float)(0.0f - tipo * 0.2f - 0.2 * Math.random());
        countMove = 0.0f;
        flagCima = -1;
        destanciaMedia = BotFactory.DISCTANCIA_MEDIA + BotFactory.DISCTANCIA_MEDIA_ERROR - (float)(2*Math.random()*BotFactory.DISCTANCIA_MEDIA_ERROR);
    }

    @Override
    public void init(float relativeX, float relativeY){
        initEnemigo();
        super.init(relativeX, relativeY);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            if (getState() != STATE_Q5) {
                Actor enemigo = getEnemigos().get(0);
                countBullets += pSecondsElapsed;
                if (countBullets > 1.5) {
                    if(validarEnDentroDeEscena()) {
                        setAction(ACTION_SHOOT);
                    }
                    countBullets = 0;
                }
                switch (tipo) {
                    case TIPO_NORMAL:
                        countMove += pSecondsElapsed;
                        if (countMove > 0.25) {
                            if (Math.abs(getRelativeX() - enemigo.getRelativeX()) > destanciaMedia) {
                                if (getRelativeX() > enemigo.getRelativeX()) {
                                    setAction(ACTION_LEFT);
                                } else {
                                    setAction(ACTION_RIGHT);
                                }
                            } else {
                                setAction(-ACTION_LEFT);
                                setAction(-ACTION_RIGHT);
                            }
                            countMove = 0;
                        }
                        if (getRelativeX() > enemigo.getRelativeX() && getOrientation() == ORIENTATION_RIGHT) {
                            setAction(ACTION_LEFT);
                            setAction(-ACTION_LEFT);
                        }
                        if (getRelativeX() < enemigo.getRelativeX() && getOrientation() == ORIENTATION_LEFT) {
                            setAction(ACTION_RIGHT);
                            setAction(-ACTION_RIGHT);
                        }
                        break;
                    case TIPO_AGACHADO:
                        if (validarEnDentroDeEscena()) {
                            if (getRelativeX() > enemigo.getRelativeX() && getOrientation() == ORIENTATION_RIGHT) {
                                setOrientation(ORIENTATION_LEFT);
                                actionDown = false;
                                setAction(ACTION_DOWN);
                            }
                            if (getRelativeX() < enemigo.getRelativeX() && getOrientation() == ORIENTATION_LEFT) {
                                setOrientation(ORIENTATION_RIGHT);
                                actionDown = false;
                                setAction(ACTION_DOWN);
                            }
                            setAction(ACTION_DOWN);
                        }
                        break;
                    case TIPO_CIMA:
                        if (validarEnDentroDeEscena()) {
                            float dx = enemigo.getRelativeX() - getRelativeX();
                            float dy = enemigo.getRelativeY() - getRelativeY();
                            float adx = Math.abs(dx);
                            float ady = Math.abs(dy);
                            float tanPi8 = (float) Math.tan(Math.PI / 8);
                            float tan3Pi8 = (float) Math.tan((3 * Math.PI) / 8);
                            float tan = ady / adx;
                            if (getWidth() / 2 > adx && adx >= 0 && getHeight() / 2 > ady && ady >= 0) {
                                cimaDispararLeftRight(dx);
                            } else if (adx == 0) {
                                cimaDispararUpdown(dy);
                            } else {
                                if (tan <= tanPi8) {
                                    cimaDispararLeftRight(dx);
                                } else if (tan <= tan3Pi8) {
                                    cimaDispararLeftRightUpDown(dx, dy);
                                } else {
                                    cimaDispararUpdown(dy);
                                }
                            }
                        }
                        break;
                }
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    @Override
    protected void setStateQ2(){
        if(tipo == TIPO_CIMA) {
            if (actionLeft) {
                resetActionsLeftRight();
                actionLeft = true;
                orientation = ORIENTATION_LEFT;
                if (actionUp) {
                    stopAnimation(19);
                } else if (actionDown) {
                    stopAnimation(35);
                } else {
                    stopAnimation(7);
                }
            } else if (actionRight) {
                resetActionsLeftRight();
                actionRight = true;
                orientation = ORIENTATION_RIGHT;
                if (actionUp) {
                    stopAnimation(28);
                } else if (actionDown) {
                    stopAnimation(44);
                } else {
                    stopAnimation(8);
                }
            }
            state = STATE_Q2;
        } else {
            super.setStateQ2();
        }
    }

    @Override
    protected void setStateQ3(){
        if(tipo == TIPO_CIMA) {
            if (!actionDown) {
                resetActionsUpDown();
                actionDown = true;
                state = STATE_Q3;
                if (orientation == ORIENTATION_RIGHT) {
                    stopAnimation(79);
                } else {
                    stopAnimation(64);
                }
            }
        }else{
            super.setStateQ3();
        }
    }

    @Override
    protected void animateStateQ3() {
        if(tipo != TIPO_CIMA) {
            super.animateStateQ3();
        }
    }

    @Override
    protected void updateLeftRight(){
        if(tipo == TIPO_CIMA) {
            setRelativeX(getRelativeX());
        }else{
            super.updateLeftRight();
        }
    }

    protected void cimaDispararLeftRight(float dx){
        if (flagCima != 0 && flagCima != 1) {
            setStateQ0();
        }
        if (dx < 0) {
            if (flagCima != 0) {
                setAction(ACTION_LEFT);
                setAction(-ACTION_LEFT);
                flagCima = 0;
            }
        } else {
            if (flagCima != 1) {
                setAction(ACTION_RIGHT);
                setAction(-ACTION_RIGHT);
                flagCima = 1;
            }
        }
    }

    protected void cimaDispararUpdown(float dy){
        if (flagCima != 2 && flagCima != 3) {
            setStateQ0();
        }
        if (dy >= 0) {
            if (flagCima != 2) {
                setAction(-ACTION_DOWN);
                setAction(ACTION_UP);
                flagCima = 2;
            }
        } else {
            if (flagCima != 3) {
                setAction(-ACTION_UP);
                setAction(ACTION_DOWN);
                flagCima = 3;
            }
        }
    }

    protected void cimaDispararLeftRightUpDown(float dx, float dy){
        if(flagCima != -1){
            setStateQ0();
        }
        if (dx < 0) {
            setAction(ACTION_LEFT);
        } else {
            setAction(ACTION_RIGHT);
        }
        if (dy >= 0) {
            setAction(ACTION_UP);
        } else {
            setAction(ACTION_DOWN);
        }
        flagCima = -1;
    }

    @Override
    protected void despuesDeMorir(){
        if(tipo == TIPO_NORMAL){
            float right = escenario.getCropResolutionPolicy().getRight();
            float left = escenario.getCropResolutionPolicy().getLeft();
            float ancho = right - left;
            float backLayerX = escenario.getParallaxLayerBackSprite().getX();
            float backLayerW = escenario.getParallaxLayerBackSprite().getWidth();

            if((left - backLayerX) + (5.5f * ancho / 2) < backLayerW) {
                if (Math.random() >= 0.5) {
                    init((left - backLayerX) + (-3.5f * ancho / 2), 0);
                } else {
                    init((left - backLayerX) + (5.5f * ancho / 2), 0);
                }
                activar();
            }
        }
    }

    public int getTipo() {
        return tipo;
    }
}
