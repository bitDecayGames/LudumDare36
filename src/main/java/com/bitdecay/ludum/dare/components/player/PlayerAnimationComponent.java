package com.bitdecay.ludum.dare.components.player;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class PlayerAnimationComponent extends AnimationComponent {
    public PlayerAnimationComponent(PositionComponent position, boolean carry) {
        super("player", position, 0.5f, new Vector2(0, -3));

        String carryStr = carry ? "/carry" : "";
        String standPath = carry ? "player/run/carry/1" : "player/stand";

        animator.addAnimation(new Animation("run", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/run" + carryStr).toArray(AnimagicTextureRegion.class)));
        animator.addAnimation(new Animation("jump", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/idle" + carryStr).toArray(AnimagicTextureRegion.class)));
        animator.addAnimation(new Animation("apex", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/apex" + carryStr).toArray(AnimagicTextureRegion.class)));
        animator.addAnimation(new Animation("fall", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/fall" + carryStr).toArray(AnimagicTextureRegion.class)));
        animator.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions(standPath).toArray(AnimagicTextureRegion.class)));
        animator.addAnimation(new Animation("wall", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/fall" + carryStr).toArray(AnimagicTextureRegion.class)));

        animator.switchToAnimation("stand");
    }
}