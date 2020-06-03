package wmaclean.env.game.objs;

import wmaclean.env.Environment;
import wmaclean.env.game.RubyColour;

import java.awt.*;
import java.util.Random;

public class Mozzie extends Enemy {

    private static final int WIDTH = Environment.WIDTH / 30;
    private static final int HEIGHT = Environment.WIDTH / 30;
    private static final Color COLOUR = RubyColour.Vaporwave.violet;
    private static final double damageMultiplier = 20;
    private static final double SPEED = 5;

    public Mozzie(int x, int y) {
        super(x, y, WIDTH, HEIGHT, damageMultiplier, COLOUR);

        Random r = new Random();
        double angle = r.nextDouble() * Math.PI / 2;
        this.velX = SPEED * Math.cos(angle);
        this.velY = SPEED * Math.sin(angle);
    }

    @Override
    public void tick() {
        this.x += this.velX;
        this.y += this.velY;

        borderDetection();
    }

    private void borderDetection() {
        if(this.collidesWithXEdge()){
            this.velX *= -1;
        }
        if(this.collidesWithYEdge()){
            this.velY *= -1;
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(this.color);

        g.fillRect((int)this.x, (int)this.y, WIDTH, HEIGHT);
    }
}
