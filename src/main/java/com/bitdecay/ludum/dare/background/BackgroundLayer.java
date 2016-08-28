package com.bitdecay.ludum.dare.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.ludum.dare.interfaces.IDraw;

import java.util.ArrayList;
import java.util.List;

public class BackgroundLayer implements IDraw{
    private static float tileWidth = 0;
    private static float tileHeight = 0;

    private List<TextureRegion> backgrounds;
    private float verticalOffsetIndex = 0;
    private float verticalOffset = 0;
    private BackgroundLayerDirection direction = BackgroundLayerDirection.UP;

    private BackgroundLayer() {
        backgrounds = new ArrayList<>();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        float verticalIndex = verticalOffsetIndex;

        for (TextureRegion background : backgrounds) {
            float y = verticalIndex * tileHeight + verticalOffset;
            spriteBatch.draw(background, -tileWidth, y);

            spriteBatch.draw(background, 0, y);

            spriteBatch.draw(background, tileWidth, y);

            switch (direction) {
                case UP:
                    verticalIndex++;
                    break;
                case DOWN:
                    verticalIndex--;
                    break;
            }
        }
    }

    public static BackgroundLayer create() {
        return new BackgroundLayer();
    }

    public static void setTileWidth(float tileWidth) {
        BackgroundLayer.tileWidth = tileWidth;
    }

    public static void setTileHeight(float tileHeight) {
        BackgroundLayer.tileHeight = tileHeight;
    }

    public BackgroundLayer addBackground(String name) {
        backgrounds.add(BackgroundUtil.getBackground(name));

        return this;
    }

    public BackgroundLayer setVerticalOffsetIndex(float verticalOffsetIndex) {
        this.verticalOffsetIndex = verticalOffsetIndex;

        return this;
    }

    public BackgroundLayer setDirection(BackgroundLayerDirection direction) {
        if (direction == null) {
            throw new RuntimeException("Direction must be valid.");
        }

        this.direction = direction;

        return this;
    }

    public BackgroundLayer setVerticalOffset(float verticalOffset) {
        this.verticalOffset = verticalOffset;

        return this;
    }
}

