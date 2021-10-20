package pison.soccergame.movement.steering;

import math.geom2d.Vector2D;

import pison.soccergame.movement.KinematicInfo;
import pison.soccergame.movement.StaticInfo;

public class KinematicSeek implements SteeringBehavior {

    Vector2D targetPosition;

    public KinematicSeek(Vector2D targetPosition) {
        this.targetPosition = targetPosition;
    }

    @Override
    public SteeringInfo getSteering(StaticInfo staticInfo, KinematicInfo kinematicInfo) {
        Vector2D velocity = targetPosition.minus(staticInfo.getPosition());
        double rotation =0;

        return new SteeringInfo(velocity,rotation, SteeringInfo.SteeringType.Kinematic);
    }

    @Override
    public void setTarget(StaticInfo targetStatic) {
        targetPosition= targetStatic.getPosition();
    }
}
