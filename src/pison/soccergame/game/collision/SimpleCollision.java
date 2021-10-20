package pison.soccergame.game.collision;

import math.geom2d.Vector2D;

import pison.soccergame.game.entity.MovingEntity;
import pison.soccergame.movement.KinematicInfo;
import pison.soccergame.movement.StaticInfo;

public class SimpleCollision implements CollisionHandler {
    double radius;

    public SimpleCollision(double radius) {
        this.radius = radius;
    }


    @Override
    public void handle(MovingEntity movingEntity) {

        KinematicInfo kinematic = movingEntity.kinematicInfo;
        StaticInfo staticInfo = movingEntity.staticInfo;

        if (staticInfo.position.x() + radius > movingEntity.bounds.getMaxX() && kinematic.velocity.x() > 0) {
            kinematic.velocity = new Vector2D(-kinematic.velocity.x(), kinematic.velocity.y());
            staticInfo.setOrientation(Math.atan2(kinematic.velocity.y(), kinematic.velocity.x()));
        }
        if (staticInfo.position.x() - radius < movingEntity.bounds.getMinX() && kinematic.velocity.x() < 0) {
            kinematic.velocity = new Vector2D(-kinematic.velocity.x(), kinematic.velocity.y());
            staticInfo.setOrientation(Math.atan2(kinematic.velocity.y(), kinematic.velocity.x()));
        }
        if (staticInfo.position.y() + radius > movingEntity.bounds.getMaxY() && kinematic.velocity.y() > 0) {
            kinematic.velocity = new Vector2D(kinematic.velocity.x(), -kinematic.velocity.y());
            staticInfo.setOrientation(Math.atan2(kinematic.velocity.y(), kinematic.velocity.x()));
        }
        if (staticInfo.position.y() - radius < movingEntity.bounds.getMinY() && kinematic.velocity.y() < 0) {
            kinematic.velocity = new Vector2D(kinematic.velocity.x(), -kinematic.velocity.y());
            staticInfo.setOrientation(Math.atan2(kinematic.velocity.y(), kinematic.velocity.x()));
        }
    }
}
