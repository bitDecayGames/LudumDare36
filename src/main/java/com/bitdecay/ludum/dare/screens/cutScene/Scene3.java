package com.bitdecay.ludum.dare.screens.cutScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

/**
 * Created by jacob on 8/28/16.
 */
public class Scene3 extends CutSceneFrame{
    Animation ship;
    Animation cock;
    Animation engine;
    Animation navMod;
    Animation wing;
    Animation urt;
    Animation space;

    private float urtX = -350;
    private float urtY = -400;
    private float urtXsize;
    private float urtYsize;

    private float shipX = -300;
    private float shipY = 0;
    private float shipRotation = 0;
    private float shipXScale = 1;
    private float shipYScale = 1;

    private float cockX = -300;
    private float cockY = 0;
    private float cockRotation = 0;
    private float cockXScale = 1;
    private float cockYScale = 1;

    private float engineX = -300;
    private float engineY = 0;
    private float engineRotation = 0;
    private float engineXScale = 1;
    private float engineYScale = 1;

    private float navModX = -300;
    private float navModY = 0;
    private float navModRotation = 0;
    private float navModXScale = 1;
    private float navModYScale = 1;

    private float wingX = -300;
    private float wingY = 0;
    private float wingRotation = 0;
    private float wingXScale = 1;
    private float wingYScale = 1;

    private boolean musicPlaying = false;

    public Scene3(){
        AnimagicTextureAtlas atlas = LudumDareGame.atlas;

        ship = new Animation("deadShip", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("ship/pieces/deadShip")});
        cock = new Animation("cock", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("ship/pieces/cockpit")});
        engine = new Animation("engine", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("ship/pieces/engine")});
        navMod = new Animation("navMod", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("ship/pieces/navModule")});
        wing = new Animation("wing", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("ship/pieces/wings")});
        urt = new Animation("urt", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/omicron-persei6")});
        urtXsize = urt.getFrame().getRegionWidth();
        urtYsize = urt.getFrame().getRegionHeight();

        space = new Animation("bg", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("bg/space")});

        time = 5;
    }

    @Override
    public void update(float delta) {
        if (!musicPlaying) {
            musicPlaying = true;
            SoundLibrary.loopMusic("AlarmExtended");
        }

        shipRotation ++;
        shipXScale -= .002;
        shipYScale -= .002;
        shipX += .6;
        shipY -= .1;


        cockRotation ++;
        cockXScale -= .002;
        cockYScale -= .002;
        cockX += .6;
        cockY -= .2;

        engineRotation ++;
        engineXScale -= .002;
        engineYScale -= .002;
        engineX += .5;
        engineY += .1;

        navModRotation ++;
        navModXScale -= .002;
        navModYScale -= .002;
        navModX += .7;
        navModY += .1;

        wingRotation ++;
        wingXScale -= .002;
        wingYScale -= .002;
        wingX += .7;
        wingY -= .2;

        urtXsize *= 1.0008;
        urtYsize *= 1.0008;
        urtX -= .1;
        urtY -= .1;


    }

    @Override
    public TextureRegion getRenderedTextureRegion(OrthographicCamera camera) {
        FrameBuffer buff = new FrameBuffer(Pixmap.Format.RGB888, (int) camera.viewportWidth, (int) camera.viewportHeight, false);
        buff.begin();

        batchy.setProjectionMatrix(camera.combined);
        batchy.begin();

        batchy.draw(space.getFrame(), camera.position.x-camera.viewportWidth/2, camera.position.y-camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight);
        batchy.draw(urt.getFrame(), urtX, urtY, urtXsize, urtYsize);
        batchy.draw(ship.getFrame(), shipX, shipY, ship.getFrame().getRegionWidth()/2, ship.getFrame().getRegionHeight()/2, ship.getFrame().getRegionHeight(), ship.getFrame().getRegionWidth(), shipXScale, shipYScale, shipRotation, true);
        batchy.draw(cock.getFrame(), cockX, cockY, cock.getFrame().getRegionWidth()/2, cock.getFrame().getRegionHeight()/2, cock.getFrame().getRegionHeight(), cock.getFrame().getRegionWidth(), cockXScale, cockYScale, cockRotation, false);
        batchy.draw(engine.getFrame(), engineX, engineY, engine.getFrame().getRegionWidth()/2, engine.getFrame().getRegionHeight()/2, engine.getFrame().getRegionHeight(), engine.getFrame().getRegionWidth(), engineXScale, engineYScale, engineRotation, false);
        batchy.draw(navMod.getFrame(), navModX, navModY, navMod.getFrame().getRegionWidth()/2, navMod.getFrame().getRegionHeight()/2, navMod.getFrame().getRegionHeight(), navMod.getFrame().getRegionWidth(), navModXScale, navModYScale, navModRotation, false);
        batchy.draw(wing.getFrame(), wingX, wingY, wing.getFrame().getRegionWidth()/2, wing.getFrame().getRegionHeight()/2, wing.getFrame().getRegionHeight(), wing.getFrame().getRegionWidth(), wingXScale, wingYScale, wingRotation, false);

        batchy.end();

        buff.end();
        return new TextureRegion(buff.getColorBufferTexture());
    }
}
