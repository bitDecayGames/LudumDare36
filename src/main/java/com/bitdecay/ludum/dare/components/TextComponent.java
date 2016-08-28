package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;

public class TextComponent implements IComponent, IDraw {

    BitmapFont font;
    String text;

    PositionComponent position;

    public TextComponent(PositionComponent position) {
        this.position = position;

        font = new BitmapFont(Gdx.files.internal("fonts/bit.fnt"));
        font.getData().setScale(5);
        font.setColor(Color.BLUE);

    }

    public TextComponent setText(String text) {
        this.text = text;

        return this;
    }

    public TextComponent setColor(Color color) {
        font.setColor(color);

        return this;
    }

    public TextComponent setScale(float s) {
        font.getData().setScale(s);
        return this;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        font.draw(spriteBatch, text, position.x, position.y);
    }
}
