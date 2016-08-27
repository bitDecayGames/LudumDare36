package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

public class TitleScreen implements Screen {

    private Image datBoat;
    private Label dankTitle;

    private Stage stage;
    private LudumDareGame game;

    public TitleScreen(LudumDareGame game) {
        this.game = game;
        stage = new Stage();
    }

    @Override
    public void show() {
        datBoat = new Image(new TextureRegion(new Texture(Gdx.files.internal("assets/sprites/dankest_boat.jpg"))));
        datBoat.setScaling(Scaling.fill);
        datBoat.setWidth(Gdx.graphics.getWidth());
        datBoat.setHeight(Gdx.graphics.getHeight());

        stage.addActor(datBoat);

        Skin skin = new Skin(Gdx.files.internal("assets/skins/skin.json"));
        dankTitle = new Label("The Dankest Boat", skin);
        dankTitle.setFontScale(10);
        dankTitle.setFillParent(true);
        dankTitle.setAlignment(Align.top, Align.center);
        dankTitle.setColor(Color.GREEN);

        stage.addActor(dankTitle);
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
