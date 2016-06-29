package com.proyectosfisi.game.contrawithbots;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 27/06/2015.
 */
public class PersonajeCorredor extends Actor{

    // Stand, L&R
    public static final float MOVING_BALA_X[] = new float[]{-12.0f,-14.5f};
    public static final float MOVING_BALA_Y[] = new float[]{  6.5f,  6.5f};

    public static final float CHOQUE_X_MIN = -6.5f;
    public static final float CHOQUE_X_MAX =  6.5f;
    public static final float CHOQUE_Y_MIN = -20.0f;
    public static final float CHOQUE_Y_MAX =  19.0f;

    public static final int STATE_Q0 = 0; // Reposo
    public static final int STATE_Q1 = 1; // Corriendo
    public static final int STATE_Q2 = 2; // Muerto

    protected float destanciaMedia;
    protected float count2;

    //Efecto de muerte
    protected float vx;
    protected float vy;
    protected float a;

    protected float vida;

    public PersonajeCorredor(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void init(float relativeX, float relativeY) {
        super.init(relativeX, relativeY);
        destanciaMedia = BotFactory.DISCTANCIA_MEDIA + BotFactory.DISCTANCIA_MEDIA_ERROR - (float)(2*Math.random()*BotFactory.DISCTANCIA_MEDIA_ERROR);
        count0 = (0.5f + ((float)(Math.random()/2))) * BotFactory.BALA_TIME;
        vida = Vida.VIDA_DEFAULT;
        setStateQ0();
    }

    public void setAction(int action) {
        switch (state) {
            case STATE_Q0:
                switch (action) {
                    case ACTION_LEFT:
                        resetActionsLeftRight();
                        orientation = ORIENTATION_LEFT;
                        actionLeft = true;
                        setStateQ1();
                        break;
                    case ACTION_RIGHT:
                        resetActionsLeftRight();
                        orientation = ORIENTATION_RIGHT;
                        actionRight = true;
                        setStateQ1();
                        break;
                    case ACTION_SHOOT:
                        shoot();
                        break;
                    case ACTION_DIE:
                        setStateQ2();
                        break;
                }
                break;
            case STATE_Q1:
                switch (action) {
                    case -ACTION_LEFT:
                        actionLeft = false;
                        setStateQ0();
                        break;
                    case -ACTION_RIGHT:
                        actionRight = false;
                        setStateQ0();
                        break;
                    case ACTION_SHOOT:
                        shoot();
                        break;
                    case ACTION_DIE:
                        setStateQ2();
                        break;
                }
        }
    }


    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if (!escenario.isPausa()) {
            Actor enemigo = getEnemigos().get(0);
            switch (state) {
                case STATE_Q0:
                    if (Math.abs(getRelativeX() - enemigo.getRelativeX()) > destanciaMedia) {
                        int k = 1;
                        if (getRelativeX() > enemigo.getRelativeX()) {
                            setAction(ACTION_LEFT);
                            k = -1;
                        } else {
                            setAction(ACTION_RIGHT);
                        }
                        float k2 = 1.20f;
                        if(validarEnDentroDeEscena()){
                            k2 = 1;
                        }
                        setVelocityX(k * VELOCITY_X * k2);
                    } else {
                        if (getRelativeX() > enemigo.getRelativeX() && getOrientation() == ORIENTATION_RIGHT) {
                            setOrientation(ORIENTATION_LEFT);
                            setStateQ0();
                        }
                        if (getRelativeX() < enemigo.getRelativeX() && getOrientation() == ORIENTATION_LEFT) {
                            setOrientation(ORIENTATION_RIGHT);
                            setStateQ0();
                        }
                    }
                    break;
                case STATE_Q1:
                    if (Math.abs(getRelativeX() - enemigo.getRelativeX()) <= destanciaMedia) {
                        setStateQ0();
                    }
                    break;
                case STATE_Q2:
                    count1 += pSecondsElapsed;
                    if (count1 > 0.25) {
                        inactivar();
                        despuesDeMorir();
                        count1 = -1;
                    } else if (count1 >= 0) {
                        setRelativeX(getRelativeX() + vx);
                        setRelativeY(getRelativeY() + vy);
                        vx += a;
                        vy += -Math.abs(a);
                        if(count1 > 0.0625) {
                            if (!flag0) {
                                if (orientation == ORIENTATION_LEFT) {
                                    stopAnimation(5);
                                } else {
                                    stopAnimation(8);
                                }
                                flag0 = true;
                            }
                        }
                    }
                    break;
            }
            //Disparo
            if(!isActionDie()){
                count0 += pSecondsElapsed;
                if (count0 > BotFactory.BALA_TIME) {
                    setAction(ACTION_SHOOT);
                    count0 = 0;
                }
            }
            //Efecto de iluminacion
            if(state == STATE_Q0 || state == STATE_Q1){
                if(count2 > 0.0625){
                    if(getCurrentTileIndex() >= 14){
                        stopAnimation(getCurrentTileIndex() - 14);
                    }
                    count2 = -1;
                }else if(count2 >=0){
                    count2 += pSecondsElapsed;
                }
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    protected void setStateQ0(){
        switch (orientation) {
            case ORIENTATION_LEFT:
                stopAnimation(1);
                break;
            case ORIENTATION_RIGHT:
                stopAnimation(12);
                break;
        }
        resetActions();
        setVelocityX(0);
        state = STATE_Q0;
    }

    protected void setStateQ1(){
        if(actionLeft){
            animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{4, 3, 2}, true);
        }else{
            animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME}, 9, 11, true);
        }
        state = STATE_Q1;
    }

    protected void setStateQ2(){
        if(!isActionDie()) {
            setVelocityX(0);
            actionDie = true;
            flag0 = false;
            count1 = 0;
            vx = 1.5f;
            vy = 1.5f;
            a = -0.125f;
            vx = orientation == ORIENTATION_RIGHT ? -vx : vx;
            a = orientation == ORIENTATION_RIGHT ? -a : a;
            if(orientation == ORIENTATION_LEFT) {
                stopAnimation(6);
            } else {
                stopAnimation(7);
            }
            setRelativeX(getRelativeX() + vx * 4);
            setRelativeY(getRelativeY() + vy * 4);
            state = STATE_Q2;
        }
    }

    @Override
    protected void despuesDeMorir(){
        super.despuesDeMorir();

        explosion(getX(), getY());

        float top = escenario.getFillCropResolutionPolicy().getTop();
        float bottom = escenario.getFillCropResolutionPolicy().getBottom();
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float medio = (right - left) / 2;
        float alto = top - bottom;
        float backLayerX = escenario.getParallaxX();
        float backLayerW = escenario.getParallaxWidth();

        float newX;
        if (Math.random() >= 0.5) {
            newX = (left - backLayerX + medio) + (-3.0f * alto / 2);
        } else {
            newX = (left - backLayerX + medio) + (3.0f * alto / 2);
        }
        if(newX < backLayerX + backLayerW + 1.5f * alto){
            init(newX, 0);
            activar();
        }
    }

    protected void asesinar(Actor victima){}

    public PosicionYVelocidad getPosicionYVelocidadDeBala(){
        int p;
        if(state == STATE_Q0){
            p = 0;
        }else{
            p = 1;
        }
        PosicionYVelocidad pv = new PosicionYVelocidad();
        int aux = orientation == ORIENTATION_RIGHT ? -1: 1;
        pv.setX(aux * MOVING_BALA_X[p] + getX());
        pv.setY(MOVING_BALA_Y[p] + getY());
        pv.setVx( - aux * Bala.VELOCITY_X);
        pv.setVy(0);
        return pv;
    }

    public boolean colisionBala(Bala bala){
        return validarColision(CHOQUE_X_MIN, CHOQUE_X_MAX, CHOQUE_Y_MIN, CHOQUE_Y_MAX, bala);
    }

    public boolean restarVidaOMorir(float danio) {
        vida -= danio;
        if(vida <= 0) {
            return true;
        }else{
            count2 = 0;
            if(getCurrentTileIndex() < 14){
                stopAnimation(getCurrentTileIndex() + 14);
            }
            return false;
        }
    }

    public MinMaxXY getMinMaxXY() {
        MinMaxXY mXY = new MinMaxXY(getX() + CHOQUE_X_MIN, getX() + CHOQUE_X_MAX, getY() + CHOQUE_Y_MIN, getY() + CHOQUE_Y_MAX);
        return mXY;
    }

    @Override
    protected boolean shoot() {
        if(state == STATE_Q0 || state == STATE_Q1){
            boolean shoot = super.shoot();
            if(shoot && state == STATE_Q0){
                if (orientation == ORIENTATION_LEFT) {
                    animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 0, 1, 1);
                }else{
                    animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{13, 12}, 1);
                }
            }
            return shoot;
        }
        return false;
    }

    @Override
    protected void initFlagsAndCounts() {
        super.initFlagsAndCounts();
        count2 = -1;
    }

    @Override
    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        setY(relativeY + escenario.getFillCropResolutionPolicy().getBottom() + Escenario.PISO_ALTO - 8);
    }

}
