package com.bitdecay.ludum.dare.components;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.common.RenderState;
import com.bitdecay.jump.common.StateListener;
import com.bitdecay.jump.render.JumperRenderState;
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.interfaces.IComponent;

public class JumperRenderStateComponent implements IComponent, StateListener {
    private final JumperRenderStateWatcher watcher;

    private JumperRenderState previousState;
    private JumperRenderState currentState;

    public JumperRenderStateComponent() {
        watcher = new JumperRenderStateWatcher();
        watcher.addListener(this);
    }

    @Override
    public void stateChanged(RenderState renderState) {
        if (renderState instanceof JumperRenderState) {
            previousState = currentState;
            currentState = (JumperRenderState) renderState;
        }
    }

    public void addToBody(JumperBody body) {
        body.renderStateWatcher = watcher;
    }

    protected JumperRenderState getPreviousState() {
        return previousState;
    }

    protected JumperRenderState getCurrentState() {
        return currentState;
    }
}
