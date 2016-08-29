package com.bitdecay.ludum.dare.screens.cutScene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Michael on 8/22/2015.
 */
public class Sprite extends com.badlogic.gdx.graphics.g2d.Sprite {
    public TextureRegion normalTexture;

    public Sprite(){
        super();
    }

    public Sprite(TextureRegion reg){
        super(reg);
    }

    public void setNormalRegion(TextureRegion normalTexture){
        this.normalTexture = normalTexture;
    }

    @Override
    public void draw(Batch batch){
        if (normalTexture != null) {
            batch.flush();
            normalTexture.getTexture().bind(1);
        }
        getTexture().bind(0);
        super.draw(batch);
    }
}
