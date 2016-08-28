package com.bitdecay.ludum.dare.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager for the background of the game.
 */
public class BackgroundManager implements IUpdate, IDraw {
    // Controls distortion of parallax on backgrounds.
    private static final float PARALLAX_EFFECT = 0.2f;

    private List<BackgroundLayer> layers;

    // Camera to render parallax effect.
    private OrthographicCamera backgroundCamera;
    // Camera of character we're following.
    private OrthographicCamera characterCamera;

    public BackgroundManager(OrthographicCamera characterCamera) {
        this.characterCamera = characterCamera;

        initDimensions();

        // First backgrounds are further back and move slower than later entries
        // when the character this is watching moves.
        layers = new ArrayList<>();
        // Space
        layers.add(
            BackgroundLayer.create()
                .addBackground("space")
                .addBackground("space")
        );
        // Mountains
        layers.add(
            BackgroundLayer.create()
                .addBackground("hillsFar")
        );
        layers.add(
            BackgroundLayer.create()
                .addBackground("hillsMiddle")
        );
        layers.add(
            BackgroundLayer.create()
                .addBackground("hillsClose")
        );
        // Underground
        layers.add(
            BackgroundLayer.create()
                .setVerticalOffsetIndex(-1)
                .setDirection(BackgroundLayerDirection.DOWN)
                .setVerticalOffset(300 * PARALLAX_EFFECT)
                .addBackground("startUnderground")
                .addBackground("underground")
        );

        backgroundCamera = new OrthographicCamera(characterCamera.viewportWidth, characterCamera.viewportHeight);
    }

    @Override
    public void update(float delta) {
        // Sync with character camera.
        backgroundCamera.position.set(
            characterCamera.position.x * PARALLAX_EFFECT + Gdx.graphics.getWidth() / 3,
            characterCamera.position.y * PARALLAX_EFFECT + Gdx.graphics.getHeight() / 3,
            0);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        final float parallax = 1 + PARALLAX_EFFECT;
        for (BackgroundLayer l : layers) {
            // Increase move value as we move from background to foreground.
            backgroundCamera.position.set(
                backgroundCamera.position.x * parallax,
                backgroundCamera.position.y * parallax,
                0);
            backgroundCamera.update();

            spriteBatch.setProjectionMatrix(backgroundCamera.combined);
            l.draw(spriteBatch);
        }
    }

    // Sets initial layer specs.
    private void initDimensions() {
        TextureRegion mainTile = BackgroundUtil.getBackground("space");
        BackgroundLayer.setTileWidth(mainTile.getRegionWidth());
        BackgroundLayer.setTileHeight(mainTile.getRegionHeight());
        BackgroundLayer.setHorizontalSize(10);
    }
}
