package com.bitdecay.ludum.dare.components.health;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.HealthComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

public class HealthTotemComponent implements IComponent, IUpdate {
    private static final float HEALTH_RADIUS = (float) Math.pow(20, 2);
    private static final float HEALTH_RECHARGE = 10;

    private final PositionComponent position;
    private final Player player;

    public HealthTotemComponent(PositionComponent position, Player player) {
        this.position = position;
        this.player = player;
    }

    @Override
    public void update(float delta) {
        Vector2 pos = new Vector2(position.x, position.y);

        if (player.getPosition().dst2(pos) <= HEALTH_RADIUS) {

            HealthComponent health = player.getHealth();
            health.health += HEALTH_RECHARGE * delta;

            // Clamp health.
            if (health.health >= health.max) {
                health.health = health.max;
            }
        }
    }
}
