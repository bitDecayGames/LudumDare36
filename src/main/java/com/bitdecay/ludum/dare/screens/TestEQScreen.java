package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.bitdecay.ludum.dare.util.SoundLibrary;

/**
 * THIS CLASS IS FOR DEBUGGING THE MUSIC AND SOUND EFFECTS, SHOULD NOT BE USED!!!!!!!!!
 */
public class TestEQScreen implements Screen {

    String music = "fight";
    String sound = "Jump4";


    Game game;

    public TestEQScreen(Game game) {
        super();
        this.game = game;
    }

    @Override
    public void show() {
        SoundLibrary.loopMusic(music);

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            SoundLibrary.playSound(sound);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.exit(0);
        }

        Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
