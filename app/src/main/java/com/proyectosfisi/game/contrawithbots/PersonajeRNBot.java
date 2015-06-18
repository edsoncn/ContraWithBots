package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by MARISSA on 14/06/2015.
 */
public class PersonajeRNBot extends Personaje {

    private RNBot red;

    protected float countBullets;
    protected float countMove;
    public static final int TIPO_NORMAL = 3;
    protected float destanciaMedia;
    private double[] entradas = new double[4];
    private long[] salidas = new long[6];
    private long[] salidasPrevias;

    public PersonajeRNBot(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
        red = new RNBot();
    }

    public void initEnemigo(){
        countBullets = 0;
        countMove = 0.0f;
        salidasPrevias = new long[]{-1,-1,-1,-1,-1,-1};
        destanciaMedia = BotFactory.DISCTANCIA_MEDIA + BotFactory.DISCTANCIA_MEDIA_ERROR - (float)(2*Math.random()*BotFactory.DISCTANCIA_MEDIA_ERROR);
    }

    @Override
    public void init(float relativeX, float relativeY){
        initEnemigo();
        super.init(relativeX, relativeY);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if (!escenario.isPausa()) {
            if (getState() != STATE_Q5) {
                Personaje enemigo = getEnemigos().get(0);
                countMove += pSecondsElapsed;
                if (countMove > 0.25) {
                    if (Math.abs(getRelativeX() - enemigo.getRelativeX()) > (destanciaMedia + 20)) {
                        entradas[0] = 1;
                    }else if(Math.abs(getRelativeX() - enemigo.getRelativeX()) < (destanciaMedia - 20)){
                        entradas[0] = 0;
                    }else {
                        entradas[0] = 0.5;
                    }
                    Log.i(""+"estados ", "yo:"+enemigo.getState()+" bot:"+getState());
                    if(enemigo.getState() == STATE_Q3){//agachado
                        entradas[1] = 0;
                    }else if(enemigo.getState() == STATE_Q0 || enemigo.getState() == STATE_Q2 || enemigo.getState() == STATE_Q4){//parado
                        entradas[1] = 0.5;
                    }else if(enemigo.getState() == STATE_Q1 || enemigo.getState() == STATE_Q6){//saltando
                        entradas[1] = 1;
                    }

                    if(getState() == STATE_Q3){//agachado
                        entradas[2] = 0;
                    }else if(getState() == STATE_Q0 || getState() == STATE_Q2 || getState() == STATE_Q4){//parado
                        entradas[2] = 0.5;
                    }else if(getState() == STATE_Q1 || getState() == STATE_Q6){//saltando
                        entradas[2] = 1;
                    }

                    double d = 0;
                    if(BalaFactory.getInstance().getListaBalasActivas().size()!=0){
                        for(Bala b: BalaFactory.getInstance().getListaBalasActivas()) {
                            if (b.getPersonaje() instanceof PersonajeJugador) {
                                d =  Math.sqrt( Math.pow(b.getX()-getX(),2) + Math.pow(b.getY()-getY(),2) );
                                if(d < 100){
                                    entradas[3] = 1;
                                }else{
                                    entradas[3] = 0;
                                }
                            }
                        }
                    }else{
                        entradas[3] = 0;
                    }

                    salidas = red.compute(entradas);
                    pintarEntradasSalidas();

                    if(salidas[0] == 1) {//retroceder
                        if (salidasPrevias[0] != salidas[0]) {
                            if (getRelativeX() > enemigo.getRelativeX()) {//personaje se encuentra a la izquierda del bot
                                Log.i("bot", "retro R");
                                setAction(ACTION_RIGHT);
                            } else {
                                Log.i("bot", "retro L");
                                setAction(ACTION_LEFT);
                            }
                        }
                    }else if(salidas[1] == 1){
                        if(salidasPrevias[1] != salidas[1]) {
                            if (getRelativeX() > enemigo.getRelativeX()) {//personaje se encuentra a la izquierda del bot
                                Log.i("bot", "avan L");
                                setAction(-ACTION_RIGHT);
                                setAction(ACTION_LEFT);
                            } else {
                                Log.i("bot", "avan R");
                                setAction(-ACTION_LEFT);
                                setAction(ACTION_RIGHT);
                            }
                        }
                    }else{
                        if(salidasPrevias[0] != 0 || salidasPrevias[1] != 0) {
                            Log.i("bot", "soltar L&R");
                            setAction(-ACTION_LEFT);
                            setAction(-ACTION_RIGHT);
                        }
                    }

                    if(salidasPrevias[2] != salidas[2]) {
                        if (salidas[2] == 1) {
                            setAction(ACTION_DOWN);
                        } else {
                            setAction(-ACTION_DOWN);
                        }
                    }

                    if(salidasPrevias[3] != salidas[3]) {
                        if (salidas[3] == 1) {
                            setAction(ACTION_JUMP);
                        } else {
                            setAction(-ACTION_JUMP);
                        }
                    }

                    if(salidas[4] == 1) {//esquivar
                        for(Bala b: BalaFactory.getInstance().getListaBalasActivas()){
                            if(b.getPersonaje() instanceof PersonajeJugador){
                                Log.i("bot","vy:"+b.getVelocityY()+" vx:"+b.getVelocityX());
                                if(b.getVelocityY()>0 && b.getVelocityX()>0){
                                    if (getX()>b.getX()) {
                                        setAction(ACTION_RIGHT);
                                    }else {
                                        setAction(ACTION_LEFT);
                                    }
                                }
                                if(b.getVelocityY() == 0){
                                    setAction(ACTION_JUMP);
                                }
                                if(b.getVelocityX()==0){
                                    if (getX()>enemigo.getX()) {
                                        setAction(ACTION_RIGHT);
                                    }else{
                                        setAction(ACTION_LEFT);
                                    }
                                }
                            }
                        }
                    }

                    if(salidas[5] == 1) {
                        if (getRelativeX() > enemigo.getRelativeX() && getOrientation() == ORIENTATION_RIGHT) {
                            setAction(ACTION_LEFT);
                            setAction(-ACTION_LEFT);
                        }
                        if (getRelativeX() < enemigo.getRelativeX() && getOrientation() == ORIENTATION_LEFT) {
                            setAction(ACTION_RIGHT);
                            setAction(-ACTION_RIGHT);
                        }
                        shoot();
                    }

                    countMove = 0;

                    salidasPrevias[0] = salidas[0];
                    salidasPrevias[1] = salidas[1];
                    salidasPrevias[2] = salidas[2];
                    salidasPrevias[3] = salidas[3];
                    salidasPrevias[4] = salidas[4];
                    salidasPrevias[5] = salidas[5];

                }
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    private void pintarEntradasSalidas() {
        Log.i("" +
                "bot", "entradas:"+
                String.format("%2.1f", entradas[0])+","+
                String.format("%2.1f", entradas[1])+","+
                String.format("%2.1f", entradas[2])+","+
                String.format("%2.1f", entradas[3])+" salidas:" +
                String.format("%2d", salidas[0]) + "," +
                String.format("%2d", salidas[1]) + "," +
                String.format("%2d", salidas[2]) + "," +
                String.format("%2d", salidas[3]) + "," +
                String.format("%2d", salidas[4]) + "," +
                String.format("%2d", salidas[5]));
    }

    @Override
    public synchronized void setAction(int action){
        Log.i("bot", "Action: "+action);
        super.setAction(action);
    }
}
