package com.bitdecay.ludum.dare.gameobject;

import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.components.SizeComponent;

/**
 * Created by Admin on 12/13/2015.
 */
public class BasePlacedObject extends GameObject {
    protected SizeComponent size;
    protected PositionComponent pos;
    protected PhysicsComponent phys;
    protected AnimationComponent anim;
}
