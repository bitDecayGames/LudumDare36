package com.bitdecay.ludum.dare.components.ship;

import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class ShipPartAnimationComponent extends AnimationComponent {
    public static final float SCALE = 0.5f;

    public ShipPartAnimationComponent(String name, Boolean carry) {
        super(name, null, SCALE, null);

        // Is the ship part a game object or are we carrying it?
        String imageName = "ship/pieces/" + (carry ? "carry/" : "") + name;

        animator.addAnimation(new Animation("static", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions(imageName).toArray(AnimagicTextureRegion.class)));

        animator.switchToAnimation("static");
    }
}
