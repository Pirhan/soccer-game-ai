package pison.soccergame.game.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import java.awt.geom.Rectangle2D;

import pison.soccergame.game.collision.CollisionHandler;
import pison.soccergame.game.renderable.Renderable;
import pison.soccergame.movement.KinematicInfo;
import pison.soccergame.movement.StaticInfo;
import pison.soccergame.movement.steering.SteeringBehavior;
import pison.soccergame.movement.steering.SteeringInfo;

public class MovingEntity extends BasicGameEntity {
    private double MaxVelocity = 1;
    private double MaxRotation = 0.1;
    private static final double TIME_COEFFICIENT = 0.2;
    public KinematicInfo kinematicInfo;
    public Rectangle2D bounds;

    protected SteeringBehavior steeringBehavior;
    protected CollisionHandler collisionHandler;


    public MovingEntity(Renderable shape, StaticInfo staticInfo, KinematicInfo kinematicInfo, Rectangle2D bounds) {
        super(shape, staticInfo);
        this.kinematicInfo = kinematicInfo;
        this.bounds = bounds;
    }

    protected void setMaxVelocity(double maxVelocity) {
        MaxVelocity = maxVelocity;
    }

    void setMaxRotation(double maxRotation) {
        MaxRotation = maxRotation;
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) {
        super.update(gameContainer, stateBasedGame, i);

        SteeringInfo steeringInfo = (steeringBehavior != null) ?
                steeringBehavior.getSteering(staticInfo, kinematicInfo) : SteeringInfo.getNoSteering();


        kinematicInfo.update(steeringInfo, 1 * TIME_COEFFICIENT, MaxVelocity, MaxRotation);


        staticInfo.update(kinematicInfo, steeringInfo, 1 * TIME_COEFFICIENT);

        if (collisionHandler != null)
            collisionHandler.handle(this);
    }

    public SteeringBehavior getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }


    public void setCollisionHandler(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
    }


    public void setBounds(Rectangle2D bounds) {
        this.bounds = bounds;
    }

    public KinematicInfo getKinematicInfo() {
        return kinematicInfo;
    }
}
