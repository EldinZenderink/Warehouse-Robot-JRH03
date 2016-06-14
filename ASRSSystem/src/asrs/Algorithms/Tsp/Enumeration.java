package asrs.Algorithms.Tsp;

import asrs.Models.Location;
import asrs.Models.Product;

import java.util.ArrayList;

public class Enumeration extends Algorithm {

    private Location startLocation = new Location(1, 1);
    private Product startProduct = new Product(-1," random name", startLocation, 0 );
    private int shortestRoute = 999999;
    ArrayList<Product> shortestProduct = new ArrayList<>();

    @Override
    public void performCalculation() {
        ArrayList<Product> orderedProducts = new ArrayList<>();
        Location lastLocation = startLocation;
        Product nearestNeighbour = new Product();
        Double distance;
        Double smallestDistance;

        int productCount = products.size();
        permute(products, 1);
        products = shortestProduct;

        System.out.println();
        System.out.println("Ordered products: " + products);

    }

    public void permute(java.util.List<Product> arr, int k){
        Location lastLocation,nextLocation;
        int productSize;
        ArrayList<Product> tempProducts = new ArrayList<>();
        for (int i = k; i < arr.size() - 1 && k < arr.size() -1; i++) {
            java.util.Collections.swap(arr, i, k);
            permute(arr, k + 1);
            java.util.Collections.swap(arr, k, i);

        }
        lastLocation = startLocation;
        productSize = 0;

        if ((k == arr.size() - 1)) {
//            Route route = new Route(initial);

            for (int x = 0; x < arr.size() && productSize < shortestRoute; x++) {
                nextLocation = arr.get(x).getLocation();
                tempProducts.add(arr.get(x));
                productSize += getDistance(lastLocation, nextLocation);
                lastLocation = nextLocation;
            }

            if (productSize < shortestRoute) {
                System.out.println("shortest distance: " + shortestRoute + " current distance: " + productSize);
                this.shortestRoute = productSize;
                shortestProduct.clear();
                for(Product p : tempProducts) {
                    shortestProduct.add(p);
                }

            }
        }
    }

}
