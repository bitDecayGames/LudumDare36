package com.bitdecay.ludum.dare.components.upgradeComponents;

import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AttackComponent;
import com.bitdecay.ludum.dare.components.HealthComponent;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.interfaces.IComponent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jake on 12/12/2015.
 */
public class MysteryBagComponent implements IComponent {
    public int cost = 0;
    private boolean haveProjectile = false;
    private List<String> myUpgradeComponents;
    private final List<String> ALL_UPGRADE_COMPONENTS = Arrays.asList("DOUBLE_JUMP", "FLOAT", "METAL", "SPEED", "WALL_JUMP", "FIRE", "ICE", "POISON", "WEB");
    private List<String> notMyUpgrades;

    public MysteryBagComponent(Player me, PhysicsComponent phys, HealthComponent health, AttackComponent attack) {
//        if (me.hasComponent(DoubleJumpComponent.class)) {
//            myUpgradeComponents.add("DOUBLE_JUMP");
//        }
//        if (me.hasComponent(FloatUpgradeComponent.class)) {
//            myUpgradeComponents.add("FLOAT");
//        }
//        if (me.hasComponent(MetalComponent.class)) {
//            myUpgradeComponents.add("METAL");
//        }
//        if (me.hasComponent(SpeedComponent.class)) {
//            myUpgradeComponents.add("SPEED");
//        }
//        if (me.hasComponent(WallJumpComponent.class)) {
//            myUpgradeComponents.add("WALL_JUMP");
//        }
//        if (me.hasComponent(FireProjectileComponent.class)) {
//            haveProjectile = true;
//            myUpgradeComponents.add("FIRE");
//        }
//        if (me.hasComponent(IceProjectileComponent.class)) {
//            haveProjectile = true;
//            myUpgradeComponents.add("ICE");
//        }
//        if (me.hasComponent(PoisonProjectileComponent.class)) {
//            haveProjectile = true;
//            myUpgradeComponents.add("POISON");
//        }
//        if (me.hasComponent(WebProjectileComponent.class)) {
//            haveProjectile = true;
//            myUpgradeComponents.add("WEB");
//        }
//
//        for (String s : ALL_UPGRADE_COMPONENTS) {
//            if (!myUpgradeComponents.contains(s)) {
//                notMyUpgrades.add(s);
//            }
//        }
//
//        String randomComp = myUpgradeComponents.get((int) (Math.random() * myUpgradeComponents.size()));
//
//        if (randomComp == "DOUBLE_JUMP") {
//            me.append(new DoubleJumpComponent(phys));
//        } else if (randomComp == "FLOAT") {
//            me.append(new FloatUpgradeComponent(phys));
//        } else if (randomComp == "METAL") {
//            me.append(new MetalComponent(phys, health, attack));
//        } else if (randomComp == "SPEED") {
//            me.append(new SpeedComponent(phys));
//        } else if (randomComp == "WALL_JUMP") {
//            me.append(new WallJumpComponent(phys));
//        } else if (randomComp == "FIRE") {
//            me.append(new FireProjectileComponent());
//        } else if (randomComp == "ICE") {
//            me.append(new IceProjectileComponent());
//        } else if (randomComp == "WEB") {
//            me.append(new WebProjectileComponent());
//        } else if (randomComp == "POISON") {
//            me.append(new PoisonProjectileComponent());
//        }
    }
}
