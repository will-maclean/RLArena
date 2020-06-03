package wmaclean.env.game;

import wmaclean.env.Environment;
import wmaclean.rl.RLApp;

public class App {

    // run the game
    public static void main(String[] args){

        (new Environment(new RLApp())).newGame();
    }
}
