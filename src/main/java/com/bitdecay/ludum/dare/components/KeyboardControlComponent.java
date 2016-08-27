package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.jump.gdx.input.GDXControls;
import com.bitdecay.ludum.dare.control.InputAction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Adapter for interfacing keyboard with JUMP
 */
public class KeyboardControlComponent extends InputComponent {

    private GDXControls keyboard;

    private Set<InputAction> previousPresses = new HashSet<>();

    static Map<InputAction, Integer> actionsToKeys = new HashMap<>();

    static {
        actionsToKeys.put(InputAction.JUMP, Input.Keys.SPACE);
        actionsToKeys.put(InputAction.LEFT, Input.Keys.A);
        actionsToKeys.put(InputAction.RIGHT, Input.Keys.D);
        actionsToKeys.put(InputAction.UP, Input.Keys.W);
        actionsToKeys.put(InputAction.DOWN, Input.Keys.S);
        actionsToKeys.put(InputAction.PUNCH, Input.Keys.Q);
        actionsToKeys.put(InputAction.PROJECTILE, Input.Keys.E);
    }

    public KeyboardControlComponent() {
        keyboard = GDXControls.defaultMapping;
    }

    @Override
    public void update(float delta) {
        previousPresses.clear();
        for (InputAction action : InputAction.values()) {
            if (isPressed(action)) previousPresses.add(action);
        }
    }

    @Override
    public boolean isJustPressed(InputAction action) {
        return inControl && isPressed(action) && !previousPresses.contains(action);
    }

    @Override
    public boolean isPressed(InputAction action) {
        int key;
        if (!inControl) return false;
        else if (actionsToKeys.containsKey(action)) key = actionsToKeys.get(action);
        else return false;

        return Gdx.input.isKeyPressed(key);
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

    @Override
    public boolean isJustPressed(PlayerAction playerAction) {
        InputAction action = InputAction.forPlayerAction(playerAction);
        return inControl && action != null && keyboard.isJustPressed(playerAction);
    }

    @Override
    public boolean isPressed(PlayerAction playerAction) {
        InputAction action = InputAction.forPlayerAction(playerAction);
        return inControl && action != null && keyboard.isPressed(playerAction);
    }
}
