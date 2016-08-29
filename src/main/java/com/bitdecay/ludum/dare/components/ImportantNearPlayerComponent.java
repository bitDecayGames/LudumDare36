package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;

public class ImportantNearPlayerComponent implements IComponent {
    public boolean near = false;
    public float range;
    public String sound = "Power";
    public ImportantNearPlayerComponent(){
        this(100);
    }
    public ImportantNearPlayerComponent(float range){
        this.range = range;
    }
}
