package com.bitdecay.ludum.dare.screens.cutScene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

/**
 * Created by jacob on 8/28/16.
 */
public class Scene4 extends CutSceneFrame{
    Animation cock;
    Animation bg;
    Animation alien;

    private float cockX = 500;
    private float cockY = 200;
    private float cockRotation = 135;
    private float cockXScale = 1;
    private float cockYScale = 1;

    private float alienX = 0;
    private float alienY = 0;
    private float alienRotation = 135;
    private float alienXScale = 1;
    private float alienYScale = 1;

    private boolean cockSwitch = false;


    private float sceneTime = 0;

    private boolean musicPlaying = false;

    public Scene4(){

        AnimagicTextureAtlas atlas = LudumDareGame.atlas;

        cock = new Animation("cock", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("ship/pieces/cockpit")});
        alien = new Animation("cock", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("player/cutscene/thrown")});

        bg = new Animation("bg", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("bg/scene4BG")});

        time = 3.5f;
    }

    @Override
    public void update(float delta) {
        sceneTime += delta;

        if(!cockSwitch){
            cockX -= 5;
            cockY -= 3;
            cockRotation += 3;
        } else {
            alienX -= 5;
            alienY += 1;
            alienRotation += 4;
        }

    }

    @Override
    public void getRenderedTextureRegion(OrthographicCamera camera, FrameBuffer buff) {
        batchy.setProjectionMatrix(camera.combined);
        batchy.begin();

        batchy.draw(bg.getFrame(), camera.position.x-camera.viewportWidth/2, camera.position.y-camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight);

        if(sceneTime < 1.6) {
            batchy.draw(cock.getFrame(), cockX, cockY, cock.getFrame().getRegionWidth() / 2, cock.getFrame().getRegionHeight() / 2, cock.getFrame().getRegionHeight(), cock.getFrame().getRegionWidth(), cockXScale, cockYScale, cockRotation, false);
        } else {
            if (!cockSwitch){
                cockSwitch = true;
                alienX = cockX;
                alienY = cockY;
            }
            batchy.draw(cock.getFrame(), cockX, cockY, cock.getFrame().getRegionWidth() / 2, cock.getFrame().getRegionHeight() / 2, cock.getFrame().getRegionHeight(), cock.getFrame().getRegionWidth(), cockXScale, cockYScale, cockRotation, false);
            batchy.draw(alien.getFrame(), alienX, alienY, alien.getFrame().getRegionWidth() / 2, alien.getFrame().getRegionHeight() / 2, alien.getFrame().getRegionHeight(), alien.getFrame().getRegionWidth(), alienXScale, alienYScale, alienRotation, false);
        }

        batchy.end();
    }
}
