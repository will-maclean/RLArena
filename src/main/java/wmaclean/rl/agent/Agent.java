package wmaclean.rl.agent;

import wmaclean.env.Action;
import wmaclean.rl.dqn.DQN;
import wmaclean.rl.state.State;
import wmaclean.rl.strategy.EpsilonGreedyStrategy;
import wmaclean.rl.strategy.Strategy;

import java.util.Random;

/**
 * RL Agent which chooses actions for the game
 */
public class Agent {

    /**
     * How many steps have been completed so far
     */
    private int stepCount;

    /**
     * Strategy for choosing between exploration and exploitation
     */
    private final Strategy strategy = new EpsilonGreedyStrategy();

    /**
     * How many possible actions there are
     */
    private final int numActions = Action.values().length;

    /**
     * Random instance
     */
    private final Random r = new Random();

    /**
     * Default constructor
     */
    public Agent(){
        this.stepCount = 0;
    }

    /**
     * Selects an action by implementing the strategy
     * @param state - current game state
     * @param dqn - Deep Q Network to emulate a Q-Table
     * @return - chosen action
     */
    public Action selectAction(State state, DQN dqn){
        double rate = this.strategy.explorationRate(this.stepCount);
        this.stepCount++;

        if(rate > r.nextDouble()){
            // explore
            return Action.values()[r.nextInt(this.numActions)];
        }else{
            // exploit
            return Action.values()[dqn.evaluate(state)[0]];
        }
    }
}
