package com.bitdecay.ludum.dare.components;

import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.collection.GameObjects;
import com.bitdecay.ludum.dare.interfaces.IComponent;

public class LevelInteractionComponent implements IComponent {
    BitWorld world;
    GameObjects objects;

    public LevelInteractionComponent(BitWorld world, GameObjects objects) {
        this.world = world;
        this.objects = objects;
    }

    public BitWorld getWorld() {
        return world;
    }

    public GameObjects getObjects() {
        return objects;
    }

    public void addToLevel(GameObject obj, PhysicsComponent phys) {
        if (phys != null) {
            getWorld().addBody(phys.getBody());
        }
        getObjects().add(obj);
    }

    public void addToObjects(GameObject obj){
        getObjects().add(obj);
    }

    public void removeFromObjects(GameObject obj){
        obj.append(new RemoveNowComponent());
    }

    public void removeFromLevel(GameObject obj, PhysicsComponent phys) {
        getWorld().removeBody(phys.getBody());
        obj.append(new RemoveNowComponent());
    }
}
