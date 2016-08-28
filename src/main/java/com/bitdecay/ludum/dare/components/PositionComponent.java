package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.interfaces.IComponent;

public class PositionComponent implements IComponent {
    public float x = 0;
    public float y = 0;

    public PositionComponent(){}
    public PositionComponent(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2 toVector2(){
        return new Vector2(x, y);
    }
}
