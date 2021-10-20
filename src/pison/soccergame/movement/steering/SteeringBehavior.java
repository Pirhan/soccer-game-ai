package pison.soccergame.movement.steering;

import pison.soccergame.movement.KinematicInfo;
import pison.soccergame.movement.StaticInfo;

public interface SteeringBehavior {
    SteeringInfo getSteering(StaticInfo staticInfo, KinematicInfo kinematicInfo);

    void setTarget(StaticInfo targetStatic);
}
