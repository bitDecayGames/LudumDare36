package com.bitdecay.ludum.dare.editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.ludum.dare.actors.ai.Gorilla;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class GorillaEditorObject extends RenderableLevelObject {
    @CantInspect
    final public String name;

    public GorillaEditorObject() {
        super();

        name = "gorilla";

        rect = new BitRectangle(0, 0, texture().getRegionWidth() * Gorilla.SCALE,
                texture().getRegionHeight() * Gorilla.SCALE);
    }

    @Override
    public TextureRegion texture() {
        return atlas.findRegion("gorilla/sit/1");
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
