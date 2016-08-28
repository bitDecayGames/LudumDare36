package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

/**
 * Timer component that tracks down from a specified time.
 */
public class TimerComponent implements IComponent, IUpdate {
    private final float maxTime;
    private float time;

    /**
     * Create a TimerComponent.
     *
     * @param maxTime boolean Time before this timer is completed each cycle.
     */
    public TimerComponent(float maxTime) {
        this.maxTime = maxTime;
        time = this.maxTime;
    }

    @Override
    public void update(float delta) {
        time -= delta;
        if (time < 0) {
            time = 0f;
        }
    }

    /**
     * Has the timer completed.
     *
     * @return boolean
     */
    public boolean complete() {
        return time <= 0;
    }

    /**
     * Reset the timer to its starting value.
     */
    public void reset() {
        time = maxTime;
    }
}
