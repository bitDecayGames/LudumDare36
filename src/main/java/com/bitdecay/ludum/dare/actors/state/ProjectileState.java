package com.bitdecay.ludum.dare.actors.state;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.Facing;
import com.bitdecay.ludum.dare.actors.projectile.FireProjectile;
import com.bitdecay.ludum.dare.actors.projectile.IceProjectile;
import com.bitdecay.ludum.dare.actors.projectile.PoisonProjectile;
import com.bitdecay.ludum.dare.actors.projectile.WebProjectile;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.components.upgradeComponents.FireProjectileComponent;
import com.bitdecay.ludum.dare.components.upgradeComponents.IceProjectileComponent;
import com.bitdecay.ludum.dare.components.upgradeComponents.PoisonProjectileComponent;
import com.bitdecay.ludum.dare.components.upgradeComponents.WebProjectileComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bitdecay.ludum.dare.util.SoundLibrary;

import java.util.Set;

public class ProjectileState extends AbstractState {

    LevelInteractionComponent levelComponent;

    public ProjectileState(Set<IComponent> components) {
        super(components);

        components.forEach(comp -> {
            if (comp instanceof LevelInteractionComponent) levelComponent = (LevelInteractionComponent) comp;
        });

        if (levelComponent == null || levelComponent.getObjects() == null || levelComponent.getWorld() == null) {
            throw new RuntimeException(LevelInteractionComponent.class + " with valid data expected");
        }
    }

    public Boolean shouldRun(IState currentState) {
        if (inputComponent.isJustPressed(InputAction.PROJECTILE)) {
            if (!(currentState instanceof ProjectileState)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void enter() {
        super.enter();
        int randomizer = (int) (Math.random() * 4) + 1;
        SoundLibrary.playSound("Punch" + randomizer);
        Vector2 direction = new Vector2();
        if (!physicsComponent.getBody().grounded) {
            if (inputComponent.isPressed(InputAction.UP)) {
                direction.y = 1;
            } else if (inputComponent.isPressed(InputAction.DOWN)) {
                direction.y = -1;
            } else {
                direction.x = 1;
            }
        } else {
            if (inputComponent.isPressed(InputAction.UP)) {
                direction.y = 1;
            } else {
                direction.x = 1;
            }
        }

        Facing facing = physicsComponent.getBody().facing;
        switch (facing) {
            case LEFT:
                direction.x *= -1;
                break;
            case RIGHT:
                // Do nothing.
                break;
            default:
                throw new Error("Invalid facing set");
        }

        addProjectile(direction);
    }

    private void addProjectile(Vector2 direction) {
        components.forEach(comp -> {
            if (comp instanceof FireProjectileComponent){
                FireProjectile fireProjectile = new FireProjectile(positionComponent, direction, levelComponent, physicsComponent);
                levelComponent.addToLevel(fireProjectile, fireProjectile.getPhysics());
            } else if (comp instanceof IceProjectileComponent){
                IceProjectile iceProjectile = new IceProjectile(positionComponent, direction, levelComponent, physicsComponent);
                levelComponent.addToLevel(iceProjectile, iceProjectile.getPhysics());
            }else if (comp instanceof PoisonProjectileComponent){
                PoisonProjectile poisonProjectile = new PoisonProjectile(positionComponent, direction, levelComponent, physicsComponent);
                levelComponent.addToLevel(poisonProjectile, poisonProjectile.getPhysics());
            }else if (comp instanceof WebProjectileComponent){
                WebProjectile webProjectile = new WebProjectile(positionComponent, direction, levelComponent, physicsComponent);
                levelComponent.addToLevel(webProjectile, webProjectile.getPhysics());
            }
        });
    }

    @Override
    public void exit() {
        super.exit();
    }

    @Override
    public IState update(float delta) {
        return getJumpState();
    }
}
