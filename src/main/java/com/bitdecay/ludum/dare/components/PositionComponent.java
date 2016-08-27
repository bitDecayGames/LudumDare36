package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;

public class PositionComponent implements IComponent {
    public float x = 0;
    public float y = 0;

    public PositionComponent(){}
    public PositionComponent(float x, float y){
        this.x = x;
        this.y = y;
    }
}
