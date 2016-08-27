package com.bitdecay.ludum.dare.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.ContactListener;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.*;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.Arrays;
import java.util.List;

public class FinishLineGameObject extends BasePlacedObject implements ContactListener {

    public boolean raceOver = false;

    LightComponent light;

    @Override
    public List<BitBody> build(LevelObject levelObject) {
        size = new SizeComponent(0, 0);
        pos = new PositionComponent(0, 0);
        anim = new AnimationComponent("finish", pos, 1f, new Vector2(0, -8));
        light = new LightComponent(pos, new Vector2());
        setupAnimation();

        phys = new PhysicsComponent(levelObject.buildBody(), pos, size);
        phys.getBody().addContactListener(this);
        append(size).append(pos).append(phys).append(anim);
        return Arrays.asList(phys.getBody());
    }

    private void setupAnimation() {
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/level.atlas", AnimagicTextureAtlas.class);

        Array<AnimagicTextureRegion> textures = atlas.findRegions("collect/finish");
        anim.animator.addAnimation(new Animation("ongoing", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), new AnimagicTextureRegion[]{textures.get(0)}));
        anim.animator.addAnimation(new Animation("finished", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), new AnimagicTextureRegion[]{textures.get(1)}));
        anim.animator.switchToAnimation("ongoing");
    }

    @Override
    public void contactStarted(BitBody bitBody) {
        if (!raceOver) {
            if (bitBody.userObject instanceof Player) {
                raceOver = true;
                remove(LightComponent.class);
                anim.animator.switchToAnimation("finished");
                ((Player) bitBody.userObject).achieveMoney(10);
                ((Player) bitBody.userObject).winner = true;
            }
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

    public Vector2 getPosition() {
        return new Vector2(pos.x, pos.y);
    }
}
