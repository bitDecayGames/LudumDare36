package com.bitdecay.ludum.dare.actors.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.ludum.dare.actors.InteractableObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.components.ship.ShipPartAnimationComponent;
import com.bitdecay.ludum.dare.components.ship.ShipPartComponent;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class ShipPart extends InteractableObject {
    public static final String ALIEN_GUN = "alienGun";
    public static final String COCKPIT = "cockpit";
    public static final String ENGINE = "engine";
    public static final String NAV_MODULE = "navModule";
    public static final String SHIELD_MODULE = "shieldModule";
    public static final String WINGS = "wings";


    // What s given to the player object when the player collects this.
    private final ShipPartComponent shipPartComponent;

    public final String name;

    public ShipPart(String name) {
        super(new ShipPartAnimationComponent(name, false));
        this.name = name;

        shipPartComponent = new ShipPartComponent(name, this);
    }

    public static TextureRegion getRegion(String name) {
        return atlas.findRegion("ship/pieces/" + name);
    }

    public static ShipPart alienGun() {
        return new ShipPart(ALIEN_GUN);
    }

    public static ShipPart cockpit() {
        return new ShipPart(COCKPIT);
    }

    public static ShipPart engine() {
        return new ShipPart(ENGINE);
    }

    public static ShipPart navModule() {
        return new ShipPart(NAV_MODULE);
    }

    public static ShipPart shieldModule() {
        return new ShipPart(SHIELD_MODULE);
    }

    public static ShipPart wings() {
        return new ShipPart(WINGS);
    }

    @Override
    public void contact(BitBody bitBody) {
        if (bitBody.userObject instanceof Player) {
            Player player = ((Player) bitBody.userObject);
            TimerComponent timer = player.getTimer();

            if (timer.complete() &&
                !player.hasShipPart() &&
                player.getInputComponent().isJustPressed(PlayerAction.DOWN)) {
                shipPartComponent.addToPlayer(player, levelInteraction);

                kill();
                timer.reset();
            }
        }
    }

    @Override
    public void remove() {
        super.remove();

        shouldRemove = false;
    }

    public void kill() {
        shouldRemove = true;
    }
}
