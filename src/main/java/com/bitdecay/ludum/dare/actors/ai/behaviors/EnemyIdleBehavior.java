package com.bitdecay.ludum.dare.actors.ai.behaviors;

import com.badlogic.gdx.math.MathUtils;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bytebreakstudios.animagic.animation.Animator;

import java.util.List;

public class EnemyIdleBehavior implements IState {

    private Animator animator;
    private List<String> idles;

    public RoamBehavior roamBehavior;

    private float timer;

    public EnemyIdleBehavior(Animator animator, List<String> idles){
        this.animator = animator;
        this.idles = idles;
    }

    @Override
    public void enter() {
        switchToRandomIdle();
        resetTimer();
    }

    @Override
    public void exit() {

    }

    @Override
    public IState update(float delta) {
        timer -= delta;
        if (timer <= 0){
            if (MathUtils.random(0, 3) == 0)
                return roamBehavior;
            switchToRandomIdle();
            resetTimer();
        }
        return null;
    }

    public void switchToRandomIdle(){
        animator.switchToAnimation(idles.get(MathUtils.random(0, idles.size() - 1)));
    }

    public void resetTimer(){
        timer = MathUtils.random(3, 8);
    }
}
