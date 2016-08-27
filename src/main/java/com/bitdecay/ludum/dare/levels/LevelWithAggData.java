package com.bitdecay.ludum.dare.levels;

import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.TileObject;

public class LevelWithAggData {

    public Level level;
    public TileObject leftMostTile;
    public TileObject rightMostTile;

    public LevelWithAggData(Level level){
        this.level = level;

        leftMostTile = new TileObject(new BitRectangle(0, 0, 0, 0), false, 0);
        leftMostTile.rect.xy.x = 999999;
        rightMostTile = new TileObject(new BitRectangle(0, 0, 0, 0), false, 0);
        rightMostTile.rect.xy.x = -999999;

        calculateLeftAndRightTilesPerSegment();

    }

    private void calculateLeftAndRightTilesPerSegment(){
        for (TileObject[] ta : level.gridObjects) {
            for (TileObject t : ta) {
                if (t != null) {

                    if (t.rect.xy.x < getLeftMostTileX()) {
                        setLeftMostTile(t);
                    }

                    if (t.rect.xy.x > getRightMostTileX()) {
                        setRightMostTile(t);
                    }
                }
            }
        }
    }

    public float getLeftMostTileX(){
        return leftMostTile.rect.xy.x;
    }

    public float getLeftMostTileY(){ return leftMostTile.rect.xy.y; }

    public float getRightMostTileX(){
        return rightMostTile.rect.xy.x;
    }

    public float getRightMostTileY(){
        return rightMostTile.rect.xy.y;
    }

    public void setLeftMostTile(TileObject t){
        leftMostTile = t;
    }

    public void setRightMostTile(TileObject t){
        rightMostTile = t;
    }
}
