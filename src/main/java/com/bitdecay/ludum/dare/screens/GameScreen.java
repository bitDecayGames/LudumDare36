package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.leveleditor.render.LibGDXWorldRenderer;
import com.bitdecay.jump.leveleditor.utils.LevelUtilities;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.collection.GameObjects;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.hud.Hud;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;

/**
 * Created by jacob on 8/27/16.
 */
public class GameScreen implements Screen {

    private LudumDareGame game;

    OrthographicCamera camera = new OrthographicCamera(900, 600);
    LibGDXWorldRenderer worldRenderer = new LibGDXWorldRenderer();
    BitWorld world = new BitWorld();
    GameObjects gobs = new GameObjects();
    private Hud hud;
    private Player player;
    private SpriteBatch uiBatch;

    public GameScreen(LudumDareGame game) {
        this.game = game;

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
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(delta);
        camera.update();
        worldRenderer.render(world, camera);

        uiBatch.begin();
        hud.render(uiBatch);
        uiBatch.end();

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
}
