package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 01/07/2015.
 */
public class PersonajePerro extends Actor{

    public static final float CHOQUE_X_MIN = -18.5f;
    public static final float CHOQUE_X_MAX =  17.5f;
    public static final float CHOQUE_Y_MIN = -15.0f;
    public static final float CHOQUE_Y_MAX =   7.0f;

    public static final int STATE_Q0 = 0; // Comiendo
    public static final int STATE_Q1 = 1; // Corriendo
    public static final int STATE_Q2 = 2; // Muerto

    protected float destanciaMedia;

    //Efecto de muerte
    protected float vx;
    protected float vy;
    protected float a;

    public PersonajePerro(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void init(float relativeX, float relativeY) {
        super.init(relativeX, relativeY);
        destanciaMedia = BotFactory.DISCTANCIA_MEDIA;
        setStateQ0();
    }

    public void setAction(int action) {
        switch (state){
            case STATE_Q0:
                switch (action){
                    case ACTION_LEFT:
                        actionLeft = true;
                        orientation = ORIENTATION_LEFT;
                        setStateQ1();
                        break;
                    case ACTION_RIGHT:
                        actionRight = true;
                        orientation = ORIENTATION_RIGHT;
                        setStateQ1();
                        break;
                }
                break;
            case STATE_Q1:
                switch (action){
                    case ACTION_DIE:
                        setStateQ2();
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
                    if(enemigo.getRelativeX() - getRelativeX() >= destanciaMedia){
                        setAction(ACTION_RIGHT);
                    }
                    break;
                case STATE_Q1:
                    if(getX() - getWidth() / 2 >= escenario.getCropResolutionPolicy().getRight()){
                        inactivar();
                        despuesDeMorir();
                    }
                    if(!flag1){
                        if(validarCruze(enemigo.getMinMaxXY())){
                            if(enemigo.restarVidaOMorir(15.0f)) {
                                enemigo.setAction(Actor.ACTION_DIE);
                                this.asesinar(enemigo);
                            }
                            flag1 = true;
                        }
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
                                    stopAnimation(0);
                                } else {
                                    stopAnimation(13);
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
        long ft = FRAME_TIME;
        long[] fts = new long[]{ft,ft*3,ft,ft*3,ft,ft*3,ft,ft*3,ft,ft*3,ft*4};
        if(orientation == ORIENTATION_LEFT){
            animate(fts, new int[]{4,5,4,5,4,5,4,5,4,5,6}, true);
        }else{
            animate(fts, new int[]{9, 8, 9, 8, 9, 8, 9, 8, 9, 8, 7}, true);
        }
        resetActions();
        state = STATE_Q0;
        setRelativeY(0);
    }

    protected void setStateQ1(){
        float k = 1.75f;
        long f = (FRAME_TIME * 4) / 5;
        if(orientation == ORIENTATION_LEFT){
            setVelocityX(-VELOCITY_X*k);
            animate(new long[]{f,f,f}, new int[]{3, 2, 1}, true);
        }else{
            setVelocityX(VELOCITY_X*k);
            animate(new long[]{f, f, f}, 10, 12, true);
        }
        state = STATE_Q1;
        setRelativeY(getRelativeY());
    }

    protected void setStateQ2() {
        if (!isActionDie()) {
            setVelocityX(0);
            actionDie = true;
            flag0 = false;
            count1 = 0;
            vx = 1.5f;
            vy = 1.5f;
            a = -0.125f;
            vx = orientation == ORIENTATION_RIGHT ? -vx : vx;
            a = orientation == ORIENTATION_RIGHT ? -a : a;
            if (orientation == ORIENTATION_LEFT) {
                stopAnimation(14);
            } else {
                stopAnimation(15);
            }
            setRelativeX(getRelativeX() + vx * 4);
            setRelativeY(getRelativeY() + vy * 4);
            state = STATE_Q2;
        }
    }

    @Override
    protected void despuesDeMorir() {
        super.despuesDeMorir();
        explosion(getX(), getY());
    }

    protected void asesinar(Actor victima) {
    }

    public PosicionYVelocidad getPosicionYVelocidadDeBala() {
        return null;
    }

    public boolean colisionBala(Bala bala) {
        if(state == STATE_Q0){
            return false;
        }else{
            return validarColision(CHOQUE_X_MIN, CHOQUE_X_MAX, CHOQUE_Y_MIN, CHOQUE_Y_MAX, bala);
        }
    }

    public boolean restarVidaOMorir(float danio) {
        if(state == STATE_Q0){
            return false;
        }else{
            return true;
        }
    }

    public MinMaxXY getMinMaxXY() {
        MinMaxXY mXY = new MinMaxXY(getX() + CHOQUE_X_MIN, getX() + CHOQUE_X_MAX, getY() + CHOQUE_Y_MIN, getY() + CHOQUE_Y_MAX);
        return mXY;
    }

    @Override
    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        setY(relativeY + escenario.getCropResolutionPolicy().getBottom() + Escenario.PISO_ALTO + (state == STATE_Q0 ? - 3 : -13));
    }
}
