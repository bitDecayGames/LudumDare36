package com.bitdecay.ludum.dare.actors.state;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.Facing;
import com.bitdecay.jump.common.RenderState;
import com.bitdecay.jump.common.StateListener;
import com.bitdecay.jump.render.JumperRenderState;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.InputComponent;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bitdecay.ludum.dare.util.SoundLibrary;

import java.util.Set;

public abstract class AbstractState implements IState, StateListener {
    protected Set<IComponent> components;
    protected PhysicsComponent physicsComponent;
    protected AnimationComponent animationComponent;
    protected InputComponent inputComponent;
    protected PositionComponent positionComponent;

    private IState jumpState = null;

    protected RenderState currentRenderState;
    protected RenderState previousRenderState;

    public AbstractState(Set<IComponent> components) {
        this.components = components;
        components.forEach(comp -> {
            if (comp instanceof PhysicsComponent) physicsComponent = (PhysicsComponent) comp;
            if (comp instanceof AnimationComponent) animationComponent = (AnimationComponent) comp;
            if (comp instanceof InputComponent) inputComponent = (InputComponent) comp;
            if (comp instanceof PositionComponent) positionComponent = (PositionComponent) comp;
        });

        checkValidData();
    }

    @Override
    public void enter() {
        BitBody body = physicsComponent.getBody();
        body.renderStateWatcher.addListener(this);
        stateChanged(body.renderStateWatcher.getState());
    }

    @Override
    public void exit() {
        physicsComponent.getBody().renderStateWatcher.removeListener(this);
    }

    @Override
    public IState update(float delta) {
        IState internalState = internalUpdate(delta);
        return internalState != null ? internalState : jumpState;
    }

    protected IState internalUpdate(float delta) {
        return null;
    }

    @Override
    public void stateChanged(RenderState state) {
        previousRenderState = currentRenderState;
        currentRenderState = state;

        updateFacing();

        switch ((JumperRenderState) state) {
            case RIGHT_STANDING:
            case LEFT_STANDING:
                jumpState = new StandState(components);
                break;
            case RIGHT_RUNNING:
            case LEFT_RUNNING:
                jumpState = new RunState(components);
                break;
            case RIGHT_JUMPING:
            case LEFT_JUMPING:
//                int randomizer = (int) (Math.random() * 4) + 1;
                SoundLibrary.playSound("Jetpack");
                jumpState = new JumpState(components);
                break;
            case RIGHT_APEX:
            case LEFT_APEX:
                SoundLibrary.stopSound("Jetpack");
                jumpState = new ApexState(components);
                break;
            case RIGHT_FALLING:
            case LEFT_FALLING:
                SoundLibrary.stopSound("Jetpack");
                jumpState = new FallState(components);
                break;
            case RIGHT_AIR_AGAINST_WALL:
            case LEFT_AIR_AGAINST_WALL:
                jumpState = new AirWallState(components);
                break;
            case RIGHT_GROUNDED_AGAINST_WALL:
            case LEFT_GROUNDED_AGAINST_WALL:
                jumpState = new GroundWallState(components);
                break;
            case RIGHT_PUSHED:
            case LEFT_PUSHED:
                jumpState = new PushState(components);
                break;
            default:
                jumpState = null;
        }
    }

    protected IState getJumpState() {
        return jumpState;
    }

    private void checkValidData() {
        if (physicsComponent == null) {
            throw createDataError(PhysicsComponent.class);
        } else if (physicsComponent.getBody() == null || physicsComponent.getBody().renderStateWatcher == null) {
            throw new RuntimeException(PhysicsComponent.class + " missing data");
        }
        if (animationComponent == null) {
            throw createDataError(AnimationComponent.class);
        }
        if (inputComponent == null) {
            throw createDataError(InputComponent.class);
        }
        if (positionComponent == null) {
            throw createDataError(PositionComponent.class);
        }
    }

    private RuntimeException createDataError(Class clazz) {
        return new RuntimeException("No " + clazz + " provided");
    }

    private void updateFacing() {
        Facing facing = physicsComponent.getBody().facing;
        switch (facing) {
            case LEFT:
                animationComponent.setFlipVerticalAxis(true);
                break;
            case RIGHT:
                animationComponent.setFlipVerticalAxis(false);
                break;
            default:
                throw new Error("Invalid facing set");
        }
    }
}
