package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.leveleditor.render.LibGDXWorldRenderer;
import com.bitdecay.jump.leveleditor.utils.LevelUtilities;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

public class DemoScreen implements Screen {

    private Image datBoat;

    private Stage stage;
    private LudumDareGame game;
    OrthographicCamera camera = new OrthographicCamera(512, 512);
    LibGDXWorldRenderer worldRenderer = new LibGDXWorldRenderer();
    BitWorld world = new BitWorld();

    public DemoScreen(LudumDareGame game) {
        this.game = game;
        stage = new Stage();

        world.setLevel(LevelUtilities.loadLevel("thePit.level"));
    }

    @Override
    public void show() {
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/sprites.atlas", AnimagicTextureAtlas.class);
        datBoat = new Image(new TextureRegion(new Texture(Gdx.files.internal("assets/sprites/dankest_boat.jpg"))));

        stage.addActor(datBoat);
    }

    @Override
    public void render(float delta) {
//        LudumDareGame.assetManager.update();
//        Gdx.gl.glClearColor(1, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        if (InputUtil.checkInputs(Input.Keys.S, Xbox360Pad.BACK)) {
//            nextScreen();
//        }

        world.step(delta);
        camera.update();
        worldRenderer.render(world, camera);

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
