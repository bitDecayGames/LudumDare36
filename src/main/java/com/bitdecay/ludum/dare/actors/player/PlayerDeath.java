package com.bitdecay.ludum.dare.actors.player;

import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.components.RemoveNowComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IUpdate;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.AnimationListener;
import com.bytebreakstudios.animagic.animation.IFrameByFrameAnimation;

public class PlayerDeath extends GameObject implements IRemoveable,IUpdate {
    private AnimationComponent anim;
    private PositionComponent pos;
    private Player player;
    private LevelInteractionComponent levelComp;
    private boolean shouldRemove = false;
    private double respawnTimer;

    public PlayerDeath(AnimationComponent anim, PositionComponent pos, String deathSound, Player player, LevelInteractionComponent levelComponent) {
        this.anim = anim;
        this.pos = pos;
        levelComp = levelComponent;
        this.player = player;
        anim.animator.switchToAnimation("death");
        append(anim).append(pos);
        SoundLibrary.playSound(deathSound);
        setRespawnTimer();

    }

    @Override
    public void update(float delta){
        respawnTimer -= delta;
        if(respawnTimer<= 0 ){
            shouldRemove = true;
        }
        anim.animator.update(delta);
    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    @Override
    public void remove() {
        player.remove(RemoveNowComponent.class);
        player.setPosition(0,0);
        player.getHealth().health = player.getHealth().max;
        levelComp.addToObjects(player);
        System.out.println("Player Death removed");
    }

    public void setRespawnTimer(){
        respawnTimer = 1.50;
    }
}