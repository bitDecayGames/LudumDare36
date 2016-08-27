package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

/**
 * Created by jacob on 8/27/16.
 */
public class JetPackComponent implements IComponent, IUpdate{
    public final float maxFuel = 100;

    public float currentFuel = 100;

    public boolean isFiring = false;
    public boolean canRefuel = false;

    public JetPackComponent() {

    }

    @Override
    public void update(float delta) {
        if(isFiring && currentFuel > 0){
            currentFuel--;
        } else if (canRefuel && currentFuel < maxFuel){
            currentFuel++;
        }
    }
}
