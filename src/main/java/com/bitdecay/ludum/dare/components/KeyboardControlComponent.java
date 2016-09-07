package com.bitdecay.ludum.dare.components;

import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.jump.gdx.input.GDXControls;
import com.bitdecay.ludum.dare.control.GameControls;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.control.MultiKeyState;

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

    static Map<InputAction, MultiKeyState> actionsToKeys = new HashMap<>();

    static {
        actionsToKeys.put(InputAction.SHOOT, GameControls.Fire.state());
    }

    public KeyboardControlComponent() {
        keyboard = new GDXControls();
        keyboard.set(PlayerAction.JUMP, GameControls.JetPack.state());
        keyboard.set(PlayerAction.LEFT, GameControls.Left.state());
        keyboard.set(PlayerAction.RIGHT, GameControls.Right.state());
        keyboard.set(PlayerAction.DOWN, GameControls.PickUp.state());
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
        return inControl && actionsToKeys.containsKey(action) && actionsToKeys.get(action).isPressed();
    }

    @Override
    public boolean isJustPressed(InputAction action) {
        return inControl && actionsToKeys.containsKey(action) && actionsToKeys.get(action).isJustPressed();
    }
}
