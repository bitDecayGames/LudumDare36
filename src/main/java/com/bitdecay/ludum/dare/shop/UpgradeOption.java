package com.bitdecay.ludum.dare.shop;

import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

/**
 * Represents a single choice a player has for an upgrade
 * Created by Admin on 12/12/2015.
 */
public class UpgradeOption {
    public Class<? extends IComponent> clazz;
    public Animation animation;
    public String description; // Not sure if we need this, butt fucket.
    public int cost;

    public UpgradeOption(Class<? extends IComponent> clazz, String textureName, int i) {
        this.clazz = clazz;
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/upgrades.atlas", AnimagicTextureAtlas.class);
        this.animation = new Animation("item", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(.25f), new AnimagicTextureRegion[] {atlas.findRegion(textureName)});
        cost = i;
    }

    public void update(float delta) {
        animation.update(delta);
        //maybe some text effects or some sheeeeeit.
    }
}
