package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

public class AgroCooldownComponent implements IComponent, IUpdate, IRemoveable {
    public float timer = 10;
    private boolean shouldRemove = false;

    @Override
    public void update(float delta) {
        timer -= delta;
        if (timer <= 0) shouldRemove = true;
    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    @Override
    public void remove() {}
}
