package com.bitdecay.ludum.dare.actors.ai.movement;

import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.geom.BitPointInt;

public class AiNodeNeighbors {
    public boolean topLeft;
    public boolean top;
    public boolean topRight;
    public boolean right;
    public boolean bottomRight;
    public boolean bottom;
    public boolean bottomLeft;
    public boolean left;

    private BitPointInt index;
    private BitWorld world;

    public AiNodeNeighbors(int x, int y, BitWorld world){
        this(new BitPointInt(x, y), world);
    }

    public AiNodeNeighbors(BitPointInt index, BitWorld world){
        this.index = index;
        this.world = world;
        int x = index.x;
        int y = index.y;

        topLeft = isBodyAtIndex(x - 1, y + 1);
        top = isBodyAtIndex(x, y + 1);
        topRight = isBodyAtIndex(x + 1, y + 1);
        right = isBodyAtIndex(x + 1, y);
        bottomRight = isBodyAtIndex(x + 1, y - 1);
        bottom = isBodyAtIndex(x, y - 1);
        bottomLeft = isBodyAtIndex(x - 1, y - 1);
        left = isBodyAtIndex(x - 1, y);
    }

    public AiNodeNeighbors topLeftNeighbors(){
        return new AiNodeNeighbors(index.x - 1, index.y + 1, world);
    }
    public AiNodeNeighbors topNeighbors(){
        return new AiNodeNeighbors(index.x, index.y + 1, world);
    }
    public AiNodeNeighbors topRightNeighbors(){
        return new AiNodeNeighbors(index.x + 1, index.y + 1, world);
    }
    public AiNodeNeighbors rightNeighbors(){
        return new AiNodeNeighbors(index.x + 1, index.y, world);
    }
    public AiNodeNeighbors bottomRightNeighbors(){
        return new AiNodeNeighbors(index.x + 1, index.y - 1, world);
    }
    public AiNodeNeighbors bottomNeighbors(){
        return new AiNodeNeighbors(index.x, index.y - 1, world);
    }
    public AiNodeNeighbors bottomLeftNeighbors(){
        return new AiNodeNeighbors(index.x - 1, index.y - 1, world);
    }
    public AiNodeNeighbors leftNeighbors(){
        return new AiNodeNeighbors(index.x - 1, index.y, world);
    }

    private boolean isBodyAtIndex(int x, int y){
        try {
            return world.getGrid()[x][y] != null;
        } catch (Exception e){
            return false;
        }
    }

    public String toString(){
        return s(topLeft) + s(top) + s(topRight) + "\n" +
                s(left) + "O" + s(right) + "\n" +
                s(bottomLeft) + s(bottom) + s(bottomRight);
    }

    private String s(boolean b){return (b ? "X" : "-");}
}


