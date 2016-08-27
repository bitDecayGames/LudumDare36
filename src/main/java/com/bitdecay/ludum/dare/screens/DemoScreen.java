package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;

public class DemoScreen implements Screen {

    private Stage stage;
    private LudumDareGame game;

    public DemoScreen(LudumDareGame game){
        this.game = game;
        stage = new Stage();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        LudumDareGame.assetManager.update();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (InputUtil.checkInputs(Input.Keys.S, Xbox360Pad.BACK)) {
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
