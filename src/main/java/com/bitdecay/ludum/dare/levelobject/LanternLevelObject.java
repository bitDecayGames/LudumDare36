package com.bitdecay.ludum.dare.levelobject;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.annotation.ValueRange;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;

/**
 * Created by Admin on 12/14/2015.
 */
public class LanternLevelObject extends RenderableLevelObject {
    @CantInspect
    private TextureRegion texture;

    @ValueRange(min = -1, max = 1)
    public float attenuationModifier = 0;

    @ValueRange(min = -1, max = 1)
    public float zModifier = 0;

    public LanternLevelObject() {
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/level.atlas", AnimagicTextureAtlas.class);

        this.texture = atlas.findRegion("lantern/1");
        rect = new BitRectangle(0, 0, 33, 75);
    }

    @Override
    public TextureRegion texture() {
        return texture;
    }

    @Override
    public BitBody buildBody() {
        BitBody body = new BitBody();
        body.props.gravitational = false;
        body.props.collides = false;
        body.bodyType = BodyType.STATIC;
        body.aabb.set(new BitRectangle(rect));
        return body;
    }

    @Override
    public String name() {
        return "Lantern";
    }
}
