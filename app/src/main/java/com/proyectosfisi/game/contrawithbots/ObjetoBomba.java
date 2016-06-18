package com.proyectosfisi.game.contrawithbots;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by edson on 08/07/2015.
 */
public class ObjetoBomba extends Objeto {

    public ObjetoBomba(Escenario escenario, float relativeX, float relativeY, final TiledTextureRegion pTextureRegion, final TiledTextureRegion mBulletTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(escenario, relativeX, relativeY, pTextureRegion, mBulletTextureRegion, pVertexBufferObjectManager);
    }

    protected void animacion() {
        long f = (long)(FRAME_TIME * 0.5f);
        animate(new long[]{f, f, f, f, f, f, f, f, f, f, f, f, f, f}, new int[]{0, 1, 2, 3, 2, 1, 0, 1, 2, 3, 0, 3, 2, 1}, true);
    }

    protected void cogioObjeto(PersonajeJugador jugador) {
        jugador.actualizarBombas(1);
    }
}
