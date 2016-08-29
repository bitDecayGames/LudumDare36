package com.bitdecay.ludum.dare.actors.ai.bat;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.ContactListener;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.JumperProperties;
import com.bitdecay.jump.render.JumperRenderStateWatcher;
import com.bitdecay.ludum.dare.actors.StateMachine;
import com.bitdecay.ludum.dare.actors.ai.EnemyDeath;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.actors.projectile.Projectile;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.interfaces.IAgroable;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bitdecay.ludum.dare.interfaces.IShapeDraw;
import com.bitdecay.ludum.dare.util.SoundLibrary;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.Animator;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

import static com.bitdecay.ludum.dare.LudumDareGame.atlas;

public class Bat extends StateMachine implements IShapeDraw, ContactListener, IRemoveable, IAgroable {

    public static final float SCALE = 0.35f;

    private PositionComponent pos;
    private AnimationComponent anim;
    private HealthComponent health;
    private SizeComponent size;
    private AttackComponent attack;
    private AIControlComponent input;
    private PhysicsComponent phys;
    private LevelInteractionComponent levelComponent;

    private Player player;
    private boolean shouldRemove = false;
    private Vector2 startPosition;

    private String NAME(){
        return "BAT";
    }

    protected float SCALE() {
        return Bat.SCALE;
    }

    protected float SIZE() {
        return 5;
    }

    private float START_HEALTH(){
        return 5;
    }

    private float MAX_HEALTH(){
        return 5;
    }

    private int ATTACK_STRENGTH(){
        return 5;
    }

    private float AGGRO_RANGE(){
        return 100;
    }

    private String HURT_SFX(){
        return "MonkeyHurt";
    }

    private String DEATH_SFX(){
        return "MonkeyVaporize";
    }

    public Bat(float startX, float startY, Player player){
        startPosition = new Vector2(startX, startY);
        this.player = player;
        size = new SizeComponent(100, 100);
        pos = new PositionComponent(startX, startY);
        health = new HealthComponent(START_HEALTH(), MAX_HEALTH());
        attack = new AttackComponent(ATTACK_STRENGTH());
        anim = new AnimationComponent(NAME(), pos, SCALE(), new Vector2(0, -3));
        setupAnimation(anim.animator);
        phys = createBody();

        append(size).append(pos).append(phys).append(health).append(anim);
    }

    private PhysicsComponent createBody() {
        JumperBody body = new JumperBody();
        body.props.deceleration = 10000;
        body.props.maxVoluntarySpeed = 20;
        body.props.gravitational = false;
        body.jumperProps = new JumperProperties();
        body.jumperProps.jumpCount = 0;
        body.renderStateWatcher = new JumperRenderStateWatcher();
        body.bodyType = BodyType.DYNAMIC;
        body.aabb.set(new BitRectangle(pos.x, pos.y, SIZE(), SIZE()));
        body.userObject = this;
        body.addContactListener(this);

        setupAnimation(anim.animator);
        return new PhysicsComponent(body, pos, size);
    }

    protected void setupAnimation(Animator a){
        a.addAnimation(new Animation("fly", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.1f), atlas.findRegions("bat/fly").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("hang", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(0.2f), atlas.findRegions("bat/hang").toArray(AnimagicTextureRegion.class)));
        a.addAnimation(new Animation("death", Animation.AnimationPlayState.ONCE, FrameRate.perFrame(0.2f), atlas.findRegions("bat/death").toArray(AnimagicTextureRegion.class)));
        a.switchToAnimation("hang");
    }

    public void addToScreen(LevelInteractionComponent levelComp) {
        // Remove any existing level components.
        remove(LevelInteractionComponent.class);

        levelComponent = levelComp;
        append(levelComponent);

        levelComponent.addToLevel(this, phys);

        setupBehaviors();
    }

    public void setupBehaviors(){
        setActiveState(new BatHang(this, startPosition, phys, anim, player, AGGRO_RANGE()));
    }

    @Override
    public void goAgro(){
        if (activeState instanceof IAgroable) ((IAgroable) activeState).goAgro();
        broadcastAgro();
    }

    public void broadcastAgro(){
        append(new AgroComponent(AGGRO_RANGE()));
    }

    public void unBroadcastAgro(){
        append(new AgroCooldownComponent());
        remove(AgroComponent.class);
    }

    public Vector2 getPosition() {
        return new Vector2(pos.x, pos.y);
    }

    @Override
    public void update(float delta){
        super.update(delta);
        if (phys.getBody().velocity.x < 0){
            // facing left
            anim.setFlipVerticalAxis(false);
        } else if (phys.getBody().velocity.x > 0){
            // facing right
            anim.setFlipVerticalAxis(true);
        }

        if ((activeState instanceof BatFlyHome || activeState instanceof BatHang) && hasComponent(AgroComponent.class)) unBroadcastAgro();
    }

    @Override
    public void contactStarted(BitBody bitBody) {
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
    public void contact(BitBody bitBody) {}

    @Override
    public void contactEnded(BitBody bitBody) {}

    @Override
    public void crushed() {}

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    @Override
    public void remove() {
        // Remove ourselves from the physics world.
        levelComponent.getWorld().removeBody(phys.getBody());
        shouldRemove = true;

        levelComponent.getObjects().add(new EnemyDeath(anim, pos, DEATH_SFX()));
    }
}
