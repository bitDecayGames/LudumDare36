package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

/**
 * Created by Monday on 8/28/2016.
 */
public class LaserExplodeComponent implements IRemoveable, IUpdate, IComponent, IDraw {

    final float LIFE_SPAN = .05f;

    ParticleEffect fx;

    boolean started = false;

    float particleDuration = 0;

    public LaserExplodeComponent(PositionComponent pos) {
        fx = new ParticleEffect();
        fx.load(Gdx.files.internal("particle/laserHit4.p"), Gdx.files.internal("particle"));
        fx.scaleEffect(.5f);
        fx.setPosition(pos.x + 14, pos.y + 13);
    }

    @Override
    public void update(float delta) {
        if (!started) {
            started = true;
            fx.start();
        } else {
            fx.update(delta);
            particleDuration += delta;
        }

        if (particleDuration >= LIFE_SPAN) {
            fx.allowCompletion();
        }
    }

    @Override
    public boolean shouldRemove() {
        return fx.isComplete();
    }

    @Override
    public void remove() {

    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        fx.draw(spriteBatch);
    }
}
