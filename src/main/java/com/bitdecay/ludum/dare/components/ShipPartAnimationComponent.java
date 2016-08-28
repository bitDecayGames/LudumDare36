package com.bitdecay.ludum.dare.components;

import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class ShipPartAnimationComponent extends AnimationComponent {
    public ShipPartAnimationComponent(String name, PositionComponent position) {
        super(name, position, 1f, null);

        animator.addAnimation(new Animation("static", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("ship/pieces/" + name).toArray(AnimagicTextureRegion.class)));

        animator.switchToAnimation("static");
    }
}
