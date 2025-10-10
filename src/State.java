import java.util.Objects;

public class State {
    private int x;
    private int y;
    private double reward;
    boolean isTerminal;

    public State(int x, int y,boolean isTerminal,double reward) {
        this.x = x;
        this.y = y;
        this.isTerminal = isTerminal;
        this.reward = reward;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return x == state.x && y == state.y;
    }



    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }
}
