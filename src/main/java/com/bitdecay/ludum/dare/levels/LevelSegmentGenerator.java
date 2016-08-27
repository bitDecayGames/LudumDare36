package com.bitdecay.ludum.dare.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.bitdecay.jump.level.FileUtils;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.LevelObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LevelSegmentGenerator {
    public static final boolean DEBUG = false;
    public static final String SEGMENT_DIR = "levelSegments/";

    int numSegmentToUse;

    public LevelSegmentGenerator(int numSegmentToUse) {
        this.numSegmentToUse = numSegmentToUse;
    }

    public List<Level> generateLevelSegments(){

        int segmentsAvailable;

        // Minus 1 is to account for the starting segment that we don't want chosen
        System.out.println("GENERATING LEVEL SEGEMENTS");
        FileHandle f = Gdx.files.internal(SEGMENT_DIR);
        if (!f.exists()) System.out.println("FILE DOES NOT EXIST OH SNAP: " + f);
        FileHandle[] list = f.list();
        segmentsAvailable = 12; // TODO: THIS IS TOTALLY HARD CODED, BAD!
        System.out.println("Segments available: " + segmentsAvailable);

        List<Level> generatedListOfSegments = new ArrayList<>();

        FileHandle child = f.child("segment_start");
        if (!child.exists()) System.out.println("CHILD FILE DOES NOT EXIST OH SNAP: " + child);

        generatedListOfSegments.add(FileUtils.loadFileAs(Level.class, child.readString()));
//        generatedListOfSegments.add(LevelUtilities.loadLevel(SEGMENT_DIR + "segment_start"));

        for (int i = 1; i < numSegmentToUse-1; i++){
            int levelIndex = (int) (Math.random() * segmentsAvailable + 1);
            if(DEBUG) {
                System.out.println("The segment chosen was " + levelIndex);
            }
            child = f.child("segment_" + levelIndex);
            if (!child.exists()) System.out.println("CHILD FILE DOES NOT EXIST OH SNAP: " + child);

            Level loadedLevel = FileUtils.loadFileAs(Level.class, child.readString());
            for (LevelObject obj : loadedLevel.otherObjects) {
                obj.uuid = UUID.randomUUID().toString();
            }
            generatedListOfSegments.add(loadedLevel);
        }

        child = f.child("segment_end");
        if (!child.exists()) System.out.println("CHILD FILE DOES NOT EXIST OH SNAP: " + child);
        generatedListOfSegments.add(FileUtils.loadFileAs(Level.class, child.readString()));

        return generatedListOfSegments;
    }
}
