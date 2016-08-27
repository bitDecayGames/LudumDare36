package com.bitdecay.ludum.dare.util;

import com.bitdecay.ludum.dare.actors.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Players {
    static Boolean initialized = false;

    static List<Player> players;

    public static void initialize(List<Player> startingPlayers) {
        if (initialized) {
//            throw new RuntimeException("Players have already been initialized");
        } else if (startingPlayers == null || startingPlayers.size() < 1) {
            throw new RuntimeException("No players were initialized");
        }

        players = startingPlayers;
        initialized = true;
    }

    public static List<Player> list() {
        if (!initialized) {
            throw new RuntimeException("Initialize was not called. Did you run the SetupScreen?");
        }

        return shallowCopy();
    }

    private static List<Player> shallowCopy() {
        List<Player> shallowCopy = new ArrayList<>();
        for (Player player : players) {
            shallowCopy.add(player);
        }
        return shallowCopy;
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
