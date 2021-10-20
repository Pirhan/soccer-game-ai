package pison.soccergame.ai;

import pison.soccergame.game.SoccerGame;
import pison.soccergame.game.player.SoccerPlayer;
import pison.soccergame.movement.steering.SteeringBehavior;
import pison.soccergame.movement.steering.SteeringInfo;

public interface PlayerAI {
    void init(SoccerPlayer soccerPlayer, SoccerGame game);

    SteeringBehavior getSteering(SoccerPlayer soccerPlayer, SoccerGame game);

    SteeringInfo getBallSteering(SoccerPlayer soccerPlayer, SoccerGame game);
}
