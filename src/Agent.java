public class Agent {
    private static Agent agent; // The only instance of the object
    private int x,y;
    private State currentState;

    private Agent(int x,int y) {
        //private constructor for singleton
        this.x = x;
        this.y = y;
    }

    public static Agent getAgent(int x,int y) { // Singleton object
        if(agent == null) {
            agent = new Agent(x,y);
        }

        return agent;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
