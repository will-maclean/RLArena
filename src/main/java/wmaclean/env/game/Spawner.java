package wmaclean.env.game;

import wmaclean.env.Environment;

public class Spawner {

    private Environment environment;
    private Handler handler;

    public Spawner(Environment environment, Handler handler) {
        this.environment = environment;
        this.handler = handler;
    }

    public void tick() {
    }
}
