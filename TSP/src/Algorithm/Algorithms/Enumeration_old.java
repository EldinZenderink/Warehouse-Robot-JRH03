/**
 * #--- Enumeration algorithm class ---#
 * This class contains the algorithm for the enumeration TSP method.
 * When you run it, it will calculate all possible routes it can
 * take and will give you back the shortest route. This is arguably
 * the best method for the most accurate path, but it exponentially
 * gets bigger when you have more points to travel between. This is
 * why right now (until a new way of computing arrives) this class
 * isn't optimal to use.
 */

package Algorithm.Algorithms;

import Algorithm.Algorithm;
import Interface.Field;
import Interface.Point;

import java.util.ArrayList;
import java.util.List;

public class Enumeration_old extends Algorithm {

    private Field field;
    private List<Route> routes;

    private Route shortestRoute;
    private int shortestRouteDistance;

    private int totalCalculations;
    private long startTime;
    private long endTime;
    private long totalTime;

    public Enumeration_old(Field field) {
        super(field.getGrid());
        this.field = field;
        routes = new ArrayList<>();

        shortestRoute = new Route(initial);

        shortestRouteDistance = 9999999;
        totalCalculations = 0;
        calculate();
    }

    @Override
    public void calculate() {
        startTime = System.currentTimeMillis();
        System.out.println("#------ Simulation Start (Full enumeration) ------#");

        // Calculate all possible combinations
        permute(grid, 0);

        // Check which route is the shortest
        for (Route route : routes) {
            if (route.getTotalDistance() < shortestRouteDistance) {
                shortestRoute = route;
                shortestRouteDistance = route.getTotalDistance();
                totalCalculations++;
            }
        }

        // Draw the route on the field
        field.drawRoute(shortestRoute);
        endTime = System.currentTimeMillis();
        totalTime = startTime - endTime;
    }

    public void permute(List<Point> arr, int k) {
        /**
         * This is the array builder that calls itself to
         * calculate every possible combination of a given
         * list of points.
         *
         * #-- DO NOT CALCULATE MORE THAN 10 POINTS --#
         * Will overflow the RAM usage and crash the application.
         * TODO: Optimize the algorithm
         */

        for (int i = k; i < arr.size(); i++) {
            java.util.Collections.swap(arr, i, k);
            permute(arr, k + 1);
            java.util.Collections.swap(arr, k, i);

            totalCalculations++;
        }

        if ((k == arr.size() - 1)) {
            Route route = new Route(initial);

            for (Point point : arr) {
                route.add(point);
                totalCalculations++;
            }

            routes.add(route);
            System.out.println(route.toStringRoute() + " distance: " + route.getTotalDistance());
        }
    }

    private double calculateTotalDistance(List<Point> route) {
        /** Calculate the total distance of a given list of points. **/
        int distance = 0;
        Point current = initial;

        for (Point point : route) {
            if (!(point == route.get(route.size() - 1))) {
                distance += current.calculateDistance(point);
            }
        }

        return distance;
    }

    @Override
    public void getResult() {
//        SimResult result = new SimResult("Full Enumeration", shortestRoute.getTotalDistance(), totalCalculations);
//        System.out.println(result.toString());
    }

}
