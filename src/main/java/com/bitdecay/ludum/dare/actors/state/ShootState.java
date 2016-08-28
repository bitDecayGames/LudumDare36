package com.bitdecay.ludum.dare.actors.state;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.actors.projectile.Projectile;
import com.bitdecay.ludum.dare.components.KeyboardControlComponent;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.util.SoundLibrary;

import java.util.Random;
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
        if (physicsComponent.getBody().facing.toString().equals("LEFT")){
            Projectile projectile = new Projectile(positionComponent, new Vector2(-1, 0), levelInteractionComponent, physicsComponent);
            levelInteractionComponent.addToLevel(projectile, projectile.getPhysics());
            SoundLibrary.playSound("Laser_Shoot" + MathUtils.random(1, 4));
        } else {
            Projectile projectile = new Projectile(positionComponent, new Vector2(1, 0), levelInteractionComponent, physicsComponent);
            levelInteractionComponent.addToLevel(projectile, projectile.getPhysics());
            SoundLibrary.playSound("Laser_Shoot" + MathUtils.random(1, 4));
        }
    }

    public void exit () {
        super.exit();
    }
}
