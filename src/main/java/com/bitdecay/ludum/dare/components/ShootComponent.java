package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

/**
 * Created by jacob on 8/27/16.
 */
public class ShootComponent implements IUpdate, IComponent{
    private KeyboardControlComponent keyboard;

    public ShootComponent (KeyboardControlComponent keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public void update(float delta) {
        if(keyboard.isJustPressed(InputAction.SHOOT)){
            //TODO shoot a thing
        }
    }
}
