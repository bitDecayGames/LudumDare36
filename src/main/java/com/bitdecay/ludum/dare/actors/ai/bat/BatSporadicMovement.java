package com.bitdecay.ludum.dare.actors.ai.bat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IAgroable;
import com.bitdecay.ludum.dare.interfaces.IState;

public class BatSporadicMovement implements IState, IAgroable {

    private Bat me;
    private Vector2 home;
    private PhysicsComponent phys;
    private AnimationComponent anim;
    private Player player;
    private float agroRange;

    private float timer;

    public BatSporadicMovement(Bat me, Vector2 home, PhysicsComponent phys, AnimationComponent anim, Player player, float agroRange){
        this.me = me;
        this.home = home;
        this.phys = phys;
        this.anim = anim;
        this.player = player;
        this.agroRange = agroRange;
    }

    @Override
    public void enter() {
        resetTimer();
        anim.animator.switchToAnimation("fly");
        me.broadcastAgro();
    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        sporadicMovement();
        timer -= delta;
        if (timer <= 0 && diffToPlayer() > agroRange){
            return new BatFlyHome(me, home, phys, anim, player, agroRange);
        }
        return null;
    }

    private void sporadicMovement(){
        this.phys.getBody().velocity = this.phys.getBody().velocity.plus(range(20f), range(10f));
        this.phys.getBody().velocity = this.phys.getBody().velocity.normalize();
        this.phys.getBody().velocity = this.phys.getBody().velocity.scale(rnd(20f, 40f));
    }

    private float rnd(float start, float end){ return MathUtils.random(start, end); }

    private float range(float range){ return MathUtils.random(-range, range); }

    private float diffToPlayer(){
        BitPoint me = this.phys.getBody().aabb.center();
        return player.getPosition().dst(me.x, me.y);
    }

    private void resetTimer(){
        timer = 5;
    }

    @Override
    public void goAgro() {
        // since I'm already agro, I'll just reset my agro timer
        resetTimer();
    }
}
