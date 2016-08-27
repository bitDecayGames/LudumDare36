package com.bitdecay.ludum.dare.actors.items;


import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.components.SizeComponent;
import com.bitdecay.ludum.dare.components.TimedComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;

public class Projectile extends GameObject implements IRemoveable {

    protected TimedComponent timer;
    protected Player owner = null;

    protected Projectile(Player owner, TimedComponent timer, PositionComponent pos, SizeComponent size, AnimationComponent anim) {
        super(timer, pos, size, anim);
        this.owner = owner;
        this.timer = timer;
    }

    protected Projectile(TimedComponent timer, PositionComponent pos, SizeComponent size, AnimationComponent anim) {
        super(timer, pos, size, anim);
        this.timer = timer;
    }

    @Override
    public boolean shouldRemove() {
        return timer.shouldRemove();
    }

    @Override
    public void remove() {
        // TODO: clean up physics body here???
    }
}