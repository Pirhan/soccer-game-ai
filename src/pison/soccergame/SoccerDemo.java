package pison.soccergame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Bootstrap;

import pison.soccergame.ai.SimplePlayerAI;
import pison.soccergame.game.SoccerGameState;
import pison.soccergame.ai.pisonai.PisonAI;
import pison.soccergame.game.player.SimpleSoccerPlayer;

public class SoccerDemo extends StateBasedGame {
    private static final int DemoWidth = 1000;
    private static final int DemoHeight = 600;
    private final int pitch = 0;

    public SoccerDemo(String name) {
        super(name);
        addState(new SoccerGameState());
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        getState(pitch).init(gameContainer, this);
        enterState(pitch);
    }

    SoccerGameState getGameState() {
        return (SoccerGameState) getState(pitch);
    }

    public static void main(String[] args) {
        SoccerDemo demo = new SoccerDemo("Soccer Demo");

        /*
        SimpleSoccerPlayer p11 = new SimpleSoccerPlayer(demo.getGameState(), 0, new SimplePlayerAI());
        SimpleSoccerPlayer p12 = new SimpleSoccerPlayer(demo.getGameState(), 0, new SimplePlayerAI());
        SimpleSoccerPlayer p13 = new SimpleSoccerPlayer(demo.getGameState(), 0, new SimplePlayerAI());
        SimpleSoccerPlayer p14 = new SimpleSoccerPlayer(demo.getGameState(), 0, new SimplePlayerAI());
        SimpleSoccerPlayer p15 = new SimpleSoccerPlayer(demo.getGameState(), 0, new SimplePlayerAI());
        */

        SimpleSoccerPlayer p11 = new SimpleSoccerPlayer(demo.getGameState(), 0, new PisonAI());
        SimpleSoccerPlayer p12 = new SimpleSoccerPlayer(demo.getGameState(), 0, new PisonAI());
        SimpleSoccerPlayer p13 = new SimpleSoccerPlayer(demo.getGameState(), 0, new PisonAI());
        SimpleSoccerPlayer p14 = new SimpleSoccerPlayer(demo.getGameState(), 0, new PisonAI());
        SimpleSoccerPlayer p15 = new SimpleSoccerPlayer(demo.getGameState(), 0, new PisonAI());

        SimpleSoccerPlayer p21 = new SimpleSoccerPlayer(demo.getGameState(), 1, new PisonAI());
        SimpleSoccerPlayer p22 = new SimpleSoccerPlayer(demo.getGameState(), 1, new PisonAI());
        SimpleSoccerPlayer p23 = new SimpleSoccerPlayer(demo.getGameState(), 1, new PisonAI());
        SimpleSoccerPlayer p24 = new SimpleSoccerPlayer(demo.getGameState(), 1, new PisonAI());
        SimpleSoccerPlayer p25 = new SimpleSoccerPlayer(demo.getGameState(), 1, new PisonAI());

        demo.getGameState().addPlayer(0, p11);
        demo.getGameState().addPlayer(0, p12);
        demo.getGameState().addPlayer(0, p13);
        demo.getGameState().addPlayer(0, p14);
        demo.getGameState().addPlayer(0, p15);
        demo.getGameState().addPlayer(1, p21);
        demo.getGameState().addPlayer(1, p22);
        demo.getGameState().addPlayer(1, p23);
        demo.getGameState().addPlayer(1, p24);
        demo.getGameState().addPlayer(1, p25);

        Bootstrap.runAsApplication(demo, DemoWidth, DemoHeight, false);
    }
}
