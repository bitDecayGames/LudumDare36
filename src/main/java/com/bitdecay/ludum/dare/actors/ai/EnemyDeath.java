package com.bitdecay.ludum.dare.actors.ai;

import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.AnimationListener;
import com.bytebreakstudios.animagic.animation.IFrameByFrameAnimation;

public class EnemyDeath extends GameObject implements IRemoveable, AnimationListener {
    private AnimationComponent anim;
    private PositionComponent pos;
    private boolean shouldRemove = false;

    public EnemyDeath(AnimationComponent anim, PositionComponent pos, String deathSound) {
        this.anim = anim;
        this.pos = pos;
        anim.animator.switchToAnimation("death");
        ((Animation) anim.animator.getAnimationByName("death")).listen(this);
        append(anim).append(pos);
        SoundLibrary.playSound(deathSound);
    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    @Override
    public void remove() {

    }

    @Override
    public void animationNotification(IFrameByFrameAnimation iFrameByFrameAnimation, Animation.AnimationListenerState animationListenerState) {
        if (animationListenerState == Animation.AnimationListenerState.FINISHED) shouldRemove = true;
    }
}
