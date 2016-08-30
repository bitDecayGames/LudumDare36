package com.bitdecay.ludum.dare.actors.ai;

import com.bitdecay.ludum.dare.actors.ai.behaviors.AttackBehavior;
import com.bitdecay.ludum.dare.actors.ai.behaviors.EnemyIdleBehavior;
import com.bitdecay.ludum.dare.actors.ai.behaviors.RoamBehavior;
import com.bitdecay.ludum.dare.actors.ai.behaviors.RunAttackBehavior;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.Animator;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.ArrayList;
import java.util.List;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class Warrior extends Enemy {

    public static final float SCALE = 0.5f;

    public Warrior(float startX, float startY, Player player){
        super(startX, startY, player);
    }

    @Override
    protected String NAME() {
        return "warrior";
    }

    @Override
    protected float SCALE() {
        return Warrior.SCALE;
    }

    @Override
    protected float SIZE() {
        return 12;
    }

    @Override
    protected int WALKING_SPEED() {
        return 20;
    }

    @Override
    protected int ATTACK_SPEED() {
        return 100;
    }

    @Override
    protected int FLYING_SPEED() {
        return 60;
    }

    @Override
    protected float AGRO_RANGE() {
        return 64;
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
        return 16;
    }

    @Override
    protected int ATTACK_STRENGTH(){
        return 20;
    }

    @Override
    protected String HURT_SFX(){
        return "MonkeyHurt";
    }

    @Override
    protected String DEATH_SFX(){
        return "MonkeyVaporize";
    }

    @Override
    protected List<String> getIdleAnimations() {
        List<String> idles = new ArrayList<>();
        idles.add("stand");
        return idles;
    }

    @Override
    protected void setupAnimation(Animator a) {
        a.addAnimation(new Animation("walk", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.3f), atlas.findRegions("native/run").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("native/stand").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("jump", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.2f), atlas.findRegions("native/jump").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("death", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("native/death").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("pain", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.2f), atlas.findRegions("native/pain").toArray(AnimagicTextureRegion.class)));
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
        return new RunAttackBehavior(this, player, input, ATTACK_RANGE());
    }
}
