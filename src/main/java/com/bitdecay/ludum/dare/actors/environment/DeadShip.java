package com.bitdecay.ludum.dare.actors.environment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.collision.ContactListener;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.ludum.dare.actors.StateMachine;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.components.ship.DeadShipAnimationComponent;

public class DeadShip extends StateMachine implements ContactListener {
    private final PositionComponent position;
    private final SizeComponent size;
    private final AnimationComponent animation;
    private final PhysicsComponent physics;
    private LevelInteractionComponent levelInteraction;

    private DeadShip() {
        super();

        position = new PositionComponent(0, 0);
        size = new SizeComponent(100, 100);

        animation = new DeadShipAnimationComponent(position);
        TextureRegion region = animation.animator.getFrame();
        float width = region.getRegionWidth() * DeadShipAnimationComponent.SCALE;
        float height = region.getRegionHeight() * DeadShipAnimationComponent.SCALE;

        physics = createPhysics(width, height);

        append(position).append(size).append(animation).append(physics);
    }

    public static DeadShip create(LevelInteractionComponent levelInteraction) {
        return (new DeadShip()).addToLevel(levelInteraction);
    }

    private PhysicsComponent createPhysics(float width, float height) {
        BitBody bitBody = new BitBody();
        bitBody.bodyType = BodyType.DYNAMIC;
        bitBody.aabb.set(new BitRectangle(0, 0, width, height));
        bitBody.userObject = this;
        bitBody.addContactListener(this);

        return new PhysicsComponent(bitBody, position, size);
    }

    private DeadShip addToLevel(LevelInteractionComponent levelInteraction) {
        if (this.levelInteraction != null) {
            this.levelInteraction.removeFromLevel(physics);
        }
        remove(LevelInteractionComponent.class);

        this.levelInteraction = levelInteraction;
        append(levelInteraction);

        levelInteraction.addToLevel(this, physics);

        return this;
    }

    public DeadShip setPosition(float x, float y) {
        physics.getBody().aabb.xy = new BitPoint(x, y);

        return this;
    }

    public DeadShip setPostion(Vector2 position) {
        return setPosition(position.x, position.y);
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

    @Override
    public void contact(BitBody bitBody) {

    }

    @Override
    public void contactEnded(BitBody bitBody) {

    }

    @Override
    public void crushed() {

    }
}
