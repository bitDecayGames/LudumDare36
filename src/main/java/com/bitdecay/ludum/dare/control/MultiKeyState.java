package com.bitdecay.ludum.dare.control;

import com.badlogic.gdx.Gdx;
import com.bitdecay.jump.gdx.input.KeyState;

import java.util.ArrayList;
import java.util.List;

public class MultiKeyState extends KeyState {

    private List<Integer> keys = new ArrayList<>();

    public MultiKeyState(int... keys) {
        super(keys[0]);
        for (int key : keys) this.keys.add(key);
    }

    public MultiKeyState(List<Integer> keys){
        super(keys.get(0));
        this.keys.addAll(keys);
    }


    @Override
    public boolean isJustPressed() {
        return keys.stream().filter(key -> Gdx.input.isKeyJustPressed(key)).findFirst().isPresent();
    }


    @Override
    public boolean isPressed() {
        return keys.stream().filter(key -> Gdx.input.isKeyPressed(key)).findFirst().isPresent();
    }
}
