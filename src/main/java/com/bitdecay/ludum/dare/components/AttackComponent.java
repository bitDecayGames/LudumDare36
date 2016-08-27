package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;

/**
 * Created by jake on 12/12/2015.
 */
public class AttackComponent implements IComponent {
    public float attack;

    public AttackComponent(int attack){
        this.attack = attack;
    }
}
