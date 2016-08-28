package com.bitdecay.ludum.dare.components.ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.actors.items.ShipPart;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

public class ShipPartComponent implements IComponent, IUpdate, IDraw, IRemoveable {
    private static final Vector2 DRAW_OFFSET = new Vector2(0, 5);

    private final PositionComponent position;
    private final AnimationComponent animation;
    private LevelInteractionComponent levelInteraction;

    private Player player;
    private ShipPart shipPart;
    private boolean shouldRemove;

    public final String name;

    public ShipPartComponent(String name, ShipPart shipPart) {
        this.name = name;
        this.shipPart = shipPart;

        position = new PositionComponent(0, 0);
        animation = new ShipPartAnimationComponent(name, true);
        animation.setPositionComponent(position);
    }

    public void addToPlayer(Player player, LevelInteractionComponent levelInteraction) {
        this.levelInteraction = levelInteraction;
        this.player = player;
        this.player.queueAdd(this);
    }

    public void removeFromPlayer(boolean toDeadShip) {
        shouldRemove = true;

        // Don't drop if being given to ship.
        if (toDeadShip) {
            return;
        }

        if (levelInteraction != null && shipPart != null) {
            shipPart.addToLevel(levelInteraction);
            Vector2 playerPosition = player.getPosition();
            shipPart.setPosition(playerPosition.x, playerPosition.y);

            levelInteraction = null;
        }

        player = null;
    }

    public void flipVerticalAxis(boolean flip) {
        animation.setFlipVerticalAxis(flip);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    @Override
    public void remove() {
        shouldRemove = false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        // Done here otherwise it trails the player by a bit.
        if (player != null) {
            Vector2 playerPosition = player.getPosition();
            position.x = playerPosition.x + DRAW_OFFSET.x;
            position.y = playerPosition.y + DRAW_OFFSET.y;
        }

        animation.draw(spriteBatch);
    }
}
