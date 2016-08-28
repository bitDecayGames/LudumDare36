package com.bitdecay.ludum.dare.actors.environment;

import com.bitdecay.jump.BitBody;
import com.bitdecay.ludum.dare.actors.InteractableObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.components.ship.DeadShipAnimationComponent;

public class DeadShip extends InteractableObject {

    private DeadShip() {
        super(new DeadShipAnimationComponent());
    }

    public static DeadShip create(LevelInteractionComponent levelInteraction) {
        return ((DeadShip) (new DeadShip()).addToLevel(levelInteraction));
    }

    @Override
    public void contactStarted(BitBody bitBody) {
        if (bitBody.userObject instanceof Player) {
            Player player = ((Player) bitBody.userObject);

            if (player.hasShipPart()) {
                player.getShipPart().removeFromPlayer();
            }
        }
    }
}
