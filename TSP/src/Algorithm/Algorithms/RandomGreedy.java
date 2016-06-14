/**
 * #--- Random algorithm class ---#
 * This class contains the algorithm for the Random TSP method.
 * Running this algorithm makes the program search for random
 * points all over the selected map and construct a path that way.
 */

package Algorithm.Algorithms;


import Algorithm.Algorithm;
import Interface.Field;
import Interface.Point;
import Interface.Result;

import java.util.Random;

public class RandomGreedy extends Algorithm {

    private Random randomGenerator;
    private Field field;
    private double totalDistance;
    private int totalSteps;
    private long startTime;
    private long endTime;
    private long totalTime;

    public RandomGreedy(Field field) {
        super(field.getGrid());
        this.field = field;
        field.clearLines();
        randomGenerator = new Random();
        calculate();
    }

    @Override
    public void calculate() {
        startTime = System.currentTimeMillis(); /** Start counting time **/
        int arrayLength = grid.size();
        int loopCounter = 0;
        Point lastPoint = initial;

        totalDistance = initial.calculateDistance(grid.get(0));

        while (loopCounter < arrayLength) {
            Point current = grid.get(randomGenerator.nextInt(grid.size())); /** Get random point in the grid **/
            field.drawLine(lastPoint, current, loopCounter);    /** Draw the line from last point to current point **/
            double distance = lastPoint.calculateDistance(current); /** calculate the difference between those two **/
            totalDistance += distance; /** Add difference on to the total distance **/
            lastPoint = current;
            grid.remove(current);
            loopCounter++;
        }

        totalSteps = loopCounter;
        endTime = System.currentTimeMillis(); /** End counting time **/

        totalTime = endTime - startTime; /** Difference = total time **/
    }

    /** Give the results to result.java **/
    @Override
    public void getResult() {
        Result result = new Result("Random Greedy", totalDistance, totalSteps, totalTime);
    }
}