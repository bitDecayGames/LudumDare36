package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

/**
 * Created by Monday on 8/28/2016.
 */
public class JetPackParticleComponent implements IComponent, IUpdate, IDraw {
    ParticleEffect fx;
    private JumperBody body;

    public JetPackParticleComponent(JumperBody body) {
        this.body = body;
        fx = new ParticleEffect();
        fx.load(Gdx.files.internal("particle/flame.p"), Gdx.files.internal("particle"));
        fx.scaleEffect(.5f);
        fx.setPosition(body.aabb.xy.x, body.aabb.xy.y);
    }

    @Override
    public void update(float delta) {
        fx.setPosition(body.aabb.xy.x, body.aabb.xy.y);
        fx.update(delta);
        fx.start();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        fx.draw(spriteBatch);
    }
}
