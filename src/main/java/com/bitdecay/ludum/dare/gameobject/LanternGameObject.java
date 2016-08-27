package com.bitdecay.ludum.dare.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.components.AnimationComponent;
import com.bitdecay.ludum.dare.components.LightComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.components.SizeComponent;
import com.bitdecay.ludum.dare.levelobject.LanternLevelObject;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 12/14/2015.
 */
public class LanternGameObject extends BasePlacedObject {

    LightComponent light;
    float attenMod = 0;
    float zMod = 0;

    @Override
    public List<BitBody> build(LevelObject levelObject) {

        attenMod = ((LanternLevelObject)levelObject).attenuationModifier;
        zMod = ((LanternLevelObject)levelObject).zModifier;
        size = new SizeComponent(1, 1);
        pos = new PositionComponent(levelObject.rect.xy.x, levelObject.rect.xy.y);

        anim = new AnimationComponent("lantern", pos, 1f, new Vector2(0, -5));
        setupAnimation();

        light = new LightComponent(pos, new Vector2(10, 40));

        append(size).append(pos).append(anim).append(light);

        return Collections.emptyList();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch ( anim.animator.getFrameIndex()) {
            case 0:
                light.setAttenuation(1f + attenMod);
                light.setzAxis(.1f + zMod);
                break;
            case 1:
                light.setAttenuation(1.2f + attenMod);
                light.setzAxis(.13f + zMod);
                break;
            case 2:
                light.setAttenuation(1.05f + attenMod);
                light.setzAxis(.11f + zMod);
                break;
            default:
                light.setAttenuation(.8f + attenMod);
                light.setzAxis(.07f + zMod);
        }
    }

    private void setupAnimation() {
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/level.atlas", AnimagicTextureAtlas.class);

        anim.animator.addAnimation(new Animation("lantern", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.05f), atlas.findRegions("lantern").toArray(AnimagicTextureRegion.class)));
        anim.animator.switchToAnimation("lantern");
    }
}
