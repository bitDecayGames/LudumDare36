package com.bitdecay.ludum.dare.actors.ai;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bitdecay.ludum.dare.levels.ai.Node;
import com.bitdecay.ludum.dare.levels.ai.Nodes;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class AIRunAlongState implements IState {

    private Player me;
    private AIControlComponent input;

    private Vector2 target;

    private int maxSize = 30;
    private List<Vector2> lastPositions = new ArrayList<>();

    private Node node;
    private Nodes nodes;

    public AIRunAlongState(Player me, AIControlComponent input, Nodes nodes, Node node, Vector2 target) {
        this.me = me;
        this.input = input;
        this.node = node;
        this.target = target;
        this.nodes = nodes;
    }

    @Override
    public void enter() {
        node.debugSelected = true;
    }

    @Override
    public void exit() {
        node.debugSelected = false;
    }

    @Override
    public IState update(float delta) {
        Vector2 pos = me.getPosition();
        if (pos.x > target.x - 3 && pos.x < target.x + 3) {
            return new AIStopMovingState(me, input, nodes, target, 3);
        } else if (pos.x < target.x) {
            if (node.equalsRight(pos.x) || node.whereIsPointInNode(pos.x) > 1)
                return new AIJumpToNearest(me, input, nodes, node, target);
            input.pressed(InputAction.RIGHT);
        } else if (pos.x > target.x) {
            if (node.equalsLeft(pos.x) || node.whereIsPointInNode(pos.x) < 0)
                return new AIJumpToNearest(me, input, nodes, node, target);
            input.pressed(InputAction.LEFT);
        }
        lastPositions.add(pos);
        if (lastPositions.size() > maxSize) lastPositions.remove(0);
        if (lastPositions.size() == maxSize) {
            if (aiIsStuck()) {
                return new AIJumpToNearest(me, input, nodes, node, target);
            }
        }
        return null;
    }

    private boolean aiIsStuck() {
        for (Vector2 lastPosition : lastPositions) {
            if (lastPosition.dst(lastPositions.get(0)) > 5) return false;
        }
        return true;
    }
}
