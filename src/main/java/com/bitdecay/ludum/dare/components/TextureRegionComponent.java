package com.bitdecay.ludum.dare.components;

import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;
public class TextureRegionComponent implements IComponent, IDraw {

    private final PositionComponent position;
    private final SizeComponent size;
    public final AnimagicTextureRegion region;

    public TextureRegionComponent(AnimagicTextureRegion region, PositionComponent position, SizeComponent size) {
        this.position = position;
        this.size = size;
        this.region = region;
    }

    @Override
    public void draw(AnimagicSpriteBatch spriteBatch) {
        spriteBatch.draw(region,
                position.x,
                position.y,
                size.w,
                size.h);
    }
}
