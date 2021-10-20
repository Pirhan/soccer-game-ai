package pison.soccergame.game.renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import pison.soccergame.movement.StaticInfo;

public class Ball extends BasicRenderable {

    protected float radius;

    public Ball(Color color, float radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public void _render(StaticInfo staticInfo, Graphics graphics) {

        graphics.fillOval((float) staticInfo.position.x()-radius,(float) staticInfo.position.y()-radius,2*radius,2*radius);
        float ex = (float) (staticInfo.position.x()+ radius* Math.cos(staticInfo.orientation));
        float ey = (float) (staticInfo.position.y()+ radius* Math.sin(staticInfo.orientation));
        graphics.setColor(Color.black);
        graphics.drawLine((float) staticInfo.position.x(),(float) staticInfo.position.y(),ex,ey);


    }

    @Override
    public double getRadius() {
        return radius;
    }
}
