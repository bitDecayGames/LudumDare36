package com.bitdecay.ludum.dare.components.upgradeComponents;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;

/**
 * Created by jake on 12/12/2015.
 */
public class DoubleJumpComponent implements IComponent {
    public int cost = 0;

    public DoubleJumpComponent(PhysicsComponent phys){
            ((JumperBody) phys.getBody()).jumperProps.jumpCount = 2;
    }
}
