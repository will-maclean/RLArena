package wmaclean.env.game.objs;

import wmaclean.env.Environment;
import wmaclean.env.game.Handler;
import wmaclean.env.game.RubyColour;
import wmaclean.env.game.Window;

import java.awt.*;

public class Bloodhound extends Enemy {

    private static final int WIDTH = Environment.WIDTH / 30;
    private static final int HEIGHT = Environment.WIDTH / 30;
    private static final Color COLOUR = RubyColour.Vaporwave.purple;
    private static final double damageMultiplier = 50;
    private static final double SPEED = 2;

    private final Handler handler;
    private Player player;

    public Bloodhound(int x, int y, Handler handler) {
        super(x, y, WIDTH, HEIGHT, damageMultiplier, COLOUR);

        this.velX = 0;
        this.velY = 0;

        this.handler = handler;
    }

    @Override
    public void tick() {
        this.x += velX;
        this.y += velY;

        setVelocity();
    }

    private void setVelocity() {
        if(this.player == null){
            this.player = this.handler.getPlayer();
        }

        int abovePlayer = this.player.getY() > this.y ? 1 : -1;
        int leftOfPlayer = this.player.getX() > this.x ? 1 : -1;

        // something to do with angles flipping

        double difX = x - player.getX();
        double difY = y - player.getY();
        double angle = Math.atan(difY / difX);

        velX = leftOfPlayer *  SPEED * Math.abs(Math.cos(angle));
        velY =  abovePlayer * SPEED * Math.abs(Math.sin(angle));

        this.x = clamp(this.x, (int) (Environment.WIDTH - this.width));
        this.y = clamp(this.y, (int) (Environment.HEIGHT - this.height - Window.BOTTOM_BORDER_OFFSET));
    }

    @Override
    public void render(Graphics g) {
        g.setColor(this.color);

        g.fillRect((int)this.x, (int)this.y, WIDTH, HEIGHT);
    }
}
