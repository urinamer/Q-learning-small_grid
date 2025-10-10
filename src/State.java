import java.util.Objects;

public class State {
    private int x;
    private int y;
    private double reward;
    private boolean isTerminal;

    public State(int x, int y,boolean isTerminal,double reward) {
        this.x = x;
        this.y = y;
        this.isTerminal = isTerminal;
        this.reward = reward;
    }

    public State(int y, int x) {
        this.y = y;
        this.x = x;
        this.isTerminal = false;
        this.reward = 0;
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

    @Override
    public int hashCode() {
        return Objects.hash(x, y, reward, isTerminal);
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
