package com.bitdecay.ludum.dare.levels.ai;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.level.Direction;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.TileObject;
import com.bitdecay.ludum.dare.interfaces.IShapeDraw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Deprecated
public class Nodes extends ArrayList<Node> implements IShapeDraw {

    public Nodes adding(Node node) {
        add(node);
        return this;
    }

    public Node closestContainingNode(Vector2 pos) {
        List<Node> xContainingNodes = new ArrayList<>();
        forEach(node -> {
            if (node.isPointContainedBetweenNode(pos.x)) xContainingNodes.add(node);
        });

        Node closestNode = null;
        float closestNodeDist = 1000000;
        if (xContainingNodes.size() > 0) {
            closestNode = xContainingNodes.get(0);
            for (Node node : xContainingNodes) {
                if (pos.y > node.y && Math.abs(pos.y - node.y) < closestNodeDist) {
                    closestNode = node;
                    closestNodeDist = Math.abs(pos.y - node.y);
                }
            }
        }
        return closestNode;
    }

    @Override
    public void draw(final ShapeRenderer shapeRenderer) {
        forEach(node -> {
            if (node.debugSelected) shapeRenderer.setColor(Color.RED);
            else shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.rectLine(node.left, node.y, node.right, node.y, 5);
//            shapeRenderer.circle(node.left, node.y, 5);
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.rect(node.right, node.y, 5, 5);
        });
    }

    public static Nodes generateNodesFromLevel(Level level) {
        Nodes nodes = new Nodes();

        Set<TileObject> visited = new HashSet<>();

        for (int col = 0; col < level.gridObjects.length; col++) {
            for (int row = 0; row < level.gridObjects[row].length; row++) {
                TileObject current = level.gridObjects[col][row];
                if (current != null && !visited.contains(current) && (current.collideNValue & Direction.UP) == 0) {
                    visited.add(current);
                    float y = (row + level.gridOffset.y) * level.tileSize + level.tileSize;
                    float left = (col + level.gridOffset.x) * level.tileSize;
                    float right = Integer.MIN_VALUE;

                    for (int subCol = col + 1; subCol < level.gridObjects.length; subCol++) {
                        TileObject next = level.gridObjects[subCol][row];
                        if (next != null && !visited.contains(next) && (next.collideNValue & Direction.UP) == 0) {
                            visited.add(next);
                            continue;
                        }
                        if (next != null) visited.add(next);
                        right = (subCol + level.gridOffset.x) * level.tileSize;
                        break;
                    }
                    if (right == Integer.MIN_VALUE) {
                        right = left + level.tileSize;
                    }
                    nodes.add(new Node(left, right, y, false));
                }
                if (current != null)
                    visited.add(current);
            }
        }

        return nodes;
    }
}
