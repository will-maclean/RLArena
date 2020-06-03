package wmaclean.env.game.objs;

import java.awt.*;

public abstract class Enemy extends GameObject {

    protected double damageMultiplier;

    public Enemy(int x, int y, int width, int height, double damageMultiplier, Color color) {
        super(x, y, width, height, color);

        this.damageMultiplier = damageMultiplier;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }
}
