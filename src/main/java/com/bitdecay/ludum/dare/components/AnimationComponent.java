package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bitdecay.ludum.dare.interfaces.IUpdate;
import com.bytebreakstudios.animagic.animation.Animator;

public class AnimationComponent implements IComponent, IUpdate, IDraw {

    public final Animator animator;
    private final PositionComponent position;
    private final float scale;
    public Vector2 offset;
    private boolean flipVerticalAxis = false;

    public AnimationComponent(String name, PositionComponent position, float scale, Vector2 offset) {
        this.position = position;
        animator = new Animator(name);
        this.scale = scale;
        if (offset != null) this.offset = offset;
        else this.offset = new Vector2(0, 0);
    }

    @Override
    public void update(float delta) {
        animator.update(delta);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        TextureRegion reg = animator.getFrame();
        spriteBatch.draw(reg, position.x + offset.x + (flipVerticalAxis ? reg.getRegionWidth() * scale : 0), position.y + offset.y, reg.getRegionWidth() * scale * (flipVerticalAxis ? -1 : 1), reg.getRegionHeight() * scale);
    }

    public void setFlipVerticalAxis(boolean value) {
        flipVerticalAxis = value;
    }

    public boolean getFlipVerticalAxis() {
        return flipVerticalAxis;
    }
}