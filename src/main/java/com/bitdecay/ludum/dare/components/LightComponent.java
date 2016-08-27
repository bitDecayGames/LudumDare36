package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IPreDraw;
import com.bitdecay.ludum.dare.util.LightUtil;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;

public class LightComponent implements IComponent, IDraw, IPreDraw {

    private final PositionComponent pos;
    private Vector2 offset;

    private float attenuation = .9f;
    private float zAxis = .1f;

    public LightComponent(PositionComponent pos, Vector2 offset) {
        this.pos = pos;
        this.offset = offset;
    }

    public void setAttenuation(float att) {
        attenuation = att;
    }

    public void setzAxis(float z) {
        zAxis = z;
    }

    @Override
    public void draw(AnimagicSpriteBatch spriteBatch) {
    }

    @Override
    public void preDraw(AnimagicSpriteBatch spriteBatch) {
        LightUtil.addCustomLight(spriteBatch, new Vector2(pos.x + offset.x, pos.y + offset.y), zAxis, attenuation, Color.WHITE);
    }
}
