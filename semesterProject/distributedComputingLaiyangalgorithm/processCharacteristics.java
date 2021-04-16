package distributedComputingLaiyangalgorithm;

/**
 * This class holds the process attributes for associating the characterstics with timeslots to each process
 * @author jitnakra 
 */

public class processCharacteristics {

    String processName;
    int amount;
    Color color;
    int intialTime;
    int finalTime;

    public processCharacteristics(int processNum, int amount, Color color, int intialTime, int finalTime) {
        processName = "Process" + processNum;
        this.amount = amount;
        this.color = color;
        this.intialTime = intialTime;
        this.finalTime = finalTime;
    }

    @Override
    public String toString() {
        return " [ processName=" + processName + "| amount=" + amount + "| color=" + color
                + "| intialTime=" + intialTime + "| finalTime=" + finalTime + " ]" + "\n";
    }
}