package com.bitdecay.ludum.dare.screens.cutScene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitdecay.ludum.dare.interfaces.IUpdate;

/**
 * Created by jacob on 8/28/16.
 */
public abstract class CutSceneFrame implements IUpdate{
    public float time;
    public SpriteBatch batchy = new SpriteBatch();

    public abstract void getRenderedTextureRegion(OrthographicCamera camera, FrameBuffer buff);
}
