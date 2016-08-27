package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.leveleditor.render.LibGDXWorldRenderer;
import com.bitdecay.jump.leveleditor.utils.LevelUtilities;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.actors.state.StandState;
import com.bitdecay.ludum.dare.cameras.FollowOrthoCamera;
import com.bitdecay.ludum.dare.collection.GameObjects;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.hud.Hud;

public class GameScreen implements Screen {

    private LudumDareGame game;

    FollowOrthoCamera camera;
    LibGDXWorldRenderer worldRenderer = new LibGDXWorldRenderer();
    BitWorld world = new BitWorld();
    GameObjects gobs = new GameObjects();
    private Hud hud;
    private Player player;

    private SpriteBatch uiBatch;
    private SpriteBatch gobsBatch;

    public GameScreen(LudumDareGame game) {
        this.game = game;

        camera = new FollowOrthoCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.maxZoom = 0.5f;

        world.setGravity(0, -900);
        player = new Player();
        LevelInteractionComponent playerLevelLink = new LevelInteractionComponent(world, gobs);
        player.addToScreen(playerLevelLink);

        world.setLevel(LevelUtilities.loadLevel(ResourceDir.path("thePit.level")));
    }

    @Override
    public void show() {
        hud = new Hud(player);
        uiBatch = new SpriteBatch();
        gobsBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        update(delta);

        draw();
    }

    @Override
    public void resize(int i, int i1) {

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

    private void update (float delta) {
        world.step(delta);
        gobs.update(delta);

        camera.addFollowPoint(player.getPosition());
        camera.update();

        gobsBatch.setProjectionMatrix(camera.combined);
    }

    private void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldRenderer.render(world, camera);

        gobsBatch.begin();
        gobs.draw(gobsBatch);
        gobsBatch.end();

        uiBatch.begin();
        hud.render(uiBatch);
        uiBatch.end();
    }
}
