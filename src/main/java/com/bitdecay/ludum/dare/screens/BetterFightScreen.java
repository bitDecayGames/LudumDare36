package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.collision.ContactAdapter;
import com.bitdecay.jump.control.PlayerInputController;
import com.bitdecay.jump.gdx.input.GDXControls;
import com.bitdecay.jump.gdx.level.EditorIdentifierObject;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.jump.level.TileObject;
import com.bitdecay.jump.leveleditor.EditorHook;
import com.bitdecay.jump.leveleditor.example.game.SecretObject;
import com.bitdecay.jump.leveleditor.utils.LevelUtilities;
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.collection.GameObjects;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.gameobject.*;
import com.bitdecay.ludum.dare.levelobject.*;
import com.bitdecay.ludum.dare.levels.LevelSegmentAggregator;
import com.bitdecay.ludum.dare.util.Players;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.*;

public class BetterFightScreen implements Screen, EditorHook {
    LudumDareGame game;

    private Music music;

    public static final int CRYSTAL_MATERIAL = 0;
    public static final int WOOD_MATERIAL = 1;

    OrthographicCamera camera;
    AnimagicSpriteBatch batch;
    SpriteBatch ui;

    Map<Class, Class> builderMap = new HashMap<>();

    Map<Integer, TextureRegion[]> tilesetMap = new HashMap<>();

    BitWorld world = new BitWorld();
    Level currentLevel = new Level();
    GameObjects gameObjects = new GameObjects();

    TextureRegion splitScreenSeparator;
    AnimagicTextureRegion background;

    public BetterFightScreen(LudumDareGame game) {
        if (game == null) {
            throw new Error("No game provided");
        }

        constructBuilderMap();

        world.setGravity(0, -700);

        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/tiles.atlas", AnimagicTextureAtlas.class);
        Array<AnimagicTextureRegion> crystalTileTextures = atlas.findRegions("crystal");
        Array<AnimagicTextureRegion> bridgesTileTextures = atlas.findRegions("bridges");
        tilesetMap.put(0, crystalTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(1, bridgesTileTextures.toArray(TextureRegion.class));

        atlas = LudumDareGame.assetManager.get("packed/ui.atlas", AnimagicTextureAtlas.class);
        splitScreenSeparator = atlas.findRegion("splitscreenSeparator");

        atlas = LudumDareGame.assetManager.get("packed/level.atlas", AnimagicTextureAtlas.class);
        background = atlas.findRegion("background");


        this.game = game;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        loadLevel();
    }

    private void loadLevel() {
        Level loadedLevel = LevelUtilities.loadLevel("fightLevels/" + "fight_" + 1);
        levelChanged(loadedLevel);
    }

    private void constructBuilderMap() {
        builderMap.put(SpawnLevelObject.class, SpawnGameObject.class);
        builderMap.put(CoinLevelObject.class, CoinGameObject.class);
        builderMap.put(FinishLineLevelObject.class, FinishLineGameObject.class);
        builderMap.put(PowerupLevelObject.class, PowerupGameObject.class);
        builderMap.put(LightLevelObject.class, LightGameObject.class);
        builderMap.put(LanternLevelObject.class, LanternGameObject.class);
        builderMap.put(AINodeLevelObject.class, AINodeGameObject.class);
    }

    @Override
    public void show() {
        if(LudumDareGame.MUSIC_ON) {
            music = SoundLibrary.loopMusic("fight");
        }

        batch = new AnimagicSpriteBatch();
        batch.isShaderOn(true);

        ui = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        update(delta);

        draw();
    }

    public void update(float delta){
//        if (!finishLine.raceOver) {
//            world.step(delta);
//            // Reset level
//            if (InputUtil.checkInputs(Input.Keys.R, Xbox360Pad.BACK)) {
//                game.setScreen(new RaceScreen(game));
//            }
//        } else {
//            game.setScreen(new UpgradeScreen(game));
//        }
        gameObjects.update(delta);

    }

    @Override
    public void render(OrthographicCamera cam) {
        draw();
    }

    @Override
    public BitWorld getWorld() {
        return world;
    }

    @Override
    public List<EditorIdentifierObject> getTilesets() {
        return Arrays.asList(new EditorIdentifierObject(0, "Fallback", tilesetMap.get(0)[0]),
                new EditorIdentifierObject(1, "Bridges", tilesetMap.get(1)[0]));
    }

    @Override
    public List<EditorIdentifierObject> getThemes() {
        return Collections.emptyList();
    }

    @Override
    public List<RenderableLevelObject> getCustomObjects() {
        List<RenderableLevelObject> exampleItems = new ArrayList<>();
        exampleItems.add(new SpawnLevelObject());
        exampleItems.add(new CoinLevelObject());
        exampleItems.add(new FinishLineLevelObject());
        exampleItems.add(new PowerupLevelObject());
        exampleItems.add(new LightLevelObject());
        exampleItems.add(new LanternLevelObject());

        return exampleItems;
    }

    private void draw(){
        Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        draw(camera);
    }

    private void draw(Camera cam) {
        batch.setCamera(cam);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        Vector3 mousePos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        batch.setAmbientColor(new Color(0.1f, 0.1f, 0.1f, 1));
        batch.setAmbientIntensity(1f);
//        batch.setLight(0, mousePos.x, mousePos.y, testZ, testAtten, Color.WHITE);
        gameObjects.preDraw(batch);

        Vector3 bottomLeft = cam.unproject(new Vector3(0, Gdx.graphics.getHeight(),0));
        int yLimit = (int) (bottomLeft.y + Gdx.graphics.getHeight());
        int xLimit = (int) (bottomLeft.x + Gdx.graphics.getWidth());
        int renderY = (int) bottomLeft.y;
        while (renderY < yLimit) {
            int renderX = (int) bottomLeft.x;
            while (renderX < xLimit) {
                batch.draw(background, renderX, renderY, background.getRegionWidth() * 4, background.getRegionHeight() * 4);
                renderX += background.getRegionHeight() * 4;
            }
            renderY += background.getRegionWidth() * 4;
        }
//        batch.draw(background,bottomLeft.x, bottomLeft.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        batch.draw(background,0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        drawLevelEdit();
        gameObjects.draw(batch);
        batch.end();
    }

    private void drawLevelEdit() {
        /**
         * TODO: we still need to find a better way to load a grid into the world but with custom tile objects.
         * It shouldn't be hard, but it does need to be done.
         **/
        for (int x = 0; x < currentLevel.gridObjects.length; x++) {
            for (int y = 0; y < currentLevel.gridObjects[0].length; y++) {
                TileObject obj = currentLevel.gridObjects[x][y];
                if (obj != null) {
                    batch.draw(tilesetMap.get(obj.material)[obj.renderNValue], obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
                }
            }
        }
    }

    @Override
    public void levelChanged(Level level) {
        LevelSegmentAggregator.updateAllNeighborRenderValues(level);
        gameObjects.clear();
        world.removeAllBodies();

        currentLevel = level;
        // TODO: deprecated? world.setTileSize(16);
        world.setGridOffset(level.gridOffset);
        world.setGrid(level.gridObjects);
        // TODO: deprecated? world.setTileSize(level.tileSize);
        world.setObjects(buildBodies(level.otherObjects));
        world.resetTimePassed();

        gameObjects.doAdds();

        boolean spawnFound = false;
        Iterator<GameObject> iter = gameObjects.getIter();
        GameObject object;
        while (iter.hasNext()) {
            object = iter.next();
            if (object instanceof SpawnGameObject) {
                spawnFound = true;
                SpawnGameObject spawn = (SpawnGameObject) object;
                for (Player player : Players.list()) {
                    player.addToScreen(new LevelInteractionComponent(world, gameObjects));
                    player.setPosition(spawn.pos.x, spawn.pos.y);
                }
            }
        }

        if (!spawnFound) {
            for (Player player : Players.list()) {
                player.activateControls();
                player.addToScreen(new LevelInteractionComponent(world, gameObjects));
                // TODO handle spawn points.
                player.setPosition(0, 0);
            }
        }



        if (level.debugSpawn != null) {
            JumperBody playerBody = new JumperBody();
            playerBody.props = level.debugSpawn.props;
            playerBody.jumperProps = level.debugSpawn.jumpProps;

            playerBody.bodyType = BodyType.DYNAMIC;
            playerBody.aabb = new BitRectangle(level.debugSpawn.rect.xy.x, level.debugSpawn.rect.xy.y, 16, 32);
            playerBody.renderStateWatcher = new JumperRenderStateWatcher();
            playerBody.controller = new PlayerInputController(GDXControls.defaultMapping);

            playerBody.addContactListener(new ContactAdapter() {
                @Override
                public void contactStarted(BitBody other) {
                    if (other.userObject instanceof SecretObject) {
                        playerBody.props.gravityModifier = -1;
                    }
                }

                @Override
                public void contactEnded(BitBody other) {
                    if (other.userObject instanceof SecretObject) {
                        playerBody.props.gravityModifier = 1;
                    }
                }
            });

            world.addBody(playerBody);
        }
    }

    private Collection<BitBody> buildBodies(Collection<LevelObject> otherObjects) {
        try {
            ArrayList<BitBody> bodies = new ArrayList<>();
            for (LevelObject levelObject : otherObjects) {
                if (builderMap.containsKey(levelObject.getClass())) {
                    GameObject newObject;
                    newObject = ((GameObject) builderMap.get(levelObject.getClass()).newInstance());
                    bodies.addAll(newObject.build(levelObject));
                    newObject.append(new LevelInteractionComponent(world, gameObjects));
                    gameObjects.add(newObject);
                } else {
                    throw new RuntimeException("Found object that doesn't have mapping: " + levelObject);
                }
            }
            return bodies;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
