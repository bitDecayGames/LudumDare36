package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;

public class HealthComponent implements IComponent {
    public float health = 0;
    public float max = 0;

    public HealthComponent() {}
    public HealthComponent(float health, float max){
        this.health = health;
        this.max = max;
    }
    public HealthComponent(float health){
        this.health = health;
        this.max = health;
    }
}
