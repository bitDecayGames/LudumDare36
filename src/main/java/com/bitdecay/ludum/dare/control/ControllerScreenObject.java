package com.bitdecay.ludum.dare.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;

import java.util.List;

public class ControllerScreenObject extends GameObject {
    // Contoller inputs to select controller.
    int keyboardSelect;
    int keyboardDeselect;
    int xbox360ControllerIndex;
    Xbox360Pad xbox360Select = Xbox360Pad.A;
    Xbox360Pad xbox360Deselect = Xbox360Pad.B;

    // What to render when a particular controller is selected.
    TextureRegionComponent aiTexture;
    TextureRegionComponent keyboardTexture;
    TextureRegionComponent xbox360Texture;

    public ControllerScreenObject(int keyboardSelect, int keyboardDeselect, int xbox360ControllerIndex, PositionComponent position, SizeComponent size) {
        this.keyboardSelect = keyboardSelect;
        this.keyboardDeselect = keyboardDeselect;
        this.xbox360ControllerIndex = xbox360ControllerIndex;

        aiTexture = getTextureCompnent("computer", position, size);
        keyboardTexture = getTextureCompnent("keyboard", position, size);
        xbox360Texture = getTextureCompnent("controller", position, size);

        append(position);
        append(size);

        // Default to AI selection.
        swapInput(new AIControlComponent());
    }

    private TextureRegionComponent getTextureCompnent(String name, PositionComponent p, SizeComponent s) {
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/ui.atlas", AnimagicTextureAtlas.class);
        return new TextureRegionComponent(atlas.findRegion(name), p, s);
    }

    private void updateControllerSelection() {
        InputComponent newInput = null;

        Controller xboxController = safeGetXboxController(xbox360ControllerIndex);
        // Reset to AI
        if (Gdx.input.isKeyPressed(keyboardDeselect) || safeGetXboxButton(xboxController, xbox360Deselect)) {
            newInput = new AIControlComponent();
        // Select Keyboard
        } else if (Gdx.input.isKeyPressed(keyboardSelect)) {
            newInput = new KeyboardControlComponent();
        // Select Xbox
        } else if (safeGetXboxButton(xboxController, xbox360Select)) {
            newInput = new GamepadControlComponent(xbox360ControllerIndex);
        }

        swapInput(newInput);
    }

    private Boolean safeGetXboxButton(Controller xboxController, Xbox360Pad button) {
        return xboxController != null ? xboxController.getButton(button.val) : false;
    }

    private Controller safeGetXboxController(int index) {
        Array<Controller> controllerList =  Controllers.getControllers();
        if (index < controllerList.size) {
            return controllerList.get(index);
        }

        return null;
    }

    private void swapInput(InputComponent input) {
        if (input == null) {
            return;
        }

        remove(InputComponent.class);
        remove(TextureRegionComponent.class);

        TextureRegionComponent texture;

        if (input instanceof AIControlComponent) texture = aiTexture;
        else if (input instanceof KeyboardControlComponent) texture = keyboardTexture;
        else if (input instanceof GamepadControlComponent) texture = xbox360Texture;
        else throw new RuntimeException("Input type not supported");

        append(texture);
        append(input);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        updateControllerSelection();
    }

    public IComponent getInputComponent() {
        List<IComponent> inputComponents = getComponents(InputComponent.class);
        if (inputComponents.size() != 1) {
            throw new Error("Only adding one InputComponent is allowed");
        }

        return inputComponents.get(0);
    }

    private void debugController() {
        for (Xbox360Pad value : Xbox360Pad.values()) {
            try {
                if (safeGetXboxButton(safeGetXboxController(xbox360ControllerIndex), value)) {
                    System.out.println(value.val + " hit");
                } else {
                    Controller c = safeGetXboxController(xbox360ControllerIndex);
                    float axisVal = c != null ? c.getAxis(value.val) : 0;
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
