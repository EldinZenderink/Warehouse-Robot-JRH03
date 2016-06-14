/**
 * #--- Greedy algorithm class ---#
 * This class contains the algorithm for the Greedy TSP method.
 * Greedy or Nearest Neighbor will calculate the closest point
 * away from the current point. It'll start in the bottom right
 * corner and move to the closest point until there's no points
 * left in the selected point array.
 */

package Algorithm.Algorithms;

import Algorithm.Algorithm;
import Algorithm.SimResult;
import Interface.Field;
import Interface.Point;
import Interface.Result;

public class Greedy extends Algorithm {

    private Point current;
    private Point closest;
    private Field field;
    private double totalDistance;
    private int totalSteps;
    private long startTime;
    private long endTime;
    private long totalTime;

    public Greedy(Field field) {
        super(field.getGrid());
        this.field = field;
        field.clearLines();
        current = initial;
        totalDistance = 0;
        calculate();
    }

    @Override
    public void calculate() {
        startTime = System.currentTimeMillis();
        double closestDistance = 999999999;

        int count = 0;
        int gridSize = grid.size();

        // Main loop
        while (true) {
            if (!(current.getIndex() == 201285)) {
                grid.remove(current);
            }

            // For each point, if it's the closest point, make this the current closest point
            for (Point point : grid) {
                if (current.calculateDistance(point) <= closestDistance) {
                    closestDistance = current.calculateDistance(point);
                    closest = point;
                }
            }

            // Add the new distance to the current distance
            totalDistance += current.calculateDistance(closest);

            // Draw the route line on the field
            field.drawLine(current, closest, count);

            // Move to the new point by saying the new current is the closest point
            current = closest;

            closestDistance = 999999;

            if (grid.size() == 0) {
                field.removeLine(count);
                break;
            }

            if (count != gridSize) {
                count++;
            } else {
                break;
            }
        }

        totalSteps = count;
        endTime = System.currentTimeMillis();
        totalTime = endTime - startTime;
    }

    @Override
    public void getResult() {
        Result results = new Result ("Greedy", totalDistance, totalSteps, totalTime);
    }

    @Override
    public String toString() {
        return "Greedy{" +
                "current=" + current +
                ", closest=" + closest +
                ", totalDistance=" + totalDistance +
                '}';
    }
}
