package com.bitdecay.ludum.dare.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.*;

/**
 * Created by Luke on 8/26/2016.
 */
public class Hud {

        private Player player;
        AnimagicSpriteBatch uiBatch;
        private Animation healthFace;
        private Animation fuelGauge;

        public Hud(Player newPlayer, AnimagicSpriteBatch spriteBatch) {
           player = newPlayer;
           uiBatch = spriteBatch;
           healthFace = new Animation();
        }

        public void show() {
            //healthFace = new Image(new TextureRegion(new Texture(Gdx.files.internal())));
            datBoat = new Image(new TextureRegion(new Texture(Gdx.files.internal("src/main/resources/assets/buzzkill/dankest_boat.jpg"))));
        }

        public void render(float delta) {
            try {
                update();
                draw();
            } catch (Exception e) {

            }
        }

        public void update() {
            //if player.HealthComponent != currentHealth{
           // render
        //}
        }


        public void render() {

            draw();
        }


        private void draw(){
            Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            int screenWidth = Gdx.graphics.getWidth() / 2;
            int screenHeight = Gdx.graphics.getHeight() / 2;


//        worldRenderer.render(world, cameras[0]);
        }
    }


