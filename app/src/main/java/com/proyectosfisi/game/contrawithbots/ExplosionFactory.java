package com.proyectosfisi.game.contrawithbots;

import android.util.Log;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by edson on 06/05/2015.
 */
public class ExplosionFactory {

    private static ExplosionFactory instance;

    private Stack<Explosion> pilaExplosionInactivas;
    private ArrayList<Explosion> listaExplosionActivas;
    private int countId;

    private ExplosionFactory(){
        pilaExplosionInactivas = new Stack<>();
        listaExplosionActivas = new ArrayList<>();
        countId = 0;
    }

    public static ExplosionFactory getInstance(){
        if(instance == null){
            synchronized (ExplosionFactory.class){
                if (instance == null){
                    instance = new ExplosionFactory();
                }
            }
        }
        return instance;
    }

    public synchronized int getNextId(){
        countId += 1;
        Log.i("Explosion", "numero: " + countId);
        return countId;
    }

    public synchronized Explosion getExplosion(Escenario escenario, final VertexBufferObjectManager pVertexBufferObjectManager){
        if(pilaExplosionInactivas.isEmpty()){
            Explosion explosion = new Explosion(escenario, escenario.getmExplosionTextureRegion(), pVertexBufferObjectManager);
            explosion.setsExplosion(escenario.getsExplosion());
            escenario.getLayerPlayer().attachChild(explosion);
            getListaExplosionActivas().add(explosion);
            return explosion;
        }else{
            Explosion explosion = pilaExplosionInactivas.pop();
            getListaExplosionActivas().add(explosion);
            return explosion;
        }
    }

    public synchronized void inactivarExplosionsActivas(){
        Explosion[] explosions = listaExplosionActivas.toArray(new Explosion[listaExplosionActivas.size()]);
        for(Explosion explosion : explosions){
            explosion.inactivar();
        }
    }

    public synchronized void removeExplosion(Explosion explosion){
        getListaExplosionActivas().remove(explosion);
        pilaExplosionInactivas.push(explosion);
    }

    public static void reset(){
        instance = null;
    }

    public synchronized ArrayList<Explosion> getListaExplosionActivas() {
        return listaExplosionActivas;
    }
}
