package com.bitdecay.ludum.dare.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.components.SizeComponent;
import com.bitdecay.ludum.dare.util.LightUtil;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;

import java.util.Collections;
import java.util.List;

public class LightGameObject extends BasePlacedObject {

    @Override
    public List<BitBody> build(LevelObject levelObject) {
        size = new SizeComponent(1, 1);
        pos = new PositionComponent(levelObject.rect.center().x, levelObject.rect.center().y);
        append(size).append(pos);
        return Collections.emptyList();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
//        LightUtil.addLocatedLight(spriteBatch, new Vector2(pos.x, pos.y));
    }
}
