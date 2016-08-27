package com.bitdecay.ludum.dare;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bytebreakstudios.animagic.texture.AnimagicTexturePacker;

public class Launcher {

    public static void main(String[] arg) {
        AnimagicTexturePacker.pack(ResourceDir.get("assets"), ResourceDir.get("packed"));

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.width = 900;
        config.height = 600;

        if (arg != null && arg.length > 0) {
            if (arg[0].equalsIgnoreCase("medium")) {
                config.width = 1200;
                config.height = 675;
            } else if (arg[0].equalsIgnoreCase("small")) {
                config.width = 800;
                config.height = 450;
            }
        }

        new LwjglApplication(new LudumDareGame(), config);
    }
}