package com.bitdecay.ludum.dare.actors.items;

import com.bitdecay.jump.BitBody;
import com.bitdecay.ludum.dare.actors.InteractableObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.components.ship.ShipPartAnimationComponent;
import com.bitdecay.ludum.dare.components.ship.ShipPartComponent;

public class ShipPart extends InteractableObject {
    // What s given to the player object when the player collects this.
    private final ShipPartComponent shipPartComponent;

    private ShipPart(String name) {
        super(new ShipPartAnimationComponent(name, false));

        shipPartComponent = new ShipPartComponent(name);
    }

    private static ShipPart create(String name, LevelInteractionComponent levelInteraction) {
        return ((ShipPart) (new ShipPart(name)).addToLevel(levelInteraction));
    }

    public static ShipPart alienGun(LevelInteractionComponent levelInteraction) {
        return create("alienGun", levelInteraction);
    }

    public static ShipPart cockpit(LevelInteractionComponent levelInteraction) {
        return create("cockpit", levelInteraction);
    }

    public static ShipPart engine(LevelInteractionComponent levelInteraction) {
        return create("engine", levelInteraction);
    }

    public static ShipPart navModule(LevelInteractionComponent levelInteraction) {
        return create("navModule", levelInteraction);
    }

    public static ShipPart shieldModule(LevelInteractionComponent levelInteraction) {
        return create("shieldModule", levelInteraction);
    }

    public static ShipPart wings(LevelInteractionComponent levelInteraction) {
        return create("wings", levelInteraction);
    }

    @Override
    public void contactStarted(BitBody bitBody) {
        if (bitBody.userObject instanceof Player) {
            Player player = ((Player) bitBody.userObject);

            if (!player.hasShipPart()) {
                shipPartComponent.addToPlayer(player);

                shouldRemove = true;
            }
        }
    }
}
