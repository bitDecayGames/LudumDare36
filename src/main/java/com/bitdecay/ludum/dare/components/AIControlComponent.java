package com.bitdecay.ludum.dare.components;

import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.ludum.dare.actors.ai.AIMoveState;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.gameobject.AINodeGameObject;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bitdecay.ludum.dare.util.Players;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO Mike implement for AI
public class AIControlComponent extends InputComponent {
    Player me;
    List<AINodeGameObject> nodes = null;

    IState activeState;

    Set<InputAction> currentActions = new HashSet<>();
    Set<InputAction> previousActions = new HashSet<>();

    public AIControlComponent() {
        super();
    }

    public void discoverMe() {
        for (int i = 0; i < Players.list().size(); i++) {
            if (Players.list().get(i).getInputComponent() == this) {
                me = Players.list().get(i);
            }
        }
    }

    @Override
    public boolean isPressed(PlayerAction action) {
        InputAction act = InputAction.forPlayerAction(action);
        return action != null && inControl && currentActions.contains(act);
    }

    public void pressed(PlayerAction action) {
        currentActions.add(InputAction.forPlayerAction(action));
        previousActions.add(InputAction.forPlayerAction(action));
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isJustPressed(PlayerAction action) {
        InputAction act = InputAction.forPlayerAction(action);
        return action != null && inControl && currentActions.contains(act) && !previousActions.contains(act);
    }

    public void justPressed(PlayerAction action) {
        currentActions.add(InputAction.forPlayerAction(action));
        previousActions.remove(InputAction.forPlayerAction(action));
    }

    @Override
    public boolean isPressed(InputAction action) {
        return action != null && inControl && currentActions.contains(action);
    }

    public void pressed(InputAction action) {
        currentActions.add(action);
        previousActions.add(action);
    }

    @Override
    public boolean isJustPressed(InputAction action) {
        return action != null && inControl && currentActions.contains(action) && !previousActions.contains(action);
    }

    public void justPressed(InputAction action) {
        currentActions.add(action);
        previousActions.remove(action);
    }

    @Override
    public void update(float delta) {
        previousActions.addAll(currentActions);
        currentActions.clear();
        if (me == null && Players.isInitialized() && nodes != null) discoverMe();
        else if (me != null && activeState != null) {
            IState newState = activeState.update(delta);
            if (newState != null) {
                activeState.exit();
                newState.enter();
                activeState = newState;
            }
        }
    }

    public void setAINodes(List<AINodeGameObject> nodes) {
        this.nodes = nodes;

        activeState = new AIMoveState(me, this, this.nodes);
        activeState.enter();
    }
}
