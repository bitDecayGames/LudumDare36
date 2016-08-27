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
public class AIJumpToNearest implements IState {

    private Player me;
    private AIControlComponent input;


    private Vector2 target;

    private Node node;
    private Nodes nodes;


    private int numberOfMisTries = 0;
    private int maxSize = 30;
    private List<Vector2> lastPositions = new ArrayList<>();

    private float waitToCalc = 1;

    public AIJumpToNearest(Player me, AIControlComponent input, Nodes nodes, Node node, Vector2 target) {
        this.me = me;
        this.input = input;
        this.node = node;
        this.target = target;
        this.nodes = nodes;
    }

    @Override
    public void enter() {
    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        waitToCalc -= delta;
        Vector2 pos = me.getPosition();
        if (waitToCalc < 0) {
            if (pos.x > target.x - 3 && pos.x < target.x + 3) {
                return new AIStopMovingState(me, input, nodes, target, 3);
            } else if (pos.x < target.x) {
                input.justPressed(InputAction.JUMP);
                input.pressed(InputAction.RIGHT);
            } else if (pos.x > target.x) {
                input.justPressed(InputAction.JUMP);
                input.pressed(InputAction.LEFT);
            }
            Node closestNode = nodes.closestContainingNode(pos);
            if (closestNode != null && closestNode != node) {
                return new AIRunAlongState(me, input, nodes, closestNode, target);
            }
            lastPositions.add(pos);
            if (lastPositions.size() > maxSize) lastPositions.remove(0);
            if (lastPositions.size() == maxSize) {
                if (aiIsStuck()) {
                    input.justPressed(InputAction.JUMP);
                    numberOfMisTries++;
                    if (numberOfMisTries > 20) {
                        numberOfMisTries = 0;
                        return new AIJumpToNearest(me, input, nodes, node, new Vector2(-10000, target.y));
                    }
                }
            }
        }

        if (pos.x < target.x) {
            input.justPressed(InputAction.JUMP);
            input.pressed(InputAction.RIGHT);
        } else if (pos.x > target.x) {
            input.justPressed(InputAction.JUMP);
            input.pressed(InputAction.LEFT);
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
