package com.bitdecay.ludum.dare.components.upgradeComponents;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;

/**
 * Created by jake on 12/12/2015.
 */
public class WallJumpComponent implements IComponent {
    public int cost = 0;

    public WallJumpComponent(PhysicsComponent phys){
            ((JumperBody) phys.getBody()).jumperProps.wallJumpEnabled = true;
            ((JumperBody) phys.getBody()).jumperProps.wallSlideEnabled = true;
            ((JumperBody) phys.getBody()).jumperProps.wallMaxSlideSpeed = 50;
            ((JumperBody) phys.getBody()).jumperProps.wallJumpLaunchPower = 100;
        }
    }
