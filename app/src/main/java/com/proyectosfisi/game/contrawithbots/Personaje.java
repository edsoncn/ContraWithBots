package com.proyectosfisi.game.contrawithbots;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by edson on 07/04/2015.
 */
public class Personaje extends AnimatedSprite {

    public static final long FRAME_TIME = 150;

    public static final int ACTION_LEFT = 101;
    public static final int ACTION_RIGHT = 102;
    public static final int ACTION_DOWN = 103;
    public static final int ACTION_UP = 104;
    public static final int ACTION_JUMP = 105;
    public static final int ACTION_DIE = 106;

    public static final int STATE_Q0 = 0; // Reposo
    public static final int STATE_Q1 = 1; // Saltando
    public static final int STATE_Q2 = 2; // Correr Izq&Der
    public static final int STATE_Q3 = 3; // Arriba
    public static final int STATE_Q4 = 4; // Abajo
    public static final int STATE_Q5 = 5; // Muerto
    public static final int STATE_Q6 = 6; // Cayendo

    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_RIGHT = 2;

    public static final float VELOCITY_X = 1.5f;
    public static final float VELOCITY_Y = 6f;
    public static final float GRAVEDAD = -0.25f;

    protected boolean actionLeft;
    protected boolean actionRight;
    protected boolean actionDown;
    protected boolean actionUp;
    protected boolean actionJump;
    protected boolean actionDie;
    protected boolean actionCayendo;

    protected boolean dead;

    protected boolean flagDead;
    protected float countDead;
    protected float countDead2;

    protected int orientation;
    protected int state;

    protected float relativeX;
    protected float relativeY;
    protected float velocityX;
    protected float velocityY;

    protected Escenario escenario;
    protected TiledTextureRegion mBulletTextureRegion; // Textura de la bala

    protected int pisoEscalon;

    protected Vida vida;
    protected ArrayList<Personaje> enemigos;

    protected float relativeXInicial;
    protected float relativeYInicial;

    public Personaje(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, 0, pTextureRegion, pVertexBufferObjectManager);

        this.escenario = escenario;
        this.mBulletTextureRegion = mBulletTextureRegion;

        enemigos = null;
        vida = new Vida(escenario.getLayerPlayer(), getX(), getY(), getWidth(), getVertexBufferObjectManager());

        relativeXInicial = relativeX;
        relativeYInicial = relativeY;
        init(relativeX, relativeY);
    }

    public void init(){
        init(relativeXInicial, relativeYInicial);
    }

    public void init(float relativeX, float relativeY){
        setRelativeX(relativeX);
        setRelativeY(relativeY);
        setVelocityX(0);
        setVelocityY(0);

        orientation = ORIENTATION_RIGHT;
        setStateQ0();
        pisoEscalon = -1;

        initFlagsAndCounts();

        setAlpha(1.0f);
        vida.init();
    }

    public synchronized void setAction(int action){
        if(!escenario.isPausa()) {
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
                            setStateQ1();
                            setVelocityY(VELOCITY_Y);
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
                                setStateQ1();
                                setVelocityX(-VELOCITY_X);
                            }
                            break;
                        case ACTION_RIGHT:
                            if (!actionRight) {
                                actionRight = true;
                                actionJump = false;
                                orientation = ORIENTATION_RIGHT;
                                setStateQ1();
                                setVelocityX(VELOCITY_X);
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
                            setStateQ1();
                            setVelocityY(VELOCITY_Y);
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
                            if(pisoEscalon <= 0) {
                                setStateQ1();
                                setVelocityY(VELOCITY_Y);
                            }else{
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
                            setStateQ1();
                            setVelocityY(VELOCITY_Y);
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
            }
        }
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(!escenario.isPausa()) {
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
                    if(!dead) {
                        if (!flagDead) {
                            int escalon = escenario.tocoPisoOEscalon(this);
                            if (escalon >= 0) { // Validamos que toco el piso
                                if (escalon > 0) {
                                    setRelativeY(Escenario.ESCALONES_ALTO[escalon - 1]);
                                } else {
                                    setRelativeY(0);
                                }
                                setVelocityX(0);
                                setVelocityY(0);
                                flagDead = true;
                                if (getOrientation() == ORIENTATION_LEFT) {
                                    stopAnimation(66);
                                } else {
                                    stopAnimation(77);
                                }
                            } else {
                                if (countDead >= 0.2) {
                                    if (getOrientation() == ORIENTATION_LEFT) {
                                        animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{70, 69, 68, 67}, true);
                                    } else {
                                        animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 73, 76, true);
                                    }
                                    countDead = -1;
                                    vida.setVisible(false);
                                } else if (countDead >= 0) {
                                    countDead += pSecondsElapsed;
                                }
                                setRelativeY(getRelativeY() + getVelocityY());
                                setVelocityY(getVelocityY() + GRAVEDAD);
                            }
                        } else {
                            if (countDead2 >= 1.5) {
                                countDead2 = -1;
                                setAlpha(0.0f);
                                dead = true;
                                despuesDeMorir();
                            } else if (countDead2 >= 0) {
                                float frac = 1.5f / 15;
                                int multiplo = (int) (10 * (countDead2 / frac));
                                if (multiplo % 2 == 0) {
                                    setAlpha(0.75f);
                                } else {
                                    setAlpha(0.25f);
                                }
                                countDead2 += pSecondsElapsed;
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
            updateLeftRight();
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    public void resetActionsAndStates(){
        switch (state){
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
                if(getOrientation() == ORIENTATION_LEFT){
                    stopAnimation(48);
                }else{
                    stopAnimation(63);
                }
                break;
        }
    }

    public void setStateQ0(){
        state = STATE_Q0;
        switch (orientation){
            case ORIENTATION_LEFT:
                stopAnimation(7);
                break;
            case ORIENTATION_RIGHT:
                stopAnimation(8);
                break;
        }
        resetActions();
        setVelocityX(0);
    }

    public void setStateQ1(){
        if(!actionJump) {
            actionJump = true;
            state = STATE_Q1;
            if(orientation == ORIENTATION_RIGHT) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 56, 59, true);
            }else{
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{55, 54, 53, 52}, true);
            }
        }
    }

    public void setStateQ2(){
        if(actionLeft) {
            resetActionsLeftRight();
            actionLeft = true;
            orientation = ORIENTATION_LEFT;
            if(actionUp) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{21, 20, 19, 18, 17, 16}, true);
            }else if(actionDown) {
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{37, 36, 35, 34, 33, 32}, true);
            }else{
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, new int[]{5, 4, 3, 2, 1, 0}, true);
            }
            setVelocityX(-VELOCITY_X);
        }else if(actionRight) {
            resetActionsLeftRight();
            actionRight = true;
            orientation = ORIENTATION_RIGHT;
            if(actionUp){
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 26, 31, true);
            }else if(actionDown){
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 42, 47, true);
            }else{
                animate(new long[]{FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME, FRAME_TIME}, 10, 15, true);
            }
            setVelocityX(VELOCITY_X);
        }
        state = STATE_Q2;
    }

    public void setStateQ3(){
        if(!actionDown){
            resetActionsUpDown();
            actionDown = true;
            state = STATE_Q3;
            if(orientation == ORIENTATION_RIGHT) {
                stopAnimation(40);
            }else{
                stopAnimation(39);
            }
            setVelocityX(0);
        }
    }

    public void setStateQ4(){
        if(!actionUp){
            resetActionsUpDown();
            actionUp = true;
            state = STATE_Q4;
            if(orientation == ORIENTATION_RIGHT) {
                stopAnimation(24);
            }else{
                stopAnimation(23);
            }
            setVelocityX(0);
        }
    }

    public void setStateQ5(){
        if(!actionDie) {
            state = STATE_Q5;
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
            flagDead = false;
            countDead = 0;
            setVelocityY(VELOCITY_Y);
        }
    }

    public void setStateQ6(){
        if(!actionCayendo){
            state = STATE_Q6;
            switch (orientation) {
                case ORIENTATION_LEFT:
                    if(actionLeft){
                        setVelocityX(-VELOCITY_X);
                        if(actionUp){
                            stopAnimation(50);
                        }else if(actionDown){
                            stopAnimation(49);
                        }else{
                            stopAnimation(48);
                        }
                    }else{
                        if(actionUp){
                            stopAnimation(65);
                        }else if(actionDown){
                            stopAnimation(64);
                        }else{
                            stopAnimation(48);
                        }
                    }
                    break;
                case ORIENTATION_RIGHT:
                    if(actionRight){
                        setVelocityX(VELOCITY_X);
                        if(actionUp){
                            stopAnimation(61);
                        }else if(actionDown){
                            stopAnimation(62);
                        }else{
                            stopAnimation(63);
                        }
                    }else{
                        if(actionUp){
                            stopAnimation(78);
                        }else if(actionDown){
                            stopAnimation(79);
                        }else{
                            stopAnimation(63);
                        }
                    }
                    break;
            }
            actionCayendo = true;
        }
    }

    protected void updateLeftRight(){
        setRelativeX(getRelativeX() + getVelocityX());
    }

    public void resetActions(){
        actionLeft = false;
        actionRight = false;
        actionDown = false;
        actionUp = false;
        actionDie = false;
        actionCayendo = false;
    }

    public void resetActionsLeftRight(){
        actionLeft = false;
        actionRight = false;
    }
    public void resetActionsUpDown(){
        actionDown = false;
        actionUp = false;
    }

    public void initFlagsAndCounts(){
        flagDead = false;
        dead = false;
        countDead = 0;
        countDead2 = 0;
    }

    protected void despuesDeMorir(){

    }

    public void shoot(){
        if(!actionDie) {
            Bala bala = BalaFactory.getInstance().getBala(escenario, mBulletTextureRegion, getVertexBufferObjectManager());
            bala.setPersonaje(this);
            bala.setEnemigos(enemigos);
            bala.initBala();
            switch (state){
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
    }

    protected void animateStateQ0(){
        if(orientation == ORIENTATION_RIGHT){
            animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{9, 8}, 1);
        }else{
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

    protected void animateStateQ4(){
        if(orientation == ORIENTATION_RIGHT) {
            animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, new int[]{25, 24}, 1);
        }else{
            animate(new long[]{Bala.FRAME_TIME_CHISPA, Bala.FRAME_TIME_CHISPA}, 22, 23, 1);
        }
    }

    public float getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
        setX(escenario.getParallaxLayerBackSprite().getX() + relativeX);
        vida.actualizarPosicionX(getX() - getWidth()/2);
    }

    public float getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        setY(relativeY + escenario.getCropResolutionPolicy().getBottom() +  Escenario.PISO_ALTO);
        vida.actualizarPosicionY(getY() + getHeight() / 2);
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public int getState() {
        return state;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean isActionLeft() {
        return actionLeft;
    }

    public boolean isActionRight() {
        return actionRight;
    }

    public boolean isActionDown() {
        return actionDown;
    }

    public boolean isActionUp() {
        return actionUp;
    }

    public boolean isActionJump() {
        return actionJump;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Vida getVida() {
        return vida;
    }

    public void setVida(Vida vida) {
        this.vida = vida;
    }

    public boolean isActionDie() {
        return actionDie;
    }

    public void setActionDie(boolean actionDie) {
        this.actionDie = actionDie;
    }

    public ArrayList<Personaje> getEnemigos() {
        return enemigos;
    }

    public void setEnemigos(ArrayList<Personaje> enemigos) {
        this.enemigos = enemigos;
    }

    public boolean isDead() {
        return dead;
    }
}
