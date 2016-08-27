package com.bitdecay.ludum.dare.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicSpriteBatch;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.*;

/**
 * Created by Luke on 8/26/2016.
 */
public class Hud {

    private Player player;
    private Animation healthFace;
    private Animation fuelGauge;

    public Hud(Player newPlayer) {
        player = newPlayer;

        //healthFace = new Animation("explode", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.03f), LudumDareGame.atlas.findRegions("hud/healthface"));
        //fuelGauge = new Animation("explode", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.03f), LudumDareGame.atlas.findRegions("hud/fuelGauge"));

    }

    public void render(SpriteBatch uiBatch) {
       /* double newHealth= player.health.current / player.health.max;
        healthFace.setFrameIndex((int)Math.floor(newHealth));
*/
        double newFuel= player.getJetpack().currentFuel / player.getJetpack().maxFuel;
        fuelGauge.setFrameIndex((int)Math.floor(newFuel));

        /*TextureRegion temp = LudumDareGame.atlas.findRegion("buzzkill/dankest_boat");
        uiBatch.draw(temp,450,0);*/

        //uiBatch.draw(healthFace.getFrame(),450,0);
        uiBatch.draw(fuelGauge.getFrame(),200,0);

    }

}


