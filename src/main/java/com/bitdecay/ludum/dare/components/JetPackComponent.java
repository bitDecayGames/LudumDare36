package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

/**
 * Created by jacob on 8/27/16.
 */
public class JetPackComponent implements IComponent, IUpdate{
    public int fuel = 100;
    public boolean isFiring = false;
    public boolean canRefuel = false;

    public JetPackComponent() {

    }

    @Override
    public void update(float delta) {
        if(isFiring && fuel > 0){
            fuel --;
        } else if (canRefuel && fuel < 100){
            fuel ++;
        }
    }
}
