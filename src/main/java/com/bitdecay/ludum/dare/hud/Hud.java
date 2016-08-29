package com.bitdecay.ludum.dare.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.StringBuilder;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.environment.DeadShip;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.HealthComponent;
import com.bitdecay.ludum.dare.components.PositionComponent;
import com.bitdecay.ludum.dare.text.TextScreenObject;

public class Hud {

    private Player player;
    private TextureRegion fuelGauge;
    private TextureRegion fuelNeedle;
    private TextureRegion healthGauge;
    private TextScreenObject shipPartsText;
    private TextScreenObject healthText;
    private int screenWidth;
    private int screenHeight;
    private final int healthX;
    private final int healthY;

    public Hud(Player newPlayer) {
        player = newPlayer;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        fuelGauge = LudumDareGame.atlas.findRegion("fuelGauge/FuelGauge");
        fuelNeedle = LudumDareGame.atlas.findRegion("fuelGauge/FuelNeedle");

        healthGauge = LudumDareGame.atlas.findRegion("energy/1");

        shipPartsText = new TextScreenObject(new PositionComponent(screenWidth - 175, screenHeight - 115), "SHIP PARTS 0/5", Color.WHITE);

        healthX = screenWidth - 175da;
        healthY = screenHeight - 100;
        healthText = new TextScreenObject(new PositionComponent(healthX + 10, healthY + 31), "", new Color(0x00, 0xfc, 0xfc, 1));
        healthText.useFont2(true);
    }


    public void render(SpriteBatch uiBatch) {

        int fuelX = screenWidth - 100;
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

        uiBatch.draw(healthGauge, healthX, healthY);


        HealthComponent health = player.getHealth();
        String healthStr = Float.toString(health.health / health.max * 100);
        healthStr = healthStr.substring(0, healthStr.indexOf("."));
        StringBuilder builder = new StringBuilder();
        for (int i = healthStr.length(); i < 3; i++) {
            builder.append(" ");
        }
        builder.append(healthStr);
        healthText.setText(builder.toString().replaceAll("(.)", "$1 "));
        healthText.draw(uiBatch);
    }

}
