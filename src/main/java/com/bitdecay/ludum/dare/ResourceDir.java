package com.bitdecay.ludum.dare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

public final class ResourceDir {
    public static final String DEV_DIR = "src/main/resources/";
    public static final String PROD_DIR = "src/main/resources/";

    private static RunMode RUN_MODE = RunMode.PROD;

    public static void setRunMode(RunMode runMode){
        ResourceDir.RUN_MODE = runMode;
    }

    public static RunMode getRunMode(){
        return RUN_MODE;
    }

    public static File get(String path){
        return new File(ResourceDir.path(path));
    }

    public static String path(String path){
        return (RUN_MODE == RunMode.DEV ? DEV_DIR : PROD_DIR) + path;
    }

    public static FileHandle internal(String path){
        return Gdx.files.internal(ResourceDir.path(path));
    }

    public enum RunMode{
        DEV,
        PROD
    }
}