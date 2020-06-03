package wmaclean.env.game;

import wmaclean.env.Environment;
import wmaclean.rl.RLApp;

import java.awt.*;

public class HUD {

    private final Environment environment;
    private final Handler handler;
    private final RLApp rlApp;

    private int score = 0;

    private static final int WIDTH = Environment.WIDTH / 5;
    private static final int HEIGHT = Environment.HEIGHT / 30;
    private static final int x = Environment.WIDTH / 50;
    private static final int y = Environment.HEIGHT / 50;
    private final Color greenHealthColour = Color.GREEN;
    private final Color redHealthColour = Color.RED;
    private final Color fontColour = Color.white;

    public HUD(Environment environment, Handler handler, RLApp rlApp){
        this.environment = environment;
        this.handler = handler;
        this.rlApp = rlApp;
    }

    public void render(Graphics g) {
        if(this.rlApp.isTraining()){
            drawTrainingFeedback(g);
        }else{
            renderHealthBox(g);
        }
    }

    private void drawTrainingFeedback(Graphics g) {
        g.setColor(fontColour);
        g.drawString("Episode: " + this.rlApp.getEpisodeCounter() + "/" + this.rlApp.getEpisodes(), 15, 64);
        g.drawString("Score: " + score, 15, 32);
    }

    private void renderHealthBox(Graphics g) {
        // render red background
        g.setColor(redHealthColour);
        g.fillRect(x, y, WIDTH, HEIGHT);

        // render health
        int playerHealth = this.handler.getPlayer().getHealth();
        int maxPlayerHealth = this.handler.getPlayer().getMaxHealth();
        double ratio = (double)playerHealth / (double)maxPlayerHealth;
        int healthBarWidth = (int) (ratio * WIDTH);

        g.setColor(greenHealthColour);
        g.fillRect(x, y, healthBarWidth, HEIGHT);
    }

    public void newGame(){
        this.score = 0;
    }

    public void tick() {
        if(this.environment.getGamestate() == Gamestate.ALIVE) {
            this.score++;
        }
    }

    public int getScore() {
        return this.score;
    }
}
