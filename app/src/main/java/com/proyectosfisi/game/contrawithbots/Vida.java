package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import static org.andengine.util.adt.color.Color.WHITE;

/**
 * Created by edson on 02/05/2015.
 */
public class Vida extends Rectangle{

    public static final float VIDA_DEFAULT = 100f;
    public static final float VIDA_ALTO = 3f;
    public static final float ALFA = 0.5f;

    protected float vida;
    protected float vidaInicial;

    protected Rectangle vidaRestante;
    protected Text tVida;
    protected float anchoInicial;
    protected float vidaAux;

    protected Escenario escenario;

    protected Sound sLive;

    protected float tCount;

    public Vida(Escenario escenario, Entity layaut, final float pX, final float pY, final float pWidth, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, Vida.VIDA_ALTO, pVertexBufferObjectManager);

        this.escenario = escenario;

        setColor(0.96f, 0.05f, 0.05f);
        setOffsetCenter(0, 0);
        setAlpha(ALFA);
        layaut.attachChild(this);

        vidaRestante = new Rectangle(pX, pY, pWidth, Vida.VIDA_ALTO, pVertexBufferObjectManager);
        vidaRestante.setColor(0.05f, 0.96f, 0.05f);
        vidaRestante.setOffsetCenter(0, 0);
        layaut.attachChild(vidaRestante);

        anchoInicial = pWidth;

        tVida = new Text(0, 0, escenario.getFontVida(), "00000", new TextOptions(HorizontalAlign.RIGHT), pVertexBufferObjectManager);
        tVida.setOffsetCenter(1, 0);
        tVida.setVisible(false);
        layaut.attachChild(tVida);

        init();
    }

    public void init(){
        vida = VIDA_DEFAULT;
        vidaInicial = vida;
        vidaAux = vida;
        vidaRestante.setWidth(anchoInicial);

        tVida.setVisible(false);
        tCount = -1;

        setAlpha(ALFA);
        vidaRestante.setAlpha(ALFA);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        if(!escenario.isPausa()) {
            if (vidaAux != vida) {
                float resto = vida - vidaAux;
                tVida.setText(String.valueOf((int) resto));
                tVida.setPosition(getX() + getWidth(), getY() + getHeight() * 1.5f - 10);
                tVida.setAlpha(0.0f);
                tVida.setVisible(true);
                tCount = 0;
                vidaAux = vida;
            }
            if (tCount > 0.25) {
                tVida.setVisible(false);
                tCount = -1;
            } else if (tCount >= 0) {
                float div = tCount / 0.125f ;
                div = div > 1 ? 1 : div;
                tVida.setPosition(getX() + getWidth(), getY() + getHeight() * 1.5f - (10 - 10 * div));
                tVida.setAlpha(div);
                tCount += pSecondsElapsed;
            }
            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    public void inactivar(){
        setVisible(false);
        tVida.setVisible(false);
        vidaRestante.setVisible(false);
    }

    public boolean isInactivo(){
        return isVisible() ;
    }

    public void activar(){
        setVisible(true);
        vidaRestante.setVisible(true);
    }

    public boolean restarVidaOMorir(float danio){
        sLive.play();
        if(vida - danio <= 0){
            vida = 0;
            vidaRestante.setWidth(0.0f);
            return true;
        }else{
            vida -= danio;
            vidaRestante.setWidth((anchoInicial * vida) / vidaInicial);
            return false;
        }
    }

    public void actualizarPosicionX(float pX){
        setX(pX);
        vidaRestante.setX(pX);
    }

    public void actualizarPosicionY(float pY){
        setY(pY);
        vidaRestante.setY(pY);
    }

    public Sound getsLive() {
        return sLive;
    }

    public void setsLive(Sound sLive) {
        this.sLive = sLive;
    }

    public float getVida() {
        return vida;
    }

    public void setVida(float vida) {
        this.vida = vida;
    }
}
