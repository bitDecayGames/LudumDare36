package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;

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

    boolean active = true;

    public MainMenuScreen(final LudumDareGame game) {
        this.game = game;

        menuSelection = 0;

        Skin skin = new Skin(ResourceDir.internal("skins/skin.json"));

        background = new Image(LudumDareGame.atlas.findRegion("splash/TitleScreen"));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        title = new Label("Crystal", skin);
        title.setFontScale(15);
        title.setAlignment(Align.top);
        title.setFillParent(true);
        title.setColor(Color.WHITE);

        startLbl = new Label("Start", skin);
        startLbl.setFontScale(8);
        startLbl.setColor(Color.WHITE);
        startLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gotoGame();
            }
        });

        creditsLbl = new Label("Credits", skin);
        creditsLbl.setFontScale(8);
        creditsLbl.setColor(Color.WHITE);
        creditsLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gotoCredits();
            }
        });

        quitLbl = new Label("Quit", skin);
        quitLbl.setFontScale(8);
        quitLbl.setColor(Color.WHITE);
        quitLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitGame();
            }
        });

        menu = new Table();
        menu.setFillParent(true);
        menu.add(startLbl).height(60).padBottom(20).padTop(150).row();
        menu.add(creditsLbl).height(60).padBottom(20).row();
        menu.add(quitLbl).height(60).padBottom(20).row();
        menu.align(Align.center);
        menu.padLeft(300);
        menu.padBottom(200);

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

        if (InputUtil.isJustPressed(Input.Keys.ENTER, Xbox360Pad.A)){
            switch (menuSelection) {
                case 0:
                    gotoGame();
                    break;
                case 1:
                    gotoCredits();
                    break;
                case 2:
                    exitGame();
                    break;
            }
        }

        if (InputUtil.isJustPressed(Input.Keys.DOWN, Xbox360Pad.LS_DOWN)) {
//            SoundLibrary.playSound("Select_change");
            menuSelection = (menuSelection + 1) % 3;
        }

        if (InputUtil.isPressed(Input.Keys.UP, Xbox360Pad.LS_UP)) {
//            SoundLibrary.playSound("Select_change");
            menuSelection -= 1;
            if (menuSelection < 0) {
                menuSelection = 2;
            }
        }

        updateMenuSelection();
    }

    private void gotoGame() {
        game.setScreen(new GameScreen(game));
    }

    private void gotoCredits() {
        game.setScreen(new CreditsScreen(game));
    }

    private void exitGame() {
        Gdx.app.exit();
    }

    private void updateMenuSelection() {
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
