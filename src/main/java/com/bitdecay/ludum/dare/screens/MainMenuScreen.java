package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;
import com.bitdecay.ludum.dare.util.SoundLibrary;


/**
 * Created by mwingfield on 8/6/15.
 */
public class MainMenuScreen implements Screen {

    private LudumDareGame game;
    private Stage stage = new Stage();

    private Table menu;
    private Image background;
    private Label title;
    private Label startLbl;
    private Label creditsLbl;
    private Label quitLbl;

    private int menuSelection;
    private boolean downIsPressed;
    private boolean upIsPressed;
    private boolean enterWasPressed;

    boolean active = true;

    public MainMenuScreen(final LudumDareGame game) {
        this.game = game;

        menuSelection = 0;
        downIsPressed = false;
        upIsPressed = false;
        enterWasPressed = false;

        TextureAtlas atlas = LudumDareGame.assetManager.get("skins/ui.atlas", TextureAtlas.class);
        Skin skin = new Skin(Gdx.files.internal("skins/menu-skin.json"), atlas);

        background = new Image(new TextureRegion(new Texture(Gdx.files.internal("assets/ui/title.png"))));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        title = new Label("Crystal", skin);
        title.setFontScale(15);
        title.setAlignment(Align.top);
        title.setFillParent(true);
        title.setColor(Color.WHITE);

        startLbl = new Label("Start", skin);
        startLbl.setFontScale(8);
        startLbl.setColor(Color.WHITE);

        creditsLbl = new Label("Credits", skin);
        creditsLbl.setFontScale(8);
        creditsLbl.setColor(Color.WHITE);

        quitLbl = new Label("Quit", skin);
        quitLbl.setFontScale(8);
        quitLbl.setColor(Color.WHITE);

        menu = new Table();
        menu.setFillParent(true);
        menu.add(startLbl).height(60).padBottom(20).padTop(150).row();
        menu.add(creditsLbl).height(60).padBottom(20).row();
        menu.add(quitLbl).height(60).padBottom(20).row();
        menu.align(Align.center);

        stage.addActor(background);
        stage.addActor(menu);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
//         animate the main menu when entering
        menu.addAction(Actions.alpha(0));
        background.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.delay(0.25f),
                Actions.fadeIn(2.5f)

        ));
        menu.addAction(Actions.sequence(
                Actions.fadeIn(3.5f)
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

        if (InputUtil.checkInputs(Input.Keys.ENTER, Xbox360Pad.A)) {
            enterWasPressed = true;
            SoundLibrary.playSound("Select_confirm");
        } else if (enterWasPressed && !(InputUtil.checkInputs(Input.Keys.ENTER, Xbox360Pad.A))){
            switch (menuSelection) {
                case 0:
                    game.setScreen(new SetupScreen(game));
                    break;
                case 1:
                    game.setScreen(new CreditsScreen(game));
                    break;
                case 2:
                    Gdx.app.exit();
                    break;
            }
        }

        if (InputUtil.checkInputs(Input.Keys.DOWN, Xbox360Pad.LS_DOWN) && !downIsPressed) {
            SoundLibrary.playSound("Select_change");
            menuSelection = (menuSelection + 1) % 3;
            downIsPressed = true;
        } else if(!InputUtil.checkInputs(Input.Keys.DOWN, Xbox360Pad.LS_DOWN)){
            downIsPressed = false;
        }

        if (InputUtil.checkInputs(Input.Keys.UP, Xbox360Pad.LS_UP) && !upIsPressed) {
            SoundLibrary.playSound("Select_change");
            menuSelection -= 1;
            if (menuSelection < 0) {
                menuSelection = 2;
            }
            upIsPressed = true;
        } else if(!InputUtil.checkInputs(Input.Keys.UP, Xbox360Pad.LS_UP)){
            upIsPressed = false;
        }

            updateMenuSelection();
    }

    private void updateMenuSelection(){
        switch(menuSelection){
            case 0:
                // start selected
                startLbl.setColor(Color.YELLOW);
                creditsLbl.setColor(Color.WHITE);
                quitLbl.setColor(Color.WHITE);
                break;

            case 1:
                // credits selected
                startLbl.setColor(Color.WHITE);
                creditsLbl.setColor(Color.YELLOW);
                quitLbl.setColor(Color.WHITE);
                break;

            case 2:
                // exit selected
                startLbl.setColor(Color.WHITE);
                creditsLbl.setColor(Color.WHITE);
                quitLbl.setColor(Color.YELLOW);
                break;
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
