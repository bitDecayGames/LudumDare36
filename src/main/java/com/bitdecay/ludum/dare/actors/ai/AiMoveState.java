package com.bitdecay.ludum.dare.actors.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IState;

import java.util.ArrayList;
import java.util.List;

public class AiMoveState implements IState {

    private Monkey me;
    private AIControlComponent input;
    private AiNode goal;
    private List<AiNode> nodes = new ArrayList<>();

    public AiMoveState(Monkey me, AIControlComponent input, Vector2 goalPos) {
        if (me == null) throw new RuntimeException("Cant have null ai monkey");
        if (input == null) throw new RuntimeException("Cant have null ai input");
        if (goalPos == null) throw new RuntimeException("Cant have null ai target");

        this.me = me;
        this.input = input;
        this.goal = new AiNode(goalPos, posToIndex(goalPos), AiNodeType.STOP);

        // TODO: calculate all the sub targets
        nodes.add(new AiNode(this.me.getPosition(), posToIndex(this.me.getPosition()), AiNodeType.START));

        nodes.add(this.goal);
    }

    private BitBody bodyAtIndex(BitPointInt index){
        try {
            return me.getWorld().getGrid()[index.x][index.y];
        } catch (Exception e){
            return null;
        }
    }

    private Vector2 indexToPos(BitPointInt index){
        BitBody body = bodyAtIndex(index);
        if (body == null) return null;
        else {
            BitPoint p = body.aabb.center();
            return new Vector2(p.x, p.y);
        }
    }

    private BitPointInt posToIndex(Vector2 pos){
        BitBody[][] bodies = me.getWorld().getGrid();
        for (int x = 0; x < bodies.length; x++){
            BitBody[] col = bodies[x];
            if (col != null) for (int y = 0; y < col.length; y++){
                BitBody body = col[y];
                if (body != null && body.aabb.contains(pos.x, pos.y)) return new BitPointInt(x, y);
            }
        }
        return null;
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            input.isJustPressed(InputAction.JUMP);
        else if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            input.isPressed(InputAction.JUMP);

        // TODO: figure out how to get to the next node
        if (nodes.size() == 0){
            // TODO: go to a different state, you've reached your goal
        } else {
            // TODO: travel to the node
            AiNode nextNode = nodes.get(0);
            if (isAtNode(nextNode)){
                nodes.remove(0);
            } else {
                Vector2 pos = me.getPosition();
                if (nextNode.pos.x < pos.x) input.pressed(InputAction.LEFT);
                if (nextNode.pos.x > pos.x) input.pressed(InputAction.RIGHT);
            }
        }
        return null;
    }

    private boolean isAtNode(AiNode node) {
        return node != null && me.getPosition().dst(node.pos) < 30;
    }
}
