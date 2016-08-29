package com.bitdecay.ludum.dare.editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.ludum.dare.components.health.HealthTotemAnimationComponent;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class HealthTotemEditorObject extends RenderableLevelObject {
    @CantInspect
    final public String name;

    public HealthTotemEditorObject() {
        super();

        name = "shrines";

        rect = new BitRectangle(0, 0, texture().getRegionWidth() * HealthTotemAnimationComponent.SCALE,
                texture().getRegionHeight() * HealthTotemAnimationComponent.SCALE);
    }

    @Override
    public TextureRegion texture() {
        return atlas.findRegion("decor/shrine");
    }

    @Override
    public BitBody buildBody() {
        return null;
    }

    @Override
    public String name() {
        return name;
    }
}
