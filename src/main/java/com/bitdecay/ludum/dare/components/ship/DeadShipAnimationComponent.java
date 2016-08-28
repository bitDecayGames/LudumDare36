package com.bitdecay.ludum.dare.components.ship;

import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class DeadShipAnimationComponent extends AnimationComponent {
    public static final float SCALE = 0.5f;

    public DeadShipAnimationComponent(String name) {
        super(name, null, SCALE, null);

        animator.addAnimation(new Animation("static", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("ship/pieces/" + name).toArray(AnimagicTextureRegion.class)));

        animator.switchToAnimation("static");
    }
}
