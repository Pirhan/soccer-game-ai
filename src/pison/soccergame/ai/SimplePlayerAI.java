package pison.soccergame.ai;

import pison.soccergame.game.SoccerGame;
import pison.soccergame.game.player.SoccerPlayer;
import pison.soccergame.movement.steering.KinematicSeek;
import pison.soccergame.movement.steering.SteeringBehavior;
import pison.soccergame.movement.steering.SteeringInfo;
import math.geom2d.Vector2D;

public class SimplePlayerAI implements PlayerAI {
    Vector2D kickTarget;

    @Override
    public void init(SoccerPlayer soccerPlayer, SoccerGame game) {
        kickTarget = game.getGoalAreaCenter(game.opponent(soccerPlayer.getTeam()));
    }

    @Override
    public SteeringBehavior getSteering(SoccerPlayer soccerPlayer, SoccerGame game) {
        return new KinematicSeek(game.getBallPosition());
    }

    @Override
    public SteeringInfo getBallSteering(SoccerPlayer soccerPlayer, SoccerGame game) {
        SoccerPlayer p = game.getPlayer(0,0);


        if (kickTarget!= null)
            return new SteeringInfo(kickTarget.minus(soccerPlayer.getPosition()),0, SteeringInfo.SteeringType.Kinematic);
        else return SteeringInfo.getNoSteering();

    }
}
