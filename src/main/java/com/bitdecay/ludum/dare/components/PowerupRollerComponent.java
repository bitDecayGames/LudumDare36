package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IUpdate;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

/**
 * Created by Admin on 12/14/2015.
 */
public class PowerupRollerComponent implements IComponent, IDraw, IUpdate, IRemoveable {
    private final Player player;
    PositionComponent pos;
    AnimationComponent anim;
    TimedComponent timer;

    PositionComponent sourcePos;

    public PowerupRollerComponent(Player player, PositionComponent sourcePos) {
        this.player = player;
        this.sourcePos = sourcePos;
        pos = new PositionComponent(sourcePos.x, sourcePos.y);
        anim = new AnimationComponent("spinner", pos, 1, new Vector2(-20, 30));
        timer = new TimedComponent(3);

        SoundLibrary.playSound("slotMachine");

        setupAnimation();
    }



    private void setupAnimation() {
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/power.atlas", AnimagicTextureAtlas.class);

        anim.animator.addAnimation(new Animation("spin", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("slots").toArray(AnimagicTextureRegion.class)));
        anim.animator.switchToAnimation("spin");
    }

    @Override
    public void draw(AnimagicSpriteBatch spriteBatch) {
        anim.draw(spriteBatch);
    }

    @Override
    public void update(float delta) {
        timer.update(delta);
        if (timer.shouldRemove()) {
            player.getPowerBlock();
        } else {
            anim.animator.update(delta);
            pos.x = sourcePos.x;
            pos.y = sourcePos.y;
        }
    }

    @Override
    public boolean shouldRemove() {
        return timer.shouldRemove();
    }

    @Override
    public void remove() {

    }
}
