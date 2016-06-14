package Algorithm;

public class SimResult {

    private String algorithm;
    private double totalDistance;
    private int totalSteps;
    private double totalTime;

    /** Constructor ask for results **/
    public SimResult(String algorithm, double totalDistance, int totalSteps, double totalTime) {
        this.algorithm = algorithm;
        this.totalDistance = totalDistance;
        this.totalSteps = totalSteps;
        this.totalTime = totalTime;

    }

    /** Display results in console **/
    @Override
    public String toString() {
        return "         -----Results-----\n" +
                "Used Algorithm: " + algorithm + "\n" +
                "Total Distance: " + totalDistance + "\n" +
                "Total Steps: " + totalSteps + "\n" +
                "Total Time: " + totalTime + "seconds";
    }
}

