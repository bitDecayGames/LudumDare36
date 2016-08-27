package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.components.SizeComponent;
import com.bitdecay.ludum.dare.control.ControllerScreenObject;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;
import com.bitdecay.ludum.dare.text.TextScreenObject;
import com.bitdecay.ludum.dare.util.LightUtil;
import com.bitdecay.ludum.dare.util.Players;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;

import java.util.ArrayList;
import java.util.List;

public class SetupScreen implements Screen {

    OrthographicCamera camera;
    AnimagicSpriteBatch batch;

    LudumDareGame game;

    List<GameObject> otherObjects;
    List<ControllerScreenObject> inputObjects;
    List<Player> players;

    TextureRegion splitScreenSeparator;

    final static int NUM_PLAYERS = 4;

    // Keyboard keys for selections.
    final static List<Integer> keyboardSelectKeys = new ArrayList<>();
    static {
        keyboardSelectKeys.add(Input.Keys.Q);
        keyboardSelectKeys.add(Input.Keys.W);
        keyboardSelectKeys.add(Input.Keys.E);
        keyboardSelectKeys.add(Input.Keys.R);
    }

    final static List<Integer> keyboardDeselectKeys = new ArrayList<>();
    static {
        keyboardDeselectKeys.add(Input.Keys.A);
        keyboardDeselectKeys.add(Input.Keys.S);
        keyboardDeselectKeys.add(Input.Keys.D);
        keyboardDeselectKeys.add(Input.Keys.F);
    }

    // Onscreen positions
    final static float BOX_SIDE = 250;
    final static float X = 200;
    final static float Y = 325;
    final static List<PositionComponent> playerControllerPositions = new ArrayList<>();
    static {
        playerControllerPositions.add(new PositionComponent(-X -(BOX_SIDE / 2), Y - BOX_SIDE));
        playerControllerPositions.add(new PositionComponent(X - (BOX_SIDE / 2), Y - BOX_SIDE));
        playerControllerPositions.add(new PositionComponent(-X - (BOX_SIDE / 2), -Y));
        playerControllerPositions.add(new PositionComponent(X - (BOX_SIDE / 2), -Y));
    }

    public SetupScreen(LudumDareGame game) {
        if (game == null) {
            throw new Error("game cannot be null");
        }

        this.game = game;

        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/ui.atlas", AnimagicTextureAtlas.class);
        splitScreenSeparator = atlas.findRegion("ssBackground");
    }

    public List<GameObject> getGameObjects() {
        List<GameObject> returnValues = new ArrayList<>();
        returnValues.addAll(otherObjects);
        returnValues.addAll(inputObjects);
        return returnValues;
    }

    public List<Player> getResults() {
        // Copy inputs into players.
        for (int i = 0; i < inputObjects.size(); i++) {
            players.get(i).append(inputObjects.get(i).getInputComponent());
        }

        return players;
    }

    private void setupPlayers() {
        inputObjects = new ArrayList<>();
        players = new ArrayList<>();

        players.add(new Player());

        SizeComponent size = new SizeComponent(BOX_SIDE, BOX_SIDE);
        for (int i = 0; i < players.size(); i++) {
            ControllerScreenObject obj = new ControllerScreenObject(keyboardSelectKeys.get(i), keyboardDeselectKeys.get(i), i, playerControllerPositions.get(i), size);
            inputObjects.add(obj);
        }
    }

    private void setupText() {
        otherObjects = new ArrayList<>();

        otherObjects.add(new TextScreenObject(new PositionComponent(-380, 45), "Enter or Start or continue", Color.BLACK));
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.lookAt(0, 0, 0);
        batch = new AnimagicSpriteBatch(camera);
        batch.isShaderOn(false);

        setupPlayers();

        setupText();
    }

    @Override
    public void render(float v) {
        getGameObjects().forEach(obj -> obj.update(v));

        if (InputUtil.checkInputs(Input.Keys.ENTER, Xbox360Pad.START)) {

            // Set players globally with associated inputs.
            Players.initialize(getResults());
//            List<Player> players = new ArrayList<>();
//            Player playerInstance = new Player(0);
//            playerInstance.append(new AIControlComponent());
//            players.add(playerInstance);
//            Players.initialize(players);
            // Start race.

            SplashScreen.INTRO_MUSIC.stop();

            game.setScreen(new LoadingScreen(game));
        }

        camera.update();

        Gdx.gl.glClearColor(100f / 255f, 139f / 255f, 237f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(splitScreenSeparator, -Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        LightUtil.addBasicLight(batch);
        getGameObjects().forEach(obj -> obj.draw(batch));
        batch.end();
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
        camera = null;

        batch.dispose();
        batch = null;

        inputObjects.clear();
        inputObjects = null;

        otherObjects.clear();
        otherObjects = null;
    }
}
