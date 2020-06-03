package wmaclean.env.game.objs;

import wmaclean.env.Environment;
import wmaclean.env.game.Window;

import java.awt.*;

public abstract class GameObject {

    protected double x, y, width, height, velX, velY;
    protected Color color;

    public GameObject(final int x,
                      final int y,
                      final int width,
                      final int height,
                      final Color color){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public boolean collidesWithXEdge(){
        return this.x < 0  || this.x + this.width > Environment.WIDTH;
    }

    public boolean collidesWithYEdge(){
        return this.y < 0  || this.y + this.height + Window.BOTTOM_BORDER_OFFSET > Environment.HEIGHT;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    protected double clamp(double var, int lim) {
        if (var < 0){
            return 0;
        }else{
            return Math.min(var, lim);
        }
    }
}
