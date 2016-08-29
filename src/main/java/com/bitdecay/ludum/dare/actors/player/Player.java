package com.bitdecay.ludum.dare.actors.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.jump.control.PlayerInputController;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.JumperProperties;
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.actors.StateMachine;
import com.bitdecay.ludum.dare.actors.state.ShootState;
import com.bitdecay.ludum.dare.actors.state.StandState;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.components.player.PlayerAnimationComponent;
import com.bitdecay.ludum.dare.components.ship.ShipPartComponent;
import com.bitdecay.ludum.dare.control.InputAction;
import com.bitdecay.ludum.dare.interfaces.IComponent;

public class Player extends StateMachine {
    private static final float MAX_HEALTH = 113;

    private static final float DEATH_Y = -1500;

    private static final int MAX_VOLUNTARY_SPEED = 150;
    private static final int MAX_VOLUNTARY_SPEED_CARRY = 100;

    private static final int JUMP_STRENGTH = 150;
    private static final int JUMP_STRENGTH_CARRY = 80;

    private final AnimationComponent animNormal;
    private final AnimationComponent animCarry;
    private final AnimationComponent animShoot;

    private final SizeComponent size;
    private final PositionComponent pos;
    private final HealthComponent health;
    private final AttackComponent attack;
    private final JetPackComponent jetpack;
    private final PhysicsComponent phys;
    private final KeyboardControlComponent keyboard;
    private final TimerComponent timer;

    private double invincibleTimer;
    private double shootTimer;

    private LevelInteractionComponent levelComponent;
    private float shootAgain = 0;

    public Player() {
        size = new SizeComponent(100, 100);
        pos = new PositionComponent(0, 0);
        health = new HealthComponent(MAX_HEALTH, MAX_HEALTH);

        animNormal = new PlayerAnimationComponent(pos, PlayerAnimationComponent.AnimType.NORMAL);
        animCarry = new PlayerAnimationComponent(pos, PlayerAnimationComponent.AnimType.CARRY);
        animShoot = new PlayerAnimationComponent(pos, PlayerAnimationComponent.AnimType.SHOOT);

        attack = new AttackComponent(10);

        phys = createBody();
        setCarryPhysics(false);

        jetpack = new JetPackComponent((JumperBody) phys.getBody());

        keyboard = new KeyboardControlComponent();

        phys.getBody().controller = new PlayerInputController(keyboard);

        timer = new TimerComponent(0.5f);

        append(size).append(pos).append(phys).append(health).append(jetpack).append(animNormal).append(keyboard).append(timer).append(new FollowComponent());
        setActiveState(new StandState(components));
    }

    private PhysicsComponent createBody() {
        JumperBody body = new JumperBody();
        body.props.deceleration = 10000;
        body.props.acceleration = 1000;
        body.props.airAcceleration = 700;
        body.jumperProps = new JumperProperties();
        body.jumperProps.jumpStrength = JUMP_STRENGTH;
        body.jumperProps.jumpCount = Integer.MAX_VALUE;
        body.jumperProps.jumpVariableHeightWindow = Float.POSITIVE_INFINITY;
        body.bodyType = BodyType.DYNAMIC;
        body.aabb.set(new BitRectangle(0, 0, 13, 18));
        body.userObject = this;
        body.renderStateWatcher = new JumperRenderStateWatcher();
        body.jumperProps.jumpHittingHeadStopsJump = false;

        return new PhysicsComponent(body, pos, size);
    }

    // TODO This will be expensive to do dynamically due to getFirstComponent, maybe optimize later.
    private void updateAnimationComponent() {
        IComponent currentAnim = getFirstComponent(AnimationComponent.class);
        IComponent shipPart = getShipPart();

        // Switch to carry animation set.
        if (shipPart != null && currentAnim != animCarry) {
            System.out.println("entering carry anim");
            remove(AnimationComponent.class);
            append(animCarry);
            setCarryPhysics(true);
        // Switch to normal animation set.
        } else if (shipPart == null) {
            if(shootTimer > 0) {
                if (currentAnim != animShoot) {
                    System.out.println("entering shoot anim");
                    remove(AnimationComponent.class);
                    append(animShoot);
                    setCarryPhysics(false);
                }
            }else if (currentAnim != animNormal) {
                System.out.println("entering normal anim");
                remove(AnimationComponent.class);
                append(animNormal);
                setCarryPhysics(false);
            }
        }
    }

    private void setCarryPhysics(boolean carry) {
        JumperBody body = ((JumperBody) phys.getBody());

        body.props.maxVoluntarySpeed = carry ? MAX_VOLUNTARY_SPEED_CARRY : MAX_VOLUNTARY_SPEED;
        body.jumperProps.jumpStrength = carry ? JUMP_STRENGTH_CARRY : JUMP_STRENGTH;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        updateAnimationComponent();

        shootAgain += delta;
        if (keyboard.isJustPressed(InputAction.SHOOT) && shootAgain > .5){
            shootAgain = 0;
            resetShootTimer();
            setActiveState(new ShootState(components));
        }

        if (timer.complete() &&
            keyboard.isJustPressed(PlayerAction.DOWN) &&
            hasShipPart()) {
            getShipPart().removeFromPlayer(false);
            timer.reset();
        }

        // Reset if player falls or dies.
        if (pos.y < DEATH_Y) {
            setPosition(0, 0);
        } else if (health.health <= 0) {
            health.health = health.max;
            setPosition(0, 0);
        }

        invincibleTimer -= delta;
        shootTimer -= delta;
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
        if(invincibleTimer <= 0) {
            this.health.health -= attackComponent.attack;
            this.animNormal.animator.switchToAnimation("hurt");
            resetInvincibility();
        }
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

    private void resetInvincibility(){
        invincibleTimer = .5;
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

    public void resetShootTimer(){
        shootTimer = 0.5;
    }


    public TimerComponent getTimer() {
        return timer;
    }

    public HealthComponent getHealth() {
        return health;
    }
}
