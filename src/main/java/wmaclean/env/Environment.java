package wmaclean.env;

import wmaclean.env.game.*;
import wmaclean.env.game.Window;
import wmaclean.env.game.objs.Bloodhound;
import wmaclean.env.game.objs.Mozzie;
import wmaclean.env.game.objs.Player;
import wmaclean.rl.RLApp;
import wmaclean.rl.state.State;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Environment handles the game. It is never called directly.
 */
public class Environment extends Canvas implements Runnable {

    /**
     * current Windom JFrame
     */
    private Window window;

    /**
     * Window width
     */
    public static final int WIDTH = 800;

    /**
     * Window height, based on ratio
     */
    public static final int HEIGHT = WIDTH / 12 * 9;

    /**
     * Window title
     */
    private static final String TITLE = "RL Epilepsy";

    /**
     * game background colour
     */
    private final Color backgroundColor = RubyColour.rubles;

    /**
     * If INSTANT_DEATH is true, player loses all life instantly on contact with enemies. If false, players take
     * damage proportional to the Enemy's damage multiplier
     */
    private static boolean INSTANT_DEATH = true;

    /**
     * Thread instance for the game
     */
    private Thread thread;

    /**
     * controls whether the game loop is running
     */
    private boolean running = false;

    /**
     * Keeps track of the RLApp which coordinates things like training or gameplay
     */
    private final RLApp rlApp;

    /**
     * Handler instance which stores and manages players and enemies
     */
    private final Handler handler;

    /**
     * Controls when to spawn new objects. Currently doesn't do anything, but can be set-up to implement levels etc.
     *
     * Note that before implementing levels or other creation/removal of enemies, a CNN taking the whole window pixels
     * as input should be implemented. At the moment, network takes handcrafted state as input, meaning variable enemy
     * amounts are not taken into account.
     */
    private final Spawner spawner;

    /**
     * Shows score / episode count, and other visual feedback
     */
    private final HUD hud;

    /**
     * Records whether the player is alive or not
     */
    private Gamestate gamestate;

    /**
     * Default constructor. Note the requirement of RLApp - this class should not be instantiated directly. Rather,
     * RLApp.train() or RLApp.play() should be used.
     * @param rlApp - RLApp instance
     */
    public Environment(RLApp rlApp){

        this.rlApp = rlApp;

        this.handler = new Handler(this);
        this.spawner = new Spawner(this, this.handler);
        this.hud = new HUD(this, this.handler, this.rlApp);
    }

    /**
     * Setup for a new game
     */
    public void newGame() {

        resetEnv();

        this.window = new Window(WIDTH, HEIGHT, TITLE, this);
    }

    /**
     * resets all players and scores to initial values/positions
     */
    public void resetEnv(){
        this.gamestate = Gamestate.ALIVE;

        this.handler.clear();
        this.handler.addObject(new Player(WIDTH/2, HEIGHT/2));
        this.handler.addObject(new Mozzie(WIDTH/5, HEIGHT/5));
        this.handler.addObject(new Mozzie(4*WIDTH/5, HEIGHT/5));
        this.handler.addObject(new Mozzie(WIDTH/5, 4*HEIGHT/5));
        this.handler.addObject(new Mozzie(4*WIDTH/5, 4*HEIGHT/5));
        this.handler.addObject(new Bloodhound(WIDTH/10, HEIGHT/10, this.handler));
        this.handler.addObject(new Bloodhound(9*WIDTH/10, 9*HEIGHT/10, this.handler));
        this.hud.newGame();
    }

    /**
     * starts the thread
     */
    public synchronized void start() {
        this.running = true;

        thread = new Thread(this);
        thread.start();
    }

    /**
     * Ends/Joins the thread
     */
    public synchronized void stop(){
        try{
            this.window.get().dispose();
            running = false;
            thread.join();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * run() implementation which contains the main game loop
     */
    @Override
    public void run() {

        this.requestFocus();
        running = true;
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >=1)
            {
                tick();
                delta--;
            }
            if(running)
                render();
            frames++;

            if(System.currentTimeMillis() - timer > RLApp.FRAME_DURATION)
            {
                timer += RLApp.FRAME_DURATION;
                //System.out.println("FPS: "+ frames);
                this.rlApp.nextFrame(new State(this.handler.getObjects(), this.hud.getScore(), true));
            }
        }
        stop();
    }

    /**
     * contains everything which must happen during a game tick
     */
    private void tick(){
        if(this.gamestate == Gamestate.ALIVE){
            handler.tick();
            hud.tick();
            spawner.tick();
        }else if(this.gamestate == Gamestate.OVER){
            gameOver();
        }
    }

    /**
     * handles what happens when gamestate is changed to game over. If training, the next episode is started.
     * If not training, the thread is joined and the program finishes
     */
    private void gameOver() {
        if(this.rlApp.isTraining()){
            this.rlApp.nextFrame(new State(this.handler.getObjects(), this.hud.getScore(), false));
            this.rlApp.endOfEpisode();
        }else{
            this.window.get().dispose();
            this.running = false;
            stop();
        }
    }

    /**
     * Renders everything in the window. Sets the background, then calls handler and hud so they can render as well
     */
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        handler.render(g);
        hud.render(g);

        g.dispose();
        bs.show();
    }


    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    public static boolean isInstantDeath() {
        return INSTANT_DEATH;
    }

    public static void setInstantDeath(boolean instantDeath) {
        INSTANT_DEATH = instantDeath;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    /**
     * Interface for Agent to execute an action
     * @param action - action to be executed by the Player for the next RLApp.FRAME_DURATION seconds
     */
    public void executeAction(Action action){
        action.makeMove(this.handler.getPlayer());
    }

    public JFrame getWindow(){
        return this.window.get();
    }

    public int getScore() {
        return this.hud.getScore();
    }
}
