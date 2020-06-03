package wmaclean.rl.strategy;

/**
 * Implementation of a Epsilon Greedy strategy to decide whether to explore or exploit
 */
public class EpsilonGreedyStrategy implements Strategy{

    /**
     * Initial exploration likelihood
     */
    private double start = 1;

    /**
     * Final exploration likelihood
     */
    private double end = 0.1;

    /**
     * How quickly exploration likelihood decays
     */
    private double decay = 0.01;

    /**
     * Default constructor
     */
    public EpsilonGreedyStrategy() { }

    /**
     * Gets the exploration rate
     * @param stepCount - how many steps we've taken so far
     * @return - exploration likelihood
     */
    @Override
    public double explorationRate(int stepCount) {
        return end + (start - end) * Math.exp(-1 * stepCount * decay);
    }
}
