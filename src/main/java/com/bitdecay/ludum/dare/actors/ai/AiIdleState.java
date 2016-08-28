package com.bitdecay.ludum.dare.actors.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IState;



public class AiIdleState implements IState {

    private AIControlComponent input;

    public AiIdleState(AIControlComponent input) {
        this.input = input;
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            input.justPressed(InputAction.JUMP);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            input.pressed(InputAction.LEFT);
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            input.pressed(InputAction.RIGHT);
        return null;
    }
}


