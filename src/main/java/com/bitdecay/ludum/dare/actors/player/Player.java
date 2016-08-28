package com.bitdecay.ludum.dare.actors.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.bitdecay.ludum.dare.components.player.PlayerAnimationComponent;
import com.bitdecay.ludum.dare.components.ship.ShipPartComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IComponent;

public class Player extends StateMachine {
    private static final int MAX_VOLUNTARY_SPEED = 150;
    private static final int MAX_VOLUNTARY_SPEED_CARRY = 100;

    private static final int JUMP_STRENGTH = 300;
    private static final int JUMP_STRENGTH_CARRY = 100;

    private final AnimationComponent animNormal;
    private final AnimationComponent animCarry;

    private final SizeComponent size;
    private final PositionComponent pos;
    private final HealthComponent health;
    private final AttackComponent attack;
    private final JetPackComponent jetpack;

    private final PhysicsComponent phys;
    private final KeyboardControlComponent keyboard;

    private LevelInteractionComponent levelComponent;
    private float shootAgain = 0;

    public Player() {
        size = new SizeComponent(100, 100);
        pos = new PositionComponent(0, 0);
        health = new HealthComponent(10, 10);

        animNormal = new PlayerAnimationComponent(pos, false);
        animCarry = new PlayerAnimationComponent(pos, true);

        attack = new AttackComponent(10);

        phys = createBody();
        setCarryPhysics(false);

        jetpack = new JetPackComponent((JumperBody) phys.getBody());

        keyboard = new KeyboardControlComponent();

        phys.getBody().controller = new PlayerInputController(keyboard);

        append(size).append(pos).append(phys).append(health).append(jetpack).append(animNormal).append(keyboard);
        setActiveState(new StandState(components));
    }

    private PhysicsComponent createBody() {
        JumperBody body = new JumperBody();
        body.props.deceleration = 10000;
        body.props.acceleration = 1000;
        body.props.airAcceleration = 700;
        body.jumperProps = new JumperProperties();
        body.jumperProps.jumpCount = Integer.MAX_VALUE;
        body.jumperProps.jumpVariableHeightWindow = Float.POSITIVE_INFINITY;
        body.bodyType = BodyType.DYNAMIC;
        body.aabb.set(new BitRectangle(0, 0, 13, 18));
        body.userObject = this;
        body.renderStateWatcher = new JumperRenderStateWatcher();

        return new PhysicsComponent(body, pos, size);
    }

    // TODO This will be expensive to do dynamically due to getFirstComponent, maybe optimize later.
    private void updateAnimationComponent() {
        IComponent currentAnim = getFirstComponent(AnimationComponent.class);
        IComponent shipPart = getShipPart();

        // Switch to carry animation set.
        if (shipPart != null && currentAnim != animCarry) {
            remove(AnimationComponent.class);
            append(animCarry);
            setCarryPhysics(true);
        // Switch to normal animation set.
        } else if (shipPart == null && currentAnim != animNormal) {
            remove(AnimationComponent.class);
            append(animNormal);
            setCarryPhysics(false);
        }
    }

    private void setCarryPhysics(boolean carry) {
        JumperBody body = ((JumperBody) phys.getBody());

        body.props.maxVoluntarySpeed = carry ? MAX_VOLUNTARY_SPEED_CARRY : MAX_VOLUNTARY_SPEED;
        body.jumperProps.jumpStrength = carry ? JUMP_STRENGTH_CARRY : JUMP_STRENGTH;
    }

    @Override
    public void update(float delta) {
        updateAnimationComponent();

        // Reset for now
        // TODO do this somewhere else?
//        if (pos.y < -1000) {
//            setPosition(0, 0);
//        }

//        checkForStateSwitch();
        super.update(delta);

        shootAgain += delta;
        if (keyboard.isJustPressed(InputAction.SHOOT) && shootAgain > .5){
            shootAgain = 0;
            setActiveState(new ShootState(components));
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        ShipPartComponent shipPart = getShipPart();
        AnimationComponent anim = ((AnimationComponent) getFirstComponent(AnimationComponent.class));
        if (shipPart != null && anim != null) {
            shipPart.flipVerticalAxis(!anim.getFlipVerticalAxis());
        }

        super.draw(spriteBatch);
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

    public ShipPartComponent getShipPart() {
        return ((ShipPartComponent) getFirstComponent(ShipPartComponent.class));
    }

    public boolean hasShipPart() {
        return getShipPart() != null;
    }
}
