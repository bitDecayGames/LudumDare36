package com.bitdecay.ludum.dare.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.ContactListener;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
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
public class CoinGameObject extends BasePlacedObject implements ContactListener, IRemoveable {
    private boolean collected = false;

    @Override
    public List<BitBody> build(LevelObject levelObject) {
        size = new SizeComponent(0, 0);
        pos = new PositionComponent(0, 0);
        anim = new AnimationComponent("coin", pos, 1f, new Vector2(3.5f, 3.5f));
        setupAnimation();

        phys = new PhysicsComponent(levelObject.buildBody(), pos, size);
        phys.getBody().addContactListener(this);
        append(size).append(pos).append(phys).append(anim);
        return Arrays.asList(phys.getBody());
    }

    private void setupAnimation() {
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/level.atlas", AnimagicTextureAtlas.class);

        anim.animator.addAnimation(new Animation("coin", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("collect/coin").toArray(AnimagicTextureRegion.class)));
        anim.animator.switchToAnimation("coin");
    }

    @Override
    public void contactStarted(BitBody bitBody) {
        if (!collected) {
            if (bitBody.userObject instanceof Player) {
                ((Player) bitBody.userObject).achieveMoney(1);
                collected = true;
                SoundLibrary.playSound("Coin");
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

    @Override
    public boolean shouldRemove() {
        return collected;
    }

    @Override
    public void remove() {
        ((LevelInteractionComponent)getFirstComponent(LevelInteractionComponent.class)).getWorld().removeBody(phys.getBody());
    }
}
