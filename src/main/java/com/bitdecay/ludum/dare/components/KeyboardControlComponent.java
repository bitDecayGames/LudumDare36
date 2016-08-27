package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.jump.control.PlayerInputController;
import com.bitdecay.jump.gdx.input.GDXControls;
import com.bitdecay.jump.gdx.input.KeyState;
import com.bitdecay.ludum.dare.actors.player.Player;
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

    public boolean enabled = true;

    static Map<InputAction, Integer> actionsToKeys = new HashMap<>();

    static {
        actionsToKeys.put(InputAction.SHOOT, Input.Keys.SPACE);
    }

    public KeyboardControlComponent() {
        keyboard = new GDXControls();
        keyboard.set(PlayerAction.JUMP, new KeyState(Input.Keys.W));
        keyboard.set(PlayerAction.LEFT, new KeyState(Input.Keys.A));
        keyboard.set(PlayerAction.RIGHT, new KeyState(Input.Keys.D));
        keyboard.set(PlayerAction.DOWN, new KeyState(Input.Keys.S));
    }

    @Override
    public void update(float delta) {
        previousPresses.clear();
        for (InputAction action : InputAction.values()) {
            if (isPressed(action)) previousPresses.add(action);
        }
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
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

    @Override
    public boolean isPressed(InputAction action) {
        int key;
        if (!inControl) return false;
        else if (actionsToKeys.containsKey(action)) key = actionsToKeys.get(action);
        else return false;

        return Gdx.input.isKeyPressed(key);
    }

    @Override
    public boolean isJustPressed(InputAction action) {
        return inControl && isPressed(action) && !previousPresses.contains(action);
    }
}
