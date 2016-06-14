/**
 * #------ Route class ------#
 * This is the route class for the algorithms.
 * It just stores a list of Points in a selected
 * order and that list is the route the TSP follows.
 */

package Algorithm.Algorithms;

import Interface.Point;

import java.util.ArrayList;

public class Route {

    private ArrayList<Point> routeList;
    private int totalDistance;

    public Route(Point initial) {
        routeList = new ArrayList<>();
        routeList.add(initial);
    }

    public void add(Point point) {
        totalDistance += routeList.get(routeList.size() - 1).calculateDistance(point);
        routeList.add(point);
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeList=" + routeList +
                ", totalDistance=" + totalDistance +
                '}';
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public ArrayList<Point> getRouteList() {
        return routeList;
    }

    public String toStringRoute() {
        String stringbuilder = "Enum Route: ";
        for (Point point : routeList) {
            stringbuilder += point.getIndex() + ", ";
        }
        return stringbuilder;
    }

}
