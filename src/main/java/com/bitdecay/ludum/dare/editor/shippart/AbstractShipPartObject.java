package com.bitdecay.ludum.dare.editor.shippart;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.ludum.dare.actors.items.ShipPart;
import com.bitdecay.ludum.dare.components.ship.ShipPartAnimationComponent;

public abstract class AbstractShipPartObject extends RenderableLevelObject implements IEditorShipPart {
    @CantInspect
    final public String name;

    public AbstractShipPartObject(String name) {
        this.name = name;
        rect = new BitRectangle(0, 0, texture().getRegionWidth() * ShipPartAnimationComponent.SCALE,
                                texture().getRegionHeight() * ShipPartAnimationComponent.SCALE);
    }

    @Override
    public TextureRegion texture() {
        return ShipPart.getRegion(name);
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
