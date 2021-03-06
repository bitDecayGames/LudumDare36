package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bitdecay.ludum.dare.LudumDareGame;

public class SplashScreen implements Screen {

    private Image ldWallpaper;
    private Image bdWallpaper;
    private Stage stage;
    private LudumDareGame game;
    public static Music INTRO_MUSIC;

    public SplashScreen(LudumDareGame game){
        this.game = game;
        stage = new Stage();
        ldWallpaper = new Image(LudumDareGame.atlas.findRegion("splash/ld36Splash"));
        ldWallpaper.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bdWallpaper = new Image(LudumDareGame.atlas.findRegion("splash/bitDecay"));
        bdWallpaper.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(ldWallpaper);
        stage.addActor(bdWallpaper);
    }

    @Override
    public void show() {
//        SoundLibrary.loopMusic("ambientIntro");
        // TODO add intro music.

        bdWallpaper.addAction(Actions.alpha(0));
        ldWallpaper.addAction(
            Actions.sequence(
                Actions.alpha(0),
                Actions.delay(0.25f),
                Actions.fadeIn(2f),
                Actions.delay(2f),
                Actions.fadeOut(2f),
                Actions.run(
                    new Runnable() {
                        @Override
                        public void run() {
                            bdWallpaper.addAction(Actions.sequence(
                                Actions.fadeIn(2f),
                                Actions.delay(2f),
                                Actions.fadeOut(2f),
                                Actions.run(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            nextScreen();
                                        }
                                    }
                                )));
                        }
                    }
                )
            )
        );
    }

    @Override
    public void render(float delta) {
        LudumDareGame.assetManager.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            nextScreen();
        }

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
        stage.dispose();
    }

    private void nextScreen() {
        game.setScreen(new MainMenuScreen(game));
    }
}
