package com.bitdecay.ludum.dare.components;

import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.ludum.dare.control.InputAction;

import java.util.HashSet;
import java.util.Set;

// TODO Mike implement for AI
public class AIControlComponent extends InputComponent {
    private Set<InputAction> currentActions = new HashSet<>();
    private Set<InputAction> previousActions = new HashSet<>();

    public AIControlComponent() {
        super();
    }

    @Override
    public boolean isPressed(InputAction action) {
        return action != null && inControl && currentActions.contains(action);
    }

    public void pressed(InputAction action) {
        currentActions.add(action);
        previousActions.add(action);
    }

    @Override
    public boolean isJustPressed(InputAction action) {
        return action != null && inControl && currentActions.contains(action) && !previousActions.contains(action);
    }

    public void justPressed(InputAction action) {
        currentActions.add(action);
        previousActions.remove(action);
    }


    @Override
    public boolean isPressed(PlayerAction action) {
        InputAction act = InputAction.forPlayerAction(action);
        return isPressed(act);
    }

    public void pressed(PlayerAction action) {
        InputAction a = InputAction.forPlayerAction(action);
        pressed(a);
    }

    @Override
    public boolean isJustPressed(PlayerAction action) {
        InputAction act = InputAction.forPlayerAction(action);
        return isJustPressed(act);
    }

    public void justPressed(PlayerAction action) {
        InputAction act = InputAction.forPlayerAction(action);
        justPressed(act);
    }


    @Override
    public void update(float delta) {
        previousActions.addAll(currentActions);
        currentActions.clear();
    }


    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
