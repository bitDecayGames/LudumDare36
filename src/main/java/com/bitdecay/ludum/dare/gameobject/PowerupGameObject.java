package com.bitdecay.ludum.dare.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.ContactListener;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PhysicsComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.components.SizeComponent;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 12/13/2015.
 */
public class PowerupGameObject extends BasePlacedObject implements ContactListener {
    private boolean available = true;

    @Override
    public List<BitBody> build(LevelObject levelObject) {
        size = new SizeComponent(0, 0);
        pos = new PositionComponent(0, 0);
        anim = new AnimationComponent("coin", pos, 1f, new Vector2(0, -6));
        setupAnimation();

        phys = new PhysicsComponent(levelObject.buildBody(), pos, size);
        phys.getBody().addContactListener(this);
        append(size).append(pos).append(phys).append(anim);
        return Arrays.asList(phys.getBody());
    }

    private void setupAnimation() {
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/level.atlas", AnimagicTextureAtlas.class);

        anim.animator.addAnimation(new Animation("available", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("collect/chest/closed").toArray(AnimagicTextureRegion.class)));
        anim.animator.addAnimation(new Animation("generating", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("collect/chest/open").toArray(AnimagicTextureRegion.class)));
        anim.animator.switchToAnimation("available");
    }

    @Override
    public void contactStarted(BitBody bitBody) {
        if (available && bitBody.userObject instanceof Player) {
            available = false;
            anim.animator.switchToAnimation("generating");
            SoundLibrary.playSound("Powerup");
            ((Player) bitBody.userObject).spinPowerBlock();
        }
    }

    @Override
    public void contact(BitBody bitBody) {

    }

    @Override
    public void contactEnded(BitBody bitBody) {

    }

    @Override
    public void crushed() {

    }
}
