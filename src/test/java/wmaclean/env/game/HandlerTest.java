package wmaclean.env.game;

import org.junit.Test;
import wmaclean.env.game.objs.Player;

import static org.junit.Assert.*;

public class HandlerTest {

    @Test
    public void getPlayer() {
        Handler handler = new Handler(null);

        handler.addObject(new Player(10, 10));

        Player player = handler.getPlayer();

        assertNotNull(player);
    }

    @Test
    public void checkPlayerAlive() {
        Handler handler = new Handler(null);

        handler.addObject(new Player(10, 10));

        Player player = handler.getPlayer();

        assertTrue(player.getHealth() > 0);
    }

    @Test
    public void checkPlayerCollision() {
    }
}