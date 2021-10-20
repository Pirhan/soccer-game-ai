package pison.soccergame.game.entity;

import pison.soccergame.game.GoalListener;
import pison.soccergame.game.renderable.ScoreBoardImage;
import pison.soccergame.movement.StaticInfo;

public class ScoreBoard extends BasicGameEntity implements GoalListener {
    private int[] score = new int[2];

    public ScoreBoard(StaticInfo pos) {
        super(new ScoreBoardImage(), pos);
        ((ScoreBoardImage) shape).setScoreBoard(this);
    }

    @Override
    public void goalScored(int team) {
        score[team]++;
    }

    public int getScore(int team) {
        return score[team];
    }
}
