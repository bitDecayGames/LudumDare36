package com.bitdecay.ludum.dare.components.upgradeComponents;

import com.bitdecay.ludum.dare.components.AttackComponent;
import com.bitdecay.ludum.dare.components.HealthComponent;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;

/**
 * Created by jake on 12/12/2015.
 */
public class MetalComponent implements IComponent {
    public int cost = 0;

    public MetalComponent(PhysicsComponent phys, HealthComponent health, AttackComponent attack){
        phys.getBody().props.maxVoluntarySpeed = (int) (phys.getBody().props.maxVoluntarySpeed * 0.9f);
        phys.getBody().props.gravityModifier = phys.getBody().props.gravityModifier * 1.1f;
        health.max = health.max * 1.25f;
        attack.attack = attack.attack * 1.25f;
    }
}
