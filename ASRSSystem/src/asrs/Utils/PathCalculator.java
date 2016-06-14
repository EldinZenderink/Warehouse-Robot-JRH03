package asrs.Utils;

import asrs.Models.Product;
import asrs.Algorithms.Tsp.*;

import java.util.ArrayList;

public class PathCalculator {
    private Algorithm algorithm;
    private ArrayList<Product> products;

    public PathCalculator(Algorithm algorithm) {
        // Set default algorithm
        this.algorithm = algorithm;
        products = new ArrayList<>();
    }

    public PathCalculator() {
        this(new Enumeration());

    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        algorithm = null;
        if(products.size() < 10) {
            algorithm = new Enumeration();
        } else {
            algorithm = new Greedy();
        }
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void calculate() {
        algorithm.setProducts(products);
        algorithm.performCalculation();
        products = algorithm.getProducts();
    }

}
