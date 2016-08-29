package com.bitdecay.ludum.dare.actors.ai.behaviors;

import com.bitdecay.ludum.dare.actors.ai.Monkey;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IState;

public class FrustratedBehavior implements IState {

    private Monkey ai;
    private AIControlComponent input;
    private RoamBehavior roamBehavior;

    float timer;

    public FrustratedBehavior(Monkey ai, AIControlComponent input, RoamBehavior roamBehavior){
        this.ai = ai;
        this.input = input;
        this.roamBehavior = roamBehavior;
    }

    @Override
    public void enter() {
        System.out.println("Enter Frustrated");
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
