package com.bitdecay.ludum.dare.actors.ai.behaviors;

import com.bitdecay.ludum.dare.actors.ai.Monkey;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.interfaces.IState;

public class JumpAttackBehavior implements IState {

    private Monkey ai;
    private Player player;
    private float agroRange;
    private FrustratedBehavior frustratedBehavior;

    private float timer;

    public JumpAttackBehavior(Monkey ai, Player player, FrustratedBehavior frustratedBehavior, float agroRange){
        this.ai = ai;
        this.player = player;
        this.frustratedBehavior = frustratedBehavior;
        this.agroRange = agroRange;
    }

    @Override
    public void enter() {
        System.out.println("Enter JumpAttack");
        resetTimer();
    }

    @Override
    public void exit() {}

    @Override
    public IState update(float delta) {
        timer -= delta;
        if (timer <= 0) {
            ai.debugMonkeyAi(player.getPosition().x, player.getPosition().y);
            resetTimer();
        }
        return null;
    }

    private void resetTimer(){
        timer = .2f;
    }
}
