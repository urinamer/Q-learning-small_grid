import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Environment {


    private final int height; //grid height
    private final int width; //grid width
    private final int goalX;
    private final int goalY;

    private final State[] states;
    private final Action[] actions;
    private final Map<State, Integer> stateToIndexMap;
    private final double[][] qTable;

    private Agent agent;
    private double epsilon = 1;

    private int episodeCount = 0;


    Random random = new Random();

    public Environment(int height, int width, int goalX, int goalY) {
        this.goalX = goalX;
        this.goalY = goalY;
        this.height = height;
        this.width = width;
        actions = Action.values();
        states = new State[height*width];
        initializeStates();

        qTable= new double[height*width][actions.length];

        this.stateToIndexMap = new HashMap<>();

        for (int i = 0; i < states.length; i++) {
            stateToIndexMap.put(states[i], i);
        }


    }

    private void initializeNewEpisode(){
        State startState;
        do {
            startState = states[random.nextInt(states.length)];
        }while (startState.isTerminal());

        agent = Agent.getAgent();
        agent.setCurrentState(startState);
        agent.setX(agent.getCurrentState().getX());
        agent.setY(agent.getCurrentState().getY());
    }


    public void startTrainingLoop(){
        initializeNewEpisode();
        System.out.println("Start new episode");

        for (int i = 0; i < Constants.NUMBER_OF_LOOPS ; i++) {
            Action action = chooseBestAction(stateToIndexMap.get(agent.getCurrentState()),epsilon);
            int currentStateIndex = stateToIndexMap.get(agent.getCurrentState());
            double currentReward = makeAction(action);
            int nextStateIndex = stateToIndexMap.get(agent.getCurrentState());

            if(agent.getCurrentState().isTerminal()){
                System.out.println("Start new episode");
                initializeNewEpisode();
                epsilon *= 0.995; // slowly reduce exploration
                epsilon = Math.max(0.1, epsilon); // don’t go below 0.1
                episodeCount++;
                System.out.println("Episode: " + episodeCount);
                continue;
            }
            updateQValue(currentStateIndex,action.ordinal(),currentReward,nextStateIndex);



        }
    }


    private void initializeStates(){
        int index = 0;

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                boolean isTerminal = (i == goalY && j == goalX);
                double reward = isTerminal ? 10 : -0.01;
                states[index] = new State(j,i,isTerminal,reward);
                index++;
            }
        }
    }

    private void updateQValue(int currentStateIndex,int currentActionIndex,double currentReward, int nextStateIndex){
        if(states[nextStateIndex].isTerminal()){
            qTable[currentStateIndex][currentActionIndex] +=
                    Constants.LEARNING_RATE *(currentReward-
                            qTable[currentStateIndex][currentActionIndex]);
        }
        else {
            qTable[currentStateIndex][currentActionIndex] +=
                    Constants.LEARNING_RATE * (currentReward +
                            Constants.DISCOUNT_FACTOR * getMaxQValue(nextStateIndex) -
                            qTable[currentStateIndex][currentActionIndex]);
        }
    }


    private double getMaxQValue( int currentStateIndex){
        double maxQValue = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < actions.length; i++) {
            if(qTable[currentStateIndex][i] >  maxQValue)
                maxQValue = qTable[currentStateIndex][i];
        }

        return maxQValue;
    }

    private double makeAction(Action action) {
        int newX = agent.getX();
        int newY = agent.getY();
        System.out.println("X : " + newX + " Y: " + newY + " Action: " + action);
        double reward;

        switch (action) {
            case UP:    newY -= 1; break;
            case DOWN:  newY += 1; break;
            case LEFT:  newX -= 1; break;
            case RIGHT: newX += 1; break;
        }

        // Check boundaries before committing the move
        if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
            agent.setX(newX);
            agent.setY(newY);

            int stateIndex = newY * width + newX;  // <-- map (x,y) to 1D index
            agent.setCurrentState(states[stateIndex]);
            reward = agent.getCurrentState().getReward();
        } else {
            reward = -1;
        }
        System.out.println("Reward: " + reward);
        return reward;
    }

    private Action chooseBestAction(int currentStateIndex,double epsilon){

        if(random.nextDouble() < epsilon){
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



    public void saveQTableToFile(){
        try (PrintWriter writer = new PrintWriter("qtable.csv")) {
            for (int i = 0; i < qTable.length; i++) {
                for (int j = 0; j < qTable[i].length; j++) {
                    writer.print(qTable[i][j]);
                    if (j < qTable[i].length - 1) writer.print(",");
                }
                writer.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadQTableFromFile(){
        try (Scanner scanner = new Scanner(new File("qtable.csv"))) {
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                for (int j = 0; j < values.length; j++) {
                    qTable[i][j] = Double.parseDouble(values[j]);
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testModel(int numberOfEpisodes){
        int totalSteps = 0;
        for(int i = 0; i < numberOfEpisodes; i++) {
            initializeNewEpisode();
            while (!agent.getCurrentState().isTerminal()) {
                totalSteps++;
//                System.out.println("X: " + agent.getX() + " Y: " + agent.getY());
                Action action = chooseBestAction(stateToIndexMap.get(agent.getCurrentState()), 0.1);
                makeAction(action);
//                System.out.println("Action: " + action);
            }
        }
        System.out.println("Number of episodes: " + numberOfEpisodes);
        System.out.println("Average steps: " + (double)totalSteps/numberOfEpisodes);
    }
}
