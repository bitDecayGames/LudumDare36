package com.bitdecay.ludum.dare;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.bitdecay.ludum.dare.screens.MikeTestBulletScreen;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlasLoader;

public class LudumDareGame extends Game {
    public static boolean MUSIC_ON = true;
    final static int NUM_PLAYER_ASSETS = 5;

    public static AssetManager assetManager = new AssetManager();
    public static void queueAssetsForLoad() {
        assetManager.setLoader(AnimagicTextureAtlas.class, new AnimagicTextureAtlasLoader(new InternalFileHandleResolver()));
        LudumDareGame.assetManager.load("packed/main.atlas", AnimagicTextureAtlas.class);
        LudumDareGame.assetManager.load("packed/assets.atlas", AnimagicTextureAtlas.class);
        LudumDareGame.assetManager.load("packed/fonts.atlas", AnimagicTextureAtlas.class);
    }

    @Override
    public void create() {
        queueAssetsForLoad();
        assetManager.finishLoading();
        System.out.println("Assets: " + assetManager.getAssetNames());
        setScreen(new MikeTestBulletScreen(this));
        setScreen(new SplashScreen(this));
    }
}
