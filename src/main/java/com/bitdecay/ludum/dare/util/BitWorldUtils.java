package com.bitdecay.ludum.dare.util;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;

public class BitWorldUtils {
    public static BitBody bodyAtIndex(BitWorld world, BitPointInt index){
        try {
            return world.getGrid()[index.x][index.y];
        } catch (Exception e){
            return null;
        }
    }

    public static Vector2 indexToPos(BitWorld world, BitPointInt index){
        BitBody body = bodyAtIndex(world, index);
        if (body == null) {
            BitBody[][] bodies = world.getGrid();
            for (int x = 0; x < bodies.length; x++){
                BitBody[] col = bodies[x];
                if (col != null) for (int y = 0; y < col.length; y++){
                    BitBody bod = col[y];
                    if (bod != null) {
                        BitPoint diff = new BitPoint(index.x, index.y).minus(x, y).scale(world.getTileSize());
                        return new Vector2(bod.aabb.xy.x, bod.aabb.xy.y).add(world.getTileSize() / 2, world.getTileSize() / 2).add(diff.x, diff.y);
                    }
                }
            }
            throw new RuntimeException("This should never never ever happen");
        } else {
            BitPoint p = body.aabb.center();
            return new Vector2(p.x, p.y);
        }
    }

    public static BitPointInt posToIndex(BitWorld world,  Vector2 pos){
        BitBody[][] bodies = world.getGrid();
        for (int x = 0; x < bodies.length; x++){
            BitBody[] col = bodies[x];
            if (col != null) for (int y = 0; y < col.length; y++){
                BitBody body = col[y];
                if (body != null && body.aabb.contains(pos.x, pos.y)) return new BitPointInt(x, y);
                else if (body != null) {
                    BitPoint diff = new BitPoint(body.aabb.center()).minus(pos.x, pos.y).dividedBy(-world.getTileSize());
                    return new BitPointInt(x + Math.round(diff.x), y + Math.round(diff.y));
                }
            }
        }
        throw new RuntimeException("AHHHH This should never happen!!!!");
    }
}
