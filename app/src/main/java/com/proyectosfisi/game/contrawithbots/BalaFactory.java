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
            listaBalasActivas.add(bala);
            return bala;
        }else{
            return pilaBalasInactivas.pop();
        }
    }

    public synchronized void removeBala(Bala bala){
        listaBalasActivas.remove(bala);
        pilaBalasInactivas.push(bala);
    }

    public void inactivar(){
        while(!listaBalasActivas.isEmpty()){
            Bala bala = listaBalasActivas.remove(0);
            bala.inactivar();
            pilaBalasInactivas.push(bala);
        }
    }

}
