package com.bitdecay.ludum.dare.screens.cutScene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitdecay.ludum.dare.LudumDareGame;
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

    private boolean musicPlaying = false;

    public Scene3(){
        AnimagicTextureAtlas atlas = LudumDareGame.atlas;

        ship = new Animation("badShip", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(.1f), atlas.findRegions("ship/badShip").toArray(AnimagicTextureRegion.class));
        urt = new Animation("urt", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("asteroids/omicron-persei6")});
        urtXsize = urt.getFrame().getRegionWidth();
        urtYsize = urt.getFrame().getRegionHeight();

        space = new Animation("bg", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("bg/space")});


        time = 10;
    }

    @Override
    public void update(float delta) {
        if (!musicPlaying) {
            musicPlaying = true;
            SoundLibrary.loopMusic("AlarmExtended");
        }

        ship.update(delta);
        shipRotation ++;
        shipXScale -= .001;
        shipYScale -= .001;
        shipX += .6;
        shipY -= .1;

        urtXsize *= 1.0005;
        urtYsize *= 1.0005;
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

        batchy.end();

        buff.end();
        return new TextureRegion(buff.getColorBufferTexture());
    }
}
