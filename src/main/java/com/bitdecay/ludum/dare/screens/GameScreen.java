package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.leveleditor.render.LibGDXWorldRenderer;
import com.bitdecay.jump.leveleditor.utils.LevelUtilities;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.collection.GameObjects;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;

/**
 * Created by jacob on 8/27/16.
 */
public class GameScreen implements Screen {

    private LudumDareGame game;

    OrthographicCamera camera = new OrthographicCamera(512, 512);
    LibGDXWorldRenderer worldRenderer = new LibGDXWorldRenderer();
    BitWorld world = new BitWorld();
    GameObjects gobs = new GameObjects();

    public GameScreen(LudumDareGame game) {
        this.game = game;

        world.setGravity(0, -900);
        Player player = new Player();
        LevelInteractionComponent playerLevelLink = new LevelInteractionComponent(world, gobs);
        player.addToScreen(playerLevelLink);

        world.setLevel(LevelUtilities.loadLevel("src/main/resources/thePit.level"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

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
