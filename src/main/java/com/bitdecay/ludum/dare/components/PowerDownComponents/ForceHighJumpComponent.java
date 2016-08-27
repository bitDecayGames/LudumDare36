package com.bitdecay.ludum.dare.components.PowerDownComponents;

import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.components.TimedComponent;

/**
 * Created by jake on 12/12/2015.
 */
public class ForceHighJumpComponent extends TimedComponent {

    public ForceHighJumpComponent(PhysicsComponent phys, PositionComponent pos){
        super(1);

    }

    @Override
    public void remove(){
    }
}
