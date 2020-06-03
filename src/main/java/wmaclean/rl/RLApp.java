package wmaclean.rl;

import wmaclean.env.Action;
import wmaclean.env.Environment;
import wmaclean.rl.agent.Agent;
import wmaclean.rl.dqn.DQN;
import wmaclean.rl.exp.Experience;
import wmaclean.rl.exp.ExperienceItem;
import wmaclean.rl.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Main driver class
 */
public class RLApp {

    /**
     * RL Agent which makes decisions
     */
    private final Agent agent;

    /**
     * RL Environment which stores the game
     */
    private final Environment environment;

    /**
     * Deep Q Network which models the Q-Table
     */
    private DQN dqn;

    /**
     * Experience stores a history of state-action-nextState-reward sets
     */
    private final Experience experience;

    /**
     * How many times per second the agent makes a decision
     */
    public static final int HERTZ = 15;

    /**
     * How long each decision is made for
     */
    public static final double FRAME_DURATION = 1d / HERTZ;

    /**
     * Stores whether the Agent is currently being trained or not
     */
    private boolean training = false;

    /**
     * How many episodes we train the agent for
     */
    private final int episodes = 1000;

    /**
     * How many episodes have elapsed
     */
    private int episodeCounter = 0;

    /**
     * Batch size for the DQN
     */
    private final int batchSize = 32;

    /**
     * Basic evaluation of training improvement is a list of agent scores for each episode
     */
    private final List<Integer> scores;

    /**
     * Stores the last state, used for making ExperienceItems
     */
    private State lastState;

    /**
     * Stores the last action, used for making ExperienceItems
     */
    private Action lastAction;

    /**
     * Basic constructor
     */
    public RLApp(){
        this.agent = new Agent();
        this.dqn = new DQN(State.vectorSizeCount, Action.values().length);
        this.experience = new Experience();
        this.environment = new Environment(this);

        this.scores = new ArrayList<>();
    }

    /**
     * Called every time the environment is ready to evaluate its next frame
     * @param state - current state of the environment
     */
    public void nextFrame(State state){
        // feed state through net to get action
        Action action = this.agent.selectAction(state, this.dqn);
        if(training){
            // record experience
            if(this.lastAction != null && this.lastState != null){
                this.experience.add(new ExperienceItem(lastState, lastAction, state, state.getReward()));
            }
            this.lastAction = action;
            this.lastState = state;

            if(this.experience.size() > this.batchSize){
                this.dqn.calTDLoss(this.experience, this.batchSize);
            }

            if(state.isDone()){
                return;
            }
        }

        // execute action
        this.environment.executeAction(action);
    }

    /**
     * Call this to start training
     */
    public void train(){
        this.training = true;

        this.environment.newGame();
    }

    /**
     * Call this to train an existing DQN
     * @param networkPath - path of DQN save file
     */
    public void trainExisting(String networkPath){
        this.dqn = DQN.load(this.dqn.getInputSize(), this.dqn.getOutputSize(), networkPath);

        this.train();
    }

    /**
     * Play a game with an existing DQN
     * @param networkPath - path of DQN save file
     */
    public void play(String networkPath){
        this.dqn = DQN.load(this.dqn.getInputSize(), this.dqn.getOutputSize(), networkPath);

        this.play();
    }

    /**
     * Play a game with a new (untrained) DQN
     */
    public void play(){
        this.training = false;
        this.environment.newGame();
    }

    /**
     * For now, main only starts training
     * @param args - input from CLI
     */
    public static void main(String[] args){
        (new RLApp()).trainExisting("trainedAgent");
    }

    /**
     * getter for this.training
     * @return - true if training, false otherwise
     */
    public boolean isTraining() {
        return this.training;
    }

    /**
     * Called at the start of each episode to reset the environment
     */
    public void nextEpisode(){
        this.environment.resetEnv();
    }

    /**
     * Called on the completion of an episode. Will start a new episode if still training, and calls
     * trainingFinished if all episodes are completed
     */
    public void endOfEpisode() {
        if(this.episodeCounter < this.episodes){
            System.out.println("Episode ".concat(String.valueOf(episodeCounter)).concat(" completed."));
            this.episodeCounter++;
            this.scores.add(this.environment.getScore());
            nextEpisode();
        }else{
            // finished training!!!
            trainingFinished();
        }
    }

    /**
     * Called after the completion of the final episode
     */
    public void trainingFinished(){
        System.out.println("Training completed");
        this.dqn.save("trainedAgent");
        printListInt(this.scores);


        this.environment.getWindow().dispose();
        this.environment.stop();
    }

    /**
     * Basic helper method to print a list of integers to the console
     * @param scores - list of scores to print
     */
    private static void printListInt(List<Integer> scores) {
        for(Integer score : scores){
            System.out.println(score);
        }
    }

    /**
     * Getter for this.episodeCounter
     * @return - how many episodes have been completed
     */
    public int getEpisodeCounter() {
        return episodeCounter;
    }

    /**
     * Getter for this.episodes
     * @return - how many episodes will be executed
     */
    public int getEpisodes() {
        return episodes;
    }
}
