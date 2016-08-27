package com.bitdecay.ludum.dare.components;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.state.GroundedControlState;
import com.bitdecay.jump.control.state.JumperBodyControlState;
import com.bitdecay.jump.control.state.JumpingControlState;
import com.bitdecay.ludum.dare.actors.state.JumpState;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IUpdate;
import com.sun.org.apache.xerces.internal.parsers.CachingParserPool;
import com.sun.org.apache.xpath.internal.SourceTree;

/**
 * Created by jacob on 8/27/16.
 */
public class JetPackComponent implements IComponent, IUpdate{
    private JumperBody playerJumpBody;

    public final float maxFuel = 100;

    public float currentFuel = 100;

    public boolean isFiring = false;
    public boolean canRefuel = false;

    public JetPackComponent(JumperBody playerJumpBody) {
        this.playerJumpBody = playerJumpBody;

    }

    @Override
    public void update(float delta) {
        if(playerJumpBody.controller.getStatus().equals(JumpingControlState.class.getSimpleName())){
            isFiring = true;
        } else {
            isFiring = false;
        }

        if(playerJumpBody.controller.getStatus().equals(GroundedControlState.class.getSimpleName())) {
            canRefuel = true;
        } else {
            canRefuel = false;
        }

        if(isFiring && currentFuel > 0){
            currentFuel--;
            if(currentFuel < 0){
                currentFuel = 0;
            }
        } else if (canRefuel && currentFuel < maxFuel){
            currentFuel++;
            if(currentFuel > 100){
                currentFuel = maxFuel;
            }
        }

        if (currentFuel <= 0){
            playerJumpBody.jumpsRemaining = 0;
            playerJumpBody.jumperProps.jumpVariableHeightWindow = 0;
        } else {
            playerJumpBody.jumpsRemaining = Integer.MAX_VALUE;
            playerJumpBody.jumperProps.jumpVariableHeightWindow = Float.POSITIVE_INFINITY;
        }
    }
}
