package com.bitdecay.ludum.dare.actors.state;

import com.bitdecay.ludum.dare.interfaces.IComponent;

import java.util.Set;

public class ApexState extends AbstractState {

    public ApexState(Set<IComponent> components) {
        super(components);
    }

    public void enter() {
        super.enter();
        animationComponent.animator.switchToAnimation("apex");
    }

    public void exit() {
        super.exit();
    }
}
