package pison.soccergame.game.player;

import math.geom2d.Vector2D;

public interface SoccerPlayer {
    Vector2D getInitialPosition();
    Vector2D getPosition();
    Vector2D getVelocity();
    int getTeam();

    double getStamina();

    double getFatigueStamina();
    double getFatigueSpeed();
    double getMaxStamina();
    double getMinStamina();
    double getMaxSpeed();
    double getStaminaIncrement();
}
