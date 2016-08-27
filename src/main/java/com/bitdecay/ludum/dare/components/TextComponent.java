package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;

public class TextComponent implements IComponent, IDraw {

    BitmapFont font;
    String text;

    PositionComponent position;

    public TextComponent(PositionComponent position) {
        this.position = position;

        font = new BitmapFont();
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

    @Override
    public void draw(AnimagicSpriteBatch spriteBatch) {
        font.draw(spriteBatch, text, position.x, position.y);
    }
}
