package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.jump.Facing;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.state.GroundedControlState;
import com.bitdecay.jump.control.state.JumpingControlState;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.cameras.FollowOrthoCamera;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

/**
 * Created by jacob on 8/27/16.
 */
public class JetPackComponent implements IComponent, IUpdate, IDraw {
    private JumperBody playerJumpBody;
    private Player player;
    private FollowOrthoCamera camera;

    public final float maxFuel = 50;

    public float currentFuel = 0;

    public final int FIRST_TICK_FUEL_COST = 5;

    public boolean isFiring = false;
    public boolean canRefuel = false;
    public boolean firstPress = false;

    ParticleEffect fx;

    public JetPackComponent(Player player, FollowOrthoCamera camera) {
        this.player = player;
        this.playerJumpBody = (JumperBody) player.phys.getBody();
        this.camera = camera;
        fx = new ParticleEffect();
        fx.load(Gdx.files.internal("particle/flame3.p"), Gdx.files.internal("particle"));
        fx.scaleEffect(.5f);
        fx.setPosition(playerJumpBody.aabb.xy.x, playerJumpBody.aabb.xy.y - 3);
    }

    @Override
    public void update(float delta) {
        float offset = playerJumpBody.aabb.width / 2;
        if (playerJumpBody.facing.equals(Facing.LEFT)) {
            offset += 5;
        } else {
            offset -= 5;
        }
        fx.setPosition(playerJumpBody.aabb.xy.x + offset, playerJumpBody.aabb.xy.y + 5);
        fx.update(delta);

        if(playerJumpBody.controller.getStatus().equals(JumpingControlState.class.getSimpleName())){
            if (!isFiring) {
                firstPress = true;
            }
            isFiring = true;
            fx.start();
        } else {
            isFiring = false;
            fx.allowCompletion();
        }

        if(playerJumpBody.controller.getStatus().equals(GroundedControlState.class.getSimpleName())) {
            canRefuel = true;
        } else {
            canRefuel = false;
        }

        if(isFiring && currentFuel > 0){
            camera.rumble();
            if (firstPress) {
                currentFuel -= FIRST_TICK_FUEL_COST;
                firstPress = false;
            }
            currentFuel--;
            if(currentFuel < 0){
                currentFuel = 0;
            }
        } else if (canRefuel && currentFuel < maxFuel && !player.naked){
            currentFuel+=5;
            if(currentFuel > maxFuel){
                currentFuel = maxFuel;
                currentFuel = maxFuel;
            }
        }

        if (currentFuel <= 0){
            playerJumpBody.jumpsRemaining = 0;
            playerJumpBody.jumperProps.jumpVariableHeightWindow = 0;
        } else {
            playerJumpBody.jumpsRemaining = Integer.MAX_VALUE;
            playerJumpBody.jumperProps.jumpVariableHeightWindow = Float.POSITIVE_INFINITY;
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        fx.draw(spriteBatch);
    }
}
