package com.bitdecay.ludum.dare.actors.state;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.components.AttackComponent;
import com.bitdecay.ludum.dare.components.HurtComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.AnimationListener;
import com.bytebreakstudios.animagic.animation.IFrameByFrameAnimation;

import java.util.Set;

public class HurtState extends AbstractState implements AnimationListener {

    final static String KNOCKBACK_ANIMATION = "knockback";
    private Animation hurtAnimation;

    private HurtComponent hurt;

    private boolean done = false;

    final private AttackComponent attackComponent;

    public HurtState(Set<IComponent> components, AttackComponent attackComponent) {
        super(components);
        this.attackComponent = attackComponent;
    }

    public void enter() {
        super.enter();
        hurt = new HurtComponent(inputComponent, 1, physicsComponent, 1, new Vector2(1, 1));
        hurtAnimation = (Animation) animationComponent.animator.getAnimationByName(KNOCKBACK_ANIMATION);
        hurtAnimation.listen(this);
        animationComponent.animator.switchToAnimation(KNOCKBACK_ANIMATION);
    }

    public void exit() {
        super.exit();
        if (hurtAnimation != null) hurtAnimation.stopListening(this);
        hurt.remove();
    }

    public IState update(float delta) {
        return done ? getJumpState() : null;
    }

    @Override
    public void animationNotification(IFrameByFrameAnimation self, Animation.AnimationListenerState listenerState) {
        if (listenerState == Animation.AnimationListenerState.FINISHED) done = true;
    }
}
