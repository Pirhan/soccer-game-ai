package pison.soccergame.movement.steering;

import pison.soccergame.movement.KinematicInfo;
import pison.soccergame.movement.StaticInfo;

public class FatigueBehavior implements SteeringBehavior {
    public static final double FatigeSlowDownRatio = 0.1;
    public static final double FatigueSpeed = 0.6;

    @Override
    public SteeringInfo getSteering(StaticInfo staticInfo, KinematicInfo kinematicInfo) {
        if (kinematicInfo.getVelocity().norm()> FatigueSpeed)
        {
            return new SteeringInfo(kinematicInfo.getVelocity().opposite().normalize().times(FatigeSlowDownRatio),0, SteeringInfo.SteeringType.Dynamic);
        }
        return SteeringInfo.getNoSteering();
    }

    @Override
    public void setTarget(StaticInfo targetStatic) {

    }
}
