package com.bitdecay.ludum.dare.control;

import com.bitdecay.jump.control.PlayerAction;

public enum InputAction {
    LEFT(PlayerAction.LEFT),
    RIGHT(PlayerAction.RIGHT),
    DOWN(PlayerAction.DOWN),
    JUMP(PlayerAction.JUMP),
    SHOOT();

    public PlayerAction playerAction = null;

    private InputAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    private InputAction() {
    }

    public static InputAction forPlayerAction(PlayerAction playerAction) {
        for (InputAction action : values()) {
            if (action.playerAction == playerAction) return action;
        }
        return null;
    }
}
