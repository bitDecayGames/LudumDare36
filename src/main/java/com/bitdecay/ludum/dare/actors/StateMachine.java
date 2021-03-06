package com.bitdecay.ludum.dare.actors;

import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IState;

public class StateMachine extends GameObject {
    protected IState activeState = null;
    public boolean debug = false;

    public StateMachine() {
        super();
    }

    public StateMachine(IComponent... components) {
        super(components);
    }

    public void setActiveState(IState value) {
        if (activeState == value) {
            return;
        } else if (activeState != null && value != null && activeState.getClass().equals(value.getClass())) {
            return;
        }

        if (debug) logStateTransition(activeState, value);

        if (activeState != null) {
            activeState.exit();
        }

        activeState = value;

        if (activeState != null) {
            activeState.enter();
        }
    }

    public IState getActiveState(){
        return activeState;
    }

    @Override
    public void update(float delta) {
        if (activeState != null) {
            IState newState = activeState.update(delta);
            if (newState != null) {
                setActiveState(newState);
            }
        }
        super.update(delta);
    }

    private void logStateTransition(IState currentState, IState newState) {
        System.out.println(currentState != null ? currentState.getClass().getSimpleName() : "Nothing" + " -> " + newState != null ? newState.getClass().getSimpleName() : "Nothing");
    }
}
