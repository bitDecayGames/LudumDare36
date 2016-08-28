package com.bitdecay.ludum.dare.actors.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.collision.ContactListener;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.components.ship.ShipPartAnimationComponent;
import com.bitdecay.ludum.dare.components.ship.ShipPartComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;

public class ShipPart extends GameObject implements ContactListener, IRemoveable {
    private boolean shouldRemove = false;

    private final PositionComponent position;
    private final SizeComponent size;
    private final AnimationComponent animation;
    private final PhysicsComponent physics;
    private LevelInteractionComponent levelInteraction;

    // What s given to the player object when the player collects this.
    private final ShipPartComponent shipPartComponent;

    private ShipPart(String name) {
        super();

        shipPartComponent = new ShipPartComponent(name);

        position = new PositionComponent(0, 0);
        size = new SizeComponent(100, 100);

        animation = new ShipPartAnimationComponent(name, position, false);
        TextureRegion region = animation.animator.getFrame();
        float width = region.getRegionWidth() * ShipPartAnimationComponent.SCALE;
        float height = region.getRegionHeight() * ShipPartAnimationComponent.SCALE;

        physics = createPhysics(width, height);

        append(position).append(size).append(animation).append(physics);
    }

    private static ShipPart create(String name, LevelInteractionComponent levelInteraction) {
        return (new ShipPart(name)).addToLevel(levelInteraction);
    }

    private PhysicsComponent createPhysics(float width, float height) {
        BitBody bitBody = new BitBody();
        bitBody.bodyType = BodyType.DYNAMIC;
        bitBody.aabb.set(new BitRectangle(0, 0, width, height));
        bitBody.userObject = this;
        bitBody.addContactListener(this);

        return new PhysicsComponent(bitBody, position, size);
    }

    private ShipPart addToLevel(LevelInteractionComponent levelInteraction) {
        if (this.levelInteraction != null) {
            this.levelInteraction.removeFromLevel(physics);
        }
        remove(LevelInteractionComponent.class);

        this.levelInteraction = levelInteraction;
        append(levelInteraction);

        levelInteraction.addToLevel(this, physics);

        return this;
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

    public ShipPart setPosition(float x, float y) {
        physics.getBody().aabb.xy = new BitPoint(x, y);

        return this;
    }

    public ShipPart setPostion(Vector2 position) {
        return setPosition(position.x, position.y);
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

    @Override
    public void contact(BitBody bitBody) {

    }

    @Override
    public void contactEnded(BitBody bitBody) {

    }

    @Override
    public void crushed() {

    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    @Override
    public void remove() {
        if (levelInteraction != null) {
            levelInteraction.removeFromLevel(physics);
        }
    }
}
