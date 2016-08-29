package com.bitdecay.ludum.dare.actors.environment;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.ludum.dare.actors.InteractableObject;
import com.bitdecay.ludum.dare.actors.items.ShipPart;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.components.health.HealthTotemComponent;
import com.bitdecay.ludum.dare.components.ship.DeadShipAnimationComponent;
import com.bitdecay.ludum.dare.components.ship.ShipPartComponent;

import java.util.HashSet;
import java.util.Set;

public class DeadShip extends InteractableObject {
    private static final Set<String> collectedParts = new HashSet<>();

    private final HealthTotemComponent healthTotem;

    private DeadShip(Player player) {
        super(new DeadShipAnimationComponent("deadShip"));

        healthTotem = new HealthTotemComponent(position, player);

        append(healthTotem);
    }

    public static DeadShip create(Player player, LevelInteractionComponent levelInteraction) {
        return ((DeadShip) (new DeadShip(player)).addToLevel(levelInteraction));
    }

    public static int getNumCollectedParts() {
        return collectedParts.size();
    }

    private static void addPart(DeadShip ship, String name) {
        collectedParts.add(name);

        DeadShipAnimationComponent anim = new DeadShipAnimationComponent("overlay_" + name);
        anim.setPositionComponent(ship.position);
        ship.append(anim);
    }

    @Override
    public void contactStarted(BitBody bitBody) {
        String name = null;

        if (bitBody.userObject instanceof Player) {
            Player player = ((Player) bitBody.userObject);

            if (player.hasShipPart()) {
                ShipPartComponent component = player.getShipPart();
                component.removeFromPlayer(true, new BitPoint(0, 0));
                name = component.name;
            }
        } else if (bitBody.userObject instanceof ShipPart) {
            ShipPart part = ((ShipPart) bitBody.userObject);
            part.kill();
            name = part.name;
        }

        if (name != null) {
            addPart(this, name);
        }
    }
}
