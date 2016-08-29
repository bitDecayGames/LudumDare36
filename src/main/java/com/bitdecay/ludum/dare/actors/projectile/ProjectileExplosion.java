package com.bitdecay.ludum.dare.actors.projectile;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.LaserExplodeComponent;
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
public class ProjectileExplosion extends GameObject implements IRemoveable {
    private AnimationComponent anim;
    private PositionComponent pos;
    private final LaserExplodeComponent laserPop;

    public ProjectileExplosion (PositionComponent pos) {
        this.pos = pos;

        SoundLibrary.playSound("LaserHit" + MathUtils.random(1, 3));
        anim = new AnimationComponent(ResourceDir.path("assets/main/bulletexplode"), pos, .5f, new Vector2(0, 0));

        AnimagicTextureAtlas atlas = LudumDareGame.atlas;

        anim.animator.addAnimation(new Animation("bulletExplo", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.03f), atlas.findRegions("bulletexplode").toArray(AnimagicTextureRegion.class)));

        anim.animator.switchToAnimation("bulletExplo");

        laserPop = new LaserExplodeComponent(pos);

        append(pos).append(laserPop);
    }

    @Override
    public boolean shouldRemove() {
        return laserPop.shouldRemove();
    }

    @Override
    public void remove() {

    }
}
