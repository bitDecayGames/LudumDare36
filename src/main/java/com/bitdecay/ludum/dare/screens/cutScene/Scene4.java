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
public class Scene4 extends CutSceneFrame{
    Animation cock;
    Animation bg;

    private float cockX = 500;
    private float cockY = 200;
    private float cockRotation = 135;
    private float cockXScale = 1;
    private float cockYScale = 1;

    private boolean musicPlaying = false;

    public Scene4(){
        AnimagicTextureAtlas atlas = LudumDareGame.atlas;

        cock = new Animation("cock", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("ship/pieces/cockpit")});

        bg = new Animation("bg", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(.1f), new AnimagicTextureRegion[]{atlas.findRegion("bg/scene4BG")});

        time = 10;
    }

    @Override
    public void update(float delta) {
        if (!musicPlaying) {
            musicPlaying = true;
            SoundLibrary.loopMusic("AlarmExtended");
        }

        cockX -= 2;
        cockY -= .5;

    }

    @Override
    public TextureRegion getRenderedTextureRegion(OrthographicCamera camera) {
        FrameBuffer buff = new FrameBuffer(Pixmap.Format.RGB888, (int) camera.viewportWidth, (int) camera.viewportHeight, false);
        buff.begin();

        batchy.setProjectionMatrix(camera.combined);
        batchy.begin();

        batchy.draw(bg.getFrame(), camera.position.x-camera.viewportWidth/2, camera.position.y-camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight);
        batchy.draw(cock.getFrame(), cockX, cockY, cock.getFrame().getRegionWidth()/2, cock.getFrame().getRegionHeight()/2, cock.getFrame().getRegionHeight(), cock.getFrame().getRegionWidth(), cockXScale, cockYScale, cockRotation, false);

        batchy.end();

        buff.end();
        return new TextureRegion(buff.getColorBufferTexture());
    }
}
