package pison.soccergame.movement;

import math.geom2d.Vector2D;

import pison.soccergame.movement.steering.SteeringInfo;

public class KinematicInfo {
    public Vector2D velocity;
    double rotation;

    public KinematicInfo(Vector2D velocity, double rotation) {
        this.velocity = velocity;
        this.rotation = rotation;
    }

    public static KinematicInfo Zero() {
        return new KinematicInfo(new Vector2D(0,0),0);
    }

    public void update(SteeringInfo steeringInfo, double time, double maxVelocity, double maxRotation) {
        if (steeringInfo.steeringType == SteeringInfo.SteeringType.Dynamic) {
            velocity = velocity.plus(steeringInfo.linear.times(time));
            rotation += steeringInfo.angular * time;
        }
        else {
            velocity =steeringInfo.linear;
            rotation = steeringInfo.angular;
        }

        rotation = (Math.abs(rotation)>maxRotation)? rotation*maxRotation/Math.abs(rotation):rotation;
        velocity = (velocity.norm()>maxVelocity)? velocity.normalize().times(maxVelocity):velocity;
    }


    public Vector2D getVelocity() {
        return velocity;
    }

    public double getRotation()
    {
        return rotation;
    }
}
