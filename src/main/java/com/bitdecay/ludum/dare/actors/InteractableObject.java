package com.bitdecay.ludum.dare.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.ContactListener;
import com.bitdecay.jump.control.ControlMap;
import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.jump.control.PlayerInputController;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.components.ship.DeadShipAnimationComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;

public class InteractableObject extends StateMachine implements ContactListener, IRemoveable {
    protected boolean shouldRemove = false;

    protected final PositionComponent position;
    protected final SizeComponent size;
    protected final AnimationComponent animation;
    protected final PhysicsComponent physics;
    protected LevelInteractionComponent levelInteraction;

    public InteractableObject(AnimationComponent animation) {
        super();

        position = new PositionComponent(0, 0);
        size = new SizeComponent(100, 100);

        this.animation = animation;
        this.animation.setPositionComponent(position);

        TextureRegion region = animation.animator.getFrame();
        float width = region.getRegionWidth() * DeadShipAnimationComponent.SCALE;
        float height = region.getRegionHeight() * DeadShipAnimationComponent.SCALE;
        physics = createPhysics(width, height);

        append(position).append(size).append(animation).append(physics);
    }

    protected PhysicsComponent createPhysics(float width, float height) {
        BitBody bitBody = new JumperBody();
        bitBody.bodyType = BodyType.DYNAMIC;
        bitBody.aabb.set(new BitRectangle(0, 0, width, height));
        bitBody.userObject = this;
        bitBody.props.deceleration = 1000;
        bitBody.props.maxVoluntarySpeed = 0;
        bitBody.props.airDeceleration = 1;
        bitBody.controller = new PlayerInputController(new ControlMap() {
            @Override
            public void enable() {

            }

            @Override
            public void disable() {

            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public boolean isJustPressed(PlayerAction playerAction) {
                return false;
            }

            @Override
            public boolean isPressed(PlayerAction playerAction) {
                return false;
            }
        });
        bitBody.addContactListener(this);

        return new PhysicsComponent(bitBody, position, size);
    }

    public PhysicsComponent getPhysics() {
        return physics;
    }

    public InteractableObject addToLevel(LevelInteractionComponent levelInteraction) {
        if (this.levelInteraction != null) {
            this.levelInteraction.removeFromLevel(this, physics);
        }
        remove(LevelInteractionComponent.class);

        this.levelInteraction = levelInteraction;
        append(levelInteraction);

        levelInteraction.addToLevel(this, physics);

        remove(RemoveNowComponent.class);

        return this;
    }

    public InteractableObject setPosition(float x, float y) {
        physics.getBody().aabb.xy.set(x, y);

        return this;
    }

    public InteractableObject setPostion(Vector2 position) {
        return setPosition(position.x, position.y);
    }

    @Override
    public void contactStarted(BitBody bitBody) {

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
            levelInteraction.removeFromLevel(this, physics);
        }
    }
}
