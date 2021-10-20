package pison.soccergame.game.renderable;

import org.newdawn.slick.Graphics;
import pison.soccergame.movement.StaticInfo;

public interface Renderable {
    void render(StaticInfo gameEntity, Graphics graphics);

    double getRadius();
}
