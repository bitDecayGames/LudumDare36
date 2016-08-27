package com.bitdecay.ludum.dare.actors.state;

import com.bitdecay.ludum.dare.interfaces.IComponent;

import java.util.Set;

public class StandState extends AbstractState {

    public StandState(Set<IComponent> components) {
        super(components);
    }

    public void enter() {
        super.enter();
        animationComponent.animator.switchToAnimation("stand");
    }

    public void exit() {
        super.exit();
    }
}
