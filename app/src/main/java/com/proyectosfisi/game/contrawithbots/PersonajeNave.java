package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by edson on 05/07/2015.
 */
public class PersonajeNave extends Actor{

    public static final float CHOQUE_X_MIN = -16.0f;
    public static final float CHOQUE_X_MAX =  16.0f;
    public static final float CHOQUE_Y_MIN = -14.5f;
    public static final float CHOQUE_Y_MAX =  14.5f;

    public static final int STATE_Q0 = 0; // Volando
    public static final int STATE_Q1 = 1; // Muerto

    public static float ALTO = 128.0f;

    protected float aY;
    protected static final float V_MAX = 2.0f;

    protected int cuadrante;

    protected ObjetoBomba bomba;
    protected ObjetoVida vida;

    public PersonajeNave(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, TiledTextureRegion pTextureBombaRegion, TiledTextureRegion pTextureVidaRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
        bomba = new ObjetoBomba(escenario, 0, 0, pTextureBombaRegion, mBulletTextureRegion, pVertexBufferObjectManager);
        vida = new ObjetoVida(escenario, 0, 0, pTextureVidaRegion, mBulletTextureRegion, pVertexBufferObjectManager);
        escenario.getLayerPlayer().attachChild(bomba);
        escenario.getLayerPlayer().attachChild(vida);
    }

    @Override
    public void init(float relativeX, float relativeY) {
        super.init(relativeX, relativeY);
        cuadrante = 1;
        setStateQ0();

        if(bomba != null) {
            bomba.init(0, 0);
        }
        if(vida != null) {
            vida.init(0, 0);
        }
    }

    @Override
    public void setAction(int action) {
        switch (state){
            case STATE_Q0:
                switch (action){
                    case ACTION_DIE:
                        lanzarObjeto();
                        setStateQ1();
                        break;
                }
                break;
            case STATE_Q1:
                break;
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            switch (state) {
                case STATE_Q0:
                    if (getX() + getWidth() / 2 <= escenario.getCropResolutionPolicy().getLeft()) {
                        setStateQ1();
                    }
                    if (getVelocityY() == V_MAX || getVelocityY() == -V_MAX) {
                        aY *= -1;
                    }
                    setVelocityY(getVelocityY() + aY);
                    setRelativeY(getRelativeY() + getVelocityY());
                    break;
                case STATE_Q1:
                    if(cuadrante <= 3) {
                        Actor jugador = getEnemigos().get(0);
                        float W = escenario.getParallaxLayerBackSprite().getWidth();
                        float left = escenario.getCropResolutionPolicy().getLeft();
                        float right = escenario.getCropResolutionPolicy().getRight();
                        float div = (jugador.getRelativeX() - W / 10) / W;
                        if (div <= ((cuadrante + 1) * (W / 5) / W) && div >= (cuadrante * (W / 5) / W)) {
                            cuadrante++;
                            boolean aparecer = true;
                            if((cuadrante == 2 || cuadrante == 4) && ((PersonajeJugador)jugador).getVida().getVida() > 30){
                                aparecer = false;
                            }
                            if(aparecer) {
                                setRelativeX(jugador.getRelativeX() + (right - left + getWidth() / 2));
                                setRelativeY(ALTO);
                                setStateQ0();
                            }
                        }
                    }
                    break;
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    protected void setStateQ0(){
        animate(new long[]{FRAME_TIME, FRAME_TIME}, 0, 1, true);
        aY = 0.25f;
        velocityX = -VELOCITY_X * 0.8f;
        velocityY = V_MAX;
        setVisible(true);
        state = STATE_Q0;
    }

    protected void setStateQ1(){
        setVisible(false);
        state = STATE_Q1;
    }

    protected void lanzarObjeto(){
        if(cuadrante == 1 || cuadrante == 3){
            arrojarBombas();
        }else{
            arrojarVida();
        }
    }

    protected void arrojarVida(){
        vida.initObjeto(getRelativeX(), getRelativeY());

    }

    protected void arrojarBombas(){
        bomba.initObjeto(getRelativeX(), getRelativeY());
    }

    protected void asesinar(Actor victima) {

    }

    public PosicionYVelocidad getPosicionYVelocidadDeBala() {
        return null;
    }

    public boolean colisionBala(Bala bala) {
        if(state == STATE_Q1){
            return false;
        }else{
            return validarColision(CHOQUE_X_MIN, CHOQUE_X_MAX, CHOQUE_Y_MIN, CHOQUE_Y_MAX, bala);
        }
    }

    public boolean restarVidaOMorir(float danio) {
        if(state == STATE_Q1){
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
    public void inactivar() {
        super.inactivar();
        bomba.inactivar();
        vida.inactivar();
    }

    @Override
    public void activar() {
        super.activar();
        bomba.activar();
        vida.activar();
    }

    @Override
    public void setEnemigos(ArrayList<Actor> enemigos) {
        super.setEnemigos(enemigos);
        bomba.setEnemigos(enemigos);
        vida.setEnemigos(enemigos);
    }
}
