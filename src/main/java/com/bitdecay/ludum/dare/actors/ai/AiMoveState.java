package com.bitdecay.ludum.dare.actors.ai;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.ludum.dare.components.AIControlComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AiMoveState implements IState {


    private static float JUMP_TILE_SIZE = 16;

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
        BitPointInt startIndex = posToIndex(this.me.getCenter());
        BitPointInt goalIndex = posToIndex(goalPos);
        this.goal = new AiNode(indexToPos(goalIndex), goalIndex, AiNodeType.STOP);
        AiNode start = new AiNode(this.me.getCenter(), startIndex, AiNodeType.START);

        // calculate all the sub targets
        nodes.add(start);
        recurseThroughNodes(nodes.get(0), goal, nodes);
    }

    private void recurseThroughNodes(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        // this is just a really long and dumb java way to chain options... stupid java
        List<Optional<AiNode>> optionalNodes = new ArrayList<>();
        optionalNodes.add(findWalkableNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findHopDownNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findLeapOverEmptyNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findLeapOverAndDownNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findJumpDownNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findLeapDownNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findPlummetDownNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findDoubleStepHopNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findHoppableNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findJumpableNode(currentNode, goal, visitedNodes));
        optionalNodes.add(findLeapableNode(currentNode, goal, visitedNodes));
        List<AiNode> realNodes = optionalNodes.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted((a, b) -> sortDstToGoal(a, b, goal))
                .filter((node) -> dst(node, goal) < dst(currentNode, goal))
                .collect(Collectors.toList());
        Optional<AiNode> nextNode = (realNodes.size() > 0 ? Optional.of(realNodes.get(0)) : Optional.empty());
        if (nextNode.isPresent()){
            visitedNodes.add(nextNode.get());
            recurseThroughNodes(nextNode.get(), goal, visitedNodes);
        }
    }

    private Optional<AiNode> findWalkableNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.WALK,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 1, currentNode.index.y),
                (neighbors) -> !neighbors.left && neighbors.bottomLeft,
                new BitPointInt(currentNode.index.x + 1, currentNode.index.y),
                (neighbors) -> !neighbors.right && neighbors.bottomRight
        );
    }

    private Optional<AiNode> findHopDownNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.WALK,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 1, currentNode.index.y - 1),
                (neighbors) -> !neighbors.left && !neighbors.bottomLeft && neighbors.bottomLeftNeighbors().bottom,
                new BitPointInt(currentNode.index.x + 1, currentNode.index.y - 1),
                (neighbors) -> !neighbors.right && !neighbors.bottomRight && neighbors.bottomRightNeighbors().bottom
        );
    }

    private Optional<AiNode> findJumpDownNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.WALK,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 1, currentNode.index.y - 2),
                (neighbors) -> !neighbors.left && !neighbors.bottomLeft && !neighbors.bottomLeftNeighbors().bottom && neighbors.bottomLeftNeighbors().bottomNeighbors().bottom,
                new BitPointInt(currentNode.index.x + 1, currentNode.index.y - 2),
                (neighbors) -> !neighbors.right && !neighbors.bottomRight && !neighbors.bottomRightNeighbors().bottom && neighbors.bottomRightNeighbors().bottomNeighbors().bottom
        );
    }

    private Optional<AiNode> findPlummetDownNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.WALK,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 1, currentNode.index.y - 3),
                (neighbors) -> !neighbors.left &&
                        !neighbors.bottomLeft &&
                        !neighbors.bottomLeftNeighbors().bottom &&
                        !neighbors.bottomLeftNeighbors().bottomNeighbors().bottom &&
                        neighbors.bottomLeftNeighbors().bottomNeighbors().bottomNeighbors().bottom,
                new BitPointInt(currentNode.index.x + 1, currentNode.index.y - 3),
                (neighbors) -> !neighbors.right &&
                        !neighbors.bottomRight &&
                        !neighbors.bottomRightNeighbors().bottom &&
                        !neighbors.bottomRightNeighbors().bottomNeighbors().bottom &&
                        neighbors.bottomRightNeighbors().bottomNeighbors().bottomNeighbors().bottom
        );
    }

    private Optional<AiNode> findLeapDownNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.JUMP,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 2, currentNode.index.y - 2),
                (neighbors) -> !neighbors.top &&
                        !neighbors.topLeft &&
                        !neighbors.topLeftNeighbors().left &&
                        !neighbors.leftNeighbors().left &&
                        !neighbors.bottomLeftNeighbors().left &&
                        !neighbors.bottomLeftNeighbors().bottomLeft &&
                        !neighbors.bottomLeftNeighbors().bottomNeighbors().bottom &&
                        neighbors.bottomLeftNeighbors().bottomLeftNeighbors().bottom,
                new BitPointInt(currentNode.index.x + 2, currentNode.index.y - 2),
                (neighbors) -> !neighbors.top &&
                        !neighbors.topRight &&
                        !neighbors.topRightNeighbors().right &&
                        !neighbors.rightNeighbors().right &&
                        !neighbors.bottomRightNeighbors().right &&
                        !neighbors.bottomRightNeighbors().bottomRight &&
                        !neighbors.bottomRightNeighbors().bottomNeighbors().bottom &&
                        neighbors.bottomRightNeighbors().bottomRightNeighbors().bottom
        );
    }

    private Optional<AiNode> findHoppableNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.JUMP,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 1, currentNode.index.y + 1),
                (neighbors) -> neighbors.left && !neighbors.topLeft && !neighbors.top,
                new BitPointInt(currentNode.index.x + 1, currentNode.index.y + 1),
                (neighbors) -> neighbors.right && !neighbors.topRight && !neighbors.top
        );
    }

    private Optional<AiNode> findDoubleStepHopNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.JUMP,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 2, currentNode.index.y + 2),
                (neighbors) -> neighbors.topLeftNeighbors().left && !neighbors.topLeft && !neighbors.top &&
                        !neighbors.topNeighbors().top && !neighbors.topNeighbors().topLeft && !neighbors.topLeftNeighbors().topLeft,
                new BitPointInt(currentNode.index.x + 2, currentNode.index.y + 2),
                (neighbors) -> neighbors.topRightNeighbors().right && !neighbors.topRight && !neighbors.top &&
                        !neighbors.topNeighbors().top && !neighbors.topNeighbors().topRight && !neighbors.topRightNeighbors().topRight
        );
    }

    private Optional<AiNode> findJumpableNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.JUMP,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 1, currentNode.index.y + 2),
                (neighbors) -> neighbors.topLeft && !neighbors.topLeftNeighbors().top && !neighbors.top && !neighbors.topNeighbors().top,
                new BitPointInt(currentNode.index.x + 1, currentNode.index.y + 2),
                (neighbors) -> neighbors.topRight && !neighbors.topRightNeighbors().top && !neighbors.top && !neighbors.topNeighbors().top
                );
    }

    private Optional<AiNode> findLeapableNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.JUMP,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 1, currentNode.index.y + 3),
                (neighbors) -> !neighbors.top &&
                        !neighbors.topNeighbors().top &&
                        !neighbors.topNeighbors().topNeighbors().top &&
                        !neighbors.topNeighbors().topNeighbors().topLeft &&
                        neighbors.topNeighbors().topLeft,
                new BitPointInt(currentNode.index.x + 1, currentNode.index.y + 3),
                (neighbors) -> !neighbors.top &&
                        !neighbors.topNeighbors().top &&
                        !neighbors.topNeighbors().topNeighbors().top &&
                        !neighbors.topNeighbors().topNeighbors().topRight &&
                        neighbors.topNeighbors().topRight
        );
    }

    private Optional<AiNode> findLeapOverEmptyNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.JUMP,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 2, currentNode.index.y),
                (neighbors) -> !neighbors.top &&
                        !neighbors.topLeft &&
                        !neighbors.topLeftNeighbors().left &&
                        !neighbors.leftNeighbors().left &&
                        !neighbors.leftNeighbors().bottom &&
                        neighbors.bottomLeftNeighbors().left,
                new BitPointInt(currentNode.index.x + 2, currentNode.index.y),
                (neighbors) -> !neighbors.top &&
                        !neighbors.topRight &&
                        !neighbors.topRightNeighbors().right &&
                        !neighbors.rightNeighbors().right &&
                        !neighbors.rightNeighbors().bottom &&
                        neighbors.bottomRightNeighbors().right
        );
    }

    private Optional<AiNode> findLeapOverAndDownNode(AiNode currentNode, AiNode goal, List<AiNode> visitedNodes){
        return movementCheck(
                AiNodeType.JUMP,
                currentNode,
                goal,
                visitedNodes,
                new BitPointInt(currentNode.index.x - 2, currentNode.index.y - 1),
                (neighbors) -> !neighbors.top &&
                        !neighbors.topLeft &&
                        !neighbors.topLeftNeighbors().left &&
                        !neighbors.leftNeighbors().left &&
                        !neighbors.bottomLeftNeighbors().left &&
                        !neighbors.bottomLeftNeighbors().bottomLeft &&
                        !neighbors.bottomLeftNeighbors().bottomNeighbors().bottom &&
                        neighbors.bottomLeftNeighbors().bottomLeftNeighbors().bottom,
                new BitPointInt(currentNode.index.x + 2, currentNode.index.y - 1),
                (neighbors) -> !neighbors.top &&
                        !neighbors.topRight &&
                        !neighbors.topRightNeighbors().right &&
                        !neighbors.rightNeighbors().right &&
                        !neighbors.bottomRightNeighbors().right &&
                        !neighbors.bottomRightNeighbors().bottomRight &&
                        !neighbors.bottomRightNeighbors().bottomNeighbors().bottom &&
                        neighbors.bottomRightNeighbors().bottomRightNeighbors().bottom
        );
    }

    private Optional<AiNode> movementCheck(AiNodeType nodeType, AiNode currentNode, AiNode goal, List<AiNode> visitedNodes, BitPointInt leftIndex, BoolCheck leftBoolCheck, BitPointInt rightIndex, BoolCheck rightBoolCheck){
        int x = currentNode.index.x;
        int y = currentNode.index.y;
        float curDist = dst(x, y, goal.index.x, goal.index.y);
        AiNodeNeighbors neighbors = new AiNodeNeighbors(currentNode.index, me.getWorld());

        boolean leftCheck = leftBoolCheck.boolCheck(neighbors) && !nodeListContains(leftIndex.x, leftIndex.y, visitedNodes);
        float leftDist = dst(leftIndex.x, leftIndex.y, goal.index.x, goal.index.y);

        boolean rightCheck = rightBoolCheck.boolCheck(neighbors) && !nodeListContains(rightIndex.x, rightIndex.y, visitedNodes);
        float rightDist = dst(rightIndex.x, rightIndex.y, goal.index.x, goal.index.y);

        boolean goLeft = false;
        boolean goRight = false;

        if (leftCheck && rightCheck){
            if (leftDist > rightDist) goRight = true;
            else goLeft = true;
        } else if (leftCheck) goLeft = true;
        else if (rightCheck) goRight = true;

        if (goLeft) return Optional.of(new AiNode(indexToPos(leftIndex), leftIndex, nodeType));
        else if (goRight) return Optional.of(new AiNode(indexToPos(rightIndex), rightIndex, nodeType));
        else return Optional.empty();
    }

    private float dst(int x, int y, int x2, int y2){
        return new Vector2(x, y).dst(x2, y2);
    }

    private float dst(BitPointInt a, BitPointInt b) { return dst(a.x, a.y, b.x, b.y); }

    private float dst(AiNode a, AiNode b){ return dst(a.index.x, a.index.y, b.index.x, b.index.y); }

    private int sortDstToGoal(AiNode a, AiNode b, AiNode goal){
        float aDst = dst(a, goal);
        float bDst = dst(b, goal);
        if (aDst < bDst) return -1;
        else return 1;
    }

    private boolean nodeListContains(int x, int y, List<AiNode> nodeList){
        for (int i = 0; i < nodeList.size(); i++){
            AiNode n = nodeList.get(i);
            if (n.index.x == x && n.index.y == y) return true;
        }
        return false;
    }

    private BitBody bodyAtIndex(BitPointInt index){
        try {
            return me.getWorld().getGrid()[index.x][index.y];
        } catch (Exception e){
            return null;
        }
    }

    public Vector2 indexToPos(BitPointInt index){
        BitBody body = bodyAtIndex(index);
        if (body == null) {
            BitBody[][] bodies = me.getWorld().getGrid();
            for (int x = 0; x < bodies.length; x++){
                BitBody[] col = bodies[x];
                if (col != null) for (int y = 0; y < col.length; y++){
                    BitBody bod = col[y];
                    if (bod != null) {
                        BitPoint diff = new BitPoint(index.x, index.y).minus(x, y).scale(JUMP_TILE_SIZE);
                        return new Vector2(bod.aabb.xy.x, bod.aabb.xy.y).add(JUMP_TILE_SIZE / 2, JUMP_TILE_SIZE / 2).add(diff.x, diff.y);
                    }
                }
            }
            throw new RuntimeException("This should never never ever happen");
        } else {
            BitPoint p = body.aabb.center();
            return new Vector2(p.x, p.y);
        }
    }

    public BitPointInt posToIndex(Vector2 pos){
        BitBody[][] bodies = me.getWorld().getGrid();
        for (int x = 0; x < bodies.length; x++){
            BitBody[] col = bodies[x];
            if (col != null) for (int y = 0; y < col.length; y++){
                BitBody body = col[y];
                if (body != null && body.aabb.contains(pos.x, pos.y)) return new BitPointInt(x, y);
                else if (body != null) {
                    BitPoint diff = new BitPoint(body.aabb.center()).minus(pos.x, pos.y).dividedBy(-JUMP_TILE_SIZE);
                    return new BitPointInt(x + Math.round(diff.x), y + Math.round(diff.y));
                }
            }
        }
        throw new RuntimeException("AHHHH This should never happen!!!!");
    }

    private void p(String s){
        System.out.println(s);
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        // TODO: figure out how to get to the next node
        if (nodes.size() == 0){
            // TODO: go to a different state, you've reached your goal
            return new AiIdleState(input);
        } else {
            // TODO: travel to the node
            AiNode nextNode = nodes.get(0);
            if (isAtNode(nextNode)){
                //me.setPosition(nextNode.pos.x, nextNode.pos.y);
                nodes.remove(0);
                if (nodes.size() > 0) nextNode = nodes.get(0);
            }
            if (nextNode != null){
                Vector2 pos = me.getCenter();
                if(Math.abs(nextNode.pos.x - pos.x) > 3) {
                    if (nextNode.pos.x < pos.x ) input.pressed(InputAction.LEFT);
                    else if (nextNode.pos.x > pos.x) input.pressed(InputAction.RIGHT);
                }

                if (me.isGrounded() && nextNode.nodeType == AiNodeType.JUMP) input.justPressed(InputAction.JUMP);
            }
        }
        return null;
    }

    public void debugDraw(ShapeRenderer renderer){
        if (nodes.size() > 0){
            AiNode nextNode = nodes.get(0);
            renderer.setColor(Color.RED);
            renderer.circle(nextNode.pos.x, nextNode.pos.y, 5);
        }
    }

    private boolean isAtNode(AiNode node) {
        return node != null && me.getCenter().dst(node.pos) < 5;
    }

    private interface BoolCheck{
        boolean boolCheck(AiNodeNeighbors neighbors);
    }
}


