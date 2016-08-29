package com.bitdecay.ludum.dare.actors.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BooleanArray;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.Facing;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.ContactListener;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.JumperProperties;
import com.bitdecay.ludum.dare.LudumDareGame;
import com.bitdecay.ludum.dare.ResourceDir;
import com.bitdecay.ludum.dare.actors.GameObject;
import com.bitdecay.ludum.dare.actors.player.Player;
import com.bitdecay.ludum.dare.components.*;
import com.bitdecay.ludum.dare.interfaces.IRemoveable;
import com.bytebreakstudios.animagic.animation.Animation;
import com.bytebreakstudios.animagic.animation.Animator;
import com.bytebreakstudios.animagic.animation.FrameRate;
import com.bytebreakstudios.animagic.texture.AnimagicTextureAtlas;
import com.bytebreakstudios.animagic.texture.AnimagicTextureRegion;

public class Projectile extends GameObject implements ContactListener, IRemoveable {
    protected float PROJECTILE_SPEED = 500;
    protected float PROJECTILE_TIME_TO_LIVE = 10;

    private final SizeComponent size;
    private final PositionComponent pos;
    private final PhysicsComponent phys;
    private final PhysicsComponent sourcePhysicsComponent;
    private final AnimationComponent anim;
    private final AttackComponent attackComponent;
    private final LevelInteractionComponent levelComponent;
    private final TimedComponent timedComponent;

    private Boolean shouldRemove = false;
    private Boolean leftFacing = false;

    public Projectile(PositionComponent source, Vector2 direction, LevelInteractionComponent levelComp, PhysicsComponent sourcePhysicsComponent) {
        super();

        this.sourcePhysicsComponent = sourcePhysicsComponent;

        size = new SizeComponent(100, 100);
        pos = new PositionComponent(source.x, source.y);

        attackComponent = new AttackComponent(10);

        phys = createBody(direction);

        if(sourcePhysicsComponent.getBody().facing.equals(Facing.RIGHT)){
            anim = new AnimationComponent(ResourceDir.path("assets/main/bullet"), pos, .5f, new Vector2(2, 0));
            anim.setFlipVerticalAxis(true);
        } else {
            leftFacing = true;
            anim = new AnimationComponent(ResourceDir.path("assets/main/bullet"), pos, .5f, new Vector2(-2, 0));
        }

        setupAnimation(anim.animator);

        levelComponent = levelComp;
        timedComponent = new TimedComponent(PROJECTILE_TIME_TO_LIVE);
        append(size).append(pos).append(phys).append(anim).append(levelComponent).append(timedComponent);
    }

    protected PhysicsComponent createBody(Vector2 direction) {
        JumperBody body = new JumperBody();
        body.jumperProps = new JumperProperties();
        body.bodyType = BodyType.DYNAMIC;

        body.aabb.set(new BitRectangle(pos.x, pos.y + 7, sourcePhysicsComponent.getBody().aabb.width, 8));

        body.velocity.set(PROJECTILE_SPEED * direction.x, PROJECTILE_SPEED * direction.y);
        body.userObject = this;
        body.props.gravitational = false;
        body.addContactListener(this);

        return new PhysicsComponent(body, pos, size);
    }

    protected void setupAnimation(Animator a) {
        AnimagicTextureAtlas atlas = LudumDareGame.atlas;

        a.addAnimation(new Animation("1.png", Animation.AnimationPlayState.REPEAT, FrameRate.perFrame(.1f), atlas.findRegions("bullet/1").toArray(AnimagicTextureRegion.class)));

        a.switchToAnimation("1.png");
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (timedComponent.shouldRemove()) {
            shouldRemove = true;
        }
    }

    public PhysicsComponent getPhysics() {
        return phys;
    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    @Override
    public void remove() {
        // Remove ourselves from the physics world.
        levelComponent.getWorld().removeBody(phys.getBody());
        shouldRemove = true;

        PositionComponent passin;
        if(leftFacing){
            passin = new PositionComponent(pos.x - 14, pos.y - 10);

        } else {
            passin = new PositionComponent(pos.x, pos.y - 10);
        }
        ProjectileExplosion expo = new ProjectileExplosion(passin);

        levelComponent.getObjects().add(expo);
    }

    @Override
    public void contactStarted(BitBody bitBody) {
        // Not allowed to hit source.
        if (bitBody.equals(sourcePhysicsComponent.getBody())) {
            return;
        }
        // If we hit another player, set them to their hurt state.
        if (bitBody.userObject instanceof Player) {
            ((Player) bitBody.userObject).hit(attackComponent);
        }
        // TODO Add more logic for damage here if we hit a player.
        shouldRemove = true;
    }

    public AttackComponent getAttack(){
        return attackComponent;
    }

    @Override
    public void contact(BitBody bitBody) {

    }

    @Override
    public void contactEnded(BitBody bitBody) {

    }

    @Override
    public void crushed() {

    }
}
