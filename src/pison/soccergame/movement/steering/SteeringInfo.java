package pison.soccergame.movement.steering;

import math.geom2d.Vector2D;

public class SteeringInfo {
  //  public static final SteeringInfo NoSteering = new SteeringInfo(new Vector2D(0,0),0,SteeringType.Dynamic);


    public static SteeringInfo getNoSteering()
    {
        return new SteeringInfo(new Vector2D(0,0),0, SteeringType.Dynamic);
    }

    public enum SteeringType {Kinematic, Dynamic}

    public Vector2D linear;
    public double angular;
    public SteeringType steeringType;

    public SteeringInfo(Vector2D linear, double angular, SteeringType steeringType) {
        this.linear = linear;
        this.angular = angular;
        this.steeringType = steeringType;
    }
}
