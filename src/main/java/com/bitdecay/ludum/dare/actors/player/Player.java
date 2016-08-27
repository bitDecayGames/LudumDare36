package com.bitdecay.ludum.dare.actors.player;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.ControlMap;
import com.bitdecay.jump.control.PlayerInputController;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.JumperProperties;
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.actors.StateMachine;
import com.bitdecay.ludum.dare.actors.items.PowerUpUtil;
import com.bitdecay.ludum.dare.actors.state.HurtState;
import com.bitdecay.ludum.dare.actors.state.ProjectileState;
import com.bitdecay.ludum.dare.actors.state.PunchState;
import com.bitdecay.ludum.dare.actors.state.StandState;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.components.PowerDownComponents.SlowComponent;
import com.bitdecay.ludum.dare.components.PowerDownComponents.StunComponent;
import com.bitdecay.ludum.dare.components.PowerUpComponents.TempFlyComponent;
import com.bitdecay.ludum.dare.components.PowerUpComponents.TempSpeedComponent;
import com.bitdecay.ludum.dare.components.upgradeComponents.*;
import com.bitdecay.ludum.dare.interfaces.IComponent;
import com.bitdecay.ludum.dare.interfaces.IState;
import com.bitdecay.ludum.dare.util.Players;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.Animator;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

public class Player extends StateMachine {
    private final SizeComponent size;
    private final PositionComponent pos;
    private final PhysicsComponent phys;
    private final HealthComponent health;
    private final AnimationComponent anim;
    private final PlayerCurrencyComponent wallet;
    private final AttackComponent attack;
    private final LightComponent light;

    private LevelInteractionComponent levelComponent;
    private final int playerNum;

    public boolean winner;

    public Player(int playerNum) {
        this.playerNum = playerNum;
        size = new SizeComponent(100, 100);
        pos = new PositionComponent(0, 0);
        health = new HealthComponent(10, 10);
        anim = new AnimationComponent("player", pos, 1f, new Vector2(8, -5));
        wallet = new PlayerCurrencyComponent();
        light = new LightComponent(pos, new Vector2(8, 16));
        light.setzAxis(0.1f);
        light.setAttenuation(1);
        setupAnimation(anim.animator);

        attack = new AttackComponent(10);

        phys = createBody();
        append(size).append(pos).append(phys).append(health).append(anim).append(light); // TODO: trying without the light on the players
    }

    private PhysicsComponent createBody() {
        JumperBody body = new JumperBody();
        body.props.deceleration = 10000;
        body.jumperProps = new JumperProperties();
        body.jumperProps.jumpCount = 1;
        body.renderStateWatcher = new JumperRenderStateWatcher();
        body.bodyType = BodyType.DYNAMIC;
        body.aabb.set(new BitRectangle(0, 0, 16, 32));
        body.userObject = this;

        setupAnimation(anim.animator);
        return new PhysicsComponent(body, pos, size);
    }

    private void setupAnimation(Animator a) {
        AnimagicTextureAtlas atlas = LudumDareGame.assetManager.get("packed/player" + getPlayerNum() + ".atlas", AnimagicTextureAtlas.class);

        a.addAnimation(new Animation("run", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("run").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("jump", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("jump").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("apex", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("apex").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("fall", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("fall").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("knockback", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("knockback").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("punch/front", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/front").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("punch/jumping/down", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/jumping/down").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("punch/jumping/front", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/jumping/front").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("punch/jumping/up", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.1f), atlas.findRegions("punch/jumping/up").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("punch/up", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.05f), atlas.findRegions("punch/up").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("stand", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("stand").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("wall", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("wall").toArray(AnimagicTextureRegion.class)));

        a.switchToAnimation("stand");
    }

    public int getPlayerNum() {
        return playerNum;
    }

    @Override
    public void update(float delta) {
        // Reset for now
        // TODO do this somewhere else?
//        if (pos.y < -1000) {
//            setPosition(0, 0);
//        }

        checkForStateSwitch();

        super.update(delta);
    }

    public void hit(AttackComponent attackComponent) {
        setActiveState(new HurtState(components, attackComponent));
    }

    private void checkForStateSwitch() {
        IState newState = null;
        PunchState punch = new PunchState(components);
        ProjectileState projectile = new ProjectileState(components);
        if (punch.shouldRun(activeState)) {
            newState = punch;
        } else if (projectile.shouldRun(activeState)) {
            newState = projectile;
        }

        if (newState != null) {
            setActiveState(newState);
        }
    }

    public void setPosition(float x, float y) {
        phys.getBody().velocity.set(0, 0);
        phys.getBody().aabb.xy.set(x, y);
    }

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

    public void activateControls() {
        try {
            ControlMap controls = (ControlMap) getFirstComponent(InputComponent.class);
            phys.getBody().controller = new PlayerInputController(controls);
            setActiveState(new StandState(components));
        } catch (Error e) {
            throw new Error("Could not activate player controls");
        }

    }

    public void addUpgrade(Class clazz) {
        if (clazz.equals(DoubleJumpComponent.class)) {
            append(new DoubleJumpComponent(phys));
        } else if (clazz.equals(FloatUpgradeComponent.class)) {
            append(new FloatUpgradeComponent(phys));
        } else if (clazz.equals(MetalComponent.class)) {
            append(new MetalComponent(phys, health, attack));
        } else if (clazz.equals(MysteryBagComponent.class)) {
            append(new MysteryBagComponent(this, phys, health, attack));
        } else if (clazz.equals(SpeedComponent.class)) {
            append(new SpeedComponent(phys));
        } else if (clazz.equals(WallJumpComponent.class)) {
            append(new WallJumpComponent(phys));
        } else if (clazz.equals(EmptyUpgradeComponent.class)) {
            append(new EmptyUpgradeComponent());
        } else if (clazz.equals(FireProjectileComponent.class)) {
            append(new FireProjectileComponent());
        } else if (clazz.equals(IceProjectileComponent.class)) {
            append(new IceProjectileComponent());
        } else if (clazz.equals(WebProjectileComponent.class)) {
            append(new WebProjectileComponent());
        } else if (clazz.equals(PoisonProjectileComponent.class)) {
            append(new PoisonProjectileComponent());
        } else {
            throw new RuntimeException("Could not instantiate " + clazz);
        }
    }

    public void achieveMoney(int amount) {
        this.wallet.getACoin(amount);
    }

    public void spinPowerBlock() {
        append(new PowerupRollerComponent(this, pos));
    }

    public void getPowerBlock(){
        String myPower = PowerUpUtil.randomPowerGenerator(getRank());
        if(myPower == PowerUpUtil.SPEED){
            SoundLibrary.playSound("speed");
            queueAdd(new TempSpeedComponent(phys, pos));
        }else if(myPower == PowerUpUtil.SLOW){
            SoundLibrary.playSound("slowdown");
            for(Player p: Players.list()){
                if(Players.list().indexOf(p) != Players.list().indexOf(this)){
                    p.takeAPowerDown(PowerUpUtil.SLOW);
                }
            }
        }else if(myPower == PowerUpUtil.FORCE_JUMP){
            SoundLibrary.playSound("highJump");
            for(Player p: Players.list()){
                if(Players.list().indexOf(p) != Players.list().indexOf(this)){
                    p.takeAPowerDown(PowerUpUtil.FORCE_JUMP);
                }
            }
        }else if(myPower == PowerUpUtil.FLIGHT){
            SoundLibrary.playSound("flight");
            queueAdd(new TempFlyComponent(phys, pos));
        }else if(myPower == PowerUpUtil.STUN) {
            SoundLibrary.playSound("stun");
            for (Player p : Players.list()) {
                if (Players.list().indexOf(p) != Players.list().indexOf(this)) {
                    p.takeAPowerDown(PowerUpUtil.STUN);
                }
            }
        }
    }

    public void takeAPowerDown(String powerDown){
        if(powerDown == "SLOW"){
            queueAdd(new SlowComponent(phys, pos));
        }else if(powerDown == "FORCE_HIGH_JUMP") {
            if (phys.getBody().grounded) {
                phys.getBody().velocity.y = 500;
            }
        }else if(powerDown == "STUN"){
            queueAdd(new StunComponent(phys, pos));
        }
    }

    public int getRank(){
        return 0;
    }

    public int moneyCount() {
        return this.wallet.currency;
    }

    public void draw(ShapeRenderer shapeRenderer) {
//        super.draw(shapeRenderer);
//        shapeRenderer.setColor(Color.GOLD);
//        shapeRenderer.rect(pos.x, pos.y, size.w, size.h);
    }
}
