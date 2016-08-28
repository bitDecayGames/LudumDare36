package com.bitdecay.ludum.dare.editor.deadship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.ludum.dare.components.ship.DeadShipAnimationComponent;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class DeadShipEditorObject extends RenderableLevelObject {
    @CantInspect
    final public String name;

    public DeadShipEditorObject() {
        super();

        name = "deadShip";

        rect = new BitRectangle(0, 0, texture().getRegionWidth() * DeadShipAnimationComponent.SCALE,
                texture().getRegionHeight() * DeadShipAnimationComponent.SCALE);
    }

    @Override
    public TextureRegion texture() {
        return atlas.findRegion("ship/pieces/" + name);
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
