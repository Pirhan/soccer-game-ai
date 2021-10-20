package pison.soccergame.game.renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import pison.soccergame.movement.StaticInfo;

public abstract class BasicRenderable implements Renderable {
    Color color;

    public BasicRenderable(Color color) {
        this.color = color;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void render(StaticInfo gameEntity, Graphics graphics) {
        Color c = graphics.getColor();
        graphics.setColor(color);

        _render(gameEntity,graphics);

        graphics.setColor(c);
    }

    protected abstract void _render(StaticInfo gameEntity, Graphics graphics);
}
