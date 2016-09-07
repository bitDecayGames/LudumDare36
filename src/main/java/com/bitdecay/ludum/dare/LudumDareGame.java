package com.bitdecay.ludum.dare;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.ludum.dare.screens.GameScreen;
import com.bitdecay.ludum.dare.screens.OpeningSceneCutScreen;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlasLoader;

public class LudumDareGame extends Game {
    public static final BitPoint res = new BitPoint(800, 450);
    public static AssetManager assetManager = new AssetManager();
    public static AnimagicTextureAtlas atlas;
    public boolean debug = false;

    public LudumDareGame(boolean debug){
        super();
        this.debug = debug;
    }

    public LudumDareGame(){
        super();
    }

    public static void queueAssetsForLoad() {
        assetManager.setLoader(AnimagicTextureAtlas.class, new AnimagicTextureAtlasLoader(new InternalFileHandleResolver()));
        LudumDareGame.assetManager.load(ResourceDir.path("packed/main.atlas"), AnimagicTextureAtlas.class);
    }

    @Override
    public void create() {
        queueAssetsForLoad();
        assetManager.finishLoading();
        atlas = assetManager.get(ResourceDir.path("packed/main.atlas"), AnimagicTextureAtlas.class);

        if (debug) setScreen(new GameScreen(this));
        else setScreen(new OpeningSceneCutScreen(this));
    }
}
