package com.bitdecay.ludum.dare.actors.ai.bat;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IAgroable;
import com.bitdecay.ludum.dare.interfaces.IState;

public class BatFlyHome implements IState, IAgroable {

    private Bat me;
    private Vector2 home;
    private PhysicsComponent phys;
    private AnimationComponent anim;
    private Player player;
    private float agroRange;
    private boolean goAgro = false;

    public BatFlyHome(Bat me, Vector2 home, PhysicsComponent phys, AnimationComponent anim, Player player, float agroRange){
        this.me = me;
        this.home = home;
        this.phys = phys;
        this.anim = anim;
        this.player = player;
        this.agroRange = agroRange;
    }

    @Override
    public void enter() {
        anim.animator.switchToAnimation("fly");
        me.unBroadcastAgro();
    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        this.phys.getBody().velocity = diffToHome();
        if (position().dst(home) < 10){
            phys.getBody().velocity.set(0, 0);
            phys.getBody().aabb.xy.set(home.x, home.y);
            return new BatHang(me, home, phys, anim, player, agroRange);
        } else if (goAgro){
            return new BatSporadicMovement(me, home, phys, anim, player, agroRange);
        }
        return null;
    }

    private Vector2 position(){
        BitPoint p = this.phys.getBody().aabb.xy;
        return new Vector2(p.x, p.y);
    }

    private BitPoint diffToHome(){
        BitPoint me = this.phys.getBody().aabb.center();
        return new BitPoint(home.x, home.y).minus(me);
    }

    @Override
    public void goAgro() {
        goAgro = true;
        me.broadcastAgro();
    }
}
