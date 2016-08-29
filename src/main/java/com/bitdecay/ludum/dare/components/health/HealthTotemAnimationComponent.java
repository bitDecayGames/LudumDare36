package com.bitdecay.ludum.dare.components.health;

import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class HealthTotemAnimationComponent extends AnimationComponent {
    public static final float SCALE = 0.5f;

    public HealthTotemAnimationComponent() {
        super("healthTotem", null, SCALE, null);

        animator.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.25f), atlas.findRegions("decor/shrines").toArray(AnimagicTextureRegion.class)));

        animator.switchToAnimation("stand");
    }
}
