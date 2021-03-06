package com.proyectosfisi.game.contrawithbots;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by edson on 23/06/2015.
 */
public abstract class Actor extends AnimatedSprite {

    public static final long FRAME_TIME = 120;

    public static final int ACTION_LEFT = 101;
    public static final int ACTION_RIGHT = 102;
    public static final int ACTION_DOWN = 103;
    public static final int ACTION_UP = 104;
    public static final int ACTION_JUMP = 105;
    public static final int ACTION_DIE = 106;
    public static final int ACTION_SHOOT = 107;

    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_RIGHT = 2;

    public static final float VELOCITY_X = 1.6f;
    public static final float VELOCITY_Y = 8.0f;
    public static final float GRAVEDAD = -0.4f;

    protected boolean actionLeft;
    protected boolean actionRight;
    protected boolean actionDown;
    protected boolean actionUp;
    protected boolean actionJump;
    protected boolean actionDie;
    protected boolean actionCayendo;

    protected boolean dead;

    protected boolean flag0;
    protected boolean flag1;
    protected float count0;
    protected float count1;

    protected int orientation;
    protected int state;

    protected float relativeX;
    protected float relativeY;
    protected float velocityX;
    protected float velocityY;

    protected float relativeXInicial;
    protected float relativeYInicial;

    protected Escenario escenario;
    protected TiledTextureRegion mBulletTextureRegion; // Textura de la bala

    protected ArrayList<Actor> enemigos;

    public Actor(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, 0, pTextureRegion, pVertexBufferObjectManager);

        enemigos = null;

        this.escenario = escenario;
        this.mBulletTextureRegion = mBulletTextureRegion;

        relativeXInicial = relativeX;
        relativeYInicial = relativeY;

        init(relativeX, relativeY);
    }

    public void init() {
        init(relativeXInicial, relativeYInicial);
    }

    public void init(float relativeX, float relativeY) {
        setRelativeX(relativeX);
        setRelativeY(relativeY);
        setVelocityX(0);
        setVelocityY(0);

        orientation = ORIENTATION_RIGHT;

        initFlagsAndCounts();
        setAlpha(1.0f);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            updateLeftRight();
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    public abstract void setAction(int action);

    protected void despuesDeMorir(){
        dead = true;
    }

    protected abstract void asesinar(Actor victima);

    public abstract PosicionYVelocidad getPosicionYVelocidadDeBala();

    protected boolean shoot(){
        if(!actionDie) {
            if(validarEnDentroDeEscena()) {
                Bala bala = BalaFactory.getInstance().getBala(escenario, mBulletTextureRegion, getVertexBufferObjectManager());
                bala.setActor(this);
                bala.setEnemigos(enemigos);
                bala.initBala();
                bala.getsDisparo().play();
                return true;
            }
        }
        return false;
    }

    public abstract boolean colisionBala(Bala bala);

    protected boolean validarColision(float xMin, float xMax, float yMin, float yMax, Bala bala){
        return getX() + xMin < bala.getX() && bala.getX() < getX() + xMax
                && getY() + yMin < bala.getY() && bala.getY() < getY() + yMax;
    }

    protected boolean validarColision(float xMin, float xMax, float yMin, float yMax, float xbala, float ybala){
        return getX() + xMin < xbala && xbala < getX() + xMax
                && getY() + yMin < ybala && ybala < getY() + yMax;
    }

    public abstract boolean restarVidaOMorir(float danio);

    public abstract MinMaxXY getMinMaxXY();

    protected void updateLeftRight(){
        setRelativeX(getRelativeX() + getVelocityX());
    }

    protected boolean validarCruze(MinMaxXY mXYB){
        MinMaxXY mXYA = getMinMaxXY();
        return (mXYB.xMin <= mXYA.xMax && mXYB.xMax >= mXYA.xMin) &&
                (mXYB.yMin <= mXYA.yMax && mXYB.yMax >= mXYA.yMin);
    }

    protected void resetActions(){
        actionLeft = false;
        actionRight = false;
        actionDown = false;
        actionUp = false;
        actionDie = false;
        actionCayendo = false;
        actionJump = false;
    }

    protected void resetActionsLeftRight(){
        actionLeft = false;
        actionRight = false;
    }
    protected void resetActionsUpDown(){
        actionDown = false;
        actionUp = false;
    }

    protected void initFlagsAndCounts(){
        flag0 = false;
        flag1 = false;
        dead = false;
        count0 = 0;
        count1 = 0;
    }

    public void inactivar(){
        setVisible(false);
        setIgnoreUpdate(true);
    }

    public boolean isInactivo(){
        return isIgnoreUpdate();
    }

    public void activar(){
        setIgnoreUpdate(false);
        setVisible(true);
    }

    protected boolean validarEnDentroDeEscena(){
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        return left < getX() && getX() < right;
    }

    protected void explosion(float x, float y){
        explosion(x, y, 0, 0);
    }

    protected void explosion(float x, float y, float vx, float vy){
        Explosion explosion = ExplosionFactory.getInstance().getExplosion(escenario, getVertexBufferObjectManager());
        explosion.setPosition(x, y);
        explosion.setVelocidad(vx, vy);
        explosion.activar();
    }

    public float getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
        setX(escenario.getParallaxX() + relativeX);
    }

    public float getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        setY(relativeY + escenario.getFillCropResolutionPolicy().getBottom() + Escenario.PISO_ALTO);
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

    public boolean isActionDie() {
        return actionDie;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean isDead() {
        return dead;
    }

    public ArrayList<Actor> getEnemigos() {
        return enemigos;
    }

    public void setEnemigos(ArrayList<Actor> enemigos) {
        this.enemigos = enemigos;
    }

}
