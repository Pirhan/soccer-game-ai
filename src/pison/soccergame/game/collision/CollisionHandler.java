package pison.soccergame.game.collision;

import pison.soccergame.game.entity.MovingEntity;

public interface CollisionHandler {
    void handle(MovingEntity movingEntity);
}
