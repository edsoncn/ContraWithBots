package com.proyectosfisi.game.contrawithbots;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 07/04/2015.
 */
public class Personaje extends Actor {

    // L&R,    Up,   Down, L&R&U,  L&R&D,  Jump, Cayen. D
    public static final float MOVING_BALA_X[] = new float[]{18.5f,  1.0f,  22.0f, 12.0f,  12.0f,  0.0f,  -2.0f};
    public static final float MOVING_BALA_Y[] = new float[]{-1.0f, 26.5f, -22.5f, 16.5f, -18.0f, -8.5f, -25.0f};
    // L&R,     Up,   Down,  L&R&U,  L&R&D,  Jump, Cayen. D
    public static final float CHOQUE_X_MIN[] = new float[]{-10.5f, -10.5f, -20.5f, -10.5f, -10.5f,  -8.5f, -10.5f};
    public static final float CHOQUE_X_MAX[] = new float[]{  9.5f,   9.5f,  16.5f,   9.5f,   9.5f,   8.5f,   9.5f};
    public static final float CHOQUE_Y_MIN[] = new float[]{-28.0f, -28.0f, -28.0f, -28.0f, -28.0f, -17.0f, -28.0f};
    public static final float CHOQUE_Y_MAX[] = new float[]{ 13.0f,  13.0f, -12.0f,  13.0f,  13.0f,   0.0f,  13.0f};

    public static final int STATE_Q0 = 0; // Reposo
    public static final int STATE_Q1 = 1; // Saltando
    public static final int STATE_Q2 = 2; // Correr Izq&Der
    public static final int STATE_Q3 = 3; // Abajo
    public static final int STATE_Q4 = 4; // Arriba
    public static final int STATE_Q5 = 5; // Muerto
    public static final int STATE_Q6 = 6; // Cayendo

    protected int pisoEscalon;

    protected Vida vida;

    public Personaje(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);

        vida = new Vida(escenario.getLayerPlayer(), getX(), getY(), getWidth(), getVertexBufferObjectManager());
    }

    @Override
    public void init(float relativeX, float relativeY) {
        super.init(relativeX, relativeY);

        setStateQ0();
        pisoEscalon = -1;
        if(vida != null) {
            vida.init();
        }
    }

    public synchronized void setAction(int action) {
        if (!escenario.isPausa() && !isIgnoreUpdate()) {
            switch (state) {
                case STATE_Q0:
                    switch (action) {
                        case ACTION_LEFT:
                            if (!actionLeft) {
                                actionLeft = true;
                                setStateQ2();
                            }
                            break;
                        case ACTION_RIGHT:
                            if (!actionRight) {
                                actionRight = true;
                                setStateQ2();
                            }
                            break;
                        case ACTION_JUMP:
                            setVelocityY(VELOCITY_Y);
                            setStateQ1();
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
                            if (!actionLeft) {
                                actionLeft = true;
                                actionJump = false;
                                orientation = ORIENTATION_LEFT;
                                setVelocityX(-VELOCITY_X);
                                setStateQ1();
                            }
                            break;
                        case ACTION_RIGHT:
                            if (!actionRight) {
                                actionRight = true;
                                actionJump = false;
                                orientation = ORIENTATION_RIGHT;
                                setVelocityX(VELOCITY_X);
                                setStateQ1();
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
                            break;
                        case -ACTION_UP:
                        case -ACTION_DOWN:
                            actionUp = false;
                            actionDown = false;
                            break;
                    }
                    break;
                case STATE_Q2:
                    switch (action) {
                        case -ACTION_LEFT:
                        case -ACTION_RIGHT:
                            setStateQ0();
                            break;
                        case ACTION_UP:
                            if (!actionUp) {
                                actionUp = true;
                                setStateQ2();
                            }
                            break;
                        case ACTION_DOWN:
                            if (!actionDown) {
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
                            setVelocityY(VELOCITY_Y);
                            setStateQ1();
                            break;
                    }
                    break;
                case STATE_Q3:
                    switch (action) {
                        case -ACTION_DOWN:
                            setStateQ0();
                            break;
                        case ACTION_JUMP:
                            pisoEscalon = escenario.tocoPisoOEscalon(this);
                            if (pisoEscalon <= 0) {
                                setVelocityY(VELOCITY_Y);
                                setStateQ1();
                            } else {
                                setVelocityY(getVelocityY() + GRAVEDAD);
                                setRelativeY(getRelativeY() + getVelocityY());
                                actionCayendo = false;
                                setStateQ6();
                            }
                            break;
                    }
                    break;
                case STATE_Q4:
                    switch (action) {
                        case -ACTION_UP:
                            setStateQ0();
                            break;
                        case ACTION_JUMP:
                            setVelocityY(VELOCITY_Y);
                            setStateQ1();
                            break;
                    }
                    break;
                case STATE_Q6:
                    switch (action) {
                        case ACTION_UP:
                            actionUp = true;
                            actionDown = false;
                            if (actionLeft) {
                                stopAnimation(50);
                            } else if (actionRight) {
                                stopAnimation(61);
                            } else {
                                if (getOrientation() == ORIENTATION_LEFT) {
                                    stopAnimation(65);
                                } else {
                                    stopAnimation(78);
                                }
                            }
                            break;
                        case ACTION_DOWN:
                            actionUp = false;
                            actionDown = true;
                            if (actionLeft) {
                                stopAnimation(49);
                            } else if (actionRight) {
                                stopAnimation(62);
                            } else {
                                if (getOrientation() == ORIENTATION_LEFT) {
                                    stopAnimation(64);
                                } else {
                                    stopAnimation(79);
                                }
                            }
                            break;
                        case ACTION_LEFT:
                            actionLeft = true;
                            actionRight = false;
                            setVelocityX(-VELOCITY_X);
                            setOrientation(ORIENTATION_LEFT);
                            stopAnimation(48);
                            break;
                        case ACTION_RIGHT:
                            actionLeft = false;
                            actionRight = true;
                            setVelocityX(VELOCITY_X);
                            setOrientation(ORIENTATION_RIGHT);
                            stopAnimation(63);
                            break;
                        case -ACTION_UP:
                            actionUp = false;
                            if (getOrientation() == ORIENTATION_LEFT) {
                                stopAnimation(48);
                            } else {
                                stopAnimation(63);
                            }
                            break;
                        case -ACTION_DOWN:
                            actionDown = false;
                            if (getOrientation() == ORIENTATION_LEFT) {
                                stopAnimation(48);
                            } else {
                                stopAnimation(63);
                            }
                            break;
                        case -ACTION_LEFT:
                            actionLeft = false;
                            break;
                        case -ACTION_RIGHT:
                            actionRight = false;
                            break;
                    }
                    break;
            }
            //Acciones Directas
            switch (action) {
                case ACTION_SHOOT:
                    shoot();
                    break;
                case ACTION_DIE:
                    setStateQ5();
                    break;
            }
        }
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if (!escenario.isPausa()) {
            switch (state) {
                case STATE_Q1:
                    pisoEscalon = escenario.tocoPisoOEscalon(this);
                    if (pisoEscalon < 0) {
                        setRelativeY(getRelativeY() + getVelocityY());
                        setVelocityY(getVelocityY() + GRAVEDAD);
                    } else {
                        if (pisoEscalon == 0) {
                            setRelativeY(0.0f);
                        } else {
                            setRelativeY(Escenario.ESCALONES_ALTO[pisoEscalon - 1]);
                        }
                        setVelocityY(0.0f);
                        actionJump = false;
                        state = STATE_Q0;
                        if (actionLeft) {
                            actionLeft = false;
                            setAction(ACTION_LEFT);
                        } else if (actionRight) {
                            actionRight = false;
                            setAction(ACTION_RIGHT);
                        } else if (actionUp) {
                            actionUp = false;
                            setAction(ACTION_UP);
                        } else if (actionDown) {
                            actionDown = false;
                            setAction(ACTION_DOWN);
                        } else {
                            setStateQ0();
                        }
                    }
                    break;
                case STATE_Q5:
                    if (!dead) {
                        if (!flag0) {
                            int escalon = escenario.tocoPisoOEscalon(this);
                            if (escalon >= 0) { // Validamos que toco el piso
                                if (escalon > 0) {
                                    setRelativeY(Escenario.ESCALONES_ALTO[escalon - 1]);
                                } else {
                                    setRelativeY(0);
                                }
                                setVelocityX(0);
                                setVelocityY(0);
                                flag0 = true;
                                if (getOrientation() == ORIENTATION_LEFT) {
                                    stopAnimation(66);
                                } else {
                                    stopAnimation(77);
                                }
                            } else {
                                if (count0 >= 0.2) {
                                    if (getOrientation() == ORIENTATION_LEFT) {
                                        animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{70, 69, 68, 67}, true);
                                    } else {
                                        animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 73, 76, true);
                                    }
                                    count0 = -1;
                                    vida.inactivar();
                                } else if (count0 >= 0) {
                                    count0 += pSecondsElapsed;
                                }
                                setRelativeY(getRelativeY() + getVelocityY());
                                setVelocityY(getVelocityY() + GRAVEDAD);
                            }
                        } else {
                            if (count1 >= 1.5) {
                                count1 = -1;
                                setAlpha(0.0f);
                                dead = true;
                                despuesDeMorir();
                            } else if (count1 >= 0) {
                                float frac = 1.5f / 15;
                                int multiplo = (int) (10 * (count1 / frac));
                                if (multiplo % 2 == 0) {
                                    setAlpha(0.75f);
                                } else {
                                    setAlpha(0.25f);
                                }
                                count1 += pSecondsElapsed;
                            }
                        }
                    }
                    break;
                default:
                    int pisoEscalonAux = escenario.tocoPisoOEscalon(this);
                    if (pisoEscalonAux == -2) {
                        if (pisoEscalon != pisoEscalonAux) {
                            setStateQ6();
                        }
                        setRelativeY(getRelativeY() + getVelocityY());
                        setVelocityY(getVelocityY() + GRAVEDAD);
                    } else if (pisoEscalonAux >= 0) {
                        if (pisoEscalon != pisoEscalonAux) {
                            if (pisoEscalonAux == 0) {
                                setRelativeY(0.0f);
                            } else {
                                setRelativeY(Escenario.ESCALONES_ALTO[pisoEscalonAux - 1]);
                            }
                            setVelocityY(0.0f);
                            actionCayendo = false;
                            state = STATE_Q0;
                            if (actionLeft) {
                                actionLeft = false;
                                setAction(ACTION_LEFT);
                            } else if (actionRight) {
                                actionRight = false;
                                setAction(ACTION_RIGHT);
                            } else if (actionUp) {
                                actionUp = false;
                                setAction(ACTION_UP);
                            } else if (actionDown) {
                                actionDown = false;
                                setAction(ACTION_DOWN);
                            } else {
                                setStateQ0();
                            }
                        }
                    }
                    pisoEscalon = pisoEscalonAux;
                    break;
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    public void resetActionsAndStates() {
        switch (state) {
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
            case STATE_Q6:
                if (getOrientation() == ORIENTATION_LEFT) {
                    stopAnimation(48);
                } else {
                    stopAnimation(63);
                }
                break;
        }
    }

    protected void setStateQ0() {
        switch (orientation) {
            case ORIENTATION_LEFT:
                stopAnimation(7);
                break;
            case ORIENTATION_RIGHT:
                stopAnimation(8);
                break;
        }
        resetActions();
        setVelocityX(0);
        state = STATE_Q0;
    }

    protected void setStateQ1() {
        if (!actionJump) {
            actionJump = true;
            if (orientation == ORIENTATION_RIGHT) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 56, 59, true);
            } else {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{55, 54, 53, 52}, true);
            }
            state = STATE_Q1;
        }
    }

    protected void setStateQ2() {
        if (actionLeft) {
            resetActionsLeftRight();
            actionLeft = true;
            orientation = ORIENTATION_LEFT;
            if (actionUp) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{21, 20, 19, 18, 17, 16}, true);
            } else if (actionDown) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{37, 36, 35, 34, 33, 32}, true);
            } else {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{5, 4, 3, 2, 1, 0}, true);
            }
            setVelocityX(-VELOCITY_X);
        } else if (actionRight) {
            resetActionsLeftRight();
            actionRight = true;
            orientation = ORIENTATION_RIGHT;
            if (actionUp) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 26, 31, true);
            } else if (actionDown) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 42, 47, true);
            } else {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 10, 15, true);
            }
            setVelocityX(VELOCITY_X);
        }
        state = STATE_Q2;
    }

    protected void setStateQ3() {
        if (!actionDown) {
            resetActionsUpDown();
            actionDown = true;
            if (orientation == ORIENTATION_RIGHT) {
                stopAnimation(40);
            } else {
                stopAnimation(39);
            }
            setVelocityX(0);
            state = STATE_Q3;
        }
    }

    protected void setStateQ4() {
        if (!actionUp) {
            resetActionsUpDown();
            actionUp = true;
            if (orientation == ORIENTATION_RIGHT) {
                stopAnimation(24);
            } else {
                stopAnimation(23);
            }
            setVelocityX(0);
            state = STATE_Q4;
        }
    }

    protected void setStateQ5() {
        if (!actionDie) {
            resetActions();
            switch (orientation) {
                case ORIENTATION_LEFT:
                    stopAnimation(71);
                    setVelocityX(VELOCITY_X);
                    break;
                case ORIENTATION_RIGHT:
                    stopAnimation(72);
                    setVelocityX(-VELOCITY_X);
                    break;
            }
            actionDie = true;
            flag0 = false;
            count0 = 0;
            setVelocityY(VELOCITY_Y);
            state = STATE_Q5;
        }
    }

    protected void setStateQ6() {
        if (!actionCayendo) {
            switch (orientation) {
                case ORIENTATION_LEFT:
                    if (actionLeft) {
                        setVelocityX(-VELOCITY_X);
                        if (actionUp) {
                            stopAnimation(50);
                        } else if (actionDown) {
                            stopAnimation(49);
                        } else {
                            stopAnimation(48);
                        }
                    } else {
                        if (actionUp) {
                            stopAnimation(65);
                        } else if (actionDown) {
                            stopAnimation(64);
                        } else {
                            stopAnimation(48);
                        }
                    }
                    break;
                case ORIENTATION_RIGHT:
                    if (actionRight) {
                        setVelocityX(VELOCITY_X);
                        if (actionUp) {
                            stopAnimation(61);
                        } else if (actionDown) {
                            stopAnimation(62);
                        } else {
                            stopAnimation(63);
                        }
                    } else {
                        if (actionUp) {
                            stopAnimation(78);
                        } else if (actionDown) {
                            stopAnimation(79);
                        } else {
                            stopAnimation(63);
                        }
                    }
                    break;
            }
            actionCayendo = true;
            state = STATE_Q6;
        }
    }

    protected void despuesDeMorir(){}

    protected void asesinar(Actor victima){}

    @Override
    protected boolean shoot() {
        boolean shoot = super.shoot();
        if (shoot) {
            switch (state) {
                case STATE_Q0:
                    animateStateQ0();
                    break;
                case STATE_Q3:
                    animateStateQ3();
                    break;
                case STATE_Q4:
                    animateStateQ4();
                    break;
            }
        }
        return shoot;
    }

    public PosicionYVelocidad getPosicionYVelocidadDeBala(){
        int pi = 0;
        float velocityX = 0.0f;
        float velocityY = 0.0f;
        switch (getState()){
            case Personaje.STATE_Q0:
                velocityY = 0.0f;
                if(getOrientation() == ORIENTATION_LEFT){
                    velocityX = -Bala.VELOCITY_X;
                }else {
                    velocityX = Bala.VELOCITY_X;
                }
                break;
            case Personaje.STATE_Q1:
                pi = 5;
                if(isActionLeft()){
                    velocityX = -Bala.VELOCITY_X;
                }else if(isActionRight()){
                    velocityX = Bala.VELOCITY_X;
                }
                if(isActionUp()){
                    velocityY = Bala.VELOCITY_Y;
                }else if(isActionDown()){
                    velocityY = -Bala.VELOCITY_Y;
                }else{
                    velocityY = 0.0f;
                    if (getOrientation() == ORIENTATION_LEFT){
                        velocityX = -Bala.VELOCITY_X;
                    }else {
                        velocityX = Bala.VELOCITY_X;
                    }
                }
                break;
            case Personaje.STATE_Q6:
                if(isActionUp()){
                    if(!isActionLeft() && !isActionRight()){
                        pi = 1;
                    }else{
                        pi = 3;
                    }
                    velocityY = Bala.VELOCITY_Y;
                }else if(isActionDown()){
                    if(!isActionLeft() && !isActionRight()){
                        pi = 6;
                    }else{
                        pi = 4;
                    }
                    velocityY = -Bala.VELOCITY_Y;
                }else{
                    pi = 0;
                    velocityY = 0.0f;
                    if(getOrientation() == ORIENTATION_LEFT){
                        velocityX = -Bala.VELOCITY_X;
                    }else {
                        velocityX = Bala.VELOCITY_X;
                    }
                }
                if(isActionLeft()){
                    velocityX = -Bala.VELOCITY_X;
                }else if(isActionRight()){
                    velocityX = Bala.VELOCITY_X;
                }
                break;
            case Personaje.STATE_Q2:
                if(isActionUp()){
                    pi = 3;
                    velocityY = Bala.VELOCITY_Y;
                }else if(isActionDown()){
                    pi = 4;
                    velocityY = -Bala.VELOCITY_Y;
                }else{
                    pi = 0;
                    velocityY = 0.0f;
                }
                if(getOrientation() == ORIENTATION_LEFT){
                    velocityX = -Bala.VELOCITY_X;
                }else {
                    velocityX = Bala.VELOCITY_X;
                }
                break;
            case Personaje.STATE_Q3: // Down
                if(this instanceof PersonajeEnemigo && ((PersonajeEnemigo)this).tipo == PersonajeEnemigo.TIPO_CIMA){
                    pi = 6;
                    velocityY = -Bala.VELOCITY_Y;
                }else {
                    pi = 2;
                    if (getOrientation() == ORIENTATION_LEFT) {
                        velocityX = -Bala.VELOCITY_X;
                    } else {
                        velocityX = Bala.VELOCITY_X;
                    }
                    velocityY = 0.0f;
                }
                break;
            case Personaje.STATE_Q4: // Up
                pi = 1;
                velocityX = 0.0f;
                velocityY = Bala.VELOCITY_Y;
                break;
        }
        PosicionYVelocidad pv = new PosicionYVelocidad();
        if(getOrientation() == ORIENTATION_LEFT){
            pv.setX(-MOVING_BALA_X[pi]);
        }else{
            pv.setX(MOVING_BALA_X[pi]);
        }
        pv.setY(MOVING_BALA_Y[pi]);
        pv.setVx(velocityX);
        pv.setVy(velocityY);
        if(getState() == STATE_Q1){
            pv.setX( pv.getX() + (velocityX != 0 ? (-velocityX/Math.abs(velocityX)) : 0) * MOVING_BALA_Y[pi]);
            pv.setY( pv.getY() + (velocityY != 0 ? (-velocityY/Math.abs(velocityY)) : 0) * MOVING_BALA_Y[pi]);
        }
        pv.setX( pv.getX() + getX());
        pv.setY( pv.getY() + getY());
        return pv;
    }

    private int positionPersonaje(){
        int pi = 0;
        switch (getState()){
            case Personaje.STATE_Q0:
                break;
            case Personaje.STATE_Q1:
                pi = 5;
                break;
            case Personaje.STATE_Q6:
                if(isActionUp()){
                    if(!isActionLeft() && !isActionRight()){
                        pi = 1;
                    }else{
                        pi = 3;
                    }
                }else if(isActionDown()){
                    if(!isActionLeft() && !isActionRight()){
                        pi = 6;
                    }else{
                        pi = 4;
                    }
                }else{
                    pi = 0;
                }
                break;
            case Personaje.STATE_Q2:
                if(isActionUp()){
                    pi = 3;
                }else if(isActionDown()){
                    pi = 4;
                }else{
                    pi = 0;
                }
                break;
            case Personaje.STATE_Q3: // Down
                if(this instanceof PersonajeEnemigo && ((PersonajeEnemigo) this).getTipo() == PersonajeEnemigo.TIPO_CIMA){
                    pi = 1;
                }else {
                    pi = 2;
                }
                break;
            case Personaje.STATE_Q4: // Up
                pi = 1;
                break;
        }
        return pi;
    }

    public boolean colisionBala(Bala bala){
        int pi = positionPersonaje();
        float xMin = CHOQUE_X_MIN[pi];
        float xMax = CHOQUE_X_MAX[pi];
        float yMin = CHOQUE_Y_MIN[pi];
        float yMax = CHOQUE_Y_MAX[pi];
        return validarColision(xMin, xMax, yMin, yMax, bala);
    }

    public boolean restarVidaOMorir(float danio){
        return vida.restarVidaOMorir(danio);
    }

    protected void animateStateQ0() {
        if (orientation == ORIENTATION_RIGHT) {
            animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{9, 8}, 1);
        } else {
            animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 6, 7, 1);
        }
    }

    protected void animateStateQ3() {
        if (orientation == ORIENTATION_RIGHT) {
            animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{41, 40}, 1);
        } else {
            animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 38, 39, 1);
        }
    }

    protected void animateStateQ4() {
        if (orientation == ORIENTATION_RIGHT) {
            animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{25, 24}, 1);
        } else {
            animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 22, 23, 1);
        }
    }

    @Override
    public void inactivar(){
        vida.inactivar();
        super.inactivar();
    }

    @Override
    public void activar() {
        super.activar();
        vida.activar();
    }

    @Override
    public void setRelativeX(float relativeX) {
        super.setRelativeX(relativeX);
        if(vida != null) {
            vida.actualizarPosicionX(getX() - getWidth() / 2);
        }
    }

    @Override
    public void setRelativeY(float relativeY) {
        super.setRelativeY(relativeY);
        if(vida != null) {
            vida.actualizarPosicionY(getY() + getHeight() / 2);
        }
    }

    public Vida getVida() {
        return vida;
    }

    public void setVida(Vida vida) {
        this.vida = vida;
    }

}
