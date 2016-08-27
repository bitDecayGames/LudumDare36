package com.bitdecay.ludum.dare;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bytebreakstudios.animagic.texture.AnimagicTexturePacker;

import java.io.File;

public class Launcher {

    public static void main(String[] arg) {
        //if (arg != null && arg.length > 3) {
            //AnimagicTexturePacker.pack(new File("sprites"), new File("packed"));
        System.out.println(new File(".").getAbsolutePath());
        AnimagicTexturePacker.pack(new File("src/main/resources/assets"), new File("src/main/resources/packed"));
        //}
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