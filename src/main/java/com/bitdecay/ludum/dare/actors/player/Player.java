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
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.Animator;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

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
        anim = new AnimationComponent("player", pos, 1f, new Vector2());
        setupAnimation(anim.animator);

        attack = new AttackComponent(10);

        phys = createBody();
        jetpack = new JetPackComponent((JumperBody) phys.getBody());

        keyboard = new KeyboardControlComponent();
        phys.getBody().controller = new PlayerInputController(keyboard);

        append(size).append(pos).append(phys).append(health).append(jetpack).append(anim)   ;
    }


    private PhysicsComponent createBody() {
        JumperBody body = new JumperBody();
        body.props.deceleration = 10000;
        body.jumperProps = new JumperProperties();
        body.jumperProps.jumpCount = Integer.MAX_VALUE;
        body.jumperProps.jumpVariableHeightWindow = Float.POSITIVE_INFINITY;
        body.renderStateWatcher = new JumperRenderStateWatcher();
        body.bodyType = BodyType.DYNAMIC;
        body.aabb.set(new BitRectangle(0, 0, 16, 32));
        body.userObject = this;

        setupAnimation(anim.animator);
        return new PhysicsComponent(body, pos, size);
    }


    private void setupAnimation(Animator a) {
//        a.addAnimation(new Animation("run", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("run").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("jump", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("jump").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("apex", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("apex").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("fall", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("fall").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("knockback", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("knockback").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("punch/front", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/front").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("punch/jumping/down", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/jumping/down").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("punch/jumping/front", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/jumping/front").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("punch/jumping/up", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("punch/jumping/up").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("punch/up", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/up").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("player/stand").toArray(AnimagicTextureRegion.class)));
//        a.addAnimation(new Animation("wall", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("wall").toArray(AnimagicTextureRegion.class)));
//
        a.switchToAnimation("stand");
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
    }

    public void hit(AttackComponent attackComponent) {
        setActiveState(new HurtState(components, attackComponent));
    }

    private void checkForStateSwitch() {
        IState newState = null;
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
