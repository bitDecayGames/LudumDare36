package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

public class ShipPartComponent implements IComponent, IUpdate, IDraw, IRemoveable {
    private static final Vector2 DRAW_OFFSET = new Vector2(-5, 7);

    private final PositionComponent position;
    private final AnimationComponent animation;

    private Player player;
    private boolean shouldRemove;

    public ShipPartComponent(String name) {
        position = new PositionComponent(0, 0);
        animation = new ShipPartAnimationComponent(name, position, true);
    }

    public void addToPlayer(Player player) {
        this.player = player;
        this.player.queueAdd(this);
    }

    public void removeFromPlayer() {
        player = null;
        shouldRemove = true;
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
