package pison.soccergame.ai.pisonai;

import math.geom2d.Vector2D;
import org.newdawn.slick.geom.Polygon;
import pison.soccergame.ai.PlayerAI;
import pison.soccergame.game.SoccerGame;
import pison.soccergame.game.player.SoccerPlayer;
import pison.soccergame.movement.steering.KinematicSeek;
import pison.soccergame.movement.steering.SteeringBehavior;
import pison.soccergame.movement.steering.SteeringInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PisonAI implements PlayerAI {
    private PlayerPositions tacticalPosition;
    private Tactics tactic;

    private Vector2D kickTarget;

    private static final int CLOSEST_ENCOUNTER = 2;
    private static final int DRIBBLINGCOEFFICIENT = 3;
    private static final int BALLPRESSDISTANCE = 75;
    private static final int FAREST_ENCOUNTER = 2;
    private static final double safeDistance = 70;
    private static final double shootingRange = 200;

    private static final Vector2D[][] POINTSOFDRIBBLING = {
            {
                    new Vector2D(825, 250),
                    new Vector2D(825, 200),
                    new Vector2D(825, 300),
                    new Vector2D(825, 150),
                    new Vector2D(825, 350),
                    new Vector2D(825, 100),
                    new Vector2D(825, 400),

                    new Vector2D(750, 250),
                    new Vector2D(750, 200),
                    new Vector2D(750, 300),
                    new Vector2D(750, 150),
                    new Vector2D(750, 350),
                    new Vector2D(750, 100),
                    new Vector2D(750, 400),

                    new Vector2D(600, 250),
                    new Vector2D(600, 200),
                    new Vector2D(600, 300),
                    new Vector2D(600, 150),
                    new Vector2D(600, 350),
                    new Vector2D(600, 100),
                    new Vector2D(600, 400),

                    new Vector2D(450, 250),
                    new Vector2D(450, 200),
                    new Vector2D(450, 300),
                    new Vector2D(450, 150),
                    new Vector2D(450, 350),
                    new Vector2D(450, 100),
                    new Vector2D(450, 400),
            },
            {
                    new Vector2D(100, 250),
                    new Vector2D(100, 200),
                    new Vector2D(100, 300),
                    new Vector2D(100, 150),
                    new Vector2D(100, 350),
                    new Vector2D(100, 100),
                    new Vector2D(100, 400),

                    new Vector2D(150, 250),
                    new Vector2D(150, 200),
                    new Vector2D(150, 300),
                    new Vector2D(150, 150),
                    new Vector2D(150, 350),
                    new Vector2D(150, 100),
                    new Vector2D(150, 400),

                    new Vector2D(300, 250),
                    new Vector2D(300, 200),
                    new Vector2D(300, 300),
                    new Vector2D(300, 150),
                    new Vector2D(300, 350),
                    new Vector2D(300, 100),
                    new Vector2D(300, 400),

                    new Vector2D(450, 250),
                    new Vector2D(450, 200),
                    new Vector2D(450, 300),
                    new Vector2D(450, 150),
                    new Vector2D(450, 350),
                    new Vector2D(450, 100),
                    new Vector2D(450, 400),
            }
    };

    private int friendlyTeam;
    private int enemyTeam;

    private List<List<SoccerPlayer>> teamXPositionRankingsFromLargestToSmallest;
    private List<List<SoccerPlayer>> teamClosestRankings;

    @Override
    public void init(SoccerPlayer soccerPlayer, SoccerGame game) {
        this.friendlyTeam = soccerPlayer.getTeam();
        this.enemyTeam = game.opponent(this.friendlyTeam);

        this._setTacticalPositionOfPlayer(soccerPlayer, game);

        this._initializeTeamClosestRankings(soccerPlayer, game);
        this._rankPlayersDistance(soccerPlayer, 0);
        this._rankPlayersDistance(soccerPlayer, 1);

        this._initializeTeamXPositionRankings(game);
        this._calculateXPositionRankingsFromLargestToSmallest(game, 0);
        this._calculateXPositionRankingsFromLargestToSmallest(game, 1);
    }


    @Override
    public SteeringBehavior getSteering(SoccerPlayer soccerPlayer, SoccerGame game) {
        this._rankPlayersDistance(soccerPlayer, 0);
        this._rankPlayersDistance(soccerPlayer, 1);

        this._calculateXPositionRankingsFromLargestToSmallest(game, 0);
        this._calculateXPositionRankingsFromLargestToSmallest(game, 1);

        this._setTacticsOfTheTeam(game);

        if (this.tacticalPosition == PlayerPositions.GOALKEEPER) {
            return new KinematicSeek(this._followBallOnTheGoalKeepingPath(game));
        } else if (this._isThisPlayerClosestToTheBallInMyTeam(soccerPlayer, game) || (this_isThisPlayerCloseEnoughToTheBall(soccerPlayer, game) && this._getTheClosestTeamToTheBall(game) != this.friendlyTeam)) {
            return new KinematicSeek(this._calculateNewVectorAccordingToWhereTheBallWillBe(soccerPlayer, game));
        } else {
            return new KinematicSeek(this.tacticalPosition.getTacticalPositionOfPlayer(this.friendlyTeam, this.tactic));
        }
    }

    @Override
    public SteeringInfo getBallSteering(SoccerPlayer soccerPlayer, SoccerGame game) {
        if (!this._isClearToShoot(soccerPlayer, game)) {
            Vector2D safeDribblingSpot = this._calculateSafePlaceToDribble(soccerPlayer, game);
            if (safeDribblingSpot == null || this.tacticalPosition == PlayerPositions.GOALKEEPER) {
                kickTarget = this._determinePassingPoint(soccerPlayer);
            } else {
                kickTarget = soccerPlayer.getPosition().plus(new Vector2D(safeDribblingSpot.minus(soccerPlayer.getPosition()).x() / safeDribblingSpot.minus(soccerPlayer.getPosition()).norm(), safeDribblingSpot.minus(soccerPlayer.getPosition()).y() / safeDribblingSpot.minus(soccerPlayer.getPosition()).norm()).times(this.DRIBBLINGCOEFFICIENT));
            }
        } else {
            kickTarget = this._determineShootingPoint(soccerPlayer, game);
        }

        if (kickTarget != null)
            return new SteeringInfo(kickTarget.minus(soccerPlayer.getPosition()), 0, SteeringInfo.SteeringType.Kinematic);
        else return SteeringInfo.getNoSteering();

    }

    private Vector2D _determineShootingPoint(SoccerPlayer soccerPlayer, SoccerGame game) {
        Polygon polygon = new Polygon();

        polygon.addPoint((float) soccerPlayer.getPosition().x(), (float) soccerPlayer.getPosition().y());
        polygon.addPoint((float) game.getGoalAreaCenter(this.enemyTeam).x(), (float) game.getGoalAreaCenter(this.enemyTeam).y() + SoccerGame.GoalAreaHeight / 2);
        polygon.addPoint((float) game.getGoalAreaCenter(this.enemyTeam).x(), (float) game.getGoalAreaCenter(this.enemyTeam).y() - SoccerGame.GoalAreaHeight / 2);

        for (int i = 0; i < this.teamClosestRankings.get(this.enemyTeam).size(); i++) {
            if (polygon.contains((float) this.teamClosestRankings.get(this.enemyTeam).get(i).getPosition().x(), (float) this.teamClosestRankings.get(this.enemyTeam).get(i).getPosition().y())) {
                double shootingYPoint = soccerPlayer.getPosition().y() > this.teamClosestRankings.get(this.enemyTeam).get(i).getPosition().y() ? game.getGoalAreaCenter(this.enemyTeam).y() + (SoccerGame.GoalHeight / 2) - 1 : game.getGoalAreaCenter(this.enemyTeam).y() - (SoccerGame.GoalHeight / 2) + 1;
                return new Vector2D(game.getGoalAreaCenter(this.enemyTeam).x(), shootingYPoint);
            }
        }

        return game.getGoalAreaCenter(this.enemyTeam);
    }

    private Vector2D _determinePassingPoint(SoccerPlayer soccerPlayer) {
        List<SoccerPlayer> friendlyListByXCoordinatesFromLargestToSmallest = this.teamXPositionRankingsFromLargestToSmallest.get(this.friendlyTeam);

        List<SoccerPlayer> friendlyListByXCoordinatesFromLargestToSmallestExcludingCurrentPlayer = new ArrayList<>();
        List<SoccerPlayer> friendlyListByClosestRankings = this.teamClosestRankings.get(this.friendlyTeam);

        for (SoccerPlayer friendlyPlayer : friendlyListByXCoordinatesFromLargestToSmallest) {
            if (!this._areTwoSoccerPlayersTheSame(soccerPlayer, friendlyPlayer)) {
                friendlyListByXCoordinatesFromLargestToSmallestExcludingCurrentPlayer.add(friendlyPlayer);
            }
        }

        for (int i = 0; i < PisonAI.CLOSEST_ENCOUNTER; i++) {
            for (int j = 0; j < PisonAI.FAREST_ENCOUNTER; j++) {
                if (this._areTwoSoccerPlayersTheSame(friendlyListByXCoordinatesFromLargestToSmallestExcludingCurrentPlayer.get(i), friendlyListByClosestRankings.get(j))) {
                    return friendlyListByClosestRankings.get(j).getPosition();
                }
            }
        }

        return new Random().nextInt(100) < 75 ? this._getTheClosestPlayerInATeamByIndex(this.friendlyTeam, 0).getPosition() : this._getTheClosestPlayerInATeamByIndex(this.friendlyTeam, 1).getPosition();
    }

    private SoccerPlayer _getTheClosestPlayerToTheBallInATeam(SoccerGame game, int team) {
        SoccerPlayer closestPlayer = game.getPlayer(team, 0);

        double smallestDistance = this._getEuclidianDistanceBetweenTwoVectors(game.getPlayer(team, 0).getPosition(), game.getBallPosition());

        for (int i = 0; i < game.getPlayerCount(team); i++) {
            double distanceBetweenBallAndPlayer = this._getEuclidianDistanceBetweenTwoVectors(game.getPlayer(team, i).getPosition(), game.getBallPosition());
            if (distanceBetweenBallAndPlayer < smallestDistance) {
                closestPlayer = game.getPlayer(team, i);
                smallestDistance = distanceBetweenBallAndPlayer;
            }
        }

        return closestPlayer;
    }

    private SoccerPlayer _getTheClosestPlayerToTheBallInTheGame(SoccerGame game) {
        SoccerPlayer closestPlayerInTeam0 = this._getTheClosestPlayerToTheBallInATeam(game, 0);
        SoccerPlayer closestPlayerInTeam1 = this._getTheClosestPlayerToTheBallInATeam(game, 1);

        return this._getEuclidianDistanceBetweenTwoVectors(closestPlayerInTeam0.getPosition(), game.getBallPosition()) < this._getEuclidianDistanceBetweenTwoVectors(closestPlayerInTeam1.getPosition(), game.getBallPosition()) ? closestPlayerInTeam0 : closestPlayerInTeam1;
    }

    private int _getTheClosestTeamToTheBall(SoccerGame game) {
        return _getTheClosestPlayerToTheBallInTheGame(game).getTeam();
    }

    private SoccerPlayer _getTheClosestPlayerInATeamByIndex(int team, int index) {
        return this.teamClosestRankings.get(team).get(index);
    }

    private Vector2D _calculateSafePlaceToDribble(SoccerPlayer soccerPlayer, SoccerGame game) {
        for (int i = 0; i < PisonAI.POINTSOFDRIBBLING[this.friendlyTeam].length; i++) {
            if (this._getEuclidianDistanceBetweenTwoVectors(PisonAI.POINTSOFDRIBBLING[this.friendlyTeam][i], game.getGoalAreaCenter(this.enemyTeam)) > this._getEuclidianDistanceBetweenTwoVectors(soccerPlayer.getPosition(), game.getGoalAreaCenter(this.enemyTeam))) {
                break;
            }

            Polygon polygon = new Polygon();
            polygon.addPoint((float) soccerPlayer.getPosition().x(), (float) soccerPlayer.getPosition().y() + (float) PisonAI.safeDistance / 2);
            polygon.addPoint((float) soccerPlayer.getPosition().x(), (float) soccerPlayer.getPosition().y() - (float) PisonAI.safeDistance / 2);
            polygon.addPoint((float) PisonAI.POINTSOFDRIBBLING[this.friendlyTeam][i].x(), (float) PisonAI.POINTSOFDRIBBLING[this.friendlyTeam][i].y() - (float) PisonAI.safeDistance / 2);
            polygon.addPoint((float) PisonAI.POINTSOFDRIBBLING[this.friendlyTeam][i].x(), (float) PisonAI.POINTSOFDRIBBLING[this.friendlyTeam][i].y() + (float) PisonAI.safeDistance / 2);

            for (int j = 0; j < this.teamClosestRankings.get(this.enemyTeam).size(); j++) {
                if (polygon.contains((float) this.teamClosestRankings.get(this.enemyTeam).get(j).getPosition().x(), (float) this.teamClosestRankings.get(this.enemyTeam).get(j).getPosition().y())) {
                    break;
                }

                if (j == this.teamClosestRankings.get(this.enemyTeam).size() - 1) {
                    return PisonAI.POINTSOFDRIBBLING[this.friendlyTeam][i];
                }
            }
        }

        return null;
    }

    private boolean _isThisPlayerClosestToTheBallInMyTeam(SoccerPlayer soccerPlayer, SoccerGame game) {
        return _areTwoSoccerPlayersTheSame(this._getTheClosestPlayerToTheBallInATeam(game, this.friendlyTeam), soccerPlayer);
    }

    private boolean this_isThisPlayerCloseEnoughToTheBall(SoccerPlayer soccerPlayer, SoccerGame game) {
        return this._getEuclidianDistanceBetweenTwoVectors(soccerPlayer.getPosition(), game.getBallPosition()) < this.BALLPRESSDISTANCE;
    }

    private boolean _areTwoSoccerPlayersTheSame(SoccerPlayer soccerPlayer1, SoccerPlayer soccerPlayer2) {
        return soccerPlayer1.equals(soccerPlayer2);
    }

    private double _getEuclidianDistanceBetweenTwoVectors(Vector2D vector1, Vector2D vector2) {
        double xSqr = (vector1.x() - vector2.x()) * (vector1.x() - vector2.x());
        double ySqr = (vector1.y() - vector2.y()) * (vector1.y() - vector2.y());

        return Math.sqrt(xSqr + ySqr);
    }

    private int _getTheBackNumberOfSoccerPlayer(SoccerPlayer soccerPlayer, SoccerGame game) {
        int i = 0;
        for (; i < game.getPlayerCount(this.friendlyTeam); i++) {
            if (_areTwoSoccerPlayersTheSame(soccerPlayer, game.getPlayer(this.friendlyTeam, i)) || _areTwoSoccerPlayersTheSame(soccerPlayer, game.getPlayer(this.enemyTeam, i))) {
                return i;
            }
        }
        return i;
    }

    private boolean _isTheBallOnTheEnemySide(SoccerGame game) {
        return this._getEuclidianDistanceBetweenTwoVectors(game.getBallPosition(), game.getGoalAreaCenter(this.friendlyTeam)) >= this._getEuclidianDistanceBetweenTwoVectors(game.getBallPosition(), game.getGoalAreaCenter(this.enemyTeam));
    }

    private void _setTacticsOfTheTeam(SoccerGame game) {
        if (this._getTheClosestTeamToTheBall(game) == this.friendlyTeam) {
            if (this._isTheBallOnTheEnemySide(game)) {
                this.tactic = Tactics.OFFENSIVE;
            } else {
                this.tactic = Tactics.HALFOFFENSIVE;
            }
        } else {
            if (this._isTheBallOnTheEnemySide(game)) {
                this.tactic = Tactics.HALFDEFFENSIVE;
            } else {
                this.tactic = Tactics.DEFFENSIVE;
            }
        }
    }

    private void _rankPlayersDistance(SoccerPlayer soccerPlayer, int team) {
        for (int i = 0; i < teamClosestRankings.get(team).size(); i++) {
            for (int j = i + 1; j < teamClosestRankings.get(team).size(); j++) {
                if (this._getEuclidianDistanceBetweenTwoVectors(soccerPlayer.getPosition(), this.teamClosestRankings.get(team).get(i).getPosition()) > this._getEuclidianDistanceBetweenTwoVectors(soccerPlayer.getPosition(), this.teamClosestRankings.get(team).get(j).getPosition())) {
                    SoccerPlayer swapTempPlayer = this.teamClosestRankings.get(team).get(i);
                    this.teamClosestRankings.get(team).set(i, this.teamClosestRankings.get(team).get(j));
                    this.teamClosestRankings.get(team).set(j, swapTempPlayer);
                }
            }
        }
    }

    private void _setTacticalPositionOfPlayer(SoccerPlayer soccerPlayer, SoccerGame game) {
        switch (this._getTheBackNumberOfSoccerPlayer(soccerPlayer, game)) {
            case 0: {
                this.tacticalPosition = PlayerPositions.GOALKEEPER;
                break;
            }
            case 1: {
                this.tacticalPosition = PlayerPositions.RIGHTBACK;
                break;
            }
            case 2: {
                this.tacticalPosition = PlayerPositions.LEFTBACK;
                break;
            }
            case 3: {
                this.tacticalPosition = PlayerPositions.RIGHTFORWARD;
                break;
            }
            case 4: {
                this.tacticalPosition = PlayerPositions.LEFTFORWARD;
                break;
            }
        }
    }

    private void _calculateXPositionRankingsFromLargestToSmallest(SoccerGame game, int team) {
        for (int i = 0; i < this.teamXPositionRankingsFromLargestToSmallest.get(team).size(); i++) {
            for (int j = i + 1; j < this.teamXPositionRankingsFromLargestToSmallest.get(team).size(); j++) {
                if (this._getEuclidianDistanceBetweenTwoVectors(this.teamXPositionRankingsFromLargestToSmallest.get(team).get(i).getPosition(), game.getGoalAreaCenter(team == 0 ? 1 : 0)) > this._getEuclidianDistanceBetweenTwoVectors(this.teamXPositionRankingsFromLargestToSmallest.get(team).get(j).getPosition(), game.getGoalAreaCenter(team == 0 ? 1 : 0))) {
                    SoccerPlayer swapTempPlayer = this.teamXPositionRankingsFromLargestToSmallest.get(team).get(i);
                    this.teamXPositionRankingsFromLargestToSmallest.get(team).set(i, this.teamXPositionRankingsFromLargestToSmallest.get(team).get(j));
                    this.teamXPositionRankingsFromLargestToSmallest.get(team).set(j, swapTempPlayer);
                }
            }
        }
    }

    private void _initializeTeamXPositionRankings(SoccerGame game) {
        this.teamXPositionRankingsFromLargestToSmallest = new ArrayList<>();
        this.teamXPositionRankingsFromLargestToSmallest.add(new ArrayList<>());
        this.teamXPositionRankingsFromLargestToSmallest.add(new ArrayList<>());

        for (int i = 0; i < game.getPlayerCount(0); i++) { // assuming the sizes of the teams are the same.
            this.teamXPositionRankingsFromLargestToSmallest.get(0).add(game.getPlayer(0, i));
            this.teamXPositionRankingsFromLargestToSmallest.get(1).add(game.getPlayer(1, i));
        }
    }

    private void _initializeTeamClosestRankings(SoccerPlayer soccerPlayer, SoccerGame game) {
        this.teamClosestRankings = new ArrayList<>();
        this.teamClosestRankings.add(new ArrayList<>());
        this.teamClosestRankings.add(new ArrayList<>());

        for (int i = 0; i < game.getPlayerCount(0); i++) { // assuming the sizes of the teams are the same.
            if (!this._areTwoSoccerPlayersTheSame(soccerPlayer, game.getPlayer(0, i))) {
                this.teamClosestRankings.get(0).add(game.getPlayer(0, i));
            }
            if (!this._areTwoSoccerPlayersTheSame(soccerPlayer, game.getPlayer(0, i))) {
                this.teamClosestRankings.get(1).add(game.getPlayer(1, i));
            }
        }
    }

    private Vector2D _followBallOnTheGoalKeepingPath(SoccerGame game) {
        if (this._getEuclidianDistanceBetweenTwoVectors(game.getBallPosition(), game.getGoalAreaCenter(this.friendlyTeam)) < SoccerGame.PenalyAreaWidth) {
            return game.getBallPosition().plus(game.getBallVelocity());
        }

        Vector2D tacticalPositionOfGoalKeeper = PlayerPositions.GOALKEEPER.getTacticalPositionOfPlayer(this.friendlyTeam, this.tactic);
        Vector2D friendlyGoalAreaCenter = game.getGoalAreaCenter(this.friendlyTeam);

        Vector2D lowerLimitOfGoalAreaForY = new Vector2D(tacticalPositionOfGoalKeeper.x(), friendlyGoalAreaCenter.y() - (SoccerGame.GoalAreaHeight / 2));
        Vector2D upperLimitOfGoalAreaForY = new Vector2D(tacticalPositionOfGoalKeeper.x(), friendlyGoalAreaCenter.y() + (SoccerGame.GoalAreaHeight / 2));

        Vector2D lowerLimitOfGoalAreaForX = new Vector2D(friendlyGoalAreaCenter.x() - (SoccerGame.GoalAreaWidth / 2), tacticalPositionOfGoalKeeper.y());
        Vector2D upperLimitOfGoalAreaForX = new Vector2D(friendlyGoalAreaCenter.x() + (SoccerGame.GoalAreaWidth / 2), tacticalPositionOfGoalKeeper.y());

        Vector2D ballVector = game.getBallPosition().plus(game.getBallVelocity());

        double calculatedYPoint;
        double calculatedXPoint;

        if (ballVector.x() >= upperLimitOfGoalAreaForX.x()) {
            calculatedXPoint = upperLimitOfGoalAreaForX.x();
        } else if (ballVector.x() <= lowerLimitOfGoalAreaForX.x()) {
            calculatedXPoint = lowerLimitOfGoalAreaForX.x();
        } else {
            calculatedXPoint = ballVector.x();
        }

        if (ballVector.y() >= upperLimitOfGoalAreaForY.y()) {
            calculatedYPoint = upperLimitOfGoalAreaForY.y();
        } else if (ballVector.y() <= lowerLimitOfGoalAreaForY.y()) {
            calculatedYPoint = lowerLimitOfGoalAreaForY.y();
        } else {
            calculatedYPoint = ballVector.y();
        }


        return new Vector2D(calculatedXPoint, calculatedYPoint);
    }

    private boolean _isClearToShoot(SoccerPlayer soccerPlayer, SoccerGame game) {
        return this._getEuclidianDistanceBetweenTwoVectors(soccerPlayer.getPosition(), game.getGoalAreaCenter(this.enemyTeam)) < PisonAI.shootingRange;
    }

    private Vector2D _calculateNewVectorAccordingToWhereTheBallWillBe(SoccerPlayer soccerPlayer, SoccerGame game) {
        return game.getBallPosition().plus(game.getBallVelocity());
    }
}
