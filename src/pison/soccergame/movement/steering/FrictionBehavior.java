package pison.soccergame.movement.steering;

import pison.soccergame.movement.KinematicInfo;
import pison.soccergame.movement.StaticInfo;
import pison.soccergame.utils.RandomUtils;

public class FrictionBehavior implements SteeringBehavior {
    private static final double MinSpeed = 0.0001;
    private static final double NoiseRange = 0.2;
    private double frictionCoefficient;

    public FrictionBehavior(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
    }

    @Override
    public SteeringInfo getSteering(StaticInfo staticInfo, KinematicInfo kinematicInfo) {
        if (kinematicInfo.getVelocity().norm()>MinSpeed) {
            double noise = NoiseRange*RandomUtils.nextDouble();
            return new SteeringInfo(kinematicInfo.getVelocity().opposite().normalize().times(frictionCoefficient).rotate(noise), 0, SteeringInfo.SteeringType.Dynamic);

        }
        return SteeringInfo.getNoSteering();
    }

    @Override
    public void setTarget(StaticInfo targetStatic) {

    }
}
