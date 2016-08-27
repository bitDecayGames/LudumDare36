package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

/**
 * THIS CLASS IS FOR DEBUGGING THE PLAYER, SHOULD NOT BE USED!!!!!!!!!
 */
public class TestStateScreen implements Screen {

    OrthographicCamera camera;
    AnimagicSpriteBatch batch;

    Animation animation;
    AnimagicTextureAtlas atlas;

    public TestStateScreen(Game game) {
        super();
        LudumDareGame.assetManager.load("packed/player.atlas", AnimagicTextureAtlas.class);
        LudumDareGame.queueAssetsForLoad();
    }

    @Override
    public void show() {
        LudumDareGame.assetManager.finishLoading();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new AnimagicSpriteBatch(camera);
        batch.isShaderOn(true);

        atlas = LudumDareGame.assetManager.get("packed/tiles.atlas", AnimagicTextureAtlas.class);
        animation = new Animation("test", Animation.AnimationPlayState.REPEAT, FrameRate.total(5),
                new AnimagicTextureRegion[]{atlas.findRegions("crystal").get(14)});
    }

    @Override
    public void render(float delta) {
        animation.update(delta);

        camera.update();

        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        Gdx.gl.glClearColor(0, 0.1f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setAmbientColor(Color.WHITE);
        batch.setAmbientIntensity(0.01f);
        batch.setNextLight(mousePos.x, mousePos.y, 0.1f, 0.9f, Color.WHITE);

        batch.draw(animation.getFrame(), -200, -200, 400, 400);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
