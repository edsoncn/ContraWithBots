package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

/**
 * Created by edson on 24/05/2015.
 */
public class Controles extends Entity{

    protected Sprite spriteMandoDireccional;
    protected Sprite spriteMandoAcciones;
    protected Sprite spriteMandoPausa;
    protected Sprite spriteBotonContinuar;
    protected Sprite spriteBotonMenu;
    protected Rectangle rDireccional;
    protected Rectangle rY;
    protected Rectangle rA;
    protected Rectangle rX;
    protected Rectangle rB;
    protected Rectangle rPausa;
    protected Rectangle rFondoPausa;
    protected Rectangle rContinuar;
    protected Rectangle rMenu;

    protected Text tTitulo;
    protected Text tParrafoDerecha;
    protected Text tParrafoIzquierda;

    protected TiledSprite sVida;
    protected TiledSprite sBombas;
    protected Text tVida;
    protected Text tBombas;

    protected Escenario escenario;
    protected float escala;

    public Controles(final Escenario escenario, float escala,
                     final ITextureRegion mMandoDireccionalTextureRegion,
                     final ITextureRegion mMandoAccionesTextureRegion,
                     final ITextureRegion mMandoPausaTextureRegion,
                     final ITextureRegion mBotonContinuarTextureRegion,
                     final ITextureRegion mBotonMenuTextureRegion,
                     final TiledTextureRegion mIconosTextureRegion,
                     final VertexBufferObjectManager pVertexBufferObjectManager) {
        super();

        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float bottom = escenario.getFillCropResolutionPolicy().getBottom();
        float top = escenario.getFillCropResolutionPolicy().getTop();
        float ancho = right - left;
        float alto = top - bottom;
        this.escala = escala;

        sVida = new TiledSprite(0, 0, mIconosTextureRegion, pVertexBufferObjectManager);
        sVida.setCurrentTileIndex(0);
        sVida.setVisible(false);
        sVida.setOffsetCenter(1, 1);
        this.attachChild(sVida);

        tVida = new Text(0, 0, escenario.getFontIconoVida(), "100", new TextOptions(HorizontalAlign.RIGHT), pVertexBufferObjectManager);
        tVida.setVisible(false);
        tVida.setOffsetCenterX(1);
        this.attachChild(tVida);

        sBombas = new TiledSprite(0, 0, mIconosTextureRegion, pVertexBufferObjectManager);
        sBombas.setCurrentTileIndex(1);
        sBombas.setVisible(false);
        sBombas.setOffsetCenter(1, 1);
        this.attachChild(sBombas);

        tBombas = new Text(0, 0, escenario.getFontIconoVida(), "x2", new TextOptions(HorizontalAlign.RIGHT), pVertexBufferObjectManager);
        tBombas.setVisible(false);
        tBombas.setOffsetCenterX(1);
        this.attachChild(tBombas);

        //Fondo de la pausa
        rFondoPausa = new Rectangle(left, bottom, ancho, alto, pVertexBufferObjectManager);
        rFondoPausa.setColor(new Color(0.0313f, 0.0471f, 0.1128f));
        rFondoPausa.setOffsetCenter(0.0f, 0.0f);
        rFondoPausa.setAlpha(Intro.ALFA_FONDO);
        rFondoPausa.setVisible(false);
        this.attachChild(rFondoPausa);

        //Sprites
        spriteMandoDireccional = new Sprite(0, 0, mMandoDireccionalTextureRegion, pVertexBufferObjectManager);
        spriteMandoDireccional.setSize(spriteMandoDireccional.getWidth()*escala, spriteMandoDireccional.getHeight()*escala);
        this.attachChild(spriteMandoDireccional);

        spriteMandoAcciones = new Sprite(0, 0, mMandoAccionesTextureRegion, pVertexBufferObjectManager);
        spriteMandoAcciones.setSize(spriteMandoAcciones.getWidth()*escala, spriteMandoAcciones.getHeight()*escala);
        this.attachChild(spriteMandoAcciones);

        spriteMandoPausa = new Sprite(0, 0, mMandoPausaTextureRegion, pVertexBufferObjectManager);
        spriteMandoPausa.setSize(spriteMandoPausa.getWidth()*escala, spriteMandoPausa.getHeight()*escala);
        this.attachChild(spriteMandoPausa);

        spriteBotonContinuar = new Sprite(0, 0, mBotonContinuarTextureRegion, pVertexBufferObjectManager);
        spriteBotonContinuar.setSize(spriteBotonContinuar.getWidth() * escala, spriteBotonContinuar.getHeight() * escala);
        this.attachChild(spriteBotonContinuar);

        spriteBotonMenu = new Sprite(0, 0, mBotonMenuTextureRegion, pVertexBufferObjectManager);
        spriteBotonMenu.setSize(spriteBotonMenu.getWidth() * escala, spriteBotonMenu.getHeight() * escala);
        this.attachChild(spriteBotonMenu);

        tTitulo = new Text(left + ancho/2, top - alto/4, escenario.getFontTitulo(), "Base capturada\r\nNivel XXX", new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
        tTitulo.setVisible(false);

        tParrafoDerecha = new Text(left + ancho/2, top - alto/2 - Escenario.MANDO_PADDING, escenario.getFontParrafo(), "SCORE:\r\nBOTS CAIDOS:\r\nAVANCE:\r\n", new TextOptions(HorizontalAlign.RIGHT), pVertexBufferObjectManager);
        tParrafoDerecha.setOffsetCenterX(1);
        tParrafoDerecha.setVisible(false);

        tParrafoIzquierda = new Text(left + ancho/2 + Escenario.MANDO_PADDING, top - alto/2 - Escenario.MANDO_PADDING, escenario.getFontParrafo(), "9999\r\n16\r\n100%\r\n", new TextOptions(HorizontalAlign.LEFT), pVertexBufferObjectManager);
        tParrafoIzquierda.setOffsetCenterX(0);
        tParrafoIzquierda.setVisible(false);

        this.attachChild(tTitulo);
        this.attachChild(tParrafoDerecha);
        this.attachChild(tParrafoIzquierda);

        // Control Direccional
        rDireccional = new Rectangle(0, 0, spriteMandoDireccional.getWidth(), spriteMandoDireccional.getHeight(), pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(!escenario.isPausa()) {
                    escenario.getJugador().setAction(touchEvent, X, Y, spriteMandoDireccional.getWidth(), spriteMandoDireccional.getHeight());
                }
                return true;
            };
        };
        rDireccional.setVisible(false);

        // Control Acciones
        rY = new Rectangle(0, 0, spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3, pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(!escenario.isPausa()) {
                    if (touchEvent.isActionDown()) {
                        escenario.getJugador().setAction(Personaje.ACTION_SHOOT);
                    }
                }
                return true;
            };
        };
        rY.setVisible(false);
        rA = new Rectangle(0, 0, spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3, pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(!escenario.isPausa()) {
                    if (touchEvent.isActionDown()) {
                        escenario.getJugador().activarBomba();
                    }
                }
                return true;
            };
        };
        rA.setVisible(false);
        rX = new Rectangle(0, 0, spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3 + Escenario.MANDO_PADDING * escala, pVertexBufferObjectManager);
        rX.setVisible(false);
        rB = new Rectangle(0, 0, spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getHeight()/3 + Escenario.MANDO_PADDING * escala, pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if(!escenario.isPausa()) {
                    if (touchEvent.isActionDown()) {
                        Log.i("Control", "JUMP");
                        escenario.getJugador().setAction(PersonajeJugador.ACTION_JUMP, touchEvent);
                    }
                }
                return true;
            };
        };
        rB.setVisible(false);
        rPausa = new Rectangle(0, 0, spriteMandoPausa.getWidth(), spriteMandoPausa.getHeight(), pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (touchEvent.isActionDown()) {
                    if(!escenario.isPausa()) {
                        pausa();
                    }else{
                        reanudar();
                    }
                }
                return true;
            };
        };
        rPausa.setVisible(false);
        rPausa = new Rectangle(0, 0, spriteMandoPausa.getWidth(), spriteMandoPausa.getHeight(), pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (touchEvent.isActionDown()) {
                    if(!escenario.isPausa()) {
                        pausa();
                    }else{
                        reanudar();
                    }
                }
                return true;
            }
        };
        rPausa.setVisible(false);
        rContinuar = new Rectangle(0, 0, spriteBotonContinuar.getWidth(), spriteBotonContinuar.getHeight(), pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (escenario.isPausa() && touchEvent.isActionDown()) {
                    reanudar();
                }
                return true;
            };
        };
        rContinuar.setVisible(false);
        rMenu = new Rectangle(0, 0, spriteBotonMenu.getWidth(), spriteBotonMenu.getHeight(), pVertexBufferObjectManager){
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
                if (escenario.isPausa() && touchEvent.isActionDown()) {
                    menu();
                }
                return true;
            };
        };
        rMenu.setVisible(false);

        escenario.getHud().registerTouchArea(rDireccional);
        escenario.getHud().registerTouchArea(rY);
        escenario.getHud().registerTouchArea(rA);
        escenario.getHud().registerTouchArea(rX);
        escenario.getHud().registerTouchArea(rB);
        escenario.getHud().registerTouchArea(rPausa);
        escenario.getHud().registerTouchArea(rContinuar);
        escenario.getHud().registerTouchArea(rMenu);
        escenario.getHud().attachChild(rDireccional);
        escenario.getHud().attachChild(rY);
        escenario.getHud().attachChild(rA);
        escenario.getHud().attachChild(rX);
        escenario.getHud().attachChild(rB);
        escenario.getHud().registerTouchArea(rPausa);
        escenario.getHud().registerTouchArea(rContinuar);
        escenario.getHud().registerTouchArea(rMenu);

        this.escenario = escenario;
        this.setZIndex(18);

        initPosiciones();
        ocultarBotonesPausa();
    }

    public void pausa(){
        escenario.setPausa(true);
        ocultarControles();
        ocultarTitulos();
        mostrarFondoPausa();
        mostrarBotonesPausa();
    }

    public void menu(){
        escenario.setPausa(true);
        ocultarControles();
        ocultarTitulos();
        ocultarBotonesPausa();
        ocultarControles();
        ocultarFondoPausa();
        ocultarIconos();
        escenario.getIntro().setStateQ0();
    }

    public void reanudar(){
        ocultarFondoPausa();
        ocultarBotonesPausa();
        ocultarTitulos();
        mostrarControles();
        mostrarIconos();
        escenario.setPausa(false);
    }

    public void perdiste(){
        tTitulo.setText("Perdiste\r\nnivel " + escenario.getIntro().getNivelSelec());
        score();
    }

    public void ganaste(){
        tTitulo.setText("Base capturada\r\nnivel " + escenario.getIntro().getNivelSelec());
        score();
    }

    public void score(){
        escenario.setPausa(true);
        ocultarControles();
        mostrarFondoPausa();
        mostrarTitulos();
    }

    public void setScore(int score, int botsCaidos, float avance){
        tParrafoIzquierda.setText(String.format("%4d", score).replace(" ", "0") + "\r\n" + botsCaidos + "\r\n" + ((int) (avance * 100)) + "%\r\n");
        escenario.getIntro().setScore(score);
    }

    public void initPosiciones(){
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float bottom = escenario.getFillCropResolutionPolicy().getBottom();
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float top = escenario.getFillCropResolutionPolicy().getTop();
        float ancho = right - left;
        float alto = top - bottom;

        spriteMandoDireccional.setPosition(left + spriteMandoDireccional.getWidth()/2 + Escenario.MANDO_PADDING * escala, bottom + spriteMandoDireccional.getHeight()/2 + Escenario.MANDO_PADDING * escala);
        spriteMandoAcciones.setPosition(right - spriteMandoAcciones.getWidth()/2 - Escenario.MANDO_PADDING * escala, bottom + spriteMandoAcciones.getHeight()/2 + Escenario.MANDO_PADDING * escala);
        spriteMandoPausa.setPosition(spriteMandoDireccional.getX(), top - (spriteMandoDireccional.getY() - bottom));
        spriteBotonContinuar.setPosition(left + ancho/2, bottom + alto/2 + 3*spriteBotonContinuar.getHeight()/4);
        spriteBotonMenu.setPosition(left + ancho/2, bottom + alto/2 - 3*spriteBotonMenu.getHeight()/4);
        rDireccional.setPosition(spriteMandoDireccional.getX(), spriteMandoDireccional.getY());
        rY.setPosition(spriteMandoAcciones.getX() - spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getY());
        rA.setPosition(spriteMandoAcciones.getX() + spriteMandoAcciones.getWidth()/3, spriteMandoAcciones.getY());
        rX.setPosition(spriteMandoAcciones.getX(), spriteMandoAcciones.getY() + spriteMandoAcciones.getHeight()/3 - (Escenario.MANDO_PADDING * escala)/2);
        rB.setPosition(spriteMandoAcciones.getX(), spriteMandoAcciones.getY() - spriteMandoAcciones.getHeight()/3 + (Escenario.MANDO_PADDING * escala)/2);
        rPausa.setPosition(spriteMandoPausa.getX(), spriteMandoPausa.getY());
        rContinuar.setPosition(spriteBotonContinuar.getX(), spriteBotonContinuar.getY());
        rMenu.setPosition(spriteBotonMenu.getX(), spriteBotonMenu.getY());
        tBombas.setPosition(right - escenario.MANDO_PADDING, top - escenario.MANDO_PADDING - sBombas.getHeight() / 2);
        sBombas.setPosition(tBombas.getX() - tBombas.getWidth() - escenario.MANDO_PADDING / 2, top - escenario.MANDO_PADDING);
        tVida.setPosition(sBombas.getX() - sBombas.getWidth() , tBombas.getY());
        sVida.setPosition(tVida.getX() - tVida.getWidth() - escenario.MANDO_PADDING / 2, sBombas.getY());
    }

    public void mostrarControles(){
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float ancho = right - left;
        if(spriteMandoDireccional.getX() > right){
            spriteMandoDireccional.setX(spriteMandoDireccional.getX() - ancho);
            spriteMandoAcciones.setX(spriteMandoAcciones.getX() - ancho);
            spriteMandoPausa.setX(spriteMandoPausa.getX() - ancho);
            rDireccional.setX(rDireccional.getX() - ancho);
            rY.setX(rY.getX() - ancho);
            rA.setX(rA.getX() - ancho);
            rX.setX(rX.getX() - ancho);
            rB.setX(rB.getX() - ancho);
            rPausa.setX(rPausa.getX() - ancho);
            spriteMandoDireccional.setVisible(true);
            spriteMandoAcciones.setVisible(true);
            spriteMandoPausa.setVisible(true);
        }
        mostrarIconos();
    }

    public void ocultarControles(){
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float ancho = right - left;
        if(spriteMandoDireccional.getX() < right){
            spriteMandoDireccional.setVisible(false);
            spriteMandoAcciones.setVisible(false);
            spriteMandoPausa.setVisible(false);
            spriteMandoDireccional.setX(spriteMandoDireccional.getX() + ancho);
            spriteMandoAcciones.setX(spriteMandoAcciones.getX() + ancho);
            spriteMandoPausa.setX(spriteMandoPausa.getX() + ancho);
            rDireccional.setX(rDireccional.getX() + ancho);
            rY.setX(rY.getX() + ancho);
            rA.setX(rA.getX() + ancho);
            rX.setX(rX.getX() + ancho);
            rB.setX(rB.getX() + ancho);
            rPausa.setX(rPausa.getX() + ancho);
        }
    }

    public void mostrarFondoPausa(){
        rFondoPausa.setVisible(true);
    }

    public void ocultarFondoPausa(){
        rFondoPausa.setVisible(false);
    }

    public void mostrarBotonesPausa(){
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float bottom = escenario.getFillCropResolutionPolicy().getBottom();
        float top = escenario.getFillCropResolutionPolicy().getTop();
        float ancho = right - left;
        float alto = top - bottom;

        spriteBotonMenu.setX(spriteBotonMenu.getX() - ancho);spriteBotonMenu.setPosition(left + ancho/2, bottom + alto/2 - 3*spriteBotonMenu.getHeight()/4);
        rMenu.setPosition(spriteBotonMenu.getX(), spriteBotonMenu.getY());
        if(spriteBotonContinuar.getX() > right){
            spriteBotonContinuar.setX(spriteBotonContinuar.getX() - ancho);
            rContinuar.setX(spriteBotonContinuar.getX());
            spriteBotonContinuar.setVisible(true);
            spriteBotonMenu.setVisible(true);
        }
    }

    public void ocultarBotonesPausa(){
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float ancho = right - left;
        if(spriteBotonContinuar.getX() < right){
            spriteBotonContinuar.setVisible(false);
            spriteBotonMenu.setVisible(false);
            spriteBotonContinuar.setX(spriteBotonContinuar.getX() + ancho);
            spriteBotonMenu.setX(spriteBotonMenu.getX() + ancho);
            rContinuar.setX(rContinuar.getX() + ancho);
            rMenu.setX(rMenu.getX() + ancho);
        }
    }

    public void mostrarTitulos(){
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float bottom = escenario.getFillCropResolutionPolicy().getBottom();
        float top = escenario.getFillCropResolutionPolicy().getTop();
        float ancho = right - left;
        float alto = top - bottom;

        spriteBotonMenu.setPosition(left + ancho/2, bottom + alto/4);
        rMenu.setPosition(spriteBotonMenu.getX(), spriteBotonMenu.getY());

        tTitulo.setVisible(true);
        tParrafoIzquierda.setVisible(true);
        tParrafoDerecha.setVisible(true);
        spriteBotonMenu.setVisible(true);
    }

    public void ocultarTitulos(){
        float left = escenario.getFillCropResolutionPolicy().getLeft();
        float right = escenario.getFillCropResolutionPolicy().getRight();
        float ancho = right - left;
        if(spriteBotonMenu.getX() > right) {
            spriteBotonMenu.setX(spriteBotonMenu.getX() - ancho);
            rMenu.setX(spriteBotonMenu.getX());
        }
        tTitulo.setVisible(false);
        tParrafoIzquierda.setVisible(false);
        tParrafoDerecha.setVisible(false);
        spriteBotonMenu.setVisible(false);
    }

    public void ocultarIconos(){
        sVida.setVisible(false);
        sBombas.setVisible(false);
        tVida.setVisible(false);
        tBombas.setVisible(false);
    }

    public void mostrarIconos(){
        sVida.setVisible(true);
        sBombas.setVisible(true);
        tVida.setVisible(true);
        tBombas.setVisible(true);
    }

    public Text gettVida() {
        return tVida;
    }

    public void settVida(Text tVida) {
        this.tVida = tVida;
    }

    public Text gettBombas() {
        return tBombas;
    }

    public void settBombas(Text tBombas) {
        this.tBombas = tBombas;
    }
}
