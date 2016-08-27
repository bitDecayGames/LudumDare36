package com.bitdecay.ludum.dare.actors.ai;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.geom.BitPointInt;

public class AiNode {
    public Vector2 pos;
    public BitPointInt index;
    public AiNodeType nodeType;
    public AiNode(Vector2 pos, BitPointInt index, AiNodeType nodeType){
        this.pos = pos;
        this.index = index;
        this.nodeType = nodeType;
    }
}


