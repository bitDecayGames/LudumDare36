package com.bitdecay.ludum.dare.components.player;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class PlayerAnimationComponent extends AnimationComponent {

    public enum AnimType {
        NORMAL,
        CARRY,
        SHOOT
    }

    public PlayerAnimationComponent(PositionComponent position, AnimType type) {
        super("player", position, 0.5f, new Vector2(0, -3));


        switch (type) {
            case NORMAL:
                animator.addAnimation(new Animation("run", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/run").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("jump", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/idle").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("apex", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/apex").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("fall", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/fall").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("player/stand").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("wall", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/fall").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("hurt", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.5f), atlas.findRegions("player/pain").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("death", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.16f), atlas.findRegions("player/death").toArray(AnimagicTextureRegion.class)));
                break;
            case CARRY:
                animator.addAnimation(new Animation("run", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/run/carry").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("jump", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/idle/carry").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("apex", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/apex/carry").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("fall", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/fall/carry").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("player/run/carry/1").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("wall", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/fall/carry").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("hurt", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.5f), atlas.findRegions("player/pain").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("death", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.16f), atlas.findRegions("player/death").toArray(AnimagicTextureRegion.class)));
                break;
            case SHOOT:
                animator.addAnimation(new Animation("run", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/run/shoot").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("jump", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/shoot").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("apex", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/apex/shoot").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("fall", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/fall/shoot").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("player/stand/shoot").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("wall", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("player/jetpack/fall/shoot").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("hurt", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.5f), atlas.findRegions("player/pain").toArray(AnimagicTextureRegion.class)));
                animator.addAnimation(new Animation("death", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.16f), atlas.findRegions("player/death").toArray(AnimagicTextureRegion.class)));
                break;
        }

        animator.switchToAnimation("stand");
    }
}