package com.bitdecay.ludum.dare.components.upgradeComponents;

import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;

/**
 * Created by jake on 12/12/2015.
 */
public class FloatUpgradeComponent implements IComponent {
    public int cost = 0;

    public FloatUpgradeComponent(PhysicsComponent phys){
        phys.getBody().props.gravityModifier = phys.getBody().props.gravityModifier * 0.8f;
    }
}
