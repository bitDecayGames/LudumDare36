package com.bitdecay.ludum.dare.components.ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
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

    public void removeFromPlayer(boolean toDeadShip, BitPoint velocity) {
        shouldRemove = true;

        // Don't drop if being given to ship.
        if (toDeadShip) {
            return;
        }

        if (levelInteraction != null && shipPart != null) {
            shipPart.addToLevel(levelInteraction);
            BitRectangle playerAabb = player.phys.getBody().aabb;
            BitPoint playerCenter = playerAabb.center();
            BitPoint partStartPos = new BitPoint(playerCenter);
            BitPoint initialVelocity = player.phys.getBody().velocity;
            BitPoint currentAttempt = player.phys.getBody().currentAttempt;
            BitRectangle partAabb = shipPart.getPhysics().getBody().aabb;
            if (currentAttempt.x > 0) {
                float partRightEdge = partStartPos.x + partAabb.width;
                float playerRightEdge = playerAabb.xy.x + playerAabb.width;
                if (partRightEdge > playerRightEdge) {
                    partStartPos.x -= partRightEdge - playerRightEdge;
                }
            }

            if (currentAttempt.x < 0) {
                float partLeftEdge = partStartPos.x;
                float playerLeftEdge = playerAabb.xy.x;
                if (partLeftEdge < playerLeftEdge) {
                    partStartPos.x += playerLeftEdge - partLeftEdge;
                }
            }

            if (currentAttempt.y > 0) {
                float partTopEdge = partStartPos.y + partAabb.height;
                float playerTopEdge = playerAabb.xy.y + playerAabb.height;
                if (partTopEdge > playerTopEdge) {
                    partStartPos.y -= partTopEdge - playerTopEdge;
                }
            }

            if (currentAttempt.y < 0) {
                float partBottomEdge = partStartPos.y;
                float playerBottomEdge = playerAabb.xy.y;
                if (partBottomEdge < playerBottomEdge) {
                    partStartPos.y += playerBottomEdge - partBottomEdge;
                }
            }

            System.out.println("Initial part vel: " + initialVelocity);
            shipPart.getPhysics().getBody().velocity.set(initialVelocity.scale(3.5f));
            shipPart.setPosition(partStartPos.x, partStartPos.y);

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
