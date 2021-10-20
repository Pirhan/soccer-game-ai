package pison.soccergame.game.entity;

import math.geom2d.Vector2D;
import org.newdawn.slick.Color;

import java.awt.geom.Rectangle2D;

import pison.soccergame.game.GoalListener;
import pison.soccergame.game.SoccerGame;
import pison.soccergame.game.collision.SimpleCollision;
import pison.soccergame.game.renderable.Ball;
import pison.soccergame.movement.KinematicInfo;
import pison.soccergame.movement.StaticInfo;
import pison.soccergame.movement.steering.FrictionBehavior;
import pison.soccergame.movement.steering.SteeringInfo;

public class SoccerBall extends MovingEntity implements GoalListener {
    public static final Color BallColor = Color.black;
    public static final float BallRadius = 5;
    public static final double FrictionCoefficient = 0.05;
    private static final double MinBallSpeed = 0.0001;

    Vector2D initialPosition;

    public SoccerBall(Vector2D initialPosition, Rectangle2D bounds) {
        super(new Ball(BallColor, BallRadius), new StaticInfo(initialPosition, 0), KinematicInfo.Zero(), bounds);
        setCollisionHandler(new SimpleCollision(BallRadius));
        setSteeringBehavior(new FrictionBehavior(FrictionCoefficient));
        this.initialPosition = initialPosition;
        setMaxVelocity(SoccerGame.MaxBallSpeed);
    }

    public Vector2D getPosition() {
        return staticInfo.getPosition().clone();
    }

    public void setKinematic(SteeringInfo steeringInfo) {
        if (steeringInfo.linear.norm() < MinBallSpeed)
            kinematicInfo = new KinematicInfo(new Vector2D(0, 0), 0);
        else if (steeringInfo.linear.norm() > SoccerGame.MaxBallSpeed)
            kinematicInfo = new KinematicInfo(steeringInfo.linear.normalize().times(SoccerGame.MaxBallSpeed), 0);
        else kinematicInfo = new KinematicInfo(steeringInfo.linear.clone(), 0);
    }

    @Override
    public void goalScored(int team) {
        staticInfo.setPosition(initialPosition.clone());
        kinematicInfo = KinematicInfo.Zero();
    }
}
