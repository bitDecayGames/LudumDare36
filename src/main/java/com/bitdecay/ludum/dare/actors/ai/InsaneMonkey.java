package com.bitdecay.ludum.dare.actors.ai;

import com.bitdecay.ludum.dare.actors.ai.behaviors.*;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.Animator;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.ArrayList;
import java.util.List;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class InsaneMonkey extends Enemy {

    public static final float SCALE = 0.5f;

    @Override
    protected String NAME() {
        return "insanemonkey";
    }

    @Override
    protected float SCALE() {
        return InsaneMonkey.SCALE;
    }

    @Override
    protected float SIZE() {
        return 8;
    }

    @Override
    protected int WALKING_SPEED() {
        return 20;
    }

    @Override
    protected int ATTACK_SPEED() {
        return 80;
    }

    @Override
    protected int FLYING_SPEED() {
        return 60;
    }

    @Override
    protected float AGRO_RANGE() {
        return 50;
    }

    @Override
    protected float ATTACK_RANGE() {
        return 16;
    }

    @Override
    protected float START_HEALTH() {
        return 20;
    }

    @Override
    protected float MAX_HEALTH() {
        return 20;
    }

    @Override
    protected float JUMP_HEIGHT() {
        return 32;
    }

    @Override
    protected int ATTACK_STRENGTH(){
        return 10;
    }

    @Override
    protected String HURT_SFX(){
        return "MonkeyHurt";
    }

    @Override
    protected String DEATH_SFX(){
        return "MonkeyVaporize";
    }

    public InsaneMonkey(float startX, float startY, Player player) { super(startX, startY, player); }

    @Override
    protected List<String> getIdleAnimations(){
        List<String> idles = new ArrayList<>();
        idles.add("stand");
        idles.add("scratch");
        idles.add("banana");
        return idles;
    }

    @Override
    protected void setupAnimation(Animator a) {
        a.addAnimation(new Animation("walk", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("insanemonkey/walk").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("insanemonkey/stand").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("jump", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.2f), atlas.findRegions("insanemonkey/jump").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("scratch", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.2f), atlas.findRegions("insanemonkey/idles/scratch").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("banana", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.2f), atlas.findRegions("insanemonkey/idles/eat").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("death", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.2f), atlas.findRegions("insanemonkey/death").toArray(AnimagicTextureRegion.class)));
        a.switchToAnimation("stand");
    }

    @Override
    protected void setUpBehaviors() {
        idleBehavior = new EnemyIdleBehavior(anim.animator, getIdleAnimations());
        roamBehavior = new RoamBehavior(this, getCenter(), 100);
        idleBehavior.roamBehavior = roamBehavior;
        behavior.setActiveState(idleBehavior);
    }

    @Override
    protected AttackBehavior getAttack() {
        return new InsaneAttackBehavior(this, player, input);
    }
}
