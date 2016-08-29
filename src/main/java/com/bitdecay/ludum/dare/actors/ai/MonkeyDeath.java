package com.bitdecay.ludum.dare.actors.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.Facing;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

/**
 * Created by jacob on 8/28/16.
 */
public class MonkeyDeath extends GameObject implements IRemoveable {
    private AnimationComponent anim;
    private PositionComponent pos;

    public MonkeyDeath(PositionComponent pos, Facing facing) {
        this.pos = pos;

        anim = new AnimationComponent(ResourceDir.path("assets/main/monkey/death"), pos, .5f, new Vector2(0, 0));
        AnimagicTextureAtlas atlas = LudumDareGame.atlas;
        anim.animator.addAnimation(new Animation("monkeyDeath", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.13f), atlas.findRegions("monkey/death").toArray(AnimagicTextureRegion.class)));
        anim.animator.switchToAnimation("monkeyDeath");
        if(facing == Facing.RIGHT) {
            anim.setFlipVerticalAxis(true);
        }
        append(anim).append(pos);
        SoundLibrary.playSound("MonkeyVaporize");
    }

    @Override
    public boolean shouldRemove() {
        if (anim.animator.getFrameIndex() >= 3){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void remove() {

    }
}
