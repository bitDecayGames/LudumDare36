package com.bitdecay.ludum.dare.actors.ai.movement;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.geom.BitPointInt;

public class AiNode {
    public Vector2 pos;
    public BitPointInt index;
    public AiNodeType nodeType;
    public AiNode(Vector2 pos, BitPointInt index, AiNodeType nodeType){
        if (pos == null) throw new RuntimeException("position cannot be null");
        if (index == null) throw new RuntimeException("index cannot be null");
        if (nodeType == null) throw new RuntimeException("nodeType cannot be null");
        this.pos = pos;
        this.index = index;
        this.nodeType = nodeType;
    }
}


