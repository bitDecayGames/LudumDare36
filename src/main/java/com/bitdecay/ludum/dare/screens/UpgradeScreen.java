package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.upgradeComponents.*;
import com.bitdecay.ludum.dare.interfaces.IProjectile;
import com.bitdecay.ludum.dare.shop.UpgradeGroup;
import com.bitdecay.ludum.dare.shop.UpgradeOption;
import com.bitdecay.ludum.dare.util.Players;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;

import java.util.ArrayList;
import java.util.List;

public class UpgradeScreen implements Screen {

    Music music;

    private static ArrayList<UpgradeOption> MASTER_LIST = new ArrayList<>();

    OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    SpriteBatch batch;

    List<UpgradeGroup> groups = new ArrayList<>();

    private int spacePerGroup;
    private LudumDareGame game;

    SpriteBatch ui;
    TextureRegion splitScreenSeparator;

    public UpgradeScreen(LudumDareGame game) {
        this.game = game;
        if (MASTER_LIST.size() == 0) {
            MASTER_LIST.add(new UpgradeOption(DoubleJumpComponent.class, "doubleJump", 20));
            MASTER_LIST.add(new UpgradeOption(FloatUpgradeComponent.class, "float", 15));
            MASTER_LIST.add(new UpgradeOption(MetalComponent.class, "metal", 10));
//            MASTER_LIST.add(new UpgradeOption(MysteryBagComponent.class, "mystery", 10));
            MASTER_LIST.add(new UpgradeOption(SpeedComponent.class, "speed", 20));
            MASTER_LIST.add(new UpgradeOption(WallJumpComponent.class, "wallJump", 15));
            MASTER_LIST.add(new UpgradeOption(FireProjectileComponent.class, "fire", 20));
            MASTER_LIST.add(new UpgradeOption(PoisonProjectileComponent.class, "poison", 25));
            MASTER_LIST.add(new UpgradeOption(IceProjectileComponent.class, "ice", 27));
            MASTER_LIST.add(new UpgradeOption(WebProjectileComponent.class, "web", 30));
            MASTER_LIST.add(new UpgradeOption(EmptyUpgradeComponent.class, "poop", 5));
        }
    }

    @Override
    public void show() {
        if(LudumDareGame.MUSIC_ON) {
            music = SoundLibrary.loopMusic("a_journey_awaits");
        }

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        batch = new SpriteBatch();

        spacePerGroup = Gdx.graphics.getHeight() / Players.list().size();
        for (Player player : Players.list()) {
            UpgradeGroup upgradeGroup = buildPlayerOptions(player);
            groups.add(upgradeGroup);
        }

        ui = new SpriteBatch();

        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/ui.atlas", AnimagicTextureAtlas.class);
        splitScreenSeparator = atlas.findRegion("ssBackground");

    }

    private UpgradeGroup buildPlayerOptions(Player player) {
        UpgradeGroup group = new UpgradeGroup();
        group.initialize(player);
        int tries = 10;
        int added = 0;
outer:  while (tries > 0) {
            tries--;
            UpgradeOption option = MASTER_LIST.get((int) (Math.random() * MASTER_LIST.size()));
            for(UpgradeOption selectedOption : group.getChoices()) {
                if (option == selectedOption) {
                    continue outer;
                }
            }

            Class checkClass = option.clazz;

            if(option.clazz.isAssignableFrom(IProjectile.class)) {
                checkClass = IProjectile.class;
            }
            if (!player.hasComponent(checkClass)) {
                group.addChoice(option);
                added++;
            }

            if (added >= 3) {
                break;
            }
        }
        return group;
    }

    @Override
    public void render(float delta) {
        boolean allReady = true;
        for (UpgradeGroup group : groups) {
            group.update(delta);
            if (!group.isReady()) {
                allReady = false;
            }
        }
        if (allReady) {
            if (music.isPlaying()) {
                music.stop();
            }
            game.setScreen(new LoadingScreen(game));
        }
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ui.begin();
        ui.draw(splitScreenSeparator, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ui.end();

        int screenWidth = Gdx.graphics.getWidth() / 2;
        int screenHeight = Gdx.graphics.getHeight() / 2;
        Gdx.gl.glViewport(screenWidth, 0, screenWidth, screenHeight);
        draw(groups.get(3));
        Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
        draw(groups.get(2));
        Gdx.gl.glViewport(screenWidth, screenHeight, screenWidth, screenHeight);
        draw(groups.get(1));
        Gdx.gl.glViewport(0, screenHeight, screenWidth, screenHeight);
        draw(groups.get(0));

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void draw(UpgradeGroup group) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        group.render(batch, 600, 200);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
