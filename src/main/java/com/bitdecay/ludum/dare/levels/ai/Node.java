package com.bitdecay.ludum.dare.levels.ai;

import com.badlogic.gdx.math.Vector2;

@Deprecated
public class Node {
    private static final float Y_FUZZY = 8;
    private static final float X_FUZZY = 16;

    public final float y;
    public final float left;
    public final float right;
    public final float length;
    public final boolean oneWay;
    private final float yMin;
    private final float yMax;

    public boolean debugSelected = false;

    public Node(float left, float right, float y, boolean oneWay) {
        this.left = left;
        this.right = right;
        this.y = y;
        this.length = right - left;
        this.yMin = y - Y_FUZZY;
        this.yMax = y + Y_FUZZY;
        this.oneWay = oneWay;
    }

    public float whereIsPointInNode(float x) {
        return (x - left) / length;
    }

    public boolean isPointContainedBetweenNode(float x) {
        float where = whereIsPointInNode(x);
        return where > 0 && where < 1;
    }

    public boolean isPointInNode(Vector2 point) {
        return isPointContainedBetweenNode(point.x) && point.y > yMin && point.y < yMax;
    }

    public boolean equalsRight(float x) {
        return x <= right && x > right - X_FUZZY;
    }

    public boolean equalsLeft(float x) {
        return x < left + X_FUZZY && x >= left;
    }
}
