package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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
import com.bitdecay.jump.leveleditor.render.LibGDXWorldRenderer;
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.collection.GameObjects;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;
import com.bitdecay.ludum.dare.gameobject.*;
import com.bitdecay.ludum.dare.levelobject.*;
import com.bitdecay.ludum.dare.levels.LevelSegmentAggregator;
import com.bitdecay.ludum.dare.levels.LevelSegmentGenerator;
import com.bitdecay.ludum.dare.util.Players;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.*;

public class RaceScreen implements Screen, EditorHook {
    LudumDareGame game;

    private Music music;

    public static final int CRYSTAL_MATERIAL = 0;
    public static final int WOOD_MATERIAL = 1;

    OrthographicCamera[] cameras;
    AnimagicSpriteBatch batch;
    SpriteBatch ui;
    ShapeRenderer debug;
    LibGDXWorldRenderer worldRenderer = new LibGDXWorldRenderer();

    Map<Class, Class> builderMap = new HashMap<>();

    Map<Integer, TextureRegion[]> tilesetMap = new HashMap<>();

    BitWorld world = new BitWorld();
    Level currentLevel = new Level();
    GameObjects gameObjects = new GameObjects();

    FinishLineGameObject finishLine;
    public FinishLineGameObject finishOverride;

    TextureRegion splitScreenSeparator;
    AnimagicTextureRegion background;

    float testZ = 0.1f;
    float testAtten = 0.9f;

    FPSLogger fps = new FPSLogger();

    public RaceScreen(LudumDareGame game) {
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
        cameras = new OrthographicCamera[Players.list().size()];

        generateNextLevel(10);
    }

    public void generateNextLevel(int length) {
        LevelSegmentGenerator generator = new LevelSegmentGenerator(length);
        List<Level> levels = generator.generateLevelSegments();

        List<AINodeLevelObject> aiLevelNodes = new ArrayList<>();
        for (Level level : levels) {
            List<AINodeLevelObject> lvlNodes = new ArrayList<>();
            for (LevelObject otherObject : level.otherObjects) {
                if (otherObject instanceof AINodeLevelObject) {
                    lvlNodes.add((AINodeLevelObject) otherObject);
                }
            }
            lvlNodes.sort((a, b) -> a.nodeIndex - b.nodeIndex);
            aiLevelNodes.addAll(lvlNodes);
        }


        Level raceLevel = LevelSegmentAggregator.assembleSegments(levels);

        levelChanged(raceLevel);

        List<AINodeGameObject> nodes = gameObjects.getAINodes();

        List<AINodeGameObject> sortedNodes = new ArrayList<>();
        aiLevelNodes.forEach(aiLevelNode -> {
            for (AINodeGameObject node : nodes) {
                if (node.levelObject == aiLevelNode) {
                    sortedNodes.add(node);
                    break;
                }
            }
        });

        for (Player player : Players.list()) {
            player.winner = false;
            if (player.getInputComponent() instanceof AIControlComponent) {
                AIControlComponent input = (AIControlComponent) player.getInputComponent();
                input.discoverMe();
                input.setAINodes(sortedNodes);
            }
        }
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

        for (int i = 0; i < cameras.length; i++) {
            cameras[i] = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }
        batch = new AnimagicSpriteBatch();
        batch.isShaderOn(true);

        ui = new SpriteBatch();

        debug = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        fps.log();
        try {
            update(delta);
            debug();

            draw();
        } catch (Exception e) {
            game.setScreen(new ErrorScreen(game, e));
        }
    }

    public void debug() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) batch.isShaderOn(!batch.isShaderOn());
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) batch.useShadow(!batch.useShadow());
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) batch.debugNormals(!batch.debugNormals());
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) zoomCameras(-0.01f);
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) zoomCameras(0.01f);
    }

    public void zoomCameras(float amount) {
        for (OrthographicCamera camera : cameras) {
            camera.zoom += amount;
        }
    }

    public void update(float delta){
        if (!finishLine.raceOver) {
            world.step(delta);
            // Reset level
            if (InputUtil.checkInputs(Input.Keys.R, Xbox360Pad.BACK)) {
                game.setScreen(new RaceScreen(game));
            }
        } else {
            if (LudumDareGame.MUSIC_ON && music.isPlaying()) {
                music.stop();
            }
            game.setScreen(new WaitingScreen(game));
        }
        gameObjects.update(delta);

        updateCameras(delta);
    }

    private void updateCameras(float delta) {
        for (int i = 0; i < cameras.length; i++) {
            Camera cam = cameras[i];
            // Follow player
            Vector2 playerPos = Players.list().get(i).getPosition();
            cam.position.set(new Vector3(playerPos.x, playerPos.y, 0));
            cam.update();
        }
    }

    @Override
    public void render(OrthographicCamera cam) {
        for(OrthographicCamera playerCam : cameras) {
            playerCam.position.set(cam.position);
            playerCam.zoom = cam.zoom;
            playerCam.viewportWidth = cam.viewportWidth;
            playerCam.viewportHeight = cam.viewportHeight;
            playerCam.projection.set(cam.projection);

            playerCam.update();
        }
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
        exampleItems.add(new AINodeLevelObject());
        return exampleItems;
    }

    private void draw(){
        Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int screenWidth = Gdx.graphics.getWidth() / 2;
        int screenHeight = Gdx.graphics.getHeight() / 2;

        if (cameras.length > 1) {
            if (cameras.length > 3) {
                Gdx.gl.glViewport(screenWidth, 0, screenWidth, screenHeight);
                draw(cameras[3]);
            }
            if (cameras.length > 2) {
                Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
                draw(cameras[2]);
            }
            if (cameras.length > 1) {
                Gdx.gl.glViewport(screenWidth, screenHeight, screenWidth, screenHeight);
                draw(cameras[1]);
            }
            Gdx.gl.glViewport(0, screenHeight, screenWidth, screenHeight);
            draw(cameras[0]);
        } else {
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            draw(cameras[0]);
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ui.begin();
        if (cameras.length > 1) {
            ui.draw(splitScreenSeparator, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        ui.end();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) testZ *= 1.05f;
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) testZ *= 0.95f;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) testAtten *= 1.1f;
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) testAtten *= 0.95f;

//        worldRenderer.render(world, cameras[0]);
    }

    private void draw(OrthographicCamera cam) {
        batch.setCamera(cam);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        Vector3 mousePos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        batch.setAmbientColor(new Color(0.0f, 0.0f, 0.0f, 1));
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
        drawLevelEdit(cam);
        gameObjects.draw(batch);
        batch.end();

        // TODO: only for debugging
//        debug.setProjectionMatrix(cam.combined);
//        debug.setAutoShapeType(true);
//        debug.begin();
//        debug.set(ShapeRenderer.ShapeType.Line);
//        debug.setColor(Color.WHITE);
//        gameObjects.draw(debug);
//        debug.end();
    }

    private void drawLevelEdit(OrthographicCamera cam) {
        /**
         * TODO: we still need to find a better way to load a grid into the world but with custom tile objects.
         * It shouldn't be hard, but it does need to be done.
         **/
        for (int x = 0; x < currentLevel.gridObjects.length; x++) {
            for (int y = 0; y < currentLevel.gridObjects[0].length; y++) {
                TileObject obj = currentLevel.gridObjects[x][y];
                if (obj != null) {
                    if (Math.abs(obj.rect.xy.x - cam.position.x) < cam.viewportWidth &&
                            Math.abs(obj.rect.xy.y - cam.position.y) < cam.viewportHeight) {
                        batch.draw(tilesetMap.get(obj.material)[obj.renderNValue], obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
                    }
                }
            }
        }
    }

    @Override
    public void levelChanged(Level level) {
        LevelSegmentAggregator.updateAllNeighborRenderValues(level);
        gameObjects.clear();
        world.removeAllBodies();
        finishLine = null;
        if (finishOverride != null) {
            finishLine = finishOverride;
        }

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
            if (object instanceof FinishLineGameObject) {
                finishLine = (FinishLineGameObject) object;
            }
        }

        if (finishLine == null) {
            throw new RuntimeException("No finish line found in level");
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
