package com.bitdecay.ludum.dare.actors.ai.bat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IAgroable;
import com.bitdecay.ludum.dare.interfaces.IState;

public class BatDropDown implements IState, IAgroable {

    private Bat me;
    private Vector2 home;
    private PhysicsComponent phys;
    private AnimationComponent anim;
    private Player player;
    private float agroRange;

    private float timer;

    public BatDropDown(Bat me, Vector2 home, PhysicsComponent phys, AnimationComponent anim, Player player, float agroRange){
        this.me = me;
        this.home = home;
        this.phys = phys;
        this.anim = anim;
        this.player = player;
        this.agroRange = agroRange;
    }

    @Override
    public void enter() {
        timer = 1;
        anim.animator.switchToAnimation("fly");
        phys.getBody().velocity = new BitPoint(MathUtils.random(-40f, 40f), MathUtils.random(-20f, -40f));
        me.broadcastAgro();
    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        timer -= delta;
        if (timer <= 0){
            phys.getBody().velocity = new BitPoint(0, 0);
            return new BatSporadicMovement(me, home, phys, anim, player, agroRange);
        }
        return null;
    }

    @Override
    public void goAgro() {
        // I'm already ABOUT to go agro, just gonna ignore
    }
}
