package com.bitdecay.ludum.dare.actors.ai.behaviors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.ludum.dare.actors.ai.Enemy;
import com.bitdecay.ludum.dare.interfaces.IState;

public class RoamBehavior implements IState {

    private Vector2 home;
    private float roamDistance;
    private Vector2 randomPos;
    private Enemy ai;

    public RoamBehavior(Enemy ai, Vector2 home, float roamDistance){
        this.home = home;
        this.roamDistance = roamDistance;
        this.ai = ai;
    }

    @Override
    public void enter() {
        resetRandomPosition();
        ai.setAiMovementGoal(randomPos.x, randomPos.y);
    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        return null;
    }

    private void resetRandomPosition(){
        randomPos = new Vector2(MathUtils.random(), MathUtils.random()).nor().scl(roamDistance * MathUtils.random()).add(home);
    }
}
