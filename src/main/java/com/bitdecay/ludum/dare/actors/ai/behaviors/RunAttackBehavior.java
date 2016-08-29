package com.bitdecay.ludum.dare.actors.ai.behaviors;

import com.bitdecay.ludum.dare.actors.ai.Enemy;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.interfaces.IState;

public class RunAttackBehavior extends AttackBehavior {

    private Enemy ai;
    private Player player;
    private AIControlComponent input;
    private float attackRange;

    private float timer;

    public RunAttackBehavior(Enemy ai, Player player, AIControlComponent input, float attackRange){
        this.ai = ai;
        this.player = player;
        this.input = input;
        this.attackRange = attackRange;
    }

    @Override
    public void enter() {
        ai.setAiMovementGoal(player.getPosition().x, player.getPosition().y);
        resetTimer();
    }

    @Override
    public void exit() {}

    @Override
    public IState update(float delta) {
        timer -= delta;
        if (timer <= 0) {
            if (ai.getPosition().dst(player.getPosition()) > attackRange) ai.setAiMovementGoal(player.getPosition().x, player.getPosition().y);
            resetTimer();
        }
        return null;
    }

    private void resetTimer(){
        timer = .2f;
    }
}
