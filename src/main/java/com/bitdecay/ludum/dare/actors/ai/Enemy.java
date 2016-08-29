package com.bitdecay.ludum.dare.actors.ai;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.Facing;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.collision.ContactListener;
import com.bitdecay.jump.control.PlayerInputController;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.JumperProperties;
import com.bitdecay.jump.render.JumperRenderState;
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.actors.StateMachine;
import com.bitdecay.ludum.dare.actors.ai.behaviors.AttackBehavior;
import com.bitdecay.ludum.dare.actors.ai.behaviors.EnemyIdleBehavior;
import com.bitdecay.ludum.dare.actors.ai.behaviors.FrustratedBehavior;
import com.bitdecay.ludum.dare.actors.ai.behaviors.RoamBehavior;
import com.bitdecay.ludum.dare.actors.ai.movement.AiIdleState;
import com.bitdecay.ludum.dare.actors.ai.movement.AiMoveState;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.actors.projectile.Projectile;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IShapeDraw;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animator;

import java.util.List;
import java.util.Optional;

public abstract class Enemy extends StateMachine implements IShapeDraw, ContactListener, IRemoveable {
    protected abstract String NAME();
    protected abstract float SCALE();
    protected abstract float SIZE();
    protected abstract int WALKING_SPEED();
    protected abstract int ATTACK_SPEED();
    protected abstract int FLYING_SPEED();
    protected abstract float AGRO_RANGE();
    protected abstract float ATTACK_RANGE();
    protected abstract float START_HEALTH();
    protected abstract float MAX_HEALTH();
    protected abstract float JUMP_HEIGHT();
    protected abstract int ATTACK_STRENGTH();
    protected abstract String HURT_SFX();
    protected abstract String DEATH_SFX();

    protected final SizeComponent size;
    protected final PositionComponent pos;
    protected final HealthComponent health;
    protected final AnimationComponent anim;
    protected final PhysicsComponent phys;
    protected final AIControlComponent input;
    protected final AttackComponent attack;
    private Boolean shouldRemove = false;

    protected LevelInteractionComponent levelComponent;

    protected Player player;

    protected StateMachine behavior;
    protected EnemyIdleBehavior idleBehavior;
    protected RoamBehavior roamBehavior;

    public Enemy(float startX, float startY, Player player) {
        this.player = player;
        size = new SizeComponent(100, 100);
        pos = new PositionComponent(startX, startY);
        health = new HealthComponent(START_HEALTH(), MAX_HEALTH());
        attack = new AttackComponent(ATTACK_STRENGTH());
        anim = new AnimationComponent(NAME(), pos, SCALE(), new Vector2(0, -3));
        setupAnimation(anim.animator);
        phys = createBody();
        input = new AIControlComponent();

        phys.getBody().controller = new PlayerInputController(input);

        append(size).append(pos).append(phys).append(health).append(anim);

        behavior = new StateMachine();
    }

    protected abstract List<String> getIdleAnimations();

    private PhysicsComponent createBody() {
        JumperBody body = new JumperBody();
        body.props.deceleration = 10000;
        body.props.maxVoluntarySpeed = 20;
        body.jumperProps = new JumperProperties();
        body.jumperProps.jumpCount = 1;
        body.jumperProps.jumpVariableHeightWindow = JUMP_HEIGHT();
        body.renderStateWatcher = new JumperRenderStateWatcher();
        body.bodyType = BodyType.DYNAMIC;
        body.aabb.set(new BitRectangle(pos.x, pos.y, SIZE(), SIZE()));
        body.userObject = this;
        body.addContactListener(this);

        setupAnimation(anim.animator);
        return new PhysicsComponent(body, pos, size);
    }

    protected abstract void setupAnimation(Animator a);

    public void addToScreen(LevelInteractionComponent levelComp) {
        // Remove any existing level components.
        remove(LevelInteractionComponent.class);

        levelComponent = levelComp;
        append(levelComponent);

        levelComponent.addToLevel(this, phys);

        setUpBehaviors();
    }

    protected abstract void setUpBehaviors();

    @Override
    public void update(float delta) {
        input.update(delta);
        super.update(delta);
        behavior.update(delta);

        if (!(behavior.getActiveState() instanceof AttackBehavior) && getPosition().dst(player.getPosition()) < AGRO_RANGE()){
            goAgro();
        }
        if (isGrounded()) setSpeed(WALKING_SPEED());
        else setSpeed(FLYING_SPEED());

        if (activeState instanceof AiMoveState){
            if (behavior.getActiveState() instanceof AttackBehavior) setSpeed(ATTACK_SPEED());
        }

        Optional<AgroCooldownComponent> cooldown = findComponent(AgroCooldownComponent.class);
        if (cooldown.isPresent() && cooldown.get().shouldRemove()) remove(AgroCooldownComponent.class);

        updateFacing();

        switch ((JumperRenderState)(phys.getBody().renderStateWatcher.getState())) {

            case RIGHT_STANDING:
            case LEFT_STANDING:
                if (activeState instanceof AiIdleState) {
                    if (behavior.getActiveState() instanceof AttackBehavior){
                        if (getPosition().dst(player.getPosition()) > ATTACK_RANGE() && ((AiIdleState) activeState).wasMovementBlocked()) goFrustrated();
                    } else if (!(behavior.getActiveState() instanceof FrustratedBehavior)) goIdle();
                }
                break;
            case RIGHT_RUNNING:
            case LEFT_RUNNING:
                if (anim.animator.currentAnimationName() != "walk") {
                    anim.animator.switchToAnimation("walk");
                }
                break;
            case RIGHT_JUMPING:
            case LEFT_JUMPING:
                if (anim.animator.currentAnimationName() != "jump") {
                    anim.animator.switchToAnimation("jump");
                }
                break;
            case RIGHT_APEX:
                break;
            case LEFT_APEX:
                break;
            case RIGHT_FALLING:
                break;
            case LEFT_FALLING:
                break;
            case RIGHT_AIR_AGAINST_WALL:
                break;
            case LEFT_AIR_AGAINST_WALL:
                break;
            case RIGHT_GROUNDED_AGAINST_WALL:
                break;
            case LEFT_GROUNDED_AGAINST_WALL:
                break;
            case RIGHT_PUSHED:
                break;
            case LEFT_PUSHED:
                break;
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

    public Vector2 getCenter() {
        return new Vector2(pos.x + SIZE() / 2, pos.y + SIZE() / 2);
    }

    public BitWorld getWorld(){
        return this.levelComponent.getWorld();
    }

    public boolean isGrounded() { return phys.getBody().grounded; }

    private void updateFacing() {
        Facing facing = phys.getBody().facing;
        switch (facing) {
            case LEFT:
                anim.setFlipVerticalAxis(false);
                break;
            case RIGHT:
                anim.setFlipVerticalAxis(true);
                break;
            default:
                throw new Error("Invalid facing set");
        }
    }

    private void setSpeed(int speed){
        phys.getBody().props.maxVoluntarySpeed = speed;
    }

    public void setAiMovementGoal(float x, float y){
        setActiveState(new AiIdleState(input, false));
        setActiveState(new AiMoveState(this, input, new Vector2(x, y)));
    }

    public void draw(ShapeRenderer renderer){
        if (activeState instanceof AiMoveState) ((AiMoveState)this.activeState).debugDraw(renderer);
    }

    protected abstract AttackBehavior getAttack();

    protected GameObject getDeath(){
        return new EnemyDeath(anim, pos, DEATH_SFX());
    }

    public void goAgro(){
        behavior.setActiveState(getAttack());
        broadcastAgro();
    }

    public void goIdle(){
        behavior.setActiveState(idleBehavior);
        unBroadcastAgro();
    }

    public void goFrustrated(){
        behavior.setActiveState(new FrustratedBehavior(this, input, roamBehavior));
        unBroadcastAgro();
    }

    public void broadcastAgro(){
        append(new AgroComponent());
    }

    public void unBroadcastAgro(){
        append(new AgroCooldownComponent());
        remove(AgroComponent.class);
    }

    @Override
    public void contactStarted(BitBody bitBody){// Not allowed to hit source.
        if (bitBody.equals(phys.getBody())) {
            return;
        }
        // If we hit another player, set them to their hurt state.
        if (bitBody.userObject instanceof Player) {
            ((Player) bitBody.userObject).hit(attack);
        }
        // TODO Add more logic for damage here if we hit a player.
        if (bitBody.userObject instanceof Projectile) {
            this.health.health -= ((Projectile) bitBody.userObject).getAttack().attack;
            SoundLibrary.playSound(HURT_SFX());
            goAgro();
            if(this.health.health <= 0){
                shouldRemove = true;
            }
        }
    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void remove() {
        // Remove ourselves from the physics world.
        levelComponent.getWorld().removeBody(phys.getBody());
        shouldRemove = true;

        levelComponent.getObjects().add(getDeath());
    }

    @Override
    public void contact(BitBody var1){};

    @Override
    public void contactEnded(BitBody var1){};

    @Override
    public void crushed(){};
}
