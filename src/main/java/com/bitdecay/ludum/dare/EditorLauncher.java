package com.bitdecay.ludum.dare;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bitdecay.jump.leveleditor.render.LevelEditor;

public class EditorLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        config.title = "Jump";
        config.fullscreen = false;

        LevelEditor.setAssetsFolder("../jump/jump-leveleditor/assets");
        new LwjglApplication(new LD36EditorApp(), config);
    }
}