package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IUpdate;


/**
 * Created by jake on 12/12/2015.
 */
public class TimedComponent implements IComponent, IUpdate, IRemoveable {
    final float liveTimeConst;
    float liveTime;

    public TimedComponent(float liveTime){
        liveTimeConst = liveTime;
        this.liveTime = liveTime;
    }

    @Override
    public void update(float delta) {
        if(!shouldRemove()){
            liveTime -= delta;
        }
    }

    @Override
    public boolean shouldRemove() {
        return liveTime <= 0;
    }

    @Override
    public void remove() {

    }

    public void reset() {
        liveTime = liveTimeConst;
    }
}
