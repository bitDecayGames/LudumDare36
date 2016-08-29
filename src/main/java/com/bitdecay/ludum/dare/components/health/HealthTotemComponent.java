package com.bitdecay.ludum.dare.components.health;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.HealthComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

public class HealthTotemComponent implements IComponent, IUpdate, IDraw {
    private static final float HEALTH_RADIUS = (float) Math.pow(20, 2);
    private static final float HEALTH_RECHARGE = 10;

    private final PositionComponent position;
    private final Player player;

    private ParticleEffect fx;

    public HealthTotemComponent(PositionComponent position, Player player) {
        this.position = position;
        this.player = player;
        fx = new ParticleEffect();
        fx.load(Gdx.files.internal("particle/health.p"), Gdx.files.internal("particle"));
        fx.scaleEffect(.5f);
    }

    boolean started = false;
    @Override
    public void update(float delta) {
        fx.setPosition(position.x + 10, position.y + 15);
        if (started) {
            fx.update(delta);
        }
        Vector2 pos = new Vector2(position.x, position.y);

        if (player.getPosition().dst2(pos) <= HEALTH_RADIUS) {
            if (!started) {
                fx.start();
                started = true;
            }
            HealthComponent health = player.getHealth();
            health.health += HEALTH_RECHARGE * delta;

            // Clamp health.
            if (health.health >= health.max) {
                health.health = health.max;
            }
        } else {
            started = false;
            fx.reset();
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        fx.draw(spriteBatch);
    }
}
