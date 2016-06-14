/**
 *            #--- Algorithm abstract class ---#
 * Abstract algorithm container so all subclasses are required
 * to make one specific function used for displaying results.
 */
package Algorithm;

import Interface.Point;

import java.util.ArrayList;

public abstract class Algorithm {

    /**
     * This private grid consists only of the points that
     * are selected, so the ones that matter to the
     * simulation.
     */
    public static ArrayList<Point> grid;
    public static Point initial;
    public static int fieldSize;

    public Algorithm(ArrayList<Point> field) {
        grid = new ArrayList<>();

        // Filter out the selected points from the normal
        //     points and add these to the grid.
        for (Point point : field) {
            if (point.getSelected()) {
                grid.add(point);
            }
        }

        // Create a new point in the bottom right corner
        initial = new Point(201285, 1000, 1000);
        initial.setStatus("SELECTED");
        fieldSize = 25;
    }

    // Abstract methods
    public abstract void calculate();
    public abstract void getResult();

}