package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.math.Vector2;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class PlayerAnimationComponent extends AnimationComponent {
    public PlayerAnimationComponent(PositionComponent position) {
        super("player", position, 1f, new Vector2());

        animator.addAnimation(new Animation("run", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/run").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("jump", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("jump").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("apex", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("apex").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("fall", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("fall").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("knockback", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("knockback").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("punch/front", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/front").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("punch/jumping/down", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/jumping/down").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("punch/jumping/front", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/jumping/front").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("punch/jumping/up", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("punch/jumping/up").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("punch/up", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/up").toArray(AnimagicTextureRegion.class)));
        animator.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("player/stand").toArray(AnimagicTextureRegion.class)));
//        animator.addAnimation(new Animation("wall", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("wall").toArray(AnimagicTextureRegion.class)));
//
        animator.switchToAnimation("stand");
    }
}