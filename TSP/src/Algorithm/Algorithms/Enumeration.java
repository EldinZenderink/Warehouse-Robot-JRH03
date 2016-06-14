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
import Interface.Result;

import java.util.ArrayList;
import java.util.List;

public class Enumeration extends Algorithm {

    private Field field;
    private List<Route> routes;



    private Route shortestRoute;
    private int shortestRouteDistance;

    private int totalCalculations;
    private long startTime;
    private long endTime;
    private long totalTime;

    public Enumeration(Field field) {
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
//        for (Route route : routes) {
//            if (route.getTotalDistance() < shortestRouteDistance) {
//                shortestRoute = route;
//                shortestRouteDistance = route.getTotalDistance();
//                totalCalculations++;
//            }
//        }

        // Draw the route on the field
        field.drawRoute(shortestRoute);
        endTime = System.currentTimeMillis();
        totalTime = endTime - startTime;
    }

    public void permute(java.util.List<Point> arr, int k){
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
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);

            totalCalculations++;
        }

        if ((k == arr.size() - 1)) {
            Route route = new Route(initial);

            for (int x = 0; x < arr.size() && route.getTotalDistance() < shortestRouteDistance; x++) {
                route.add(arr.get(x));
                totalCalculations++;
            }

            if (route.getTotalDistance() < shortestRouteDistance) {
                System.out.println(route.toStringRoute() + "shortest distance: " + shortestRouteDistance + " distance: " + route.getTotalDistance());
                this.shortestRoute = route;
                this.shortestRouteDistance = route.getTotalDistance();
            }
        }
    }

    @Override
    public void getResult() {
        Result results = new Result("enumeratie nieuw", shortestRouteDistance, totalCalculations, totalTime);

    }

}
