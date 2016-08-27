package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

public class HurtComponent implements IComponent, IUpdate, IRemoveable {

    private InputComponent input;
    private TimedComponent timer;
    private boolean shouldRemove = false;

    public HurtComponent(InputComponent input, float timeLimit) {
        timer = new TimedComponent(timeLimit);
        this.input = input;
        if (input != null) input.inControl(false);
    }

    public HurtComponent(InputComponent input, float timeLimit, PhysicsComponent phys, float force, Vector2 direction) {
        this(input, timeLimit);
        if (phys != null) {
            // TODO: how to hell do you push a body around?  I want to implement knockback
        }
    }

    @Override
    public void update(float delta) {
        timer.update(delta);
    }


    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void shouldRemove(boolean shouldRemove) {
        this.shouldRemove = shouldRemove;
    }

    public boolean isTimerDone() {
        return timer.shouldRemove();
    }

    @Override
    public void remove() {
        if (input != null) input.inControl(true);
    }
}