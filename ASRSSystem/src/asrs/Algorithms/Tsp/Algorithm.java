package asrs.Algorithms.Tsp;

import asrs.Models.Location;
import asrs.Models.Product;

import java.util.ArrayList;

public abstract class Algorithm {
    protected ArrayList<Product> products;

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public double getDistance(Location location1, Location location2) {
        int x = location1.getX() - location2.getX();
        int y = location1.getY() - location2.getY();
        return Math.sqrt((x * x) + (y * y));
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public abstract void performCalculation();

    public String getSanyaString() {
        String sanya = "ARR" + Integer.sum(products.size(), -2) + "=";
        for (Product p: products) {
            try {
                if (Integer.parseInt(p.getProductId()) >= 0) {
                    sanya += p.getLocation().getX() + "" + p.getLocation().getY() + "-";
                }
            } catch (Exception e) {
                System.err.println("ProductID is not Integer");
            }
        }
        sanya = sanya.substring(0, sanya.length()-1);
        sanya+=";";
        return sanya;
    }
}
