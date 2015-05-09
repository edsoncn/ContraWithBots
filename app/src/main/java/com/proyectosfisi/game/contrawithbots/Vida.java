package com.proyectosfisi.game.contrawithbots;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
    protected float anchoInicial;

    public Vida(Entity layaut, final float pX, final float pY, final float pWidth, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, Vida.VIDA_ALTO, pVertexBufferObjectManager);
        super.setColor(0.96f, 0.05f, 0.05f);
        setOffsetCenter(0 , 0);
        setAlpha(ALFA);
        layaut.attachChild(this);

        vidaRestante = new Rectangle(pX, pY, pWidth, Vida.VIDA_ALTO, pVertexBufferObjectManager);
        vidaRestante.setColor(0.05f, 0.96f, 0.05f);
        vidaRestante.setOffsetCenter(0 , 0);
        vidaRestante.setAlpha(ALFA);
        layaut.attachChild(vidaRestante);

        vida = VIDA_DEFAULT;
        vidaInicial = vida;
        anchoInicial = pWidth;
    }

    public boolean restarVidaOMorir(float danio){
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
}
