package com.bitdecay.ludum.dare.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.ludum.dare.interfaces.IDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Vertical layer of parallax background.
 */
public class BackgroundLayer implements IDraw {
    // Width of standard background tiles.
    private static float tileWidth = 0;
    // Height of standard background tile.
    private static float tileHeight = 0;
    // Tiles number of tiles that will be added to the left, and then the right form the center.
    private static float horizontalSize = 1;

    private List<TextureRegion> backgrounds;

    // Standard tile space to deviate from vertically. Can be positive or negative.
    private float verticalOffsetIndex = 0;
    // One time vertical offset to apply to layer. Can be positive or negative.
    private float verticalOffset = 0;
    // Direction to lay out backgrounds in.
    private BackgroundLayerDirection direction = BackgroundLayerDirection.UP;

    private BackgroundLayer() {
        backgrounds = new ArrayList<>();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        float verticalIndex = verticalOffsetIndex;

        for (TextureRegion background : backgrounds) {
            // Set vertical position.
            float y = verticalIndex * tileHeight + verticalOffset;

            // Draw center tile.
            spriteBatch.draw(background, 0, y);

            // Draw size tiles based on size.
            // TODO Do this dymanically with a calculation to save memory and render time.
            for (int i = 0; i < horizontalSize; i++) {
                spriteBatch.draw(background, i * -tileWidth, y);

                spriteBatch.draw(background, i * tileWidth, y);
            }

            // Set direction to go for next background in layer.
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

    public static void setHorizontalSize(float horizontalSize) {
        BackgroundLayer.horizontalSize = horizontalSize;
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

