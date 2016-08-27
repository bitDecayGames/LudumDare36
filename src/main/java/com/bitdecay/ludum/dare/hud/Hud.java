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
    private Animation healthBar;
    private TextureRegion fuelGauge;
    private TextureRegion fuelNeedle;

    public Hud(Player newPlayer) {
        player = newPlayer;

        //healthBar = LudumDareGame.atlas.findRegion("healthBar/HealthBar");
        fuelGauge = LudumDareGame.atlas.findRegion("fuelGauge/FuelGauge");
        fuelNeedle= LudumDareGame.atlas.findRegion("fuelGauge/FuelNeedle");

    }

    public void render(SpriteBatch uiBatch) {
       /* double newHealth= player.health.current / player.health.max;
        healthBar.setFrameIndex((int)Math.floor(newHealth));
*/
        float newFuel=player.getJetpack().currentFuel / player.getJetpack().maxFuel;
        
        //uiBatch.draw(healthBar.getFrame(),450,0);
        float rotation = (240*newFuel)-160;
        uiBatch.draw(fuelGauge,100,100);
        uiBatch.draw(fuelNeedle,100,100,32,32,64,64,1,1,rotation);

    }

}


