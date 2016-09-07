package com.bitdecay.ludum.dare.control;

import com.badlogic.gdx.Input;

public class GameControls {
    public static GameControl Left = new GameControl("[A] or [LEFT]", Input.Keys.A, Input.Keys.LEFT);
    public static GameControl Right = new GameControl("[D] or [RIGHT]", Input.Keys.D, Input.Keys.RIGHT);
    public static GameControl JetPack = new GameControl("[W] or [UP])", Input.Keys.W, Input.Keys.UP);
    public static GameControl PickUp = new GameControl("[S] or [B] or [DOWN]", Input.Keys.S, Input.Keys.B, Input.Keys.DOWN);
    public static GameControl Fire = new GameControl("[SPACE]", Input.Keys.SPACE);
}
