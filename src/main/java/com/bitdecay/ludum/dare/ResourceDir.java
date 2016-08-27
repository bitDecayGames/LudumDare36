package com.bitdecay.ludum.dare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

public final class ResourceDir {
    public static final String DIR = "src/main/resources/";
    public static File get(String path){
        return new File(ResourceDir.path(path));
    }

    public static String path(String path){
        return DIR + path;
    }

    public static FileHandle internal(String path){
        return Gdx.files.internal(ResourceDir.path(path));
    }
}
