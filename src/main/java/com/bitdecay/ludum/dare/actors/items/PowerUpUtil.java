package com.bitdecay.ludum.dare.actors.items;

import com.badlogic.gdx.math.MathUtils;

public class PowerUpUtil {

    public static final String FLIGHT = "FLIGHT";
    public static final String STUN = "STUN";
    public static final String FORCE_JUMP = "FORCE_HIGH_JUMP";
    public static final String SPEED = "SPEED";
    public static final String SLOW = "SLOW";

    public static String randomPowerGenerator(int playerRank){
        switch (MathUtils.random(1, 10)) {
            case 1:
                return FLIGHT;
            case 2:
            case 3:
                return STUN;
            case 4:
            case 5:
            case 6:
                return FORCE_JUMP;
            case 7:
                return SPEED;
            case 8:
            case 9:
            case 10:
            default:
                return SLOW;

        }
    }
}
