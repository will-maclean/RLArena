package wmaclean.env.game.objs;

import wmaclean.env.Environment;
import wmaclean.env.game.RubyColour;
import wmaclean.env.game.Window;

import java.awt.*;

public class Player extends GameObject {

    private static final Color DEFAULT_COLOR = RubyColour.Vaporwave.blue;
    private static final int WIDTH = Environment.WIDTH / 25;
    private static final int HEIGHT = Environment.WIDTH / 25;
    private static final int MAX_HEALTH = 500;
    private static final double speed = 5;
    private int health;

    public Player(int x, int y) {
        super(x, y, WIDTH, HEIGHT, DEFAULT_COLOR);

        this.health = MAX_HEALTH;

        this.velX = 0;
        this.velY = 0;
    }

    @Override
    public void tick() {
        this.x += this.velX;
        this.y += this.velY;

        this.x = clamp(this.x, (int) (Environment.WIDTH - this.width));
        this.y = clamp(this.y, (int) (Environment.HEIGHT - this.height - Window.BOTTOM_BORDER_OFFSET));
    }

    public void handleCollision(Enemy enemy) {
        if(Environment.isInstantDeath()){
            this.health = 0;
        }else{
            this.health -= enemy.getDamageMultiplier();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(this.color);
        g.fillRect((int)this.x, (int)this.y, WIDTH, HEIGHT);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    // actions below

    public void N(){
        this.velX = 0;
        this.velY = -1 * speed;
    }
    public void NE(){
        this.velX = speed / Math.sqrt(2);
        this.velY = -1 * speed / Math.sqrt(2);
    }
    public void E(){
        this.velX = speed;
        this.velY = 0;
    }
    public void SE(){
        this.velX = speed / Math.sqrt(2);
        this.velY = speed / Math.sqrt(2);
    }
    public void S(){
        this.velX = 0;
        this.velY =  speed;
    }
    public void SW(){
        this.velX = -1 * speed / Math.sqrt(2);
        this.velY = speed / Math.sqrt(2);
    }
    public void W(){
        this.velX = -1 * speed;
        this.velY = 0;
    }
    public void NW(){
        this.velX = -1 * speed / Math.sqrt(2);
        this.velY = -1 * speed / Math.sqrt(2);
    }
    public void PASS(){
        this.velX = 0;
        this.velY = 0;
    }

    // end actions
}
