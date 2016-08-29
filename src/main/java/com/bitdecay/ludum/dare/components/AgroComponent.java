package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;

public class AgroComponent implements IComponent {
    public float range = 50f;

    public AgroComponent(){}
    public AgroComponent(float range){
        this.range = range;
    }
}
