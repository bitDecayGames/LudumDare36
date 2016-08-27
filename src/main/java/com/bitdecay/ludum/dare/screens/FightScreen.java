package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;
import com.bitdecay.ludum.dare.gameobject.SpawnGameObject;
import com.bitdecay.ludum.dare.levelobject.SpawnLevelObject;
import com.bitdecay.ludum.dare.levels.LevelSegmentAggregator;
import com.bitdecay.ludum.dare.levels.LevelSegmentGenerator;
import com.bitdecay.ludum.dare.util.LightUtil;
import com.bitdecay.ludum.dare.util.Players;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;

import java.util.*;

public class FightScreen implements Screen, EditorHook {

    LudumDareGame game;

    Music music;

    OrthographicCamera camera;
    AnimagicSpriteBatch batch;

    Map<Class, Class> builderMap = new HashMap<>();

    Map<Integer, TextureRegion[]> tilesetMap = new HashMap<>();

    List<GameObject> gameObjects = new ArrayList<>();

    BitWorld world = new BitWorld();
    Level currentLevel = new Level();

    public FightScreen(LudumDareGame game) {
        if (game == null) {
            throw new Error("No game provided");
        }

        world.setGravity(0, -700);

        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/tiles.atlas", AnimagicTextureAtlas.class);
        tilesetMap.put(0, atlas.findRegion("fallbacktileset").split(16, 16)[0]);

        this.game = game;


        LevelSegmentGenerator generator = new LevelSegmentGenerator(10);
        Level raceLevel = LevelSegmentAggregator.assembleSegments(generator.generateLevelSegments());
        levelChanged(raceLevel);
    }

    @Override
    public void show() {

        music = SoundLibrary.loopMusic("ouroboros");

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.7f;

        batch = new AnimagicSpriteBatch();
        batch.isShaderOn(true);
        batch.setCamera(camera);
    }

    @Override
    public void render(float delta) {
        update(delta);

        draw();
    }

    public void update(float delta) {
        camera.update();

        world.step(delta);
        gameObjects.forEach(obj -> obj.update(delta));

        // Reset level
        if (InputUtil.checkInputs(Input.Keys.R, Xbox360Pad.BACK)) {
            game.setScreen(new FightScreen(game));
        }
    }

    @Override
    public void render(OrthographicCamera cam) {

    }

    @Override
    public BitWorld getWorld() {
        return world;
    }

    @Override
    public List<EditorIdentifierObject> getTilesets() {
        return null;
    }

    @Override
    public List<EditorIdentifierObject> getThemes() {
        return null;
    }

    @Override
    public List<RenderableLevelObject> getCustomObjects() {
        builderMap.put(SpawnLevelObject.class, SpawnGameObject.class);
        List<RenderableLevelObject> exampleItems = new ArrayList<>();
        exampleItems.add(new SpawnLevelObject());
        return exampleItems;
    }

    private void draw() {
        Gdx.gl.glClearColor(0.01f, 0, 0.02f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        LightUtil.addBasicLight(batch);
        drawLevelEdit();
        gameObjects.forEach(obj -> obj.draw(batch));
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
        gameObjects.clear();

        currentLevel = level;
        // TODO: deprecated? world.setTileSize(16);
        world.setGridOffset(level.gridOffset);
        world.setGrid(level.gridObjects);
        // TODO: deprecated? world.setTileSize(level.tileSize);
        world.setObjects(buildBodies(level.otherObjects));
        world.resetTimePassed();

//        for (GameObject gameObj : gameObjects) {
//            if (gameObj instanceof SpawnGameObject) {
//                SpawnGameObject spawn = (SpawnGameObject) gameObj;
//                for (Player player : Players.list()) {
//                    player.setPosition(spawn.pos.x, spawn.pos.y);
//                    player.addToWorld(world);
//                }
//            }
//        }

        for (Player player : Players.list()) {
            player.activateControls();
            // TODO fix/consolidate this.
//            player.addToScreen(new LevelInteractionComponent(world, gameObjects));
            // TODO handle spawn points.
            player.setPosition(0, 0);
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
                    gameObjects.add(newObject);
                } else {
                    bodies.add(levelObject.buildBody());
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
