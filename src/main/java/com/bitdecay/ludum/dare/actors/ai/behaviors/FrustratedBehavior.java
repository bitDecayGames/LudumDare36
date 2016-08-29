package com.bitdecay.ludum.dare.actors.ai.behaviors;

import com.bitdecay.ludum.dare.actors.ai.Enemy;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IState;

public class FrustratedBehavior implements IState {

    private Enemy ai;
    private AIControlComponent input;
    private RoamBehavior roamBehavior;

    float timer;

    public FrustratedBehavior(Enemy ai, AIControlComponent input, RoamBehavior roamBehavior){
        this.ai = ai;
        this.input = input;
        this.roamBehavior = roamBehavior;
    }

    @Override
    public void enter() {
        timer = 2;
    }

    @Override
    public void exit() {}

    @Override
    public IState update(float delta) {
        timer -= delta;
        if (timer <= 0) return roamBehavior;
        else if(ai.isGrounded()) input.justPressed(InputAction.JUMP);
        return null;
    }
}
