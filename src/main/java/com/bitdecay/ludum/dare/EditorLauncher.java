package com.bitdecay.ludum.dare;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bitdecay.jump.leveleditor.render.LevelEditor;

public class EditorLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 900;
        config.height = 600;
        config.title = "Jump";

        LevelEditor.setAssetsFolder("../jump/jump-leveleditor/assets");
        new LwjglApplication(new LD36EditorApp(), config);
    }
}