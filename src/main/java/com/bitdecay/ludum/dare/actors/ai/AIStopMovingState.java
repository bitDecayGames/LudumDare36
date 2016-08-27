package com.bitdecay.ludum.dare.actors.ai;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bitdecay.ludum.dare.levels.ai.Nodes;

@Deprecated
public class AIStopMovingState implements IState {

    private Player me;
    private AIControlComponent input;
    private Vector2 target;
    private Nodes nodes;

    private float timeToWait;

    public AIStopMovingState(Player me, AIControlComponent input, Nodes nodes, Vector2 target, float timeToWait) {
        this.me = me;
        this.input = input;
        this.target = target;
        this.nodes = nodes;
        this.timeToWait = timeToWait;
    }

    @Override
    public void enter() {
    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        timeToWait -= delta;
        if (timeToWait < 0) {
            return new AIStopMovingState(me, input, nodes, target, 3);
        }
        return null;
    }
}
