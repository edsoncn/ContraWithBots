package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by edson on 06/05/2015.
 */
public class BalaFactory {

    private static BalaFactory instance;

    private Stack<Bala> pilaBalasInactivas;
    private ArrayList<Bala> listaBalasActivas;
    private int countId;

    private BalaFactory(){
        pilaBalasInactivas = new Stack<>();
        listaBalasActivas = new ArrayList<>();
        countId = 0;
    }

    public static BalaFactory getInstance(){
        if(instance == null){
            synchronized (BalaFactory.class){
                if (instance == null){
                    instance = new BalaFactory();
                }
            }
        }
        return instance;
    }

    public synchronized int getNextId(){
        countId += 1;
        Log.i("Bala", "numero: " + countId);
        return countId;
    }

    public synchronized Bala getBala(Escenario escenario, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager){
        if(pilaBalasInactivas.isEmpty()){
            Bala bala = new Bala(escenario, mBulletTextureRegion, pVertexBufferObjectManager);
            escenario.getLayerBullets().attachChild(bala);
            getListaBalasActivas().add(bala);
            return bala;
        }else{
            Bala bala = pilaBalasInactivas.pop();
            getListaBalasActivas().add(bala);
            return bala;
        }
    }

    public synchronized void inactivarBalasActivas(){
        Bala[] balas = listaBalasActivas.toArray(new Bala[listaBalasActivas.size()]);
        for(Bala bala : balas){
            bala.inactivar();
        }
    }

    public synchronized void removeBala(Bala bala){
        getListaBalasActivas().remove(bala);
        pilaBalasInactivas.push(bala);
    }

    public static void reset(){
        instance = null;
    }

    public synchronized ArrayList<Bala> getListaBalasActivas() {
        return listaBalasActivas;
    }
}
