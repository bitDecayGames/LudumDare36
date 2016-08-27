package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bitdecay.ludum.dare.LudumDareGame;

public class WaitingScreen implements Screen {

    LudumDareGame game;

    private Image ldWallpaper;
    private Stage stage;

    public WaitingScreen(LudumDareGame game) {
        this.game = game;

        stage = new Stage();
        ldWallpaper = new Image(new TextureRegion(new Texture(Gdx.files.internal("menu/loading.png"))));
        ldWallpaper.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(ldWallpaper);
    }

    @Override
    public void show() {
        ldWallpaper.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.delay(0.2f),
                        Actions.fadeIn(1f),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        nextScreen();
                                    }
                                }
                        )
                )
        );
    }

    //TODO make this do a thing that we want it to do
    private void nextScreen() {
//        game.setScreen(new UpgradeScreen(game));
    }

    @Override
    public void render(float delta) {
        LudumDareGame.assetManager.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
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
        dispose();
    }

    @Override
    public void dispose() {
    }
}
