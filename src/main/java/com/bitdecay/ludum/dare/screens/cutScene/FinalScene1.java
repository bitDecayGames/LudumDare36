package com.bitdecay.ludum.dare.screens.cutScene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

/**
 * Created by jacob on 8/28/16.
 */
public class FinalScene1 extends CutSceneFrame{
    private Animation ship;
    private float shipX = -400;
    private float shipY = 0;

    private Animation shield;
    private float shieldX = -325;
    private float shieldY = -50;

    Animation bg;

    public FinalScene1(){
        AnimagicTextureAtlas atlas = LudumDareGame.atlas;

        ship = new Animation("best", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(.1f), atlas.findRegions("ship/bestShip").toArray(AnimagicTextureRegion.class));
        shield = new Animation("shield", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new TextureRegion[] {atlas.findRegion("ship/shield")});
        bg = new Animation("bg", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("bg/scene4BG")});

        time = 5;
    }

    @Override
    public void update(float delta) {
        ship.update(delta);

        shipY += 30 * delta;

        shieldY += 30 * delta;
    }

    @Override
    public void getRenderedTextureRegion(OrthographicCamera camera, FrameBuffer buff) {
        batchy.setProjectionMatrix(camera.combined);
        batchy.begin();
        batchy.draw(bg.getFrame(), camera.position.x-camera.viewportWidth/2, camera.position.y-camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight);

        batchy.draw(ship.getFrame(), shipX, shipY);
        batchy.draw(shield.getFrame(), shieldX, shieldY);
        batchy.end();
    }
}
