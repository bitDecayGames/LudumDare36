package com.bitdecay.ludum.dare.actors.state;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.Facing;
import com.bitdecay.ludum.dare.actors.projectile.Punch;
import com.bitdecay.ludum.dare.components.FacePunchingComponent;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.AnimationListener;
import com.bytebreakstudios.animagic.animation.IFrameByFrameAnimation;

import java.util.Set;

public class PunchState extends AbstractState implements AnimationListener {
    private Animation punchAnimation;
    private boolean done = false;
    protected FacePunchingComponent facePunch = new FacePunchingComponent();

    LevelInteractionComponent levelComponent;

    public PunchState(Set<IComponent> components) {
        super(components);

        components.forEach(comp -> {
            if (comp instanceof LevelInteractionComponent) levelComponent = (LevelInteractionComponent) comp;
        });

        if (levelComponent == null || levelComponent.getObjects() == null || levelComponent.getWorld() == null) {
            throw new RuntimeException(LevelInteractionComponent.class + " with valid data expected");
        }
    }

    public Boolean shouldRun(IState currentState) {
        if (inputComponent.isJustPressed(InputAction.PUNCH)) {
            if (!(currentState instanceof PunchState)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void enter() {
        super.enter();
        int randomizer = (int) (Math.random() * 4) + 1;
        SoundLibrary.playSound("Punch"+randomizer);
        Vector2 direction = new Vector2();
        if (!physicsComponent.getBody().grounded) {
            if (inputComponent.isPressed(InputAction.UP)) {
                switchToAnimation("punch/jumping/up");
                facePunch.FacePunchingJupingUp = true;
                direction.y = 1;
            } else if (inputComponent.isPressed(InputAction.DOWN)) {
                switchToAnimation("punch/jumping/down");
                facePunch.FacePunchingDown = true;
                direction.y = -1;
            } else {
                switchToAnimation("punch/jumping/front");
                direction.x = 1;
            }
        } else {
            if (inputComponent.isPressed(InputAction.UP)) {
                switchToAnimation("punch/up");
                facePunch.FacePunchingUp = true;
                direction.y = 1;
            } else {
                switchToAnimation("punch/front");
                direction.x = 1;
            }
        }

        Facing facing = physicsComponent.getBody().facing;
        switch (facing) {
            case LEFT:
                direction.x *= -1;
                break;
            case RIGHT:
                // Do nothing.
                break;
            default:
                throw new Error("Invalid facing set");
        }

        addProjectile(direction);
    }

    private void switchToAnimation(String animationName) {
        animationComponent.animator.switchToAnimation(animationName);
        IFrameByFrameAnimation anim = animationComponent.animator.getAnimationByName(animationName);
        if (anim != null && anim instanceof Animation) {
            punchAnimation = (Animation) anim;
            punchAnimation.listen(this);
        }
    }

    // TODO Mike Logan this is where you'll do punch projectile stuff.
    private void addProjectile(Vector2 direction) {
        Punch punch = new Punch(positionComponent, direction, levelComponent, physicsComponent, facePunch);
        levelComponent.addToLevel(punch, punch.getPhysics());
    }

    @Override
    public void exit() {
        super.exit();
        if (punchAnimation != null) punchAnimation.stopListening(this);
        if(facePunch != null) {
            facePunch.FacePunchingDown = false;
            facePunch.FacePunchingUp = false;
            facePunch.FacePunchingJupingUp = false;
        }
    }

    @Override
    public IState update(float delta) {
        return done ? getJumpState() : null;
    }

    @Override
    public void animationNotification(IFrameByFrameAnimation self, Animation.AnimationListenerState listenerState) {
        if (listenerState == Animation.AnimationListenerState.FINISHED) done = true;
    }
}
