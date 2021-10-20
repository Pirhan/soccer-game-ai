package pison.soccergame.game.player;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import pison.soccergame.game.renderable.Ball;
import pison.soccergame.movement.StaticInfo;

public class SoccerPlayerImage extends Ball {
    private static final float StaminaBarWidth = 20;
    private static final float StaminaBarHeight = 5;
    private static final Color StaminaBarColor = Color.yellow;
    private static final Color FatigueStaminaBarColor = Color.red;

    public void setPlayer(SoccerPlayer player) {
        this.player = player;
    }

    SoccerPlayer player;

    public SoccerPlayerImage(Color color, float radius) {
        super(color, radius);
    }

    @Override
    public void _render(StaticInfo pos, Graphics g) {
        super._render(pos, g);

        if (player.getStamina() < player.getFatigueStamina())
            g.setColor(FatigueStaminaBarColor);
        else g.setColor(StaminaBarColor);
        g.fillRect((float) pos.getPosition().x(), (float) (pos.getPosition().y() + 2 * radius), (float) (StaminaBarWidth * player.getStamina() / player.getMaxStamina()), StaminaBarHeight);
    }
}
