package pison.soccergame.game.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface GameEntity {
    void init(GameContainer gameContainer, StateBasedGame stateBasedGame);

    void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics);

    void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i);
}
