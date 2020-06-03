package wmaclean.rl.dqn;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.ops.NDBase;
import org.nd4j.linalg.learning.config.AdaGrad;
import org.nd4j.linalg.lossfunctions.impl.LossMSE;
import org.nd4j.shade.guava.annotations.VisibleForTesting;
import wmaclean.rl.exp.Experience;
import wmaclean.rl.exp.ExperienceItem;
import wmaclean.rl.state.State;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DQN {

    /**
     * discount factor used in TD calculation
     */
    public static final double DISCOUNT_FACTOR = 0.01;

    /**
     * Input vector size for the net
     */
    private final int inputSize;

    /**
     * output vector size for the net
     */
    private final int outputSize;

    /**
     * net used to make calculations. Standard feed-forward multilayer net with Relu applied after each layer
     */
    private MultiLayerNetwork network;

    private final NDBase ndBase;

    /**
     * folder path for network saves
     */
    private static final String saveLocation = "src/main/resources/saves/";

    /**
     * constructor to create a DQN with specified input and output sizes
     * @param inputSize - int for input vector size
     * @param outputSize - output vector size
     */
    public DQN(int inputSize, int outputSize){

        this.inputSize = inputSize;
        this.outputSize = outputSize;

        MultiLayerConfiguration config = setConfig();
        this.network = new MultiLayerNetwork(config);
        this.network.init();
        ndBase = new NDBase();
    }

    /**
     * constructor used when creating a DQN from a save file
     * @param inputSize - int for input vector size
     * @param outputSize - output vector size
     * @param path - path where save file can be located
     */
    private DQN(int inputSize, int outputSize, String path) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;

        try {
            this.network = MultiLayerNetwork.load(new File(DQN.saveLocation.concat(path)), true);
        } catch (IOException e){
            System.out.println("Error loading nn, file error. Creating new net");
            MultiLayerConfiguration config = setConfig();
            this.network = new MultiLayerNetwork(config);
            this.network.init();
        }
        ndBase = new NDBase();
    }

    /**
     * used to create the net
     * @return config for the net
     */
    private MultiLayerConfiguration setConfig(){
        return new NeuralNetConfiguration.Builder()
                .seed(12345)
                .weightInit(WeightInit.XAVIER)
                .updater(new AdaGrad(0.5))
                .activation(Activation.RELU)
                .optimizationAlgo(OptimizationAlgorithm.CONJUGATE_GRADIENT)
                .l2(0.0001)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(this.inputSize).nOut(250).weightInit(WeightInit.XAVIER).activation(Activation.RELU) //First hidden layer
                        .build())
                .layer(1, new DenseLayer.Builder().nIn(250).nOut(250).weightInit(WeightInit.XAVIER).activation(Activation.RELU) //First hidden layer
                        .build())
                .layer(2, new OutputLayer.Builder().nIn(250).nOut(this.outputSize).weightInit(WeightInit.XAVIER).activation(Activation.SOFTMAX)
                        .lossFunction(new LossMSE()) //Output layer
                        .build())
                .build();
    }

    /**
     * evaluates the action given a state
     * @param state - current state
     * @return - network's choice of action after feeding through a state
     */
    public int[] evaluate(State state){
        return this.network.predict(state.getStateAsNDArray());
    }

    /**
     * calculates TD loss given the current experience and a batchsize
     * @param experience - Experience instance, containing information about states, actions, and their rewards
     * @param batchSize - batch size for the net
     */
    @VisibleForTesting
    public void calTDLoss(Experience experience, int batchSize) {

        List<ExperienceItem> batch = experience.randomBatch(batchSize);

        List<INDArray> states = new ArrayList<>();
        List<Integer> actions = new ArrayList<>();
        List<Integer> rewards = new ArrayList<>();
        List<Integer> dones = new ArrayList<>();
        List<INDArray> nextStates = new ArrayList<>();

        for(ExperienceItem item : batch){
            states.add(item.getState().getStateAsNDArray());
            actions.add(item.getAction().intValue());
            rewards.add(item.getReward());
            dones.add(item.getState().isDone() ? 1 : 0);
            nextStates.add(item.getNextState().getStateAsNDArray());
        }

        INDArray iStates = Nd4j.vstack(states);
        INDArray iActions = Nd4j.create(actions);
        INDArray iRewards = Nd4j.create(rewards);
        INDArray iDones = Nd4j.create(dones);
        INDArray iNextStates = Nd4j.vstack(nextStates);

        // todo - WIP
        INDArray qVals = this.network.output(iStates);
        // qvals = qvals.gather(1, actions_t.unsqueeze(1)).squeeze(1)
        qVals = this.ndBase.gather(qVals, this.ndBase.expandDims(iActions, 1), 1);
        qVals = this.ndBase.squeeze(qVals, 1);

        INDArray nextQVals = this.network.output(iNextStates);
        INDArray nextQVal = nextQVals.max();

        INDArray expQVals = iRewards.add(
                iDones
                        .muli(-1)
                        .addi(1)
                        .muli(nextQVal)
                        .muli(DISCOUNT_FACTOR)
        );

        this.network.setInput(iStates);
        this.network.setLabels(expQVals);
        this.network.feedForward(true);

        // end todo
    }

    /**
     * saves net - rather than whole DQN instance - to specified path
     * @param fileName - name of the save file. Path is automatically prepended
     */
    public void save(String fileName) {
        try {
            this.network.save(new File(saveLocation.concat(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new DQN instance from a save file
     * @param inputSize - int for input vector size
     * @param outputSize - output vector size
     * @param path - path where save file can be located
     * @return - DQN instance
     */
    public static DQN load (int inputSize, int outputSize, String path) {
        return new DQN(inputSize, outputSize, path);
    }

    /**
     * standard getter
     * @return - input size
     */
    public int getInputSize() {
        return inputSize;
    }

    /**
     * standard getter
     * @return - output size
     */
    public int getOutputSize() {
        return outputSize;
    }
}
