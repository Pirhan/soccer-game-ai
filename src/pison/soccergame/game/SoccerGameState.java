package pison.soccergame.game;

import java.util.ArrayList;
import java.util.List;

import math.geom2d.Vector2D;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.geom.Rectangle2D;

import pison.soccergame.game.entity.GameEntity;
import pison.soccergame.game.entity.ScoreBoard;
import pison.soccergame.game.entity.SoccerBall;
import pison.soccergame.game.player.SimpleSoccerPlayer;
import pison.soccergame.game.player.SoccerPlayer;
import pison.soccergame.game.renderable.ScoreBoardImage;
import pison.soccergame.movement.StaticInfo;
import pison.soccergame.movement.steering.SteeringInfo;
import pison.soccergame.utils.RandomUtils;
import pison.soccergame.utils.VectorUtils;

import static pison.soccergame.game.entity.SoccerBall.BallRadius;

public class SoccerGameState extends BasicGameState implements SoccerGame {
    private static final Vector2D initialPositions[][] = new Vector2D[][]
            {
                    new Vector2D[]{
                            new Vector2D(60, 260),
                            new Vector2D(160, 160),
                            new Vector2D(160, 360),
                            new Vector2D(260, 160),
                            new Vector2D(260, 360),
                    },
                    new Vector2D[]{
                            new Vector2D(860, 260),
                            new Vector2D(760, 160),
                            new Vector2D(760, 360),
                            new Vector2D(660, 160),
                            new Vector2D(660, 360),
                    }
            };

    private static final Color FrameColor = Color.orange;
    private static final Color MeasureLineColor = Color.lightGray;
    private static final float MeasureLineLength = 3;


    private SoccerBall ball;
    private List<SimpleSoccerPlayer> players[] = new List[]{new ArrayList<>(), new ArrayList<>()};


    private float width;
    private float height;

    private Image image;
    private List<GoalListener> goalListeners = new ArrayList<>();

    ScoreBoard scoreBoard;
    private GameContainer gameContainer;
    public boolean gameOver = false;


    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


    public void addPlayer(int team, SimpleSoccerPlayer player) {
        players[team].add(player);
        player.setInitialPosition(initialPositions[team][players[team].size() - 1].clone());
        goalListeners.add(player);
    }


    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        gameOver = false;

        this.gameContainer = gameContainer;
        width = gameContainer.getWidth() - 2 * FRAME_WIDTH;
        height = gameContainer.getHeight() - 2 * FRAME_WIDTH;


        ball = new SoccerBall(new Vector2D(width / 2, height / 2), getBounds());
        scoreBoard = new ScoreBoard(new StaticInfo(new Vector2D(width - ScoreBoardImage.ScoreBoardCellWidth * 4, 5), 0));
        goalListeners.add(ball);
        goalListeners.add(scoreBoard);
        loadImage();
        for (SimpleSoccerPlayer e : players[0]
        ) {
            e.init(gameContainer, stateBasedGame);
        }
        for (SimpleSoccerPlayer e : players[1]
        ) {
            e.init(gameContainer, stateBasedGame);
        }
    }

    private void loadImage() {
        try {

            image = new Image("/res/grass2.jpg");
        } catch (SlickException e) {
            e.printStackTrace();
        }
        ;

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        Color c = graphics.getColor();

        renderSoccerPitch(gameContainer, stateBasedGame, graphics);

        renderEtities(gameContainer, stateBasedGame, graphics);

        graphics.setColor(c);
    }

    private void renderSoccerPitch(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        int w = gameContainer.getWidth();
        int h = gameContainer.getHeight();
        graphics.setColor(FrameColor);

        drawMeasureLines(graphics);

        scoreBoard.render(gameContainer, stateBasedGame, graphics);

        graphics.translate(FRAME_WIDTH, (float) height + FRAME_WIDTH);
        graphics.scale(1, -1);

        image.draw((float) 0, (float) 0, (float) width, (float) height);

        drawPitchLines(graphics);
    }

    private void drawPitchLines(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.setLineWidth(3);
        graphics.drawRect(0.0f, 0.0f, (float) width, (float) height);
        graphics.drawLine((float) width * 0.5f, 0.0f, (float) width * 0.5f, (float) height);
        graphics.drawOval((float) width * 0.5f - CenterCircleRadius, (float) height * 0.5f - CenterCircleRadius, 2 * CenterCircleRadius, 2 * CenterCircleRadius);

        graphics.drawRect(0.0f, (float) height * 0.5f - PenaltyAreaHeight * 0.5f, PenalyAreaWidth, PenaltyAreaHeight);
        graphics.drawRect((float) width - PenalyAreaWidth, (float) height * 0.5f - PenaltyAreaHeight * 0.5f, PenalyAreaWidth, PenaltyAreaHeight);

        graphics.drawRect(0.0f, (float) height * 0.5f - GoalAreaHeight * 0.5f, GoalAreaWidth, GoalAreaHeight);
        graphics.drawRect((float) width - GoalAreaWidth, (float) height * 0.5f - GoalAreaHeight * 0.5f, GoalAreaWidth, GoalAreaHeight);

        graphics.fillOval(width * 0.5f - CenterPointRadius, height * 0.5f - CenterPointRadius, 2 * CenterPointRadius, 2 * CenterPointRadius);

        graphics.setLineWidth(8);
        graphics.setColor(Color.blue);
        graphics.drawLine(0.0f, (float) height * 0.5f - GoalHeight * 0.5f, 0.0f, (float) height * 0.5f + GoalHeight * 0.5f);
        graphics.drawLine(width, (float) height * 0.5f - GoalHeight * 0.5f, width, (float) height * 0.5f + GoalHeight * 0.5f);


        graphics.setLineWidth(1);
    }

    private void drawMeasureLines(Graphics graphics) {
        graphics.setColor(MeasureLineColor);

        for (int i = 0; i < width; i += 50) {
            drawXLine(graphics, i);
            if (i < height)
                drawYLine(graphics, i);
        }
    }

    private void drawYLine(Graphics graphics, int i) {
        graphics.drawLine((float) FRAME_WIDTH - MeasureLineLength, (float) height + FRAME_WIDTH - i, (float) (FRAME_WIDTH + MeasureLineLength), (float) height + FRAME_WIDTH - i);
        graphics.drawString("" + i, FRAME_WIDTH - graphics.getFont().getWidth("" + i) - 2 * MeasureLineLength, (float) height + FRAME_WIDTH - i - graphics.getFont().getHeight("i") / 2);
    }

    private void drawXLine(Graphics graphics, int i) {
        graphics.drawLine(i + FRAME_WIDTH, (float) (height + FRAME_WIDTH - MeasureLineLength), i + FRAME_WIDTH, (float) (height + FRAME_WIDTH + MeasureLineLength));
        graphics.drawString("" + i, i + FRAME_WIDTH - graphics.getFont().getWidth("" + i) / 2, (float) height + FRAME_WIDTH + 2 * MeasureLineLength);
    }


    private void renderEtities(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {

        ball.render(gameContainer, stateBasedGame, graphics);
        for (GameEntity e : players[0]
        ) {
            e.render(gameContainer, stateBasedGame, graphics);
        }
        for (GameEntity e : players[1]
        ) {
            e.render(gameContainer, stateBasedGame, graphics);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) {
        if (gameOver)
            return;

        ball.update(gameContainer, stateBasedGame, i);

        checkGoal();

        int turn = RandomUtils.nextInt(2);
        SimpleSoccerPlayer closestPlayer = players[turn].get(0);
        double minDistance = VectorUtils.distance(ball.getPosition(), closestPlayer.getPosition());

        for (SimpleSoccerPlayer e : players[turn]
        ) {
            e.update(gameContainer, stateBasedGame, i); // For Debugging purposes kept 1
            double d = VectorUtils.distance(ball.getPosition(), e.getPosition());
            assert !Double.isNaN(d);
            if (d < minDistance && e.getBallSteering() != null) {
                minDistance = d;
                closestPlayer = e;
            }
        }
        int opponent = (turn + 1) % 2;
        for (SimpleSoccerPlayer e : players[opponent]
        ) {
            e.update(gameContainer, stateBasedGame, i); // For Debugging purposes kept 1
            double d = VectorUtils.distance(ball.getPosition(), e.getPosition());
            assert !Double.isNaN(d);
            if (d < minDistance && e.getBallSteering() != null) {
                minDistance = d;
                closestPlayer = e;
            }
        }

        if (minDistance < KickDistance && closestPlayer.getBallSteering() != null) {
            performKick(closestPlayer.getBallSteering());
        }
    }

    private void checkGoal() {
        if (ball.getPosition().y() < (height + GoalAreaHeight) / 2 && ball.getPosition().y() > (height - GoalAreaHeight) / 2) {
            if (ball.getPosition().x() + BallRadius > width && ball.getKinematicInfo().getVelocity().x() > 0)
                notifyGoal(0);
            if (ball.getPosition().x() - BallRadius < 0 && ball.getKinematicInfo().getVelocity().x() < 0)
                notifyGoal(1);
        }
    }

    private void notifyGoal(int team) {
        for (GoalListener gl : goalListeners
        ) {
            gl.goalScored(team);
        }
    }

    private void performKick(SteeringInfo ballSteering) {
        ball.setKinematic(ballSteering);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(-2, -2, width + 4, height + 4);
    }


    public SoccerBall getBall() {
        return ball;
    }

    @Override
    public Vector2D getBallPosition() {
        return ball.getPosition();
    }

    @Override
    public Vector2D getBallVelocity() {
        return ball.getKinematicInfo().getVelocity().clone();
    }

    public int getPlayerCount(int team) {
        return players[team].size();
    }

    public SoccerPlayer getPlayer(int team, int playerIndex) {
        return players[team].get(playerIndex);
    }

    @Override
    public int opponent(int team) {
        return (team + 1) % 2;
    }

    @Override
    public Vector2D getGoalAreaCenter(int team) {
        if (team == 0) {
            return new Vector2D(0, height / 2);
        } else return new Vector2D(width, height / 2);
    }

    public void disqualify(SimpleSoccerPlayer simpleSoccerPlayer) {
        if (!gameOver) {
            System.out.println("Team-" + simpleSoccerPlayer.getTeam() + " is disqualfied.");
            System.out.println("Team-" + opponent(simpleSoccerPlayer.getTeam()) + " won by forfeit");
            gameContainer.exit();
            gameOver = true;
        }
    }
}
