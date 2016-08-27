package com.bitdecay.ludum.dare;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bitdecay.ludum.dare.screens.SplashScreen;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlasLoader;

public class LudumDareGame extends Game {
    public static boolean MUSIC_ON = true;
    final static int NUM_PLAYER_ASSETS = 5;

    public static AssetManager assetManager = new AssetManager();
    public static void queueAssetsForLoad() {
        assetManager.setLoader(AnimagicTextureAtlas.class, new AnimagicTextureAtlasLoader(new InternalFileHandleResolver()));
        LudumDareGame.assetManager.load("packed/tiles.atlas", AnimagicTextureAtlas.class);
        LudumDareGame.assetManager.load("packed/level.atlas", AnimagicTextureAtlas.class);
        LudumDareGame.assetManager.load("packed/test.atlas", AnimagicTextureAtlas.class);
        LudumDareGame.assetManager.load("packed/power.atlas", AnimagicTextureAtlas.class);
        LudumDareGame.assetManager.load("packed/ui.atlas", AnimagicTextureAtlas.class);
        for (int i = 0; i < NUM_PLAYER_ASSETS; i++) {
            LudumDareGame.assetManager.load("packed/player" + i + ".atlas", AnimagicTextureAtlas.class);
        }
        LudumDareGame.assetManager.load("packed/upgrades.atlas", AnimagicTextureAtlas.class);
        LudumDareGame.assetManager.load("skins/ui.atlas", TextureAtlas.class);
    }

    @Override
    public void create() {
        queueAssetsForLoad();
        assetManager.finishLoading();
        setScreen(new SplashScreen(this));
    }
}
