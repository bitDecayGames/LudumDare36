package com.bitdecay.ludum.dare.components;

import com.bitdecay.jump.common.RenderState;
import com.bitdecay.jump.render.JumperRenderState;
import com.bitdecay.ludum.dare.actors.player.Player;

public class PlayerRenderStateComponent extends JumperRenderStateComponent {
    private final Player player;

    public PlayerRenderStateComponent(Player player) {
        super();

        this.player = player;
    }

    @Override
    public void stateChanged(RenderState renderState) {
        super.stateChanged(renderState);

        // TODO animation logic
    }
}
