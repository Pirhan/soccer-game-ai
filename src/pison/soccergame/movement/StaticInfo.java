package pison.soccergame.movement;

import math.geom2d.Vector2D;

import pison.soccergame.movement.steering.SteeringInfo;

public class StaticInfo {
    public enum OrientationType {VelocityBased, Explicit}

    public Vector2D position;
    public double orientation;
    public OrientationType orientationType;

    public StaticInfo(Vector2D position, double orientation, OrientationType orientationType) {
        this.position = position;
        this.orientation = orientation;
        this.orientationType = orientationType;
    }

    public OrientationType getOrientationType() {
        return orientationType;
    }

    public void setOrientationType(OrientationType orientationType) {
        this.orientationType = orientationType;
    }



    public StaticInfo(Vector2D position, double orientation) {
        this.position = position;
        this.orientation = orientation;
        orientationType = OrientationType.Explicit;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public void update(KinematicInfo kinematicInfo, SteeringInfo steeringInfo, double time) {
        position= position.plus(kinematicInfo.velocity.times(time));
        if (steeringInfo.steeringType== SteeringInfo.SteeringType.Dynamic)
            position = position.plus(steeringInfo.linear.times(0.5*time*time));
        if (orientationType== OrientationType.VelocityBased && (kinematicInfo.velocity.norm()>0))
        {
            orientation =  Math.atan2(kinematicInfo.velocity.y(),kinematicInfo.velocity.x());
        }
        else{
            orientation += kinematicInfo.rotation*time;
            if (steeringInfo.steeringType== SteeringInfo.SteeringType.Dynamic)
                orientation+= steeringInfo.angular*time*time*0.5;
        }



        assert !Double.isNaN(position.x());
        assert !Double.isNaN(position.y());
    }
}
