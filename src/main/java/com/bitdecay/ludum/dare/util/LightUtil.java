package com.bitdecay.ludum.dare.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;

public class LightUtil {

    public static void addLocatedLight(AnimagicSpriteBatch batch, Vector2 location) {
        addCustomLight(batch, location, 5f, .8f, Color.WHITE);
    }

    public static void addCustomLight(AnimagicSpriteBatch batch, Vector2 location, float z, float attenuation, Color color) {
        Vector3 lightPos = new Vector3(location, .1f);
        batch.setNextLight(lightPos.x, lightPos.y, z, attenuation, color);
    }

    public static void addBasicLight(AnimagicSpriteBatch batch) {
        addLocatedLight(batch, new Vector2(0, 0));
    }
}
