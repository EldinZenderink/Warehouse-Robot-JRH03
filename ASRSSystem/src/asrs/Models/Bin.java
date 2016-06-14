package asrs.Models;

import java.util.ArrayList;

/**
 * Created by Eldin on 4/9/2015.
 * This class contains all the atributes needed for a bin.
 */
public class Bin {

    private ArrayList<Product> packets;
    private Location location;
    private int binCapacityHeight,
                timesEmptied,
                emptiedHeightLeft[];
    private boolean autoEmpty = true;

    public Bin(int binCapacityHeight, Location location){
        /** This method stores the data used to define a bin,
         * such as its capacity and which packets it contains
         * and how many times it has been emptied. */
        this.binCapacityHeight = binCapacityHeight;
        this.packets = new ArrayList<>();
        this.timesEmptied = 0;
        this.emptiedHeightLeft = new int[binCapacityHeight];
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public int getCapacity() {
        return binCapacityHeight;
    }

    public void setCapacity(int capacity) {
        this.binCapacityHeight = capacity;
    }

    public int getFullnessInPercentage() {
        int content = 0;
        for(Product product : packets) {
            content += product.getSize();
        }
        return content / binCapacityHeight * 100;
    }

    public int getFullness() {
        int content = 0;
        for(Product product : packets) {
            content += product.getSize();
        }
        return content;
    }

    public int getFreeSpace() {
        return binCapacityHeight - getFullness();
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    

    public int[] getEmptiedHeightLeft() {
        return emptiedHeightLeft;
    }

    public int getBinCapicityFilled() {
        /**
         * this method returns the capacity thats in use of the bin
         */
        int binCapicity = 0;
        for( Product p: packets) {
            binCapicity += p.getSize();
        }
        return binCapicity;

    }

    public int getBinCapacityHeight() {
        return binCapacityHeight;
    }
    public void resetEmptiedHeightLeft() {
        this.emptiedHeightLeft = null;
        this.emptiedHeightLeft = new int[binCapacityHeight];
    }
    public int getBinCapacityLeft() {
        /**
         * this method returns the empty capacity in the bin
         */
        return binCapacityHeight - getBinCapicityFilled();
    }

    public void setTimesEmptied(int timesEmptied) {
        this.timesEmptied = timesEmptied;
    }

    public void addPacket(Product product, String algo) {
        /**
         * This method adds a packet to the bin, it checks if it fits, if not it empties the bin
         * and add it after it is emptied.
         */

            if((product.getSize() <= getBinCapacityLeft())) {
                packets.add(product);
            } else {
                System.err.print("Packet cant exceed binHeight");
                System.err.println(", Algo :" + algo);
            }


    }

    public int getTimesEmptied() {
        return timesEmptied;
    }

    public void addBinLeftover(int capicityLeft) {
        emptiedHeightLeft[capicityLeft]++;
    }
    public void emptyBin(){
        /**
         * empties the bin
         */
        addBinLeftover(getBinCapacityLeft());
        timesEmptied++;
        packets.clear();
    }

    public void setAutoEmpty(boolean autoEmpty) {
        this.autoEmpty = autoEmpty;
    }

    public void setBinCapacityHeight(int binCapacityHeight) {
        this.binCapacityHeight = binCapacityHeight;
    }

    public ArrayList<Product> getProducts() {
        return packets;
    }

    @Override
    public String toString() {

        String str = "";

        for (Product p : packets) {
            str += p.getProductId()+", ";
        }
        return "Bin size: " + packets.size() + " ProductId: "+ str;
    }
}
