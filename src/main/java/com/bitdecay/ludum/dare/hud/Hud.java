package com.bitdecay.ludum.dare.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.environment.DeadShip;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.text.TextScreenObject;
import com.bytebreakstudios.animagic.animation.Animation;

import java.util.*;

/**
 * Created by Luke on 8/26/2016.
 */
public class Hud {

    private Player player;
    private Animation healthBar;
    private TextureRegion fuelGauge;
    private TextureRegion fuelNeedle;
    private TextScreenObject shipPartsText;
    private int screenWidth;
    private int screenHeight;

    public Hud(Player newPlayer) {
        player = newPlayer;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        //healthBar = LudumDareGame.atlas.findRegion("healthBar/HealthBar");
        fuelGauge = LudumDareGame.atlas.findRegion("fuelGauge/FuelGauge");
        fuelNeedle= LudumDareGame.atlas.findRegion("fuelGauge/FuelNeedle");

        shipPartsText = new TextScreenObject(new PositionComponent(screenWidth - 175, screenHeight - 115), "SHIP PARTS 0/5", Color.WHITE);


    }


    public void render(SpriteBatch uiBatch) {

        int fuelX = screenWidth - 150;
        int fuelY = screenHeight - 100;


       /* double newHealth= player.health.current / player.health.max;
        healthBar.setFrameIndex((int)Math.floor(newHealth));
*/
        float newFuel=player.getJetpack().currentFuel / player.getJetpack().maxFuel;

        //uiBatch.draw(healthBar.getFrame(), 450, 0);
        float rotation = (240 * newFuel) - 160;
        uiBatch.draw(fuelGauge, fuelX, fuelY);
        uiBatch.draw(fuelNeedle, fuelX, fuelY, 32, 32, 64, 64, 1, 1, rotation);
        int parts = DeadShip.getNumCollectedParts();
        shipPartsText.setText("SHIP PARTS: " + parts + "/6");

        shipPartsText.draw(uiBatch);
    }

}
