package com.proyectosfisi.game.contrawithbots;

import android.text.TextPaint;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

/**
 * Created by edson on 22/05/2015.
 */
public class Intro extends Entity {

    public static final int ESTADO_Q0 = 0; // estado menu inicial
    public static final int ESTADO_Q1 = 1; // animación al selecionar nivel
    public static final int ESTADO_Q2 = 2; // estado fuera de escena
    public static final int ESTADO_Q3 = 3; // animacion regreso al menu
    public static final int ESTADO_Q4 = 4; // gameOver

    public static final float ALFA_FONDO = 0.25f;

    protected float countQ0;

    protected Rectangle rIntroFondo;
    protected Sprite spriteIntroTitulo;
    protected Sprite spriteIntroNivel1;
    protected Sprite spriteIntroNivel2;
    protected Sprite spriteIntroNivel3;

    protected Rectangle rNivel1;
    protected Rectangle rNivel2;
    protected Rectangle rNivel3;

    protected int estado;

    protected Escenario escenario;

    protected float velocidad;
    protected float aceleracion;
    protected float separa;

    protected float velocidadEscena;
    protected float aceleracionEscena;

    protected int nivelSelec;

    private Text[] tNivel;
    private Text[] tScoreNivel;

    public Intro(final Escenario escenario,
                 final ITextureRegion mIntroTituloTextureRegion,
                 final ITextureRegion mIntroNivel1TextureRegion,
                 final ITextureRegion mIntroNivel2TextureRegion,
                 final ITextureRegion mIntroNivel3TextureRegion,
                 final VertexBufferObjectManager pVertexBufferObjectManager) {
        super();
        this.escenario = escenario;

        float left = escenario.getCropResolutionPolicy().getLeft();
        float bottom = escenario.getCropResolutionPolicy().getBottom();
        float top = escenario.getCropResolutionPolicy().getTop();
        float right = escenario.getCropResolutionPolicy().getRight();
        float ancho = right - left;
        float alto = top - bottom;

        this.rIntroFondo = new Rectangle(left, bottom, ancho, alto, pVertexBufferObjectManager);
        this.rIntroFondo.setOffsetCenter(0, 0);
        this.rIntroFondo.setColor(new Color(0.5490f, 0.6471f, 0.7412f));
        this.rIntroFondo.setAlpha(ALFA_FONDO);
        this.attachChild(rIntroFondo);

        //Sprites
        spriteIntroTitulo = new Sprite(0, 0, mIntroTituloTextureRegion, pVertexBufferObjectManager);
        spriteIntroTitulo.setOffsetCenter(0, 0);
        this.attachChild(spriteIntroTitulo);

        //Nivel1
        spriteIntroNivel1 = new Sprite(0, 0, mIntroNivel1TextureRegion, pVertexBufferObjectManager);
        spriteIntroNivel1.setOffsetCenter(0, 0);
        this.attachChild(spriteIntroNivel1);

        //Nivel2
        spriteIntroNivel2 = new Sprite(0, 0, mIntroNivel2TextureRegion, pVertexBufferObjectManager);
        spriteIntroNivel2.setOffsetCenter(0, 0);
        this.attachChild(spriteIntroNivel2);

        //Nivel3
        spriteIntroNivel3 = new Sprite(0, 0, mIntroNivel3TextureRegion, pVertexBufferObjectManager);
        spriteIntroNivel3.setOffsetCenter(0, 0);
        this.attachChild(spriteIntroNivel3);

        tNivel = new Text[3];
        tNivel[0] = new Text(0, 0, escenario.getFontNivel(), "Nivel 1", new TextOptions(HorizontalAlign.LEFT), pVertexBufferObjectManager);
        tNivel[0].setOffsetCenter(0, 0);
        this.attachChild(tNivel[0]);
        tNivel[1] = new Text(0, 0, escenario.getFontNivel(), "Nivel 2", new TextOptions(HorizontalAlign.LEFT), pVertexBufferObjectManager);
        tNivel[1].setOffsetCenter(0, 0);
        this.attachChild(tNivel[1]);
        tNivel[2] = new Text(0, 0, escenario.getFontNivel(), "Nivel 3", new TextOptions(HorizontalAlign.LEFT), pVertexBufferObjectManager);
        tNivel[2].setOffsetCenter(0, 0);
        this.attachChild(tNivel[2]);

        tScoreNivel = new Text[3];
        tScoreNivel[0] = new Text(0, 0, escenario.getFontScoreNivel(), "SCORE: 0000", new TextOptions(HorizontalAlign.RIGHT), pVertexBufferObjectManager);
        tScoreNivel[0].setOffsetCenter(1, 1);
        this.attachChild(tScoreNivel[0]);
        tScoreNivel[1] = new Text(0, 0, escenario.getFontScoreNivel(), "SCORE: 0000", new TextOptions(HorizontalAlign.RIGHT), pVertexBufferObjectManager);
        tScoreNivel[1].setOffsetCenter(1, 1);
        this.attachChild(tScoreNivel[1]);
        tScoreNivel[2] = new Text(0, 0, escenario.getFontScoreNivel(), "SCORE: 0000", new TextOptions(HorizontalAlign.RIGHT), pVertexBufferObjectManager);
        tScoreNivel[2].setOffsetCenter(1, 1);
        this.attachChild(tScoreNivel[2]);

        // Nivel 1
        rNivel1 = new Rectangle(0, 0, spriteIntroNivel1.getWidth(), spriteIntroNivel1.getHeight(), pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(escenario.isPausa() && estado == ESTADO_Q0) {
                    if (touchEvent.isActionDown()) {
                        nivelSelec = 1;
                        setStateQ1();
                    }
                }
                return true;
            }
        };
        rNivel1.setVisible(false);

        // Nivel 2
        rNivel2 = new Rectangle(0, 0, spriteIntroNivel2.getWidth(), spriteIntroNivel2.getHeight(), pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(escenario.isPausa() && estado == ESTADO_Q0) {
                    if (touchEvent.isActionDown()) {
                        nivelSelec = 2;
                        setStateQ1();
                    }
                }
                return true;
            };
        };
        rNivel2.setVisible(false);

        // Nivel 3
        rNivel3 = new Rectangle(0, 0, spriteIntroNivel3.getWidth(), spriteIntroNivel3.getHeight(), pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(escenario.isPausa() && estado == ESTADO_Q0) {
                    if (touchEvent.isActionDown()) {
                        nivelSelec = 3;
                        setStateQ1();
                    }
                }
                return true;
            };
        };
        rNivel3.setVisible(false);

        escenario.getHud().registerTouchArea(rNivel1);
        escenario.getHud().registerTouchArea(rNivel2);
        escenario.getHud().registerTouchArea(rNivel3);
        escenario.getHud().attachChild(rNivel1);
        escenario.getHud().attachChild(rNivel2);
        escenario.getHud().attachChild(rNivel3);

        initPosiciones();
        this.setZIndex(20);

    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        switch (estado){
            case ESTADO_Q1:
                float top = escenario.getCropResolutionPolicy().getTop();
                float left = escenario.getCropResolutionPolicy().getLeft();
                float right = escenario.getCropResolutionPolicy().getRight();
                boolean bool = spriteIntroTitulo.getY() > top;
                switch (nivelSelec) {
                    case 1:
                        bool = bool && spriteIntroNivel2.getX() > right && spriteIntroNivel3.getX() > right;
                        break;
                    case 2:
                        bool = bool && spriteIntroNivel1.getX() + spriteIntroNivel1.getWidth() < left && spriteIntroNivel3.getX() > right;
                        break;
                    default:
                        bool = bool && spriteIntroNivel1.getX() + spriteIntroNivel1.getWidth() < left && spriteIntroNivel2.getX() + spriteIntroNivel2.getWidth() < left;
                        break;
                }
                if(bool) {
                    setStateQ2();
                }else{
                    spriteIntroTitulo.setY(spriteIntroTitulo.getY() + velocidad);
                    float dif = (escenario.getCropResolutionPolicy().getTop() - spriteIntroTitulo.getY()) / separa;
                    switch (nivelSelec){
                        case 1:
                            spriteIntroNivel2.setX(spriteIntroNivel2.getX() + velocidad);
                            spriteIntroNivel3.setX(spriteIntroNivel3.getX() + velocidad);
                            if(0 < dif && dif < 1) {
                                spriteIntroNivel1.setAlpha(dif);
                            }
                            break;
                        case 2:
                            spriteIntroNivel1.setX(spriteIntroNivel1.getX() - velocidad);
                            spriteIntroNivel3.setX(spriteIntroNivel3.getX() + velocidad);
                            if(0 < dif && dif < 1) {
                                spriteIntroNivel2.setAlpha(dif);
                            }
                            break;
                        default:
                            spriteIntroNivel1.setX(spriteIntroNivel1.getX() - velocidad);
                            spriteIntroNivel2.setX(spriteIntroNivel2.getX() - velocidad);
                            if(0 < dif && dif < 1) {
                                spriteIntroNivel3.setAlpha(dif);
                            }
                            break;
                    }
                    if(0 < dif && dif < 1) {
                        rIntroFondo.setAlpha(ALFA_FONDO * dif);
                    }
                    velocidad += aceleracion;
                }
            case ESTADO_Q0:
                float ancho = escenario.getCropResolutionPolicy().getRight() - escenario.getCropResolutionPolicy().getLeft();
                PersonajeJugador j = escenario.getJugador();
                if(j.getRelativeX() > ancho / 2){
                    if(j.getRelativeX() - velocidadEscena < ancho / 2){
                        j.setRelativeX(ancho / 2);
                    }else{
                        j.setRelativeX(j.getRelativeX() - velocidadEscena);
                    }
                    velocidadEscena += aceleracionEscena;
                    j.centrarEscenaAPersonaje();
                }
                break;
        }
        super.onManagedUpdate(pSecondsElapsed);
    }

    public void setStateQ0(){

        escenario.setPausa(true);
        escenario.getLayerPlayer().setVisible(false);
        escenario.getLayerBullets().setVisible(false);

        escenario.getBotFactory().inactivar();
        escenario.getJugador().inactivar();
        escenario.getBase().inactivar();

        BalaFactory.getInstance().inactivarBalasActivas();
        ExplosionFactory.getInstance().inactivarExplosionsActivas();

        countQ0 = 0.0f;
        velocidad = -2.56f;
        aceleracion = 0.32f;
        nivelSelec = 0;

        mostrarIntro();
        mostrarScores();

        separa = escenario.getCropResolutionPolicy().getTop() - spriteIntroTitulo.getY();

        //Determinando la velocidad y acelaracion de la escena
        float ancho = escenario.getCropResolutionPolicy().getRight() - escenario.getCropResolutionPolicy().getLeft();
        float t = 12.5f;
        PersonajeJugador j = escenario.getJugador();
        velocidadEscena = 2*(j.getRelativeX() - ancho / 2) / t;
        aceleracionEscena = -velocidadEscena / t;

        escenario.getmMusic().play();

        estado = ESTADO_Q0;
    }

    public void setStateQ1(){
        ocultarScores();
        estado = ESTADO_Q1;
    }

    public void setStateQ2(){

        PersonajeJugador j = escenario.getJugador();
        j.init();
        j.activar();
        escenario.getBotFactory().init();

        ocultarIntro();

        escenario.getLayerPlayer().setVisible(true);
        escenario.getLayerBullets().setVisible(true);

        escenario.getControles().mostrarControles();

        escenario.setPausa(false);

        estado = ESTADO_Q2;
    }

    public void initPosiciones(){
        float left = escenario.getCropResolutionPolicy().getLeft();
        float bottom = escenario.getCropResolutionPolicy().getBottom();
        float top = escenario.getCropResolutionPolicy().getTop();
        float right = escenario.getCropResolutionPolicy().getRight();
        float alto = top - bottom;
        float ancho = right - left;
        float separaAlto = (alto - spriteIntroTitulo.getHeight() - spriteIntroNivel1.getHeight())/3;
        float separaAncho = (ancho - 3*spriteIntroNivel1.getWidth())/4;

        spriteIntroTitulo.setPosition(left + (ancho - spriteIntroTitulo.getWidth())/2, top - spriteIntroTitulo.getHeight() - separaAlto);
        spriteIntroNivel1.setPosition(left + separaAncho, top - spriteIntroTitulo.getHeight() - 2*separaAlto - spriteIntroNivel1.getHeight());
        spriteIntroNivel2.setPosition(left + 2*separaAncho + spriteIntroNivel1.getWidth(), top - spriteIntroTitulo.getHeight() - 2*separaAlto - spriteIntroNivel2.getHeight());
        spriteIntroNivel3.setPosition(left + 3*separaAncho + spriteIntroNivel1.getWidth() + spriteIntroNivel2.getWidth(), top - spriteIntroTitulo.getHeight() - 2*separaAlto - spriteIntroNivel3.getHeight());

        rNivel1.setPosition(spriteIntroNivel1.getX()+spriteIntroNivel1.getWidth()/2, spriteIntroNivel1.getY()+ spriteIntroNivel1.getHeight()/2);
        rNivel2.setPosition(spriteIntroNivel2.getX()+spriteIntroNivel2.getWidth()/2, spriteIntroNivel2.getY()+ spriteIntroNivel2.getHeight()/2);
        rNivel3.setPosition(spriteIntroNivel3.getX()+spriteIntroNivel3.getWidth()/2, spriteIntroNivel3.getY()+ spriteIntroNivel3.getHeight()/2);

        tNivel[0].setPosition(spriteIntroNivel1.getX() + Escenario.MANDO_PADDING, spriteIntroNivel1.getY() + spriteIntroNivel1.getHeight() - Escenario.MANDO_PADDING/2);
        tNivel[1].setPosition(spriteIntroNivel2.getX() + Escenario.MANDO_PADDING, spriteIntroNivel2.getY() + spriteIntroNivel2.getHeight() - Escenario.MANDO_PADDING/2);
        tNivel[2].setPosition(spriteIntroNivel3.getX() + Escenario.MANDO_PADDING, spriteIntroNivel3.getY() + spriteIntroNivel3.getHeight() - Escenario.MANDO_PADDING/2);

        tScoreNivel[0].setPosition(spriteIntroNivel1.getX() + spriteIntroNivel1.getWidth() - 3, spriteIntroNivel1.getY());
        tScoreNivel[1].setPosition(spriteIntroNivel2.getX() + spriteIntroNivel1.getWidth() - 3, spriteIntroNivel2.getY());
        tScoreNivel[2].setPosition(spriteIntroNivel3.getX() + spriteIntroNivel1.getWidth() - 3, spriteIntroNivel3.getY());
    }

    public void ocultarIntro(){
        float left = escenario.getCropResolutionPolicy().getLeft();
        float right = escenario.getCropResolutionPolicy().getRight();
        float ancho = right - left;

        if(rNivel1.getX() < right){
            spriteIntroTitulo.setVisible(false);
            spriteIntroNivel1.setVisible(false);
            spriteIntroNivel2.setVisible(false);
            spriteIntroNivel3.setVisible(false);
            rIntroFondo.setVisible(false);
            rNivel1.setX(rNivel1.getX() + ancho);
            rNivel2.setX(rNivel2.getX() + ancho);
            rNivel3.setX(rNivel3.getX() + ancho);
        }
    }

    public void mostrarIntro(){
        float left = escenario.getCropResolutionPolicy().getLeft();
        float right = escenario.getCropResolutionPolicy().getRight();
        float ancho = right - left;

        initPosiciones();
        rIntroFondo.setAlpha(ALFA_FONDO);
        spriteIntroNivel1.setAlpha(1.0f);
        spriteIntroNivel2.setAlpha(1.0f);
        spriteIntroNivel3.setAlpha(1.0f);
        spriteIntroTitulo.setVisible(true);
        spriteIntroNivel1.setVisible(true);
        spriteIntroNivel2.setVisible(true);
        spriteIntroNivel3.setVisible(true);
        rIntroFondo.setVisible(true);
    }

    public void mostrarScores(){
        tNivel[0].setVisible(true);
        tNivel[1].setVisible(true);
        tNivel[2].setVisible(true);
        tScoreNivel[0].setVisible(true);
        tScoreNivel[1].setVisible(true);
        tScoreNivel[2].setVisible(true);
    }

    public void ocultarScores(){
        tNivel[0].setVisible(false);
        tNivel[1].setVisible(false);
        tNivel[2].setVisible(false);
        tScoreNivel[0].setVisible(false);
        tScoreNivel[1].setVisible(false);
        tScoreNivel[2].setVisible(false);
    }

    public void setScore(int score){
        if(nivelSelec != 0) {
            tScoreNivel[nivelSelec - 1].setText("SCORE: " + String.format("%4d", score).replace(" ", "0"));
        }
    }

    public int getEstado() {
        return estado;
    }

    public int getNivelSelec() {
        return nivelSelec;
    }

    public void setNivelSelec(int nivelSelec) {
        this.nivelSelec = nivelSelec;
    }
}

