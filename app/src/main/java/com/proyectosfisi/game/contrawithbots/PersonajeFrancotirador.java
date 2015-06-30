package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 24/06/2015.
 */
public class PersonajeFrancotirador extends Actor {

    // L&R,    Up,   Down, L&R&U,  L&R&D
    public static final float MOVING_BALA_X[] = new float[]{-34.0f, -6.0f,  -2.0f, -23.0f, -23.0f};
    public static final float MOVING_BALA_Y[] = new float[]{  1.0f, 34.0f, -34.0f,  21.0f, -20.0f};

    public static final float CHOQUE_X_MIN = -11.0f;
    public static final float CHOQUE_X_MAX =  10.0f;
    public static final float CHOQUE_Y_MIN = -20.0f;
    public static final float CHOQUE_Y_MAX =  11.0f;

    public static final int STATE_Q0 = 0; // Reposo
    public static final int STATE_Q1 = 1; // Muerto

    protected int flagF;
    protected float count2;

    //Efecto de muerte
    protected float vx;
    protected float vy;
    protected float a;

    protected float vida;

    public PersonajeFrancotirador(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void init(float relativeX, float relativeY) {
        super.init(relativeX, relativeY);
        resetActions();
        vida = Vida.VIDA_DEFAULT;
        setStateQ0();
    }

    public void setAction(int action){
        switch (state){
            case STATE_Q0:
                switch (action){
                    case ACTION_LEFT:
                        resetActionsLeftRight();
                        orientation = ORIENTATION_LEFT;
                        actionLeft = true;
                        break;
                    case ACTION_RIGHT:
                        resetActionsLeftRight();
                        orientation = ORIENTATION_RIGHT;
                        actionRight = true;
                        break;
                    case ACTION_UP:
                        resetActionsUpDown();
                        actionUp = true;
                        break;
                    case ACTION_DOWN:
                        resetActionsUpDown();
                        actionDown = true;
                        break;
                    case -ACTION_LEFT:
                        actionLeft = false;
                        break;
                    case -ACTION_RIGHT:
                        actionRight = false;
                        break;
                    case -ACTION_UP:
                        actionUp = false;
                        break;
                    case -ACTION_DOWN:
                        actionDown = false;
                        break;
                    case ACTION_SHOOT:
                        shoot();
                        break;
                    case ACTION_DIE:
                        setStateQ1();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            switch (state) {
                case STATE_Q0:
                    if(validarEnDentroDeEscena()) {
                        //Apuntar hacia el enemigo
                        Actor enemigo = getEnemigos().get(0);
                        float dx = enemigo.getRelativeX() - getRelativeX();
                        float dy = enemigo.getRelativeY() - getRelativeY();
                        float adx = Math.abs(dx);
                        float ady = Math.abs(dy);
                        float tanPi8 = (float) Math.tan(Math.PI / 8);
                        float tan3Pi8 = (float) Math.tan((3 * Math.PI) / 8);
                        float tan = ady / adx;
                        int auxFlagF = flagF;
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
                        if (auxFlagF != flagF) {
                            setStateQ0();
                        }
                        //Disparo
                        count0 += pSecondsElapsed;
                        if (count0 > BotFactory.BALA_TIME) {
                            setAction(ACTION_SHOOT);
                            count0 = 0;
                        }
                        //Efecto de iluminacion
                        if(count2 > 0.0625){
                            if(getCurrentTileIndex() >= 24){
                                stopAnimation(getCurrentTileIndex() - 24);
                            }
                            count2 = -1;
                        }else if(count2 >=0){
                            count2 += pSecondsElapsed;
                        }
                    }
                    break;
                case STATE_Q1:
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
                                    stopAnimation(16);
                                } else {
                                    stopAnimation(19);
                                }
                                flag0 = true;
                            }
                        }
                    }
                    break;
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    protected void setStateQ0(){
        if(orientation == ORIENTATION_LEFT){
            if(isActionLeft()){
                if(isActionUp()){
                    stopAnimation(5);
                }else if(isActionDown()){
                    stopAnimation(3);
                }else{
                    stopAnimation(1);
                }
            } else {
                if(isActionUp()){
                    stopAnimation(15);
                }else if(isActionDown()){
                    stopAnimation(13);
                }else{
                    stopAnimation(1);
                }
            }
        } else {
            if(isActionRight()){
                if(isActionUp()){
                    stopAnimation(6);
                }else if(isActionDown()){
                    stopAnimation(8);
                }else{
                    stopAnimation(10);
                }
            } else {
                if(isActionUp()){
                    stopAnimation(20);
                }else if(isActionDown()){
                    stopAnimation(22);
                }else{
                    stopAnimation(10);
                }
            }
        }
        state = STATE_Q0;
    }

    protected void setStateQ1(){
        if(!isActionDie()) {
            actionDie = true;
            flag0 = false;
            vx = 1.5f;
            vy = 1.5f;
            a = -0.125f;
            vx = orientation == ORIENTATION_RIGHT ? -vx : vx;
            a = orientation == ORIENTATION_RIGHT ? -a : a;
            if(orientation == ORIENTATION_LEFT) {
                stopAnimation(17);
            } else {
                stopAnimation(18);
            }
            state = STATE_Q1;
        }
    }

    protected void despuesDeMorir(){}

    protected void asesinar(Actor victima){}

    public PosicionYVelocidad getPosicionYVelocidadDeBala(){
        // L&R,    Up,   Down, L&R&U,  L&R&D
        int p = 0;
        int vx = -1;
        int vy =  0;
        if(isActionUp()){
            if(isActionRight() || isActionLeft()){
                p = 3;
            }else{
                p = 1;
                vx = 0;
            }
            vy = 1;
        }else if(isActionDown()){
            if(isActionRight() || isActionLeft()){
                p = 4;
            }else{
                p = 2;
                vx = 0;
            }
            vy = -1;
        }
        PosicionYVelocidad pv = new PosicionYVelocidad();
        int aux = orientation == ORIENTATION_RIGHT ? -1: 1;
        pv.setX(aux * MOVING_BALA_X[p] + getX());
        pv.setY(MOVING_BALA_Y[p] + getY());
        pv.setVx(aux * vx * Bala.VELOCITY_X);
        pv.setVy(vy * Bala.VELOCITY_Y);

        return pv;
    }

    public boolean colisionBala(Bala bala){
        return validarColision(CHOQUE_X_MIN, CHOQUE_X_MAX, CHOQUE_Y_MIN, CHOQUE_Y_MAX, bala);
    }

    @Override
    protected boolean shoot() {
        boolean shoot = super.shoot();
        if (shoot) {
            if(orientation == ORIENTATION_LEFT){
                if(isActionLeft()){
                    if(isActionUp()){
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 4, 5, 1);
                    }else if(isActionDown()){
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 2, 3, 1);
                    }else{
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 0, 1, 1);
                    }
                } else {
                    if(isActionUp()){
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 14, 15, 1);
                    }else if(isActionDown()){
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 12, 13, 1);
                    }else{
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 0, 1, 1);
                    }
                }
            } else {
                if(isActionRight()){
                    if(isActionUp()){
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{7, 6}, 1);
                    }else if(isActionDown()){
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{9, 8}, 1);
                    }else{
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{11, 10}, 1);
                    }
                } else {
                    if(isActionUp()){
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{21, 20}, 1);
                    }else if(isActionDown()){
                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{23, 22}, 1);
                    }else{

                        animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{11, 10}, 1);
                    }
                }
            }
        }
        return shoot;
    }

    public boolean restarVidaOMorir(float danio){
        vida -= danio;
        if(vida <= 0) {
            return true;
        }else{
            count2 = 0;
            if(getCurrentTileIndex() < 24){
                stopAnimation(getCurrentTileIndex() + 24);
            }
            return false;
        }
    }

    protected void cimaDispararLeftRight(float dx){
        if (dx < 0) {
            if (flagF != 0) {
                resetActionsUpDown();
                setAction(ACTION_LEFT);
                flagF = 0;
            }
        } else {
            if (flagF != 1) {
                resetActionsUpDown();
                setAction(ACTION_RIGHT);
                flagF = 1;
            }
        }
    }

    protected void cimaDispararUpdown(float dy){
        if (dy >= 0) {
            if (flagF != 2) {
                resetActionsLeftRight();
                setAction(ACTION_UP);
                flagF = 2;
            }
        } else {
            if (flagF != 3) {
                resetActionsLeftRight();
                setAction(ACTION_DOWN);
                flagF = 3;
            }
        }
    }

    protected void cimaDispararLeftRightUpDown(float dx, float dy){
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
        flagF = -1;
    }

    @Override
    protected void initFlagsAndCounts() {
        super.initFlagsAndCounts();
        flagF = -2;
        count2 = -1;
    }

    @Override
    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        setY(relativeY + escenario.getCropResolutionPolicy().getBottom() + Escenario.PISO_ALTO - 8);
    }

}
