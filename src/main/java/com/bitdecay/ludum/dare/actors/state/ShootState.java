package com.bitdecay.ludum.dare.actors.state;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.actors.projectile.Projectile;
import com.bitdecay.ludum.dare.components.KeyboardControlComponent;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;

import java.util.Set;

/**
 * Created by jacob on 8/27/16.
 */
public class ShootState extends AbstractState {
    protected KeyboardControlComponent keyboard;
    protected LevelInteractionComponent levelInteractionComponent;

    public ShootState(Set<IComponent> components) {
        super(components);
        components.forEach(comp -> {
            if (comp instanceof KeyboardControlComponent) keyboard = (KeyboardControlComponent) comp;
            if (comp instanceof LevelInteractionComponent) levelInteractionComponent = (LevelInteractionComponent) comp;
        });
    }

    public void enter() {
        super.enter();
        System.out.println("We entering shooting stance.");
        if (physicsComponent.getBody().facing.toString().equals("LEFT")){
            Projectile projectile = new Projectile(positionComponent, new Vector2(-1, 0), levelInteractionComponent, physicsComponent);
            levelInteractionComponent.addToLevel(projectile, projectile.getPhysics());
        } else {
            Projectile projectile = new Projectile(positionComponent, new Vector2(1, 0), levelInteractionComponent, physicsComponent);
            levelInteractionComponent.addToLevel(projectile, projectile.getPhysics());
        }
    }

    public void exit () {
        System.out.println("we exitingh shoootin stance...");
        super.exit();
    }
}
