package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.gdx.level.EditorIdentifierObject;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.ArrayUtilities;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.level.Direction;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.jump.level.TileObject;
import com.bitdecay.jump.leveleditor.EditorHook;
import com.bitdecay.jump.leveleditor.render.LibGDXWorldRenderer;
import com.bitdecay.jump.leveleditor.utils.LevelUtilities;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.actors.ai.Monkey;
import com.bitdecay.ludum.dare.actors.environment.DeadShip;
import com.bitdecay.ludum.dare.actors.items.ShipPart;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.background.BackgroundManager;
import com.bitdecay.ludum.dare.cameras.FollowOrthoCamera;
import com.bitdecay.ludum.dare.collection.GameObjects;
import com.bitdecay.ludum.dare.components.LevelInteractionComponent;
import com.bitdecay.ludum.dare.editor.MonkeyEditorObject;
import com.bitdecay.ludum.dare.editor.deadship.DeadShipEditorObject;
import com.bitdecay.ludum.dare.editor.shippart.*;
import com.bitdecay.ludum.dare.hud.Hud;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.*;

public class GameScreen implements Screen, EditorHook {

    private LudumDareGame game;

    FollowOrthoCamera camera;
    LibGDXWorldRenderer worldRenderer = new LibGDXWorldRenderer();
    BitWorld world = new BitWorld();
    GameObjects gobs = new GameObjects();

    private BackgroundManager backgroundManager;

    private Hud hud;
    private Player player;
    private Monkey monkey;

    private SpriteBatch uiBatch;
    private SpriteBatch gobsBatch;
    private ShapeRenderer debugRenderer;
    Map<Integer, TextureRegion[]> tilesetMap = new HashMap<>();
    private Level currentLevel;

    LevelInteractionComponent levelInteraction;

    public GameScreen(LudumDareGame game) {
        this.game = game;

        camera = new FollowOrthoCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.maxZoom = 0.3f;
        camera.snapSpeed = 0.2f;

        backgroundManager = new BackgroundManager(camera);

        world.setGravity(0, -900);
        player = new Player();
        levelInteraction = new LevelInteractionComponent(world, gobs);


        Array<AnimagicTextureRegion> aztecTileTextures = LudumDareGame.atlas.findRegions("tiles/aztec");
        Array<AnimagicTextureRegion> bridgesTileTextures = LudumDareGame.atlas.findRegions("tiles/bridges");
        Array<AnimagicTextureRegion> rockTileTextures = LudumDareGame.atlas.findRegions("tiles/rock");
        Array<AnimagicTextureRegion> aztecBackgroundTileTextures = LudumDareGame.atlas.findRegions("tiles/aztec_bgt");
        Array<AnimagicTextureRegion> aztecVinesTileTextures = LudumDareGame.atlas.findRegions("tiles/aztec_vines");

        tilesetMap.put(0, aztecTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(1, bridgesTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(2, rockTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(3, aztecBackgroundTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(4, aztecVinesTileTextures.toArray(TextureRegion.class));

        currentLevel = LevelUtilities.loadLevel(ResourceDir.path("thePit.level"));
        world.setLevel(currentLevel);
        levelChanged(currentLevel);

        monkey = new Monkey(0, 0);
        monkey.addToScreen(new LevelInteractionComponent(world, gobs));

        hud = new Hud(player);
        uiBatch = new SpriteBatch();
        gobsBatch = new SpriteBatch();
        debugRenderer = new ShapeRenderer();
        debugRenderer.setAutoShapeType(true);
    }

    private void forceBackgroundTiles(Level level) {
        for (int x = 0; x < level.gridObjects.length; x++) {
            for (int y = 0; y < level.gridObjects[0].length; y++) {
                TileObject obj = level.gridObjects[x][y];
                if (obj != null && isBackgroundMaterial(obj.material)) {
                    obj.collideNValue = 15;
                    updateOwnNeighborValues(level.gridObjects, x, y);
                }
            }
        }
    }

    private boolean isBackgroundMaterial(int m) {
        // To add another tile set, say 5, that is a background change like this:

        // return (m == 3 || m == 5);

        return (m == 3);
    }
    void updateOwnNeighborValues(TileObject[][] grid, int x, int y) {
        if (!ArrayUtilities.onGrid(grid, x, y) || grid[x][y] == null) {
            return;
        }

        // check right
        if (ArrayUtilities.onGrid(grid, x + 1, y) && grid[x + 1][y] != null && !isBackgroundMaterial(grid[x+1][y].material)) {
            grid[x+1][y].collideNValue &= Direction.NOT_LEFT;
            grid[x+1][y].renderNValue &= Direction.NOT_LEFT;
        }
        // check left
        if (ArrayUtilities.onGrid(grid, x - 1, y) && grid[x - 1][y] != null && !isBackgroundMaterial(grid[x-1][y].material)) {
            grid[x-1][y].collideNValue &= Direction.NOT_RIGHT;
            grid[x-1][y].renderNValue &= Direction.NOT_RIGHT;
        }
        // check up
        if (ArrayUtilities.onGrid(grid, x, y + 1) && grid[x][y + 1] != null && !isBackgroundMaterial(grid[x][y+1].material)) {
            grid[x][y+1].collideNValue &= Direction.NOT_DOWN;
            grid[x][y+1].renderNValue &= Direction.NOT_DOWN;
        }
        // check down
        if (ArrayUtilities.onGrid(grid, x, y - 1) && grid[x][y - 1] != null && !isBackgroundMaterial(grid[x][y-1].material)) {
            grid[x][y-1].collideNValue &= Direction.NOT_UP;
            grid[x][y-1].renderNValue &= Direction.NOT_UP;
        }
    }

    @Override
    public void show() {
        SoundLibrary.loopMusic("ambientGame");
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Background
        gobsBatch.begin();

        backgroundManager.draw(gobsBatch);

        gobsBatch.end();

        worldRenderer.render(world, cam);

        // Level and game objects.
        gobsBatch.setProjectionMatrix(cam.combined);
        gobsBatch.begin();

        drawLevel();

        gobs.draw(gobsBatch);
        gobsBatch.end();

        // debug renderer
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin();
        monkey.debugDraw(debugRenderer);
        debugRenderer.end();

        // UI/HUD
        uiBatch.begin();
        hud.render(uiBatch);
        uiBatch.end();

    }

    private void drawLevel() {
        /**
         * TODO: we still need to find a better way to load a grid into the world but with custom tile objects.
         * It shouldn't be hard, but it does need to be done.
         **/
        for (int x = 0; x < currentLevel.gridObjects.length; x++) {
            for (int y = 0; y < currentLevel.gridObjects[0].length; y++) {
                TileObject obj = currentLevel.gridObjects[x][y];
                if (obj != null) {
                    gobsBatch.draw(tilesetMap.get(obj.material)[obj.renderNValue], obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
                }
            }
        }
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

        backgroundManager.update(delta);
        //TODO: LF ,for testing monkey ai
//        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//            Vector3 worldPos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
//            monkey.debugMonkeyAi(worldPos.x, worldPos.y);
//        }
    }

    @Override
    public List<EditorIdentifierObject> getTilesets() {
        return Arrays.asList(
                new EditorIdentifierObject(0, "Aztec", tilesetMap.get(0)[0]),
                new EditorIdentifierObject(1, "Bridges", tilesetMap.get(1)[0]),
                new EditorIdentifierObject(2, "Rock", tilesetMap.get(2)[0]));
    }

    @Override
    public List<RenderableLevelObject> getCustomObjects() {
        List<RenderableLevelObject> items = new ArrayList<>();

        items.add(new AlienGunEditorObject());
        items.add(new CockpitEditorObject());
        items.add(new EngineEditorObject());
        items.add(new NavModuleEditorObject());
        items.add(new ShieldModuleEditorObject());
        items.add(new WingsEditorObject());
        items.add(new DeadShipEditorObject());
        items.add(new MonkeyEditorObject());

        return items;
    }

    private void buildGameObjects(Collection<LevelObject> otherObjects) {
        for (LevelObject levelObject : otherObjects) {
            if (levelObject instanceof RenderableLevelObject) {
                RenderableLevelObject rlo = (RenderableLevelObject) levelObject;
                BitPoint p = rlo.rect.xy;

                if (rlo instanceof IEditorShipPart) {
                    ShipPart part = new ShipPart(rlo.name());
                    part.setPosition(p.x, p.y);
                    part.addToLevel(levelInteraction);
                } else if (rlo instanceof DeadShipEditorObject) {
                    DeadShip ship = DeadShip.create(levelInteraction);
                    ship.setPosition(p.x, p.y);
                } else if (rlo instanceof MonkeyEditorObject) {
                    Monkey monkey = new Monkey(p.x, p.y);
                    monkey.addToScreen(levelInteraction);
                }
            }
        }
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
        currentLevel = level;
        world.removeAllBodies();
        world.setLevel(level);

        buildGameObjects(level.otherObjects);
        player.addToScreen(levelInteraction);
    }
}
