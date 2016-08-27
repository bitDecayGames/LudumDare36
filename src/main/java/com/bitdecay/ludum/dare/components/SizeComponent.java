package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;

public class SizeComponent implements IComponent {
    public float w = 0;
    public float h = 0;

    public SizeComponent(){}
    public SizeComponent(float width, float height){
        this.w = width;
        this.h = height;
    }

    public void set(float width, float height) {
        w = width;
        h = height;
    }
}
