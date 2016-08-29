package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.actors.ai.*;
import com.bitdecay.ludum.dare.actors.ai.bat.Bat;
import com.bitdecay.ludum.dare.actors.environment.DeadShip;
import com.bitdecay.ludum.dare.actors.environment.HealthTotem;
import com.bitdecay.ludum.dare.actors.items.ShipPart;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.background.BackgroundManager;
import com.bitdecay.ludum.dare.cameras.FollowOrthoCamera;
import com.bitdecay.ludum.dare.collection.GameObjects;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.editor.*;
import com.bitdecay.ludum.dare.editor.deadship.DeadShipEditorObject;
import com.bitdecay.ludum.dare.editor.shippart.*;
import com.bitdecay.ludum.dare.hud.Hud;
import com.bitdecay.ludum.dare.interfaces.IShapeDraw;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.*;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class GameScreen implements Screen, EditorHook {
    public static final boolean DEBUG = false;
//    public static final String LEVEL_NAME = "flatTest.level";
    public static final String LEVEL_NAME = "thePit.level";

    public static final Color HURT_TINT = new Color(1, 0.5f, 0.5f, 1);

    private LudumDareGame game;

    FollowOrthoCamera camera;
    LibGDXWorldRenderer worldRenderer = new LibGDXWorldRenderer();
    BitWorld world = new BitWorld();
    GameObjects gobs = new GameObjects();

    private BackgroundManager backgroundManager;

    private Hud hud;
    private Player player;

    private SpriteBatch uiBatch;
    private SpriteBatch gobsBatch;
    private ShapeRenderer debugRenderer;
    Map<Integer, TextureRegion[]> tilesetMap = new HashMap<>();
    private Level currentLevel;

    LevelInteractionComponent levelInteraction;

    Animation hurtAnimation;

    Pixmap black = new Pixmap(1, 1, Pixmap.Format.RGB888);
    Sprite fader;
    float faderAlpha = 0;

    public GameScreen(LudumDareGame game) {
        this.game = game;

        black.drawPixel(1, 1, 0x000000);
        fader = new Sprite(new TextureRegion(new Texture(black)));
        fader.setAlpha(faderAlpha);
        fader.setPosition(0, 0);
        fader.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        black.dispose();

        camera = new FollowOrthoCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.maxZoom = 0.4f;
        camera.minZoom = 1f;
        camera.snapSpeed = 0.04f;

        backgroundManager = new BackgroundManager(camera);

        world.setGravity(0, -900);
        player = new Player(camera);
        levelInteraction = new LevelInteractionComponent(world, gobs);


        Array<AnimagicTextureRegion> aztecTileTextures = atlas.findRegions("tiles/aztec");
        Array<AnimagicTextureRegion> bridgesTileTextures = atlas.findRegions("tiles/bridges");
        Array<AnimagicTextureRegion> rockTileTextures = atlas.findRegions("tiles/rock");
        Array<AnimagicTextureRegion> aztecBackgroundTileTextures = atlas.findRegions("tiles/aztec_bgt");
        Array<AnimagicTextureRegion> aztecVinesTileTextures = atlas.findRegions("tiles/aztec_vines");
        Array<AnimagicTextureRegion> rockBackgroundTileTextures = atlas.findRegions("tiles/rock_bgt");
        Array<AnimagicTextureRegion> rock2rockTileTextures = atlas.findRegions("tiles/rock2rock");
        Array<AnimagicTextureRegion> aztec2aztecTileTextures = atlas.findRegions("tiles/aztec2aztec");

        hurtAnimation = new Animation("hurt", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("screen/hurt").toArray(AnimagicTextureRegion.class));

        tilesetMap.put(0, aztecTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(3, aztecBackgroundTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(7, aztec2aztecTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(4, aztecVinesTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(1, bridgesTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(2, rockTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(5, rockBackgroundTileTextures.toArray(TextureRegion.class));
        tilesetMap.put(6, rock2rockTileTextures.toArray(TextureRegion.class));

        currentLevel = LevelUtilities.loadLevel(ResourceDir.path(LEVEL_NAME));
        world.setLevel(currentLevel);
        levelChanged(currentLevel);

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

        return (m == 3 || m == 5);
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
        if (faderAlpha >= 1) {
            return;
        }


        // Background
        gobsBatch.begin();

        backgroundManager.draw(gobsBatch);

        gobsBatch.end();

        // Debug Body Renderer
        if (DEBUG) worldRenderer.render(world, cam);

        // Level and game objects.
        gobsBatch.setProjectionMatrix(cam.combined);
        gobsBatch.begin();

        drawLevel();

        gobs.draw(gobsBatch);


        gobsBatch.end();

        // debug renderer
        if (DEBUG) {
            debugRenderer.setProjectionMatrix(cam.combined);
            debugRenderer.begin();
            Iterator<GameObject> iter = gobs.getIter();
            while (iter.hasNext()) {
                GameObject obj = iter.next();
                if (obj instanceof IShapeDraw) ((IShapeDraw) obj).draw(debugRenderer);
            }
            debugRenderer.end();
        }

        // UI/HUD
        uiBatch.begin();
        hud.render(uiBatch);
        fader.draw(uiBatch);

        if (player.isInvincible()) {
            hurtAnimation.update(1f / 60f);
            uiBatch.draw(hurtAnimation.getFrame(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
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

        if (DeadShip.getNumCollectedParts() >= 6) {
            faderAlpha += .01;
            fader.setAlpha(faderAlpha);

            if (faderAlpha >= 1) {
                game.setScreen(new CreditsScreen(game));
            }
        }

        // This adds anything with a FollowComponent to the camera view (player)
        gobs.findWithComponents(FollowComponent.class, PositionComponent.class).forEach(obj -> {
            Optional<PositionComponent> pos = obj.findComponent(PositionComponent.class);
            if (pos.isPresent()) camera.addFollowPoint(pos.get().toVector2());
        });
        // This adds anything that has the ImportantNearPlayerComponent to the camera view that is near the player
        gobs.findWithComponents(ImportantNearPlayerComponent.class, PositionComponent.class).forEach(obj -> {
            Optional<PositionComponent> pos = obj.findComponent(PositionComponent.class);
            if (pos.isPresent()) {
                Optional<ImportantNearPlayerComponent> imp = obj.findComponent(ImportantNearPlayerComponent.class);
                Vector2 p = pos.get().toVector2();
                if (p.dst(player.getPosition()) < imp.get().range) {
                    camera.addFollowPoint(p);
                    if (! imp.get().near) SoundLibrary.playSound(imp.get().sound);
                    imp.get().near = true;
                } else imp.get().near = false;
            }
        });
        camera.update(delta);

        // this broadcasts agro messages to any nearby enemies
        gobs.findWithComponents(AgroComponent.class, PositionComponent.class).forEach(agro -> {
            Optional<AgroComponent> agroComponent = agro.findComponent(AgroComponent.class);
            Optional<PositionComponent> posComponent = agro.findComponent(PositionComponent.class);
            gobs.getGameObjectsOfType(Enemy.class).forEach(enemy -> {
                if (!enemy.hasComponent(AgroComponent.class) &&
                        !enemy.hasComponent(AgroCooldownComponent.class) &&
                        posComponent.get().toVector2().dst(enemy.getPosition()) < agroComponent.get().range) enemy.goAgro();
            });
            gobs.getGameObjectsOfType(Bat.class).forEach(bat -> {
                if (!bat.hasComponent(AgroComponent.class) &&
                        !bat.hasComponent(AgroCooldownComponent.class) &&
                        posComponent.get().toVector2().dst(bat.getPosition()) < agroComponent.get().range) {
                    bat.goAgro();
                }
            });
        });

        backgroundManager.update(delta);
        //TODO: LF ,for testing monkey ai
//        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//            Vector3 worldPos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
//            monkey.setAiMovementGoal(worldPos.x, worldPos.y);
//        }
    }

    @Override
    public List<EditorIdentifierObject> getTilesets() {
        return Arrays.asList(

                new EditorIdentifierObject(1, "Bridges", tilesetMap.get(1)[0]),
                new EditorIdentifierObject(2, "Rock", tilesetMap.get(2)[0]),
                new EditorIdentifierObject(5, "RockBack", tilesetMap.get(5)[0]),
                new EditorIdentifierObject(6, "Rock2Rock", tilesetMap.get(6)[0]),
                new EditorIdentifierObject(0, "Aztec", tilesetMap.get(0)[0]),
                new EditorIdentifierObject(3, "AztecBack", tilesetMap.get(3)[0]),
                new EditorIdentifierObject(7, "Aztec2Aztec", tilesetMap.get(7)[0]),
                new EditorIdentifierObject(4, "Vines", tilesetMap.get(4)[0]));
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
        items.add(new HealthTotemEditorObject());
        items.add(new MonkeyEditorObject());
        items.add(new InsaneMonkeyEditorObject());
        items.add(new GorillaEditorObject());
        items.add(new WarriorEditorObject());
        items.add(new BatEditorObject());

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
                    DeadShip ship = DeadShip.create(player, levelInteraction);
                    ship.setPosition(p.x, p.y);
                } else if (rlo instanceof MonkeyEditorObject) {
                    Monkey monkey = new Monkey(p.x, p.y, player);
                    monkey.addToScreen(levelInteraction);
                } else if (rlo instanceof InsaneMonkeyEditorObject) {
                    InsaneMonkey monkey = new InsaneMonkey(p.x, p.y, player);
                    monkey.addToScreen(levelInteraction);
                } else if (rlo instanceof HealthTotemEditorObject) {
                    HealthTotem totem = new HealthTotem(player);
                    totem.setPosition(p.x, p.y);
                    totem.addToLevel(levelInteraction);
                } else if (rlo instanceof GorillaEditorObject) {
                    Gorilla gorilla = new Gorilla(p.x, p.y, player);
                    gorilla.addToScreen(levelInteraction);
                } else if (rlo instanceof WarriorEditorObject) {
                    Warrior warrior = new Warrior(p.x, p.y, player);
                    warrior.addToScreen(levelInteraction);
                } else if (rlo instanceof BatEditorObject) {
                    Bat bat = new Bat(p.x, p.y, player);
                    bat.addToScreen(levelInteraction);
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
        gobs.clear();
        world.removeAllBodies();
        world.setLevel(level);
        forceBackgroundTiles(level);

        player.addToScreen(levelInteraction);
        buildGameObjects(level.otherObjects);
    }
}
