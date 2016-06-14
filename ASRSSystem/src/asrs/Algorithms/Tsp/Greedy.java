package asrs.Algorithms.Tsp;

import asrs.Models.Location;
import asrs.Models.Product;

import java.util.ArrayList;

public class Greedy extends Algorithm {

    private Location startLocation = new Location(1, 1);

    @Override
    public void performCalculation() {
        ArrayList<Product> orderedProducts = new ArrayList<>();
        Location lastLocation = startLocation;
        Product nearestNeighbour = new Product(0, "", new Location(0,0), 0);
        Double distance;
        Double smallestDistance;

        System.out.println("Unordered products: " + products);

        int productCount = products.size();

        while(orderedProducts.size() < productCount) {
            smallestDistance = 100.0;
            for (Product product : products) {
                distance = getDistance(lastLocation, product.getLocation());
                if(distance < smallestDistance) {
                    nearestNeighbour = product;
                    smallestDistance = distance;
                }
            }
            lastLocation = nearestNeighbour.getLocation();
            orderedProducts.add(nearestNeighbour);
            products.remove(nearestNeighbour);
        }
        System.out.println("x");
        products = orderedProducts;

        System.out.println();
        System.out.print("Ordered products:" );
        for(Product product : products) System.out.print(product.toString());

    }
}
