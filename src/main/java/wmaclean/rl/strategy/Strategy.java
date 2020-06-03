package wmaclean.rl.strategy;

public interface Strategy {

    public double explorationRate(int stepCount);
}
