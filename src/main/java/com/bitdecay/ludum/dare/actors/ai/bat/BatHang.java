package com.bitdecay.ludum.dare.actors.ai.bat;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IAgroable;
import com.bitdecay.ludum.dare.interfaces.IState;

public class BatHang implements IState, IAgroable {

    private Bat me;
    private Vector2 home;
    private PhysicsComponent phys;
    private AnimationComponent anim;
    private Player player;
    private float agroRange;
    private boolean goAgro = false;


    public BatHang(Bat me, Vector2 home, PhysicsComponent phys, AnimationComponent anim, Player player, float agroRange){
        this.me = me;
        this.home = home;
        this.phys = phys;
        this.anim = anim;
        this.player = player;
        this.agroRange = agroRange;
    }

    @Override
    public void enter() {
        anim.animator.switchToAnimation("hang");
        me.unBroadcastAgro();
    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        if (diffToPlayer() < agroRange) goAgro();
        if (goAgro) return new BatDropDown(me, home, phys, anim, player, agroRange);
        return null;
    }

    private float diffToPlayer(){
        BitPoint me = this.phys.getBody().aabb.center();
        return player.getPosition().dst(me.x, me.y);
    }

    @Override
    public void goAgro() {
        goAgro = true;
        me.broadcastAgro();
    }
}
