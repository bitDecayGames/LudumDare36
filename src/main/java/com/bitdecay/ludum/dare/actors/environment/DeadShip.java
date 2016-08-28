package com.bitdecay.ludum.dare.actors.environment;

import com.bitdecay.jump.BitBody;
import com.bitdecay.ludum.dare.actors.InteractableObject;
import com.bitdecay.ludum.dare.actors.items.ShipPart;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.components.ship.DeadShipAnimationComponent;
import com.bitdecay.ludum.dare.components.ship.ShipPartComponent;

import java.util.HashSet;
import java.util.Set;

public class DeadShip extends InteractableObject {
    private static final Set<String> collectedParts = new HashSet<>();

    private DeadShip() {
        super(new DeadShipAnimationComponent());
    }

    public static DeadShip create(LevelInteractionComponent levelInteraction) {
        return ((DeadShip) (new DeadShip()).addToLevel(levelInteraction));
    }

    public static int getNumCollectedParts() {
        return collectedParts.size();
    }

    private static void addPart(String name) {
        collectedParts.add(name);
    }

    @Override
    public void contactStarted(BitBody bitBody) {
        String name = null;

        if (bitBody.userObject instanceof Player) {
            Player player = ((Player) bitBody.userObject);

            if (player.hasShipPart()) {
                ShipPartComponent component = player.getShipPart();
                component.removeFromPlayer(true);
                name = component.name;
            }
        } else if (bitBody.userObject instanceof ShipPart) {
            ShipPart part = ((ShipPart) bitBody.userObject);
            part.kill();
            name = part.name;
        }

        if (name != null) {
            addPart(name);
        }
    }
}
