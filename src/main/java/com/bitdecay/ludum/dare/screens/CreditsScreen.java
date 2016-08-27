package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;


/**
 * Created by mwingfield on 8/6/15.
 */
public class CreditsScreen implements Screen {

    private static String SPACE_AFTER_TITLE = "\n\n\n";
    private static String SPACE_AFTER_NAME = "\n\n";

    private LudumDareGame game;
    private Stage stage = new Stage();

    private Image background;
    private Label lblTitle;
    private Label lblBack;
    private Label lblCredits;


    private int menuSelection;
    private boolean downIsPressed;
    private boolean upIsPressed;
    private boolean escWasPressed;


    boolean active = true;

    public CreditsScreen(final LudumDareGame game) {
        this.game = game;

        menuSelection = 0;
        downIsPressed = false;
        upIsPressed = false;
        escWasPressed = false;

        Skin skin = new Skin(ResourceDir.internal("skins/skin.json"));

        background = new Image(LudumDareGame.atlas.findRegion("buzzkill/dankest_boat"));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        lblTitle = new Label("Credits", skin);
        lblTitle.setFontScale(10);
        lblTitle.setFillParent(true);
        lblTitle.setAlignment(Align.top, Align.left);
        lblTitle.setColor(Color.WHITE);

        lblBack = new Label("Press esc/B to go back", skin);
        lblBack.setFontScale(5);
        lblBack.setFillParent(true);
        lblBack.setAlignment(Align.bottom, Align.right);
        lblBack.setColor(Color.WHITE);

        lblCredits = new Label("Oculomancers (Art): " + SPACE_AFTER_TITLE +
                "Erik \"Groovemaster\"\n Meredith" + SPACE_AFTER_NAME +
                "Logan \"New York is too\nfar away\" Moore" + SPACE_AFTER_TITLE +
                "Bitwizards (Dev):" + SPACE_AFTER_TITLE +
                "Mike \"Font Destroyer\"\n Wingfield" + SPACE_AFTER_NAME +
                "Jake \"Donuts & Whiskey\"\n Kabob-Thompski" + SPACE_AFTER_NAME +
                "Jake \"Luke\" Fisher" + SPACE_AFTER_NAME +
                "Luke \"Jake\" Fisher" + SPACE_AFTER_NAME +
                "Tristan \"El Hombre Nuevo\"\n Havelick" + SPACE_AFTER_TITLE +
                "Soundscapes:" + SPACE_AFTER_TITLE,
                skin);
        lblCredits.setFontScale(6);
        lblCredits.setFillParent(true);
        lblCredits.setAlignment(Align.center);
        lblCredits.setColor(Color.DARK_GRAY);


        stage.addActor(background);
        stage.addActor(lblTitle);
        stage.addActor(lblBack);
        stage.addActor(lblCredits);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
//         animate the main menu when entering
        lblCredits.addAction(Actions.sequence(
                Actions.moveBy(0, -(Gdx.graphics.getHeight()*1.5f)),
                Actions.moveBy(0, Gdx.graphics.getHeight() * 4, 30),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MainMenuScreen(game));
                    }
                })

        ));
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (!active) {
            stage.addAction(Actions.sequence(
                    Actions.fadeOut(1),
                    Actions.delay(.5f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            game.setScreen(new SetupScreen(game));
                        }
                    })
            ));
        }
    }


    public void update(float delta){

        if (InputUtil.isPressed(Input.Keys.ESCAPE, Xbox360Pad.B) || InputUtil.isPressed(Input.Keys.B, Xbox360Pad.B)) {
            escWasPressed = true;
        } else if (escWasPressed && !(InputUtil.isPressed(Input.Keys.ESCAPE, Xbox360Pad.B))){
            game.setScreen(new MainMenuScreen(game));
        }
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
