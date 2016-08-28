package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

public class TimerComponent implements IComponent, IUpdate {
    private final float maxTime;
    private float time;

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

    public boolean complete() {
        return time <= 0;
    }

    public void reset() {
        time = maxTime;
    }
}
