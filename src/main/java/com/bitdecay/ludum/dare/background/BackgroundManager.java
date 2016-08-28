package com.bitdecay.ludum.dare.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

import java.util.ArrayList;
import java.util.List;

public class BackgroundManager implements IUpdate, IDraw {
    private static final float PARALLAX_EFFECT = 0.1f;

    private List<BackgroundLayer> layers;

    private OrthographicCamera backgroundCamera;
    private OrthographicCamera characterCamera;

    public BackgroundManager(OrthographicCamera characterCamera) {
        this.characterCamera = characterCamera;

        initDimensions();

        String[] backgroundNames = new String[] {
                "space",
                "hillsFar",
                "hillsMiddle",
                "hillsClose"
        };

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
                .setVerticalOffset(30)
                .addBackground("startUnderground")
                .addBackground("underground")
        );

        backgroundCamera = new OrthographicCamera(characterCamera.viewportWidth, characterCamera.viewportHeight);
    }

    @Override
    public void update(float delta) {
        backgroundCamera.position.set(characterCamera.position.x * PARALLAX_EFFECT + Gdx.graphics.getWidth() / 3, characterCamera.position.y * PARALLAX_EFFECT + Gdx.graphics.getHeight() / 3, 0);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        for (BackgroundLayer l : layers) {
            backgroundCamera.position.set(backgroundCamera.position.x * (1 + PARALLAX_EFFECT), backgroundCamera.position.y * (1 + PARALLAX_EFFECT), 0);
            backgroundCamera.update();

            spriteBatch.setProjectionMatrix(backgroundCamera.combined);
            l.draw(spriteBatch);
        }
    }

    private void initDimensions() {
        TextureRegion mainTile = BackgroundUtil.getBackground("space");
        BackgroundLayer.setTileWidth(mainTile.getRegionWidth());
        BackgroundLayer.setTileHeight(mainTile.getRegionHeight());
    }
}
