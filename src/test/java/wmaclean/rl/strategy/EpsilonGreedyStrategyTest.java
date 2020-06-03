package wmaclean.rl.strategy;

import org.junit.Test;

import static org.junit.Assert.*;

public class EpsilonGreedyStrategyTest {

    @Test
    public void explorationRate() {
        EpsilonGreedyStrategy epsilonGreedyStrategy = new EpsilonGreedyStrategy();
        assertEquals(1, epsilonGreedyStrategy.explorationRate(0), 0);
    }
}