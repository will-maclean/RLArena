package wmaclean.rl.exp;

import wmaclean.env.Action;
import wmaclean.rl.state.State;

public class ExperienceItem {
    private final State state;
    private final Action action;
    private final State nextState;
    private final int reward;

    public ExperienceItem(State state, Action action, State nextState, int reward) {
        this.state = state;
        this.action = action;
        this.nextState = nextState;
        this.reward = reward;
    }

    public State getState() {
        return state;
    }

    public Action getAction() {
        return action;
    }

    public State getNextState() {
        return nextState;
    }

    public int getReward() {
        return reward;
    }
}
