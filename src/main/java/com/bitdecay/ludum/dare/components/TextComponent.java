package com.bitdecay.ludum.dare.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IDraw;

public class TextComponent implements IComponent, IDraw {

    BitmapFont currentFont;
    BitmapFont font1;
    BitmapFont font2;
    String text;

    PositionComponent position;

    public TextComponent(PositionComponent position) {
        this.position = position;

        font1 = new BitmapFont(Gdx.files.internal("fonts/bit.fnt"));
        font1.getData().setScale(5);
        font1.setColor(Color.BLUE);

        font2 = new BitmapFont(Gdx.files.internal("fonts/digiface.fnt"));
//        font2.getData().setScale(5);
        font2.setColor(Color.BLUE);

        currentFont = font1;
    }

    public TextComponent setText(String text) {
        this.text = text;

        return this;
    }

    public TextComponent setColor(Color color) {
        currentFont.setColor(color);

        return this;
    }

    public TextComponent setScale(float s) {
        currentFont.getData().setScale(s);
        return this;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        currentFont.draw(spriteBatch, text, position.x, position.y);
    }

    public void useFont2(boolean b) {
        currentFont = b ? font2 : font1;
    }
}
