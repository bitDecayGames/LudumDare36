package com.bitdecay.ludum.dare.actors.ai;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.Facing;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.control.PlayerInputController;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.JumperProperties;
import com.bitdecay.jump.render.JumperRenderState;
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.actors.StateMachine;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.Animator;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class Monkey extends StateMachine {
    public static final float SCALE = 0.5f;

    private final float SIZE = 8;
    private final int WALKING_SPEED = 20;
    private final int FLYING_SPEED = 60;

    private final SizeComponent size;
    private final PositionComponent pos;
    private final HealthComponent health;
    private final AnimationComponent anim;
    private final PhysicsComponent phys;
    private final AIControlComponent input;

    private LevelInteractionComponent levelComponent;

    public Monkey(float startX, float startY) {
        size = new SizeComponent(100, 100);
        pos = new PositionComponent(startX, startY);
        health = new HealthComponent(10, 10);
        anim = new AnimationComponent("monkey", pos, SCALE, new Vector2());
        setupAnimation(anim.animator);
        phys = createBody();
        input = new AIControlComponent();

        phys.getBody().controller = new PlayerInputController(input);

        append(size).append(pos).append(phys).append(health).append(anim);
    }

    private PhysicsComponent createBody() {
        JumperBody body = new JumperBody();
        body.props.deceleration = 10000;
        body.props.maxVoluntarySpeed = 20;
        body.jumperProps = new JumperProperties();
        body.jumperProps.jumpCount = 1;
        body.jumperProps.jumpVariableHeightWindow = 32;
        body.renderStateWatcher = new JumperRenderStateWatcher();
        body.bodyType = BodyType.DYNAMIC;
        body.aabb.set(new BitRectangle(pos.x, pos.y, SIZE, SIZE));
        body.userObject = this;

        setupAnimation(anim.animator);
        return new PhysicsComponent(body, pos, size);
    }

    private void setupAnimation(Animator a) {
        a.addAnimation(new Animation("walk", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("monkey/walk").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("monkey/stand").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("jump", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.2f), atlas.findRegions("monkey/jump").toArray(AnimagicTextureRegion.class)));
        a.switchToAnimation("stand");
    }

    public void addToScreen(LevelInteractionComponent levelComp) {
        // Remove any existing level components.
        remove(LevelInteractionComponent.class);

        levelComponent = levelComp;
        append(levelComponent);

        levelComponent.addToLevel(this, phys);
    }

    @Override
    public void update(float delta) {
        input.update(delta);
        super.update(delta);

        if (isGrounded()) phys.getBody().props.maxVoluntarySpeed = WALKING_SPEED;
        else phys.getBody().props.maxVoluntarySpeed = FLYING_SPEED;

        updateFacing();

        switch ((JumperRenderState)(phys.getBody().renderStateWatcher.getState())) {

            case RIGHT_STANDING:
            case LEFT_STANDING:
                if (anim.animator.currentAnimationName() != "stand") {
                    anim.animator.switchToAnimation("stand");
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

    public void debugDraw(ShapeRenderer renderer){
        if (activeState instanceof AiMoveState) ((AiMoveState)this.activeState).debugDraw(renderer);
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
        return new Vector2(pos.x + SIZE / 2, pos.y + SIZE / 2);
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

    public void debugMonkeyAi(float x, float y){
        setActiveState(new AiIdleState(input));
        setActiveState(new AiMoveState(this, input, new Vector2(x, y)));
    }

    public Vector2 debugIndexToPos(int x, int y){
        return ((AiMoveState)this.activeState).indexToPos(new BitPointInt(x,y));
    }

    public BitPointInt debugPosToIndex(float x, float y){
        return ((AiMoveState)this.activeState).posToIndex(new Vector2(x,y));
    }
}
