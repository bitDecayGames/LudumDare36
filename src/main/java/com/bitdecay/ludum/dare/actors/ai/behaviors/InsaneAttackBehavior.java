package com.bitdecay.ludum.dare.actors.ai.behaviors;

import com.bitdecay.ludum.dare.actors.ai.Enemy;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IState;

public class InsaneAttackBehavior extends AttackBehavior {

    private Enemy ai;
    private Player player;
    private AIControlComponent input;

    public InsaneAttackBehavior(Enemy ai, Player player, AIControlComponent input){
        this.ai = ai;
        this.player = player;
        this.input = input;
    }

    @Override
    public void enter() {
    }

    @Override
    public void exit() {}

    @Override
    public IState update(float delta) {
        if (!playerCloseToCenter()){
            if (moveLeft()) input.pressed(InputAction.LEFT);
            else input.pressed(InputAction.RIGHT);
        }
        if (ai.isGrounded()) input.justPressed(InputAction.JUMP);
        return null;
    }

    private float diff(){
        float meX = ai.getCenter().x;
        float playerX = player.getPosition().x;
        return meX - playerX;
    }

    private boolean moveLeft(){
        return diff() > 0;
    }

    private boolean playerCloseToCenter(){
        return Math.abs(diff()) < 10;
    }
}
