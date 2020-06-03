package wmaclean.env;

import wmaclean.env.game.objs.Player;

/**
 * Action enum stores all possible moves that can be made by the agent
 */
public enum Action {
    N{
        @Override
        public void makeMove(Player player) {
            player.N();
        }

        @Override
        public int intValue() {
            return 0;
        }
    },
    NE{
        @Override
        public void makeMove(Player player) {
            player.NE();
        }

        @Override
        public int intValue() {
            return 1;
        }
    },
    E{
        @Override
        public void makeMove(Player player) {
            player.E();
        }

        @Override
        public int intValue() {
            return 2;
        }
    },
    SE{
        @Override
        public void makeMove(Player player) {
            player.SE();
        }

        @Override
        public int intValue() {
            return 3;
        }
    },
    S{
        @Override
        public void makeMove(Player player) {
            player.S();
        }

        @Override
        public int intValue() {
            return 4;
        }
    },
    SW{
        @Override
        public void makeMove(Player player) {
            player.SW();
        }

        @Override
        public int intValue() {
            return 5;
        }
    },
    W{
        @Override
        public void makeMove(Player player) {
            player.W();
        }

        @Override
        public int intValue() {
            return 6;
        }
    },
    NW{
        @Override
        public void makeMove(Player player) {
            player.NW();
        }

        @Override
        public int intValue() {
            return 7;
        }
    },
    PASS{
        @Override
        public void makeMove(Player player) {
            player.PASS();
        }

        @Override
        public int intValue() {
            return 8;
        }
    };

    /**
     * make the player from the environment execute an action
     * @param player - PLayer instance which executes the action
     */
    public abstract void makeMove(Player player);

    public abstract int intValue();
}
