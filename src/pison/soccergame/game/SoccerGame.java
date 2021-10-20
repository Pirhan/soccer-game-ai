package pison.soccergame.game;

import math.geom2d.Vector2D;
import pison.soccergame.game.player.SoccerPlayer;

public interface SoccerGame {
    float FRAME_WIDTH = 40;
    double KickDistance = 1;
    float CenterCircleRadius = 80;
    float PenaltyAreaHeight = 300;
    float PenalyAreaWidth = 200;
    float GoalAreaHeight = 140;
    float GoalAreaWidth = 70;
    float GoalHeight = 80;
    float CenterPointRadius = 5;
    double MaxBallSpeed = 5;


    Vector2D getBallPosition();

    Vector2D getBallVelocity();

    int getPlayerCount(int team);

    SoccerPlayer getPlayer(int team, int index);

    // returns the opponent team
    int opponent(int team);

    //returns the middle point of GoalArea of team
    Vector2D getGoalAreaCenter(int team);

    float getWidth();

    float getHeight();


}
