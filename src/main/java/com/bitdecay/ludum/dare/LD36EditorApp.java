package com.bitdecay.ludum.dare;

/**
 * Created by tristan on 8/27/16.
 */
import com.badlogic.gdx.Game;
import com.bitdecay.jump.leveleditor.example.ExampleEditorLevel;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.ludum.dare.screens.GameScreen;

public class LD36EditorApp extends Game {

    @Override
    public void create() {
        // Change to our game screen
        LudumDareGame game = new LudumDareGame();
        game.create();
        GameScreen screen = new GameScreen(game);
        setScreen(new LevelEditor(screen));
    }

}