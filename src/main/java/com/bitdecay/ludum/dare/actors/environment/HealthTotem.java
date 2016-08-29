package com.bitdecay.ludum.dare.actors.environment;

import com.bitdecay.ludum.dare.actors.InteractableObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.health.HealthTotemAnimationComponent;
import com.bitdecay.ludum.dare.components.health.HealthTotemComponent;

public class HealthTotem extends InteractableObject {
    private final HealthTotemComponent healthTotem;

    private final Player player;

    public HealthTotem(Player player) {
        super(new HealthTotemAnimationComponent());
        this.player = player;

        animation.setPositionComponent(position);
        healthTotem = new HealthTotemComponent(position, player);

        append(healthTotem);
    }
}
