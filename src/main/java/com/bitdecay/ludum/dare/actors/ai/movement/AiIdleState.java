package com.bitdecay.ludum.dare.actors.ai.movement;

import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.interfaces.IState;

public class AiIdleState implements IState {

    private AIControlComponent input;
    private boolean wasMovementBlocked;

    public AiIdleState(AIControlComponent input, boolean wasMovementBlocked) {
        this.input = input;
        this.wasMovementBlocked = wasMovementBlocked;
    }

    @Override
    public void enter() {
    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        return null;
    }

    public boolean wasMovementBlocked(){ return wasMovementBlocked; }
}


