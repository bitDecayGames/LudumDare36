package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.screens.cutScene.*;
import com.bitdecay.ludum.dare.util.SoundLibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwingfield on 8/6/15.
 */
public class OpeningSceneCutScreen implements Screen {
//    private static final HTMLLogger log = HTMLLogger.getLogger(GameScreen.class, LogGroup.System);

    private LudumDareGame game;

    private OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    private int currentAnimationIndex = -1;
    private CutSceneFrame currentScene;
    private boolean currentlyInChange = false;

    private float stateTime = 0;
    private List<CutSceneFrame> animations = new ArrayList<>();
    private Music music;
    private Sprite sprite = new Sprite();

    private Stage stage = new Stage();

    public OpeningSceneCutScreen(LudumDareGame game){
        music = SoundLibrary.loopMusic("ambientIntro");

        this.game = game;

        Scene1 scene1 = new Scene1();
        Scene2 scene2 = new Scene2();
        Scene3 scene3 = new Scene3();
        animations.add(scene1);
        animations.add(scene2);
        animations.add(scene3);

        currentScene = animations.get(0);

        changeToNextScene();

        Image tmpImage = new Image(new SpriteDrawable(sprite));
        tmpImage.setFillParent(true);
        stage.addActor(tmpImage);
    }

    private void changeToNextScene() {
        System.out.println("Calling next scene. Current scene: " + currentAnimationIndex);
        currentAnimationIndex++;
        if (currentAnimationIndex < animations.size()) {
            System.out.println("Going to scene " + currentAnimationIndex);
            currentScene = animations.get(currentAnimationIndex);
            stateTime = 0;
            currentlyInChange = false;
            stage.addAction(Actions.fadeIn(1));
        } else {
            System.out.println("No more scenes. Going to game");
            stage.addAction(Actions.sequence(Actions.fadeOut(1),
                    Actions.run(( () -> {
                        SoundLibrary.stopMusic("AlarmExtended");
                        SoundLibrary.stopMusic("ambientIntro");
                        game.setScreen(new GameScreen(game));
                    })))
            );
        }
    }

    @Override
    public void show() {
        SoundLibrary.loopMusic("ambientIntro");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;

        if (currentScene != null) {
            currentScene.update(delta);
            sprite.setRegion(currentScene.getRenderedTextureRegion(camera));
            sprite.flip(false, true);

            if(stateTime >= currentScene.time && !currentlyInChange) {
                currentlyInChange = true;
                stage.addAction(Actions.sequence(Actions.fadeOut(1),
                        Actions.run(() -> changeToNextScene()))
                  );
            }
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
