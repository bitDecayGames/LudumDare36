package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.radiansToDegrees;

public class DemoScreen implements Screen {

    private Stage stage;
    private LudumDareGame game;

    private List<Vector2> positions = new ArrayList<>();
    private List<Vector2> directions = new ArrayList<>();
    private TextureRegion region;
    private SpriteBatch sb;
    private float speed = 20;

    public DemoScreen(LudumDareGame game){
        this.game = game;
        stage = new Stage();
    }

    @Override
    public void show() {
        sb = new SpriteBatch();

        region = new TextureRegion(new Texture("assets/whitebullet/1.png"));
    }

    @Override
    public void render(float delta) {
        LudumDareGame.assetManager.update();
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (InputUtil.checkInputs(Input.Keys.S, Xbox360Pad.BACK)) {
            nextScreen();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 start = new Vector2(450, 300);
            positions.add(start);
            directions.add(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()).sub(start).nor().scl(speed));
        }

        sb.begin();
        for (int i = 0; i < positions.size(); i++){
            Vector2 pos = positions.get(i);
            Vector2 dir = directions.get(i);
            pos.add(dir);
            sb.draw(region, pos.x, pos.y, 50, 26, 100, 52, 1, 1, 360 - degreesBetweenTwoVectors(-1, 0, dir.x, dir.y));
        }
        sb.end();

        stage.act();
        stage.draw();
    }

    public float degreesBetweenTwoVectors(float aX, float aY, float bX, float bY){
        float res = (MathUtils.atan2(aY, aX) - MathUtils.atan2(bY, bX)) * radiansToDegrees;
        // this has to be here because the algorithm goes from -90 to +270 instead of -180 to +180
        if (res > 180) return res - 360;
        else if (res <= -180) return res + 360;
        else return res;
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

    private void nextScreen() {
        game.setScreen(new MainMenuScreen(game));
    }
}
