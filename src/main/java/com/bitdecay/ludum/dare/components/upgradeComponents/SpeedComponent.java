package com.bitdecay.ludum.dare.components.upgradeComponents;

import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;

/**
 * Created by jake on 12/12/2015.
 */
public class SpeedComponent implements IComponent {
    public int cost = 0;

    public SpeedComponent(PhysicsComponent phys){
        phys.getBody().props.maxVoluntarySpeed = (int) (phys.getBody().props.maxVoluntarySpeed * 1.25f);
    }
}
