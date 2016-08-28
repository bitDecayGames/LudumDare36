package com.bitdecay.ludum.dare.actors.player;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.PlayerInputController;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.JumperProperties;
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.actors.StateMachine;
import com.bitdecay.ludum.dare.actors.state.HurtState;

import com.bitdecay.ludum.dare.actors.state.ShootState;
import com.bitdecay.ludum.dare.actors.state.StandState;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IComponent;

public class Player extends StateMachine {
    private final SizeComponent size;
    private final PositionComponent pos;
    private final HealthComponent health;
    private final AnimationComponent anim;
    private final AttackComponent attack;
    private final JetPackComponent jetpack;
    private final PhysicsComponent phys;
    private final KeyboardControlComponent keyboard;

    private LevelInteractionComponent levelComponent;

    public Player() {
        size = new SizeComponent(100, 100);
        pos = new PositionComponent(0, 0);
        health = new HealthComponent(10, 10);
        anim = new PlayerAnimationComponent(pos);

        attack = new AttackComponent(10);

        phys = createBody();
        jetpack = new JetPackComponent((JumperBody) phys.getBody());

        keyboard = new KeyboardControlComponent();

        phys.getBody().controller = new PlayerInputController(keyboard);

        append(size).append(pos).append(phys).append(health).append(jetpack).append(anim).append(keyboard);
        setActiveState(new StandState(components));
    }

    private PhysicsComponent createBody() {
        JumperBody body = new JumperBody();
        body.props.deceleration = 10000;
        body.jumperProps = new JumperProperties();
        body.jumperProps.jumpCount = Integer.MAX_VALUE;
        body.jumperProps.jumpVariableHeightWindow = Float.POSITIVE_INFINITY;
        body.bodyType = BodyType.DYNAMIC;
        body.aabb.set(new BitRectangle(0, 0, 13, 18));
        body.userObject = this;
        body.renderStateWatcher = new JumperRenderStateWatcher();

        return new PhysicsComponent(body, pos, size);
    }

    @Override
    public void update(float delta) {
        // Reset for now
        // TODO do this somewhere else?
//        if (pos.y < -1000) {
//            setPosition(0, 0);
//        }

//        checkForStateSwitch();

        super.update(delta);
        if (keyboard.isJustPressed(InputAction.SHOOT)){
            setActiveState(new ShootState(components));
        }
    }

    public void hit(AttackComponent attackComponent) {
        setActiveState(new HurtState(components, attackComponent));
    }

    public void setPosition(float x, float y) {
        phys.getBody().velocity.set(0, 0);
        phys.getBody().aabb.xy.set(x, y);
    }

    //TODO change how controls are gotten
    public InputComponent getInputComponent() {
        IComponent input = getFirstComponent(InputComponent.class);
        if (input != null) {
            return (InputComponent) input;
        } else {
            return null;
        }
    }

    public Vector2 getPosition() {
        return new Vector2(pos.x, pos.y);
    }

    public void addToScreen(LevelInteractionComponent levelComp) {
        // Remove any existing level components.
        remove(LevelInteractionComponent.class);

        levelComponent = levelComp;
        append(levelComponent);

        levelComponent.addToLevel(this, phys);
    }



    public void draw(ShapeRenderer shapeRenderer) {
//        super.draw(shapeRenderer);
//        shapeRenderer.setColor(Color.GOLD);
//        shapeRenderer.rect(pos.x, pos.y, size.w, size.h);
    }

    public JetPackComponent getJetpack(){
        return jetpack;
    }
}
