package com.bitdecay.ludum.dare;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bytebreakstudios.animagic.texture.AnimagicTexturePacker;

public class Launcher {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.width = 1600;
        config.height = 900;

        if (args != null && args.length > 0) {
            if (arg(args, "medium")) {
                config.width = 1200;
                config.height = 675;
            } else if (arg(args, "small")) {
                config.width = 800;
                config.height = 450;
            }

            if (arg(args, "dev")) ResourceDir.setRunMode(ResourceDir.RunMode.DEV);

        }
        System.out.println("Run Mode: " + ResourceDir.getRunMode());
        if (ResourceDir.getRunMode() == ResourceDir.RunMode.DEV) AnimagicTexturePacker.pack(ResourceDir.get("assets"), ResourceDir.get("packed"));

        new LwjglApplication(new LudumDareGame(), config);
    }

    private static boolean arg(String[] args, String arg){
        for (String cmd : args){
            if (cmd.equalsIgnoreCase(arg)) return true;
        }
        return false;
    }
}
