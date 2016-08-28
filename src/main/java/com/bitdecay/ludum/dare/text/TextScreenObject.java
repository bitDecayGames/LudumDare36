package com.bitdecay.ludum.dare.text;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.components.TextComponent;

public class TextScreenObject extends GameObject {

    TextComponent textComp;

    public TextScreenObject(PositionComponent positionComponent,  String text, Color color) {
        textComp = (new TextComponent(positionComponent)).setText(text).setColor(color).setScale(1.5f);
        append(textComp);
    }

    public void setText(String s) {
        textComp.setText(s);
    }
}
