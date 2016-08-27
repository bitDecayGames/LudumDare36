package com.bitdecay.ludum.dare.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.control.InputUtil;
import com.bitdecay.ludum.dare.control.Xbox360Pad;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.radiansToDegrees;
import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class MikeTestBulletScreen implements Screen {

    private Stage stage;
    private LudumDareGame game;

    private List<Vector2> positions = new ArrayList<>();
    private List<Boolean> alive = new ArrayList<>();
    private List<Vector2> directions = new ArrayList<>();
    private TextureRegion region;
    private SpriteBatch sb;
    private float speed = 30;
    private List<Animation> animations = new ArrayList<>();
    private Array<AnimagicTextureRegion> animationRegions;
    private AnimagicTextureRegion[] asArray;

    public MikeTestBulletScreen(LudumDareGame game){
        this.game = game;
        stage = new Stage();
    }

    @Override
    public void show() {
        sb = new SpriteBatch();

        animationRegions = atlas.findRegions("bulletexplode");
        asArray = animationRegions.toArray(AnimagicTextureRegion.class);

        region = atlas.findRegion("bullet", 1);
    }

    @Override
    public void render(float delta) {
        LudumDareGame.assetManager.update();
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (InputUtil.isPressed(Input.Keys.S, Xbox360Pad.BACK)) {
            nextScreen();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 start = new Vector2(450, 300);
            positions.add(start);
            alive.add(true);
            directions.add(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()).sub(start).nor().scl(speed));
            animations.add(new Animation("explode", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.03f), asArray));
        }

        sb.begin();
        for (int i = 0; i < positions.size(); i++){
            Vector2 pos = positions.get(i);
            Vector2 dir = directions.get(i);
            boolean isAlive = alive.get(i);
            if (isAlive) {
                pos.add(dir);
                sb.draw(region, pos.x, pos.y, 15, 8, 100, 52, 1, 1, 360 - degreesBetweenTwoVectors(-1, 0, dir.x, dir.y));
            }
            else {
                Animation a = animations.get(i);
                a.update(delta);
                if (a.getFrameIndex() + 1 < a.totalFrames()) sb.draw(a.getFrame(), pos.x, pos.y, 30, 30, 60, 60, 4, 4, 360 - degreesBetweenTwoVectors(-1, 0, dir.x, dir.y));
            }
            if (pos.dst(450, 300) > 200) alive.set(i, false);
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
