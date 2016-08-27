package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.gdx.level.EditorIdentifierObject;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.leveleditor.EditorHook;
import com.bitdecay.jump.leveleditor.render.LibGDXWorldRenderer;
import com.bitdecay.jump.leveleditor.utils.LevelUtilities;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.cameras.FollowOrthoCamera;
import com.bitdecay.ludum.dare.collection.GameObjects;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.hud.Hud;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.*;

/**
 * Created by jacob on 8/27/16.
 */
public class GameScreen implements Screen, EditorHook {

    private LudumDareGame game;

    FollowOrthoCamera camera;
    LibGDXWorldRenderer worldRenderer = new LibGDXWorldRenderer();
    BitWorld world = new BitWorld();
    GameObjects gobs = new GameObjects();
    private Hud hud;
    private Player player;

    private SpriteBatch uiBatch;
    private SpriteBatch gobsBatch;
    Map<Integer, TextureRegion[]> tilesetMap = new HashMap<>();

    public GameScreen(LudumDareGame game) {
        this.game = game;

        camera = new FollowOrthoCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.maxZoom = 0.5f;

        world.setGravity(0, -900);
        player = new Player();
        LevelInteractionComponent playerLevelLink = new LevelInteractionComponent(world, gobs);
        player.addToScreen(playerLevelLink);

        Array<AnimagicTextureRegion> aztecTileTextures = LudumDareGame.atlas.findRegions("tiles/aztec");
        tilesetMap.put(0, aztecTileTextures.toArray(TextureRegion.class));
        world.setLevel(LevelUtilities.loadLevel(ResourceDir.path("thePit.level")));

        hud = new Hud(player);
        uiBatch = new SpriteBatch();
        gobsBatch = new SpriteBatch();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);

        draw(camera);
    }

    @Override
    public void render(OrthographicCamera cam) {
        draw(cam);
    }

    private void draw(OrthographicCamera cam) {
        gobsBatch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldRenderer.render(world, cam);

        gobsBatch.begin();
        gobs.draw(gobsBatch);
        gobsBatch.end();

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

    public void update(float delta) {
        world.step(delta);
        gobs.update(delta);

        camera.addFollowPoint(player.getPosition());
        camera.update();


    }

    private void draw() {


        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // worldRenderer.render(world, camera);

        // gobsBatch.begin();
        // gobs.draw(gobsBatch);
        // gobsBatch.end();

        // uiBatch.begin();
        // hud.render(uiBatch);
        // uiBatch.end();
    }

    @Override
    public List<EditorIdentifierObject> getTilesets() {
        return Arrays.asList(new EditorIdentifierObject(0, "Fallback", tilesetMap.get(0)[0]));
    }

    @Override
    public List<RenderableLevelObject> getCustomObjects() {
        List<RenderableLevelObject> exampleItems = new ArrayList<>();
        return exampleItems;
    }

    @Override
    public List<EditorIdentifierObject> getThemes() {
        return Collections.emptyList();
    }

    @Override
    public BitWorld getWorld() {
        return world;
    }

    @Override
    public void levelChanged(Level level) {
    }
}
