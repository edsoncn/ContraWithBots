package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 08/07/2015.
 */
public abstract class Objeto extends Actor {

    public static final float CHOQUE_X_MIN = -7.0f;
    public static final float CHOQUE_X_MAX =  7.0f;
    public static final float CHOQUE_Y_MIN = -8.5f;
    public static final float CHOQUE_Y_MAX =  8.5f;

    public static final int STATE_Q0 = 0; // Inactivo
    public static final int STATE_Q1 = 1; // Activo

    protected int pisoEscalon = -1;

    public Objeto(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public void init(float relativeX, float relativeY) {
        super.init(relativeX, relativeY);
        setStateQ0();
    }

    public void initObjeto(float relativeX, float relativeY) {
        setVisible(false);
        setRelativeX(relativeX);
        setRelativeY(relativeY);
        setVelocityX(0);
        setVelocityY(0);
        setStateQ1();
    }

    public void setAction(int action) {
        switch (state){
            case STATE_Q1:
                switch (action){
                    case ACTION_DIE:
                        setStateQ0();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            if(state == STATE_Q1){
                Actor enemigo = getEnemigos().get(0);
                if(validarCruze(enemigo.getMinMaxXY())){
                    setStateQ0();
                    PersonajeJugador jugador = (PersonajeJugador)enemigo;
                    cogioObjeto(jugador);
                }else{
                    if (pisoEscalon < 0) {
                        pisoEscalon = escenario.tocoPisoOEscalon(this);
                        if (pisoEscalon < 0) {
                            setRelativeY(getRelativeY() + getVelocityY());
                            setVelocityY(getVelocityY() + GRAVEDAD);
                        } else {
                            count0 = 0;
                            if (pisoEscalon == 0) {
                                setRelativeY(0.0f);
                            } else {
                                setRelativeY(Escenario.ESCALONES_ALTO[pisoEscalon - 1]);
                            }
                            setVelocityY(0.0f);
                        }
                    }else{
                        if(count0 > 6){
                            setStateQ0();
                        }else{
                            count0+=pSecondsElapsed;
                        }
                    }
                }
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    protected void setStateQ0(){
        setVisible(false);
        stopAnimation(0);
        state = STATE_Q0;
    }

    protected void setStateQ1(){
        animacion();
        setVelocityY(Personaje.VELOCITY_Y * 0.64f);
        pisoEscalon = -1;
        setVisible(true);
        state = STATE_Q1;
    }

    protected abstract void cogioObjeto(PersonajeJugador jugador);

    protected abstract void animacion();

    protected void asesinar(Actor victima) {
    }

    public PosicionYVelocidad getPosicionYVelocidadDeBala() {
        return null;
    }

    public boolean colisionBala(Bala bala) {
        return false;
    }

    public boolean restarVidaOMorir(float danio) {
        return false;
    }

    public MinMaxXY getMinMaxXY() {
        MinMaxXY mXY = new MinMaxXY(getX() + CHOQUE_X_MIN, getX() + CHOQUE_X_MAX, getY() + CHOQUE_Y_MIN, getY() + CHOQUE_Y_MAX);
        return mXY;
    }

    @Override
    public void activar() {
        setIgnoreUpdate(false);
    }

    @Override
    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        setY(relativeY + escenario.getCropResolutionPolicy().getBottom() + Escenario.PISO_ALTO - 20);
    }

}
