package asrs.Utils;

import asrs.Models.Bin;
import asrs.Algorithms.Bpp.Enumeratie;
import asrs.Models.Location;
import asrs.Models.Order;
import asrs.Models.Product;

import java.util.ArrayList;

public class BinPlacer {
    private Bin left, right;
    private Enumeratie enumeratie;
    private ArrayList<Product> products;

    public BinPlacer(Location leftBin, Location rightBin) {
        left = new Bin(10, leftBin);
        right = new Bin(10, rightBin);
        this.enumeratie = new Enumeratie(left,right);
    }

    public void setOrder(Order order) {
        ArrayList<Product> products = new ArrayList<>();
        for(Product p : order.getProducts()) {
            if(Integer.parseInt(p.getProductId()) >=0) {
                products.add(p);
            }
        }
        this.products = products;
        enumeratie.setOrder(products);
    }

    public void startAlgo() {
        enumeratie.startAlgo();
        String leftStr = "left bin: ";
        String rightStr = "right bin:  ";

        for(Product p : products) {
            if(p.isGoLeft()) {
                leftStr += p.getProductId() + "-" + p.getSize() + ", ";
            } else {
                rightStr+= p.getProductId() + "-" + p.getSize() + ", ";
            }
        }
        System.out.println("format: productId-size");
        System.out.println(leftStr);
        System.out.println(rightStr);
    }

    public Bin getLeftBin(){
        return left;
    }

    public Bin getRightBin(){
        return right;
    }
}
