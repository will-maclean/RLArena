package wmaclean.rl.dqn;

import org.junit.AfterClass;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.factory.Nd4j;
import wmaclean.env.Action;
import wmaclean.env.Environment;
import wmaclean.env.game.Handler;
import wmaclean.env.game.objs.Mozzie;
import wmaclean.env.game.objs.Player;
import wmaclean.rl.state.State;

import java.io.File;

import static org.junit.Assert.*;


public class DQNTest {

    private static final String testPath = "test_save";
    private static final String testPathFolder = "src/main/resources/saves/";

    @Test
    public void save() {
        DQN dqn = new DQN(100, 10);

        dqn.save(testPath);
    }

    @Test
    public void load() {
        File tempFile = new File(testPathFolder.concat(testPath));
        if(!(tempFile.exists())){
            DQN dqn = new DQN(100, 10);

            dqn.save(testPath);
        }

        DQN.load(100, 10, testPath);
    }

    @AfterClass
    public static void tearDown(){
        // remove created test file
        File testSave = new File(testPathFolder.concat(testPath));

        System.out.println("Tear down");

        if(testSave.exists()){
            if(!(testSave.delete())){
                fail("Error removing test save of net");
            }
        }
    }

    @Test
    public void evaluate() {

        Handler handler = new Handler(null);

        handler.addObject(new Player(Environment.WIDTH/2, Environment.HEIGHT/2));
        handler.addObject(new Mozzie(Environment.WIDTH/5, Environment.HEIGHT/5));
        handler.addObject(new Mozzie(4*Environment.WIDTH/5, Environment.HEIGHT/5));
        handler.addObject(new Mozzie(Environment.WIDTH/5, 4*Environment.HEIGHT/5));
        handler.addObject(new Mozzie(4*Environment.WIDTH/5, 4*Environment.HEIGHT/5));

        State state = new State(handler.getObjects(), 100, true);

        DQN dqn = new DQN(State.vectorSizeCount, Action.values().length);

        dqn.evaluate(state);

    }
}