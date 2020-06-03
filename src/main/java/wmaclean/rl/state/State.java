package wmaclean.rl.state;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import wmaclean.env.Environment;
import wmaclean.env.game.objs.GameObject;

import java.util.List;

/**
 * Implements a RL State class
 */
public class State {

    /**
     * Store all the game objects and their associated data
     */
    private final List<GameObject> gameObjs;

    /**
     * Score at time of state
     */
    private final int score;

    /**
     * Whether the player is currently alive
     */
    private final boolean alive;

    /**
     * Size of the hand-crafted vector that gets fed into the DQN
     */
    public static final int vectorSizeCount = 31;

    /**
     * Shape of the INDArray used for the DQN input
     */
    public static final int[] vectorShape = new int[]{1, vectorSizeCount};

    /**
     * Max score - once this score is reached, the game is called finished
     */
    public static final int MAX_SCORE = 10000;

    /**
     * Default constructor
     * @param gameObjs - list of all the game objects
     * @param score - current game score
     * @param alive - whether the player is alive
     */
    public State(final List<GameObject> gameObjs, final int score, final boolean alive) {
        this.gameObjs = gameObjs;
        this.score = score;
        this.alive = alive;
    }

    /**
     * Reward for this state
     * @return - 1 if player is alive, 0 otherwise
     */
    public int getReward(){
        return alive ? 1 : 0;
    }

    /**
     * Converts the state to an INDArray for feeding into the DQN
     * @return - INDArray vector
     */
    public INDArray getStateAsNDArray(){
        double[] data = new double[vectorSizeCount];

        for(int i = 0; i < gameObjs.size(); i++){
            data[4*i] = gameObjs.get(i).getX();
            data[4*i + 1] = gameObjs.get(i).getY();
            data[4*i + 2] = gameObjs.get(i).getVelX();
            data[4*i + 3] = gameObjs.get(i).getVelY();
        }

        data[vectorSizeCount - 3] = this.alive ? 1 : 0;
        data[vectorSizeCount - 2] = Environment.WIDTH;
        data[vectorSizeCount - 1] = Environment.HEIGHT;

        return Nd4j.create(data, vectorShape);
    }

    /**
     * Getter for whether the
     * @return - true if player is dead or max score has been reached, false otherwise
     */
    public boolean isDone(){
        return !(alive) || score > MAX_SCORE;
    }
}
