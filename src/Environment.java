import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {


    private int height; //grid height
    private int width; //grid width
    private int goalX;
    private int goalY;

    private State[] states;
    private Action[] actions;
    private int[][] gridWorld;
    private double[][] qTable;

    private Agent agent;


    Random random = new Random();

    public Environment(int height, int width, int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
        this.height = height;
        this.width = width;
        actions = Action.values();
        gridWorld = new int[height][width];
        initializeStates();
        states = new State[height*width];
        qTable= new double[height*width][actions.length];


    }

    private void initializeNewEpisode(){
        int startX = random.nextInt(Constants.GRID_X);
        int startY = random.nextInt(Constants.GRID_Y);
        agent = Agent.getAgent(startX,startY);
        agent

    }


    public void trainingLoop(){



        for (int i = 0; i < Constants.NUMBER_OF_LOOPS ; i++) {

        }
    }


    private void initializeStates(){
        int index = 0;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                states[index] = new State(i,j,false,0);
                index++;
            }
        }
    }

    private void updateQValue(int currentStateIndex,int currentActionIndex,State currentState){
        qTable[currentStateIndex][currentActionIndex] +=
                Constants.LEARNING_RATE *(currentState.getReward()+
                        Constants.DISCOUNT_FACTOR*getMaxQValue(currentState,currentStateIndex)-
                        qTable[currentStateIndex][currentActionIndex]);
    }


    private double getMaxQValue(State currentState, int currentStateIndex){
        double maxQValue = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < actions.length; i++) {
            if(qTable[currentStateIndex][i] >  maxQValue)
                maxQValue = qTable[currentStateIndex][i];
        }

        return maxQValue;
    }


    Action chooseBestAction(int currentStateIndex,double epsilon){

        if(random.nextDouble() > epsilon){
            return actions[random.nextInt(actions.length)];
        }
        else{
            return getBestAction(currentStateIndex);
        }
    }

    private Action getBestAction(int currentStateIndex) {
        double maxQ = Double.NEGATIVE_INFINITY;
        List<Action> bestActions = new ArrayList<>();

        for (int i = 0; i < actions.length; i++) {
            double q = qTable[currentStateIndex][i];

            if (q > maxQ) {
                maxQ = q;
                bestActions.clear();
                bestActions.add(actions[i]);
            } else if (q == maxQ) {
                bestActions.add(actions[i]);
            }
        }

        // Randomly choose one if multiple have same Q-value
        return bestActions.get(random.nextInt(bestActions.size()));
    }

}
