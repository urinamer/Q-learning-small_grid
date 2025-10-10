public class Main{
    public static void main(String[] args) {
        Environment environment = new Environment(4,4,3,3);

//        environment.loadQTableFromFile();
//        environment.startTrainingLoop();
//        environment.saveQTableToFile();

        environment.loadQTableFromFile();
        environment.testModel();
    }
}