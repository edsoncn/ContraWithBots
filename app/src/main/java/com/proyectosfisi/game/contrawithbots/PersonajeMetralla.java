package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 29/06/2015.
 */
public class PersonajeMetralla extends Actor{

    // L&R
    public static final float MOVING_BALA_X = -14.5f;
    public static final float MOVING_BALA_Y =   20f;

    public static final float CHOQUE_X_MIN = -11.0f;
    public static final float CHOQUE_X_MAX =  11.0f;
    public static final float CHOQUE_Y_MIN =  -7.0f;
    public static final float CHOQUE_Y_MAX =  24.0f;

    public static final int STATE_Q0 = 0; // Reposo
    public static final int STATE_Q1 = 1; // Corriendo
    public static final int STATE_Q2 = 2; // Muerto

    protected float vida;

    public PersonajeMetralla(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void init(float relativeX, float relativeY) {
        super.init(relativeX, relativeY);
        vida = Vida.VIDA_DEFAULT;
        setStateQ0();
    }

    public void setAction(int action) {
        switch (state){
            case STATE_Q0:
                switch (action){
                    case ACTION_LEFT:
                        orientation = ORIENTATION_LEFT;
                        resetActionsLeftRight();
                        actionLeft = true;
                        break;
                    case ACTION_RIGHT:
                        orientation = ORIENTATION_RIGHT;
                        resetActionsLeftRight();
                        actionRight = true;
                        break;
                }
                break;
            case STATE_Q1:
                switch (action){
                    case ACTION_LEFT:
                        orientation = ORIENTATION_LEFT;
                        resetActionsLeftRight();
                        actionLeft = true;
                        setStateQ1();
                        break;
                    case ACTION_RIGHT:
                        orientation = ORIENTATION_RIGHT;
                        resetActionsLeftRight();
                        actionRight = true;
                        setStateQ1();
                        break;
                    case ACTION_DIE:
                        setStateQ2();
                        break;
                    case ACTION_SHOOT:
                        Log.i("Metralla", "SHOOooOOT PEEE");
                        shoot();
                        break;
                }
                break;
            case STATE_Q2:
                break;
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            Actor enemigo = getEnemigos().get(0);
            switch (state){
                case STATE_Q0:
                    if((getCurrentTileIndex() == 0 || getCurrentTileIndex() == 8) && validarEnDentroDeEscena()) {
                        if (getRelativeX() < enemigo.getRelativeX()) {
                            setAction(ACTION_RIGHT);
                        } else {
                            setAction(ACTION_LEFT);
                        }
                        setStateQ1();
                    }
                    break;
                case STATE_Q1:
                    if(flag0 && !isAnimationRunning()) {
                        if (getRelativeX() > enemigo.getRelativeX() && getOrientation() == ORIENTATION_RIGHT) {
                            setAction(ACTION_LEFT);
                        }
                        if (getRelativeX() < enemigo.getRelativeX() && getOrientation() == ORIENTATION_LEFT) {
                            setAction(ACTION_RIGHT);
                        }
                        flag1 = true;
                    }
                    if(flag1){
                        //Disparo
                        count0 += pSecondsElapsed;
                        if (count0 > BotFactory.BALA_TIME) {
                            Log.i("Metralla", "SHOOOOT");
                            setAction(ACTION_SHOOT);
                            count0 = 0;
                        }
                    }
                    break;
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    protected void setStateQ0(){
        resetActions();
        initFlagsAndCounts();
        if(orientation == ORIENTATION_LEFT){
            stopAnimation(0);
        }else{
            stopAnimation(8);
        }
        state = STATE_Q0;
    }

    protected void setStateQ1(){
        if(getCurrentTileIndex() == 0 || getCurrentTileIndex() == 8) {
            if (orientation == ORIENTATION_LEFT) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME}, 0, 2, 0);
            } else {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{8, 7, 6}, 0);
            }
            flag0 = true;
        }else{
            if(orientation == ORIENTATION_LEFT){
                stopAnimation(2);
            }else{
                stopAnimation(6);
            }
        }
        state = STATE_Q1;
    }

    protected void setStateQ2(){
        if(!actionDie){
            actionDie = true;
            stopAnimation(4);
            Explosion explosion = ExplosionFactory.getInstance().getExplosion(escenario, getVertexBufferObjectManager());
            explosion.setPosition(getX(), getY() + MOVING_BALA_Y);
            explosion.activar();
            state = STATE_Q2;
        }
    }

    protected void despuesDeMorir() {

    }

    protected void asesinar(Actor victima) {

    }

    public PosicionYVelocidad getPosicionYVelocidadDeBala() {
        PosicionYVelocidad pv = new PosicionYVelocidad();
        int aux = orientation == ORIENTATION_RIGHT ? -1: 1;
        pv.setX(aux * MOVING_BALA_X + getX() + (orientation == ORIENTATION_RIGHT ? 1 : 0));
        pv.setY(MOVING_BALA_Y + getY());
        pv.setVx(-aux * Bala.VELOCITY_X);
        pv.setVy(0);
        Log.i("BALA", "x="+getX()+",y="+getY()+",bx="+pv.getX()+",by="+pv.getY());
        return pv;
    }

    public boolean colisionBala(Bala bala) {
        if(!flag1){
            return false;
        }else{
            return validarColision(CHOQUE_X_MIN, CHOQUE_X_MAX, CHOQUE_Y_MIN, CHOQUE_Y_MAX, bala);
        }
    }

    public boolean restarVidaOMorir(float danio) {
        vida -= danio;
        if(vida <= 0) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        setY(relativeY + escenario.getCropResolutionPolicy().getBottom() + Escenario.PISO_ALTO - 28);
    }

    @Override
    protected boolean shoot() {
        boolean shoot = super.shoot();
        if(shoot){
            if (orientation == ORIENTATION_LEFT) {
                animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{3, 2}, 1);
            } else {
                animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 5, 6, 1);
            }
        }
        return shoot;
    }
}
