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
public class Scene1 extends CutSceneFrame{
    private Animation ship;
    private float shipX = -400;
    private float shipY = 0;

    private Animation roid1;
    private float roid1X = 40;
    private float roid1Y = 200;

    private Animation roid2;
    private float roid2X = -300;
    private float roid2Y = 200;

    private Animation roid3;
    private float roid3X = -300;
    private float roid3Y = -300;

    private Animation roid4;
    private float roid4X = 300;
    private float roid4Y = 200;

    private Animation roid5;
    private float roid5X = 300;
    private float roid5Y = -200;

    private Animation roid6;
    private float roid6X = 0;
    private float roid6Y = -200;

    private Animation roid7;
    private float roid7X = 200;
    private float roid7Y = -250;

    private Animation roid8;
    private float roid8X = 200;
    private float roid8Y = 0;

    private Animation urt;
    private float urtX = 400;
    private float urtY = -600;

    private Animation space;


    public Scene1(){
        AnimagicTextureAtlas atlas = LudumDareGame.atlas;

        ship = new Animation("badShip", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(.1f), atlas.findRegions("ship/badShip").toArray(AnimagicTextureRegion.class));
        roid1 = new Animation("roid1", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/asteroid1")});
        roid2 = new Animation("roid2", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/asteroid2")});
        roid3 = new Animation("roid3", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/asteroid3")});
        roid4 = new Animation("roid4", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/asteroid2")});
        roid5 = new Animation("roid5", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/asteroid3")});
        roid6 = new Animation("roid6", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/asteroid1")});
        roid7 = new Animation("roid7", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/asteroid2")});
        roid8 = new Animation("roid8", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/asteroid3")});
        urt = new Animation("urt", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/omicron-persei6")});
        space = new Animation("bg", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("bg/space")});

        time = 5;
    }

    @Override
    public void update(float delta) {
        ship.update(delta);

        shipX += .1;
        shipY -= .05;
        roid1X -= .1;
        roid1Y -= .1;
        roid2X -= .2;
        roid2Y -= .1;
        roid3X -= .2;
        roid3Y += .2;
        roid4X -= .2;
        roid4Y -= .2;
        roid5X -= .1;
        roid5Y += .3;
        roid6X -= .5;
        roid6Y -= .1;
        roid7X -= .5;
        roid7Y += .1;
        roid8X -= .3;
        roid8Y -= .2;
        urtX -= .3;
        urtY += .3;

    }

    @Override
    public void getRenderedTextureRegion(OrthographicCamera camera, FrameBuffer buff) {
        batchy.setProjectionMatrix(camera.combined);
        batchy.begin();

        batchy.draw(space.getFrame(), camera.position.x-camera.viewportWidth/2, camera.position.y-camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight);
        batchy.draw(urt.getFrame(), urtX, urtY, 350, 350);
        batchy.draw(ship.getFrame(), shipX, shipY);
        batchy.draw(roid1.getFrame(), roid1X, roid1Y);
        batchy.draw(roid2.getFrame(), roid2X, roid2Y);
        batchy.draw(roid3.getFrame(), roid3X, roid3Y);
        batchy.draw(roid4.getFrame(), roid4X, roid4Y);
        batchy.draw(roid5.getFrame(), roid5X, roid5Y);
        batchy.draw(roid6.getFrame(), roid6X, roid6Y);
        batchy.draw(roid7.getFrame(), roid7X, roid7Y);
        batchy.draw(roid8.getFrame(), roid8X, roid8Y);

        batchy.end();
    }
}
