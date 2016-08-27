package com.bitdecay.ludum.dare.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;

public class InputUtil {

    private final static float STICK_TOLERANCE = 0.5f;

    // Check for global keyboard or gamepad input. Use only for menus.
    public static Boolean checkInputs(int keyBoardKey, Xbox360Pad value) {
        return  Gdx.input.isKeyPressed(keyBoardKey) || checkAllXboxInputs(value);
    }

    private static Boolean checkAllXboxInputs(Xbox360Pad value) {
        Array<Controller> cList = Controllers.getControllers();
        for (int i = 0; i < cList.size; i++) {

            Controller c = cList.get(i);

//            debugController(c);

            // If one is true, return;
            switch (value) {
                case LS_DOWN:
                    return c.getAxis(value.val) > STICK_TOLERANCE;
                case LS_UP:
                    return c.getAxis(value.val) < -STICK_TOLERANCE;
                case LS_RIGHT:
                    return c.getAxis(value.val) > STICK_TOLERANCE;
                case LS_LEFT:
                    return c.getAxis(value.val) < -STICK_TOLERANCE;
                default:
                    return c.getButton(value.val);
            }
        }

        return false;
    }

    private static void debugController(Controller c) {
        for (Xbox360Pad value : Xbox360Pad.values()) {
            try {
                if (c.getButton(value.val)) {
                    System.out.println(value.val + " hit");
                } else {
                    float axisVal = c.getAxis(value.val);
                    if (axisVal > 0.2f || axisVal < -0.2f) {
                        System.out.println(value.val + " stick");
                    }
                }
            } catch (Error e) {
                // No-op
            }
        }
    }
}
